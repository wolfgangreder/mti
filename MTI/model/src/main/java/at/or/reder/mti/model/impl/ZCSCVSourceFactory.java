/*
 * Copyright 2021 Wolfgang Reder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.or.reder.mti.model.impl;

import at.or.reder.dcc.IdentifyProvider;
import at.or.reder.dcc.cv.CVAddress;
import at.or.reder.dcc.util.SimpleCVAddress;
import at.or.reder.mti.model.api.CVResult;
import at.or.reder.mti.model.api.CVSource;
import at.or.reder.mti.model.api.CVSourceAttributes;
import at.or.reder.mti.model.api.CVSourceFactory;
import at.or.reder.mti.model.api.FileCVSourceFactory;
import at.or.reder.mti.model.api.StoreException;
import at.or.reder.mti.model.spi.IniParser;
import at.or.reder.mti.model.spi.IniParserListener;
import at.or.reder.zcan20.CVReadState;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wolfgang Reder
 */
@Messages({"ZCSCVSourceFactory_name=ZCS Datei"})
@ServiceProvider(service = CVSourceFactory.class)
public final class ZCSCVSourceFactory implements CVSourceFactory, FileCVSourceFactory
{

  private static final Charset CHARSET = Charset.forName("windows-1252");
  public static final UUID ID = UUID.fromString("1b243262-e053-4c46-a5c0-8ebdd48d74c5");

  private static enum Sections
  {
    FILEINFO("FileInfo"),
    CONFIG("Config"),
    ZCS("ZCS");
    private final String key;

    private Sections(String key)
    {
      this.key = key;
    }

    public String getKey()
    {
      return key;
    }

    public static Sections valueKey(String key)
    {
      for (Sections s : values()) {
        if (s.getKey().equals(key)) {
          return s;
        }
      }
      return null;
    }

  }

  private static final class ZCSSniffer implements IniParserListener
  {

    private String sectionEnd;
    private boolean isZCS;

    @Override
    public boolean processSectionStart(IniParser parser,
                                       String section)
    {
      if (Sections.FILEINFO.getKey().equals(section)) {
        sectionEnd = "/" + section;
      } else {
        if (sectionEnd != null) {
          return false;
        }
        sectionEnd = null;
      }
      return true;
    }

    @Override
    public boolean processValue(IniParser parser,
                                String key,
                                String value)
    {
      if (sectionEnd != null && "Content".equals(key)) {
        isZCS = "Config.CV".equals(value);
        return false;
      }
      return true;
    }

    public boolean isZCS()
    {
      return isZCS;
    }

  }

  private static final class ZCSCollector implements IniParserListener
  {

    private final Map<String, String> meta = new HashMap<>();
    private final Map<Integer, Integer> cv = new TreeMap<>();
    private Sections currentSection;

    @Override
    public boolean processSectionStart(IniParser parser,
                                       String section)
    {
      if (section.startsWith("/")) {
        currentSection = null;
      } else {
        currentSection = Sections.valueKey(section);
      }
      return true;
    }

    private void addValue(String strV)
    {
      if (strV != null) {
        String[] parts = strV.split(":");
        if (parts.length == 2) {
          int i = 0;
          int v = 0;
          try {
            i = Integer.parseInt(parts[0]);
          } catch (Throwable th) {
          }
          try {
            v = Integer.parseInt(parts[1]);
          } catch (Throwable th) {
          }
          if (i != 0) {
            cv.put(i,
                   v);
          }
        }
      }
    }

    @Override
    public boolean processValue(IniParser parser,
                                String key,
                                String value)
    {
      if (currentSection != null) {
        switch (currentSection) {
          case CONFIG:
            if ("CV".equals(key)) {
              addValue(value);
            } else {
              meta.put(currentSection.getKey() + "." + key,
                       value);
            }
            break;
          case FILEINFO:
            meta.put(currentSection.getKey() + "." + key,
                     value);
            break;
        }
      }
      return true;
    }

  }

  private static final class ZCSFileFilter implements FileFilter
  {

    private final IniParser parser;

    public ZCSFileFilter()
    {
      this.parser = new IniParser().setCommentStart("//");
    }

    @Override
    public boolean accept(File file)
    {
      if (!file.isDirectory() && file.canRead() && file.getName().toLowerCase().endsWith(".zcs")) {
        try {
          parser.setInput(new FileReader(file,
                                         CHARSET));
          ZCSSniffer sniffer = new ZCSSniffer();
          parser.setListener(sniffer);
          return sniffer.isZCS();
        } catch (Throwable th) {
        }
      }
      return false;
    }

  }

