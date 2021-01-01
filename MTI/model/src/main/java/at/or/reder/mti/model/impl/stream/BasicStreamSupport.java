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
import at.or.reder.mti.model.api.StreamSupport;
import at.or.reder.mti.model.api.Streamer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wolfgang Reder
 */
@ServiceProvider(service = StreamSupport.class)
public final class BasicStreamSupport implements StreamSupport
{

  private static final Set<StreamFormat> formats = Set.of(StreamFormat.XML,
                                                          StreamFormat.JSON);

  private static final class StreamerKey<C>
  {

    private final StreamFormat format;
    private final Class<? super C> clazz;

    public StreamerKey(StreamFormat format,
                       Class<? super C> clazz)
    {
      this.format = format;
      this.clazz = clazz;
    }

    @Override
    public int hashCode()
    {
      int hash = 7;
      hash = 97 * hash + Objects.hashCode(this.format);
      hash = 97 * hash + Objects.hashCode(this.clazz);
      return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final StreamerKey other = (StreamerKey) obj;
      if (this.format != other.format) {
        return false;
      }
      return Objects.equals(this.clazz,
                            other.clazz);
    }

  }
  private final Map<StreamerKey, Streamer<?>> streamerMap = new HashMap<>();
  private Lookup.Result<? extends Streamer> lookupResult;

  @Override
  public Set<StreamFormat> getSupportedFormats()
  {
    return formats;
  }

  @Override
  public Set<Class<?>> getSupportedClasses()
  {
    return Collections.singleton(Epoch.class);
  }

  private <C> Streamer<C> findStreamer(StreamerKey<C> key)
  {
    if (lookupResult == null) {
      Lookup lookup = Lookups.forPath("mti/streamer");
      lookupResult = lookup.lookupResult(Streamer.class);
      lookupResult.addLookupListener((ev) -> {
        synchronized (streamerMap) {
          streamerMap.clear();
        }
      });
    }
    Collection<? extends Streamer> result = lookupResult.allInstances();
    for (Streamer s : result) {
      if (s.getFormat() == key.format && s.getStreamableClass().isAssignableFrom(key.clazz)) {
        return s;
      }
    }
    return null;
  }

  @Override
  public <C> Streamer<C> getStreamer(StreamFormat format,
                                     Class<? super C> clazz)
  {
    StreamerKey<C> key = new StreamerKey<>(format,
                                           clazz);
    synchronized (streamerMap) {
      Streamer<?> result = streamerMap.get(key);
      if (result == null) {
        result = findStreamer(key);
        streamerMap.put(key,
                        result);
      }
      return (Streamer<C>) result;
    }
  }

}
