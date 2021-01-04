/*
 * Copyright 2020 Wolfgang Reder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this name except in compliance with the License.
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
package at.or.reder.mti.ui.zsx.impl;

import at.or.reder.mti.model.Epoch;
import at.or.reder.mti.model.TractionSystem;
import at.or.reder.mti.model.api.StoreException;
import at.or.reder.mti.model.api.Stores;
import at.or.reder.mti.ui.zsx.ImageItem;
import at.or.reder.mti.ui.zsx.ImageType;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import org.openide.util.Exceptions;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Wolfgang Reder
 */
public class ZSXHandler extends DefaultHandler
{

  private final File cacheRoot;
  private int num;
  private String code;
  private String name;
  private LocalDate date;
  private String version;
  private final List<ImageItem> images = new ArrayList<>();
  private boolean finished;
  private int _id;
  private int _index;
  private String _name;
  private ImageType _type;
  private int _value;
  private String _unit;
  private String _author;
  private File _cacheFile;
  private String _smallFile;
  private String _partNumber;
  private Epoch _epoch;
  private TractionSystem _tractionSystem;
  private String _largeFile;
  private final ImageWriter pngWriter;
  private final Stores stores;
  private final Map<Integer, Epoch> epochMap = new HashMap<>();

  public ZSXHandler(File cacheRoot,
                    Stores stores)
  {
    this.stores = stores;
    this.cacheRoot = cacheRoot;
    Iterator<ImageWriter> witer = ImageIO.getImageWritersByMIMEType("image/png");
    if (witer.hasNext()) {
      pngWriter = witer.next();
    } else {
      throw new IllegalStateException("Cannot find writer for MIME \"image/png\"");
    }
  }

  private Epoch getEpoch(String strZimoEpoch) throws StoreException
  {
    int i = Math.max(0,
                     toInt(strZimoEpoch));
    Epoch result = epochMap.get(i);
    if (result == null) {
      UUID id = Epoch.EPOCH_UNKNOWN_ID;
      switch (i) {
        case 1:
          id = Epoch.EPOCH_1_ID;
          break;
        case 2:
          id = Epoch.EPOCH_2_ID;
          break;
        case 3:
          id = Epoch.EPOCH_3_ID;
          break;
        case 4:
          id = Epoch.EPOCH_4_ID;
          break;
        case 5:
          id = Epoch.EPOCH_5_ID;
          break;
        case 6:
          id = Epoch.EPOCH_6_ID;
          break;
      }
      result = stores.getEpochStore().getEpoch(id);
      epochMap.put(i,
                   result);
    }
    return result;
  }

  private TractionSystem getTraction(String strTraction)
  {
    if (strTraction == null) {
      return TractionSystem.OTHER;
    }
    switch (strTraction) {
      case "S":
        return TractionSystem.STEAM;
      case "E":
        return TractionSystem.ELECTRIC;
      case "D":
        return TractionSystem.DIESEL;
      case "P":
        return TractionSystem.HORSE;
      case "H":
        return TractionSystem.HYDROGEN;
      case "B":
        return TractionSystem.HYBRID;
    }
    return TractionSystem.OTHER;
  }

  public int getNum()
  {
    return num;
  }

  public String getName()
  {
    return name;
  }

  public String getCode()
  {
    return code;
  }

  public String getFile()
  {
    return name;
  }

  public LocalDate getDate()
  {
    return date;
  }

  public String getVersion()
  {
    return version;
  }

  public List<ImageItem> getImages()
  {
    return images;
  }

  @Override
  public void endElement(String uri,
                         String localName,
                         String qName) throws SAXException
  {
    if (_cacheFile != null) {
      switch (_type) {
        case FXICON:
          images.add(new DefaultImageItem(_cacheFile,
                                          _id,
                                          _index,
                                          _name,
                                          _author,
                                          _type));
          break;
        case TACHO:
          images.add(new DefaultTachoImageItem(_cacheFile,
                                               _id,
                                               _index,
                                               _name,
                                               _author,
                                               _type,
                                               _value,
                                               _unit));
          break;
        case LOCO:
          images.add(new DefaultLocoImageItem(_cacheFile,
                                              _id,
                                              _index,
                                              _name,
                                              _smallFile,
                                              _largeFile,
                                              _author,
                                              _epoch,
                                              _tractionSystem,
                                              _partNumber,
                                              _type));
          break;
      }
      exposeMetadata();
      _cacheFile = null;
      _id = 0;
      _type = null;
      _index = 0;
      _name = null;
      _author = null;
      _value = 0;
      _unit = null;
    }
  }

