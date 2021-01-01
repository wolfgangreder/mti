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
package at.or.reder.mti.model.api;

import at.or.reder.mti.model.Epoch;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.openide.util.Lookup;

public final class Factories
{

  private static Stores stores;

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
  private static final Map<StreamerKey, Streamer<?>> streamerMap = new HashMap<>();
  private static Lookup.Result<StreamSupport> streamSupport;

  public static synchronized Stores getStores() throws StoreException
  {
    if (stores == null) {
      StoreProvider provider = Lookup.getDefault().lookup(StoreProvider.class);
      stores = provider.openStores(Collections.emptyMap());
    }
    return stores;
  }

  public static Epoch.BuilderFactory getEpochBuilderFactory()
  {
    return Lookup.getDefault().lookup(Epoch.BuilderFactory.class);
  }

  public static <C> Streamer<C> getStreamer(StreamFormat format,
                                            Class<C> clazz)
  {
    if (streamSupport == null) {
      streamSupport = Lookup.getDefault().lookupResult(StreamSupport.class);
      streamSupport.addLookupListener((ev) -> {
        synchronized (streamerMap) {
          streamerMap.clear();
        }
      });
    }
    Collection<? extends StreamSupport> allFound = streamSupport.allInstances();
    for (StreamSupport s : allFound) {
      Streamer<?> streamer = s.getStreamer(format,
                                           clazz);
      if (streamer != null) {
        return (Streamer<C>) streamer;
      }
    }
    return null;
  }

  private Factories()
  {
  }

}
