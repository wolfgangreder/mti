/*
 * Copyright 2020 Wolfgang Reder.
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
package at.or.reder.mti.ui.zsx;

import at.or.reder.dcc.util.DCCUtils;
import at.or.reder.mti.model.Epoch;
import at.or.reder.mti.model.TractionSystem;
import at.or.reder.mti.model.api.Factories;
import at.or.reder.mti.model.api.StoreException;
import at.or.reder.mti.model.api.StoreExceptionWrapper;
import at.or.reder.mti.model.api.Stores;
import at.or.reder.mti.ui.zsx.impl.DefaultImageItem;
import at.or.reder.mti.ui.zsx.impl.DefaultLocoImageItem;
import at.or.reder.mti.ui.zsx.impl.DefaultTachoImageItem;
import at.or.reder.mti.ui.zsx.impl.ZSXHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.modules.Places;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.xml.sax.SAXException;

public final class ZSXFile
{

  private static ZSXFile iconInstance;
  private static ZSXFile locoInstance;

  public synchronized static ZSXFile getIconInstance()
  {
    if (iconInstance == null) {
      iconInstance = new ZSXFile("FxIcon.zsx");
    }
    return iconInstance;
  }

  public synchronized static ZSXFile getLocoInstance()
  {
    if (locoInstance == null) {
      locoInstance = new ZSXFile("LocoImg.zsx");
    }
    return locoInstance;
  }

  public static final String FOLDER = "/at/or/reder/mti/zimo/";
  private final Future<File> cacheDir;
  private final List<ImageItem> imageItems = new CopyOnWriteArrayList<>();
  private volatile int num;
  private volatile String code;
  private volatile String name;
  private volatile LocalDate date;
  private volatile String version;

  private ZSXFile(String rootFile)
  {
    FileObject obj = FileUtil.getConfigFile(FOLDER + rootFile);
    File cache = Places.getCacheSubfile(FOLDER + rootFile);
    File theFile = FileUtil.toFile(obj);
    boolean update = true;
    FileTime zsxTime = FileTime.from(Instant.now());
    if (theFile != null) {
      try {
        zsxTime = Files.getLastModifiedTime(theFile.toPath());
        FileTime cacheTime = readCacheTime(cache,
                                           rootFile);
        update = cacheTime == null || !zsxTime.toInstant().isBefore(cacheTime.toInstant());
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    if (update) {
      byte[] fileDigest = getFileDigest(obj);
      byte[] savedDigest = readCacheDigest(cache,
                                           rootFile);
      update = !Objects.deepEquals(fileDigest,
                                   savedDigest);
    }
    if (update) {
      FileTime realTime = zsxTime;
      cacheDir = CompletableFuture.supplyAsync(() -> expose2Cache(obj,
                                                                  cache,
                                                                  realTime),
                                               RequestProcessor.getDefault());
    } else {
      cacheDir = CompletableFuture.supplyAsync(() -> readFromCache(cache),
                                               RequestProcessor.getDefault());
    }
  }

  private File writeDigestFile(File target,
                               byte[] digest) throws IOException
  {
    File digestFile = new File(target,
                               target.getName() + ".sha512");
    String strDigest = DCCUtils.byteArray2HexString(digest);
    try (FileWriter writer = new FileWriter(digestFile,
                                            StandardCharsets.UTF_8)) {
      writer.write(strDigest);
      writer.write("  ");
      writer.write(target.getName());
      writer.write("\n");
    }
    return digestFile;
  }

  @SuppressWarnings("empty-statement")
  private byte[] getFileDigest(FileObject obj)
  {
    byte[] result = null;
    try {
      FileObject sha = obj.getParent().getFileObject(obj.getNameExt(),
                                                     "sha512");
      if (sha.canRead()) {
        try (InputStreamReader reader = new InputStreamReader(sha.getInputStream())) {
          result = readDigestFromReader(reader);
        }
      }
      if (result == null) {
        MessageDigest digest = MessageDigest.getInstance("SHA-512");
        try (DigestInputStream dis = new DigestInputStream(obj.getInputStream(),
                                                           digest)) {
          byte[] buffer = new byte[4096];
          while (dis.read(buffer) > 0);
        }
        result = digest.digest();
        writeDigestFile(FileUtil.toFile(sha),
                        result);
      }
    } catch (ParseException | IOException | NoSuchAlgorithmException ex) {
      Exceptions.printStackTrace(ex);
    }

    return result;
  }

  private FileTime readCacheTime(File cacheDir,
                                 String imageFile)
  {
    File cache = new File(cacheDir,
                          imageFile + ".sha512");
    if (cache.isFile() && cache.canRead()) {
      try {
        return Files.getLastModifiedTime(cache.toPath());
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return null;
  }

  private byte[] readDigestFromReader(Reader reader) throws IOException, ParseException
  {
    char[] buffer = new char[128];
    int read = reader.read(buffer);
    if (read == buffer.length) {
      ByteBuffer bb = DCCUtils.hexString2ByteBuffer(new String(buffer),
                                                 null,
                                                 (char) 0);
      return bb.array();
    }
    return null;
  }

  private byte[] readCacheDigest(File cacheDir,
                                 String imageFile)
  {
    File cache = new File(cacheDir,
                          imageFile + ".sha512");
    if (cache.isFile() && cache.canRead()) {
      try (FileReader reader = new FileReader(cache)) {
        return readDigestFromReader(reader);
      } catch (ParseException | IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return null;
  }

  private int toInt(Object str)
  {
    try {
      return Integer.parseInt(str.toString());
    } catch (Throwable th) {
    }
    return -1;
  }

  private ImageItem readFile(Path image)
  {
    String basename = image.getFileName().toString();
    basename = basename.substring(0,
                                  basename.length() - 4);
    File propsFile = new File(image.getParent().toFile(),
                              basename + ".properties");
    if (propsFile.isFile() && propsFile.canRead()) {
      try (InputStream is = new FileInputStream(propsFile)) {
        Properties props = new Properties();
        props.load(is);
        ImageType type = null;
        try {
          type = ImageType.valueOf(props.getProperty("Type"));
        } catch (Throwable th) {
        }
        if (type != null) {
          int id = toInt(props.get("Num"));
          String author = props.getProperty("Author",
                                            "");
          String name = props.getProperty("File");
          int ndx;
          int value;
          String unit;
          if (id > 0) {
            switch (type) {
              case FXICON:
                ndx = toInt(props.get("Idx"));
                return new DefaultImageItem(image.toFile(),
                                            id,
                                            ndx,
                                            name,
                                            author,
                                            type);
              case LOCO:
                UUID epochId = Epoch.EPOCH_UNKNOWN_ID;
                try {
                  epochId = UUID.fromString(props.getProperty("Epoch"));
                } catch (Throwable th) {
                }
                Epoch epoch = null;
                try {
                  epoch = Factories.getStores().getEpochStore().getEpoch(epochId);
                } catch (StoreException ex) {
                  throw new StoreExceptionWrapper(ex);
                }
                TractionSystem tractionSystem = TractionSystem.OTHER;
                try {
                  tractionSystem = TractionSystem.valueOf(props.getProperty("Traction"));
                } catch (Throwable th) {
                }
                String partNumber = props.getProperty("PartNum");
                String smallFile = props.getProperty("FileSmall");
                String largeFile = props.getProperty("FileLarge");
                return new DefaultLocoImageItem(image.toFile(),
                                                id,
                                                0,
                                                name,
                                                smallFile,
                                                largeFile,
                                                author,
                                                epoch,
                                                tractionSystem,
                                                partNumber,
                                                type);
              case TACHO:
                ndx = toInt(props.get("Idx"));
                value = toInt(props.get("Val"));
                unit = props.getProperty("Unit",
                                         "kmh");
                return new DefaultTachoImageItem(image.toFile(),
                                                 id,
                                                 ndx,
                                                 name,
                                                 author,
                                                 type,
                                                 value,
                                                 unit);
            }
          }
        }
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return null;
  }

  private File readFromCache(File target)
  {
    try {
      List<ImageItem> items = Files.find(target.toPath(),
                                         2,
                                         (p, a) -> {
                                           return a.isRegularFile() && p.getFileName().toString().endsWith(".png");
                                         }).parallel().
              map(this::readFile).
              filter((i) -> i != null).
              collect(Collectors.toList());
      imageItems.addAll(items);
      return target;
    } catch (StoreExceptionWrapper sew) {
      Exceptions.printStackTrace(sew.getCause());
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  private File expose2Cache(FileObject source,
                            File target,
                            FileTime zsxTime)
  {
    try {
      if (Files.exists(target.toPath())) {
        Files.walkFileTree(target.toPath(),
                           new SimpleFileVisitor<>()
                   {
                     @Override
                     public FileVisitResult visitFile(Path file,
                                                      BasicFileAttributes attrs) throws IOException
                     {
                       Files.delete(file);
                       return FileVisitResult.CONTINUE;
                     }

                   });
      }
      Files.createDirectories(target.toPath());
      SAXParserFactory parserFactory = SAXParserFactory.newInstance();
      SAXParser parser = parserFactory.newSAXParser();
      Stores stores = Factories.getStores();
      ZSXHandler handler = new ZSXHandler(target,
                                          stores);
      MessageDigest digest = MessageDigest.getInstance("SHA-512");
      parser.parse(new DigestInputStream(source.getInputStream(),
                                         digest),
                   handler);
      imageItems.addAll(handler.getImages());
      num = handler.getNum();
      code = handler.getCode();
      name = handler.getName();
      date = handler.getDate();
      version = handler.getVersion();
      writeDigestFile(target,
                      digest.digest());
      Files.walkFileTree(target.toPath(),
                         new SimpleFileVisitor<>()
                 {
                   @Override
                   public FileVisitResult visitFile(Path file,
                                                    BasicFileAttributes attrs) throws IOException
                   {
                     Files.setLastModifiedTime(file,
                                               zsxTime);
                     return FileVisitResult.CONTINUE;
                   }

                 });
      return target;
    } catch (StoreException | IOException | ParserConfigurationException | SAXException | NoSuchAlgorithmException ex) {
      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  private File wait4Cache()
  {
    File result = null;
    try {
      result = cacheDir.get();
    } catch (InterruptedException | ExecutionException ex) {
      Exceptions.printStackTrace(ex);
    }
    return result;
  }

  public List<ImageItem> getImageItems()
  {
    if (wait4Cache() != null) {
      return Collections.unmodifiableList(imageItems);
    } else {
      return Collections.emptyList();
    }
  }

  public int getNum()
  {
    if (wait4Cache() != null) {
      return num;
    } else {
      return -1;
    }
  }

  public String getCode()
  {
    if (wait4Cache() != null) {
      return code;
    } else {
      return "";
    }
  }

  public String getName()
  {
    if (wait4Cache() != null) {
      return name;
    } else {
      return "";
    }
  }

  public LocalDate getDate()
  {
    if (wait4Cache() != null) {
      return date;
    } else {
      return null;
    }
  }

  public String getVersion()
  {
    if (wait4Cache() != null) {
      return version;
    } else {
      return "";
    }
  }

}