  private static final class ZCSCVSource extends AbstractCVSource implements IdentifyProvider
  {

    private final Map<String, String> meta;
    private final Map<CVAddress, CVResult> values = new TreeMap<>(Comparator.comparing(CVAddress::getFlatAddress));

    private ZCSCVSource(ZCSCVSourceFactory factory,
                        ZCSCollector collector)
    {
      super(factory,
            collector.meta.getOrDefault(Sections.CONFIG.key + ".Name",
                                        Bundle.ZCSCVSourceFactory_name()),
            EnumSet.of(CVSourceAttributes.PERSISTENT,
                       CVSourceAttributes.READ,
                       CVSourceAttributes.WRITE));
      ic.add(this);
      this.meta = new HashMap<>(collector.meta);
      for (Map.Entry<Integer, Integer> e : collector.cv.entrySet()) {
        CVAddress address = SimpleCVAddress.valueOf(e.getKey());
        CVResult result = new SimpleCVResult(this,
                                             address,
                                             e.getValue());
        values.put(address,
                   result);
      }
    }

    @Override
    public void setMeta(String key,
                        String value)
    {
      if (value != null) {
        meta.put(key,
                 value);
      } else {
        meta.remove(key);
      }
    }

    @Override
    public String getMeta(String key)
    {
      return meta.get(key);
    }

    @Override
    public Map<String, String> getMetaMap()
    {
      return meta;
    }

    private CVResult createNotFound(CVAddress address)
    {
      return new SimpleCVResult(this,
                                0,
                                address,
                                CVReadState.NO_ACK,
                                0,
                                null);
    }

    @Override
    public CVResult readCV(int decoderAddress,
                           CVAddress address,
                           long timeout,
                           TimeUnit unit)
    {
      return values.computeIfAbsent(address,
                                    this::createNotFound);
    }

    @Override
    public void setCV(int decoderAddress,
                      CVAddress address,
                      int value)
    {
      CVResult result = new SimpleCVResult(this,
                                           address,
                                           value);
      values.put(address,
                 result);
    }

    @Override
    public void enterPOMMode() throws IOException
    {
    }

    @Override
    public void enterServiceMode() throws IOException
    {
    }

    @Override
    public int readCV(int iAddress,
                      int iCV) throws IOException
    {
      CVResult result = readCV(0,
                               SimpleCVAddress.valueOf(iCV),
                               0,
                               TimeUnit.DAYS);
      if (result.getState() == CVReadState.READ) {
        return result.getValue();
      } else {
        return -1;
      }
    }

  }

  @Override
  public UUID getId()
  {
    return ID;
  }

  @Override
  public String getName()
  {
    return Bundle.ZCSCVSourceFactory_name();
  }

  @Override
  public CVSource openSourceGUI() throws IOException, StoreException
  {
    throw new UnsupportedOperationException("Not supported yet");
  }

  private Reader createReader(Object target) throws IOException
  {
    if (target instanceof Reader) {
      return (Reader) target;
    } else if (target instanceof File) {
      return new FileReader((File) target,
                            CHARSET);
    } else if (target instanceof Path) {
      return new FileReader(((Path) target).toFile(),
                            CHARSET);
    } else if (target instanceof InputStream) {
      return new InputStreamReader((InputStream) target,
                                   CHARSET);
    } else if (target instanceof URL) {
      URL url = (URL) target;
      return new InputStreamReader(url.openStream(),
                                   CHARSET);
    } else if (target instanceof URI) {
      URL url = ((URI) target).toURL();
      return new InputStreamReader(url.openStream(),
                                   CHARSET);
    } else {
      throw new IOException("No valid inputsource found");
    }
  }

  @Override
  public CVSource openSource(Object target) throws IOException, StoreException
  {
    ZCSCollector collector = new ZCSCollector();
    try (Reader reader = createReader(target)) {
      IniParser parser = new IniParser(reader).setCommentStart("//").setListener(collector);
      parser.parse();
    }
    return new ZCSCVSource(this,
                           collector);
  }

  @Override
  public Lookup getLookup()
  {
    return Lookup.EMPTY;
  }

  @Override
  public FileFilter getFileFilter()
  {
    return new ZCSFileFilter();
  }

  @Override
  public CVSource open(Path path) throws IOException, StoreException
  {
    return openSource(path);
  }

}