  @Override
  public void startElement(String uri,
                           String localName,
                           String qName,
                           Attributes attributes) throws SAXException
  {
    assertNotFinished();
    try {
      switch (qName) {
        case "FileInfo":
          processFileInfo(attributes);
          break;
        case "Tacho":
          processImage(ImageType.TACHO,
                       attributes);
          break;
        case "Image":
          processImageData(attributes);
          break;
        case "FxIcon":
          processImage(ImageType.FXICON,
                       attributes);
          break;
        case "LocoImg":
          processImage(ImageType.LOCO,
                       attributes);
          break;
      }
    } catch (StoreException ex) {
      throw new SAXException(ex);
    }
  }

  private void assertNotFinished()
  {
    if (finished) {
      throw new IllegalStateException("Handler is finised");
    }
  }

  @Override
  public void endDocument() throws SAXException
  {
    finished = true;
  }

  private int toInt(String s)
  {
    try {
      return Integer.parseInt(s);
    } catch (Throwable th) {
    }
    return -1;
  }

  private void processFileInfo(Attributes attributes)
  {
    code = attributes.getValue("Code");
    name = attributes.getValue("Name");
    version = attributes.getValue("Version");
    num = toInt(attributes.getValue("Num"));
    date = null;
    try {
      date = LocalDate.parse(attributes.getValue("Date"),
                             DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    } catch (Throwable th) {
    }
  }

  private String extractBaseName(String fileName)
  {
    int dotPos = fileName.lastIndexOf('.');
    if (dotPos > 0) {
      fileName = fileName.substring(0,
                                    dotPos);
    }
    return URLEncoder.encode(fileName,
                             StandardCharsets.UTF_8);
  }

  private String extractUnit(String baseName)
  {
    if (baseName.length() > 3) {
      return baseName.substring(baseName.length() - 3);
    }
    return "kmh";
  }

  private void processImage(ImageType type,
                            Attributes attributes) throws StoreException
  {
    _type = null;
    _id = toInt(attributes.getValue("Num"));
    switch (type) {
      case FXICON:
        _name = attributes.getValue("File");
        _author = attributes.getValue("Author");
        _index = toInt(attributes.getValue("Idx"));
        _type = type;
        break;
      case TACHO:
        _name = attributes.getValue("File");
        _author = attributes.getValue("Author");
        _index = toInt(attributes.getValue("Idx"));
        _value = toInt(attributes.getValue("Val"));
        _unit = extractUnit(extractBaseName(_name));
        _type = type;
        break;
      case LOCO:
        _name = attributes.getValue("Name");
        _epoch = getEpoch(attributes.getValue("Epoch"));
        _tractionSystem = getTraction(attributes.getValue("Engine"));
        _partNumber = attributes.getValue("PartNum");
        _smallFile = attributes.getValue("File150x50");
        _largeFile = attributes.getValue("File279x92");
        _author = null;
        _index = 0;
        _type = type;
        break;
    }
  }

  private void exposeMetadata()
  {
    Properties props = new Properties();
    props.put("Num",
              Integer.toString(_id));
    props.put("File",
              _name);
    props.put("Type",
              _type.name());
    switch (_type) {
      case FXICON:
        props.put("Author",
                  _author);
        props.put("Idx",
                  Integer.toString(_index));
        break;
      case TACHO:
        props.put("Author",
                  _author);
        props.put("Idx",
                  Integer.toString(_index));
        props.put("Val",
                  Integer.toString(_value));
        props.put("Unit",
                  _unit);
        break;
      case LOCO:
        props.put("Epoch",
                  _epoch.getId().toString());
        props.put("Traction",
                  _tractionSystem.name());
        props.put("PartNum",
                  _partNumber);
        props.put("FileSmall",
                  _smallFile);
        props.put("FileLarge",
                  _largeFile);
        break;
    }
    try (FileOutputStream out = new FileOutputStream(new File(cacheRoot,
                                                              extractBaseName(_name) + ".properties"))) {
      props.store(out,
                  null);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  private void processImageData(Attributes attributes)
  {
    File output = null;
    try {
      String baseName = extractBaseName(_name);
      output = new File(cacheRoot,
                        baseName + ".png");
      if (output.exists()) {
        output.delete();
      }
      String val = attributes.getValue("Image");
      byte[] buffer = Base64.getMimeDecoder().decode(val);
      ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(buffer));
      Iterator<ImageReader> riter = ImageIO.getImageReaders(iis);
      if (riter.hasNext()) {
        ImageReader reader = riter.next();
        if (reader.getFormatName().equals("png")) {
          try (FileOutputStream fos = new FileOutputStream(output)) {
            fos.write(buffer);
          }

        } else {
          reader.setInput(iis);
          ImageOutputStream ios = ImageIO.createImageOutputStream(output);
          pngWriter.setOutput(ios);
          BufferedImage img = reader.read(0);
          pngWriter.write(img);
        }

      } else {
      }
      _cacheFile = output;
      output = null;
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    } finally {
      if (output != null) {
        output.delete();
      }
    }
  }

}
