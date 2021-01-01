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

import at.or.reder.mti.model.Epoch;
import at.or.reder.mti.model.api.StreamFormat;
import at.or.reder.mti.model.api.Streamer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wolfgang Reder
 */
@ServiceProvider(service = Streamer.class, path = "mti/streamer")
public final class EpochJsonStreamer implements Streamer<Epoch>
{

  private ObjectMapper _mapper;

  @Override
  public StreamFormat getFormat()
  {
    return StreamFormat.JSON;
  }

  @Override
  public Class<Epoch> getStreamableClass()
  {
    return Epoch.class;
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

  private synchronized ObjectMapper getContext() throws IOException
  {
    if (_mapper == null) {
      JaxbAnnotationModule module = new JaxbAnnotationModule();
      _mapper.registerModule(module);
    }
    return _mapper;
  }

  @Override
  public Epoch unmarshal(InputStream is) throws IOException
  {
    ObjectMapper ctx = getContext();
    XmlEpoch e = ctx.readValue(is,
                               XmlEpoch.class);
    if (e != null) {
      return e.toEpoch();
    }
    return null;
  }

  @Override
  public void marshal(Epoch value,
                      OutputStream out) throws IOException
  {
    ObjectMapper ctx = getContext();
    XmlEpoch x = new XmlEpoch(value);
    ctx.writeValue(out,
                   x);
  }

}
