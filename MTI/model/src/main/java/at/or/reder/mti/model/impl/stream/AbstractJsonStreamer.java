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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.function.Function;

/**
 *
 * @author Wolfgang Reder
 */
public abstract class AbstractJsonStreamer<C, X extends XmlObject<C>> implements Streamer<C>
{

  private static ObjectMapper _mapper;
  private final Class<X> xmlClass;
  private final Function<C, X> toXml;

  protected AbstractJsonStreamer(Function<C, X> toXml,
                                 Class<X> xmlClass)
  {
    this.toXml = Objects.requireNonNull(toXml,
                                        "toXml is null");
    this.xmlClass = Objects.requireNonNull(xmlClass,
                                           "xmlClass is null");
  }

  protected ObjectMapper getContext() throws IOException
  {
    synchronized (AbstractJsonStreamer.class) {
      if (_mapper == null) {
        _mapper = new ObjectMapper();
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        _mapper.registerModule(module);
      }
      return _mapper;
    }
  }

  @Override
  public final StreamFormat getFormat()
  {
    return StreamFormat.JSON;
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
    ObjectMapper ctx = getContext();
    X o = ctx.readValue(is,
                        xmlClass);
    return o != null ? o.toModel() : null;
  }

  @Override
  public void marshal(C value,
                      OutputStream out) throws IOException
  {
    ObjectMapper ctx = getContext();
    ctx.writeValue(out,
                   toXml.apply(value));
  }

}
