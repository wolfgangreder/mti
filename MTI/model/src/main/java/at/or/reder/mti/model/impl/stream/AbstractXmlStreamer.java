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
package at.or.reder.mti.model.impl.stream;

import at.or.reder.mti.model.api.StreamFormat;
import at.or.reder.mti.model.api.Streamer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.function.Function;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Wolfgang Reder
 */
abstract class AbstractXmlStreamer<C, X extends XmlObject<C>> implements Streamer<C>
{

  private static JAXBContext _context;
  private final Function<C, Object> toXml;

  protected AbstractXmlStreamer(Function<C, Object> toXml)
  {
    this.toXml = Objects.requireNonNull(toXml,
                                        "toXml is null");
  }

  protected JAXBContext getContext() throws IOException
  {
    synchronized (AbstractXmlStreamer.class) {
      if (_context == null) {
        try {
          _context = JAXBContext.newInstance("at.or.reder.mti.model.impl.stream");
        } catch (JAXBException ex) {
          throw new IOException(ex);
        }
      }
      return _context;
    }
  }

  @Override
  public final StreamFormat getFormat()
  {
    return StreamFormat.XML;
  }

  @Override
  public boolean isMarshalSupported()
  {
    return true;
  }

  @Override
  public boolean isUnmarshalSupported()
  {
    return true;
  }

  @Override
  public C unmarshal(InputStream is) throws IOException
  {
    JAXBContext ctx = getContext();
    try {
      Unmarshaller um = ctx.createUnmarshaller();
      Object o = um.unmarshal(is);
      if (o instanceof XmlObject) {
        o = ((XmlObject) o).toModel();
        return getStreamableClass().cast(o);
      }
    } catch (JAXBException ex) {
      throw new IOException(ex);
    }
    return null;
  }

  @Override
  public void marshal(C value,
                      OutputStream out) throws IOException
  {
    JAXBContext ctx = getContext();
    try {
      Marshaller um = ctx.createMarshaller();
      um.marshal(toXml.apply(value),
                 out);
    } catch (JAXBException ex) {
      throw new IOException(ex);
    }
  }

}
