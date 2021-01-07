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

import at.or.reder.mti.model.Contact;
import at.or.reder.mti.model.Decoder;
import at.or.reder.mti.model.Entity;
import at.or.reder.mti.model.Epoch;
import at.or.reder.mti.model.Gauge;
import at.or.reder.mti.model.MTIConfig;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.netbeans.api.keyring.Keyring;
import org.openide.util.Exceptions;
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

  private static boolean loadLogin(Map<String, String> config)
  {
    Properties props = new Properties();
    try (FileInputStream fis = new FileInputStream("password.properties")) {
      props.load(fis);
      if (props.containsKey("password") && props.containsKey("user")) {
        config.put("password",
                   props.getProperty("password"));
        config.put("user",
                   props.getProperty("user"));
        return true;
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
    return false;
  }

  public static synchronized Stores getStores() throws StoreException
  {
    if (stores == null) {
      MTIConfig cfg = Lookup.getDefault().lookup(MTIConfig.class);
      AtomicReference<UUID> providerId = new AtomicReference<>(cfg.getStoreProvider());
      Collection<? extends StoreProvider> providerList = Lookup.getDefault().lookupAll(StoreProvider.class);
      StoreProvider provider = providerList.stream().filter((p) -> p.getId().equals(providerId.get())).findAny().orElse(null);
      if (provider == null) {
        providerId.set(cfg.getDefaultStoreProvider());
        provider = providerList.stream().filter((p) -> p.getId().equals(providerId.get())).findAny().orElse(null);
      }
      Map<String, String> props = new HashMap<>();
      if (!loadLogin(props)) {
        char[] tmp = Keyring.read("mti.user");
        if (tmp != null) {
        } else {
          props.put("user",
                    new String(tmp));
        }
        tmp = Keyring.read("mti.password");
        if (tmp != null) {
          props.put("password",
                    new String(tmp));
        }
      }
      props.put("host",
                "localhost");
      props.put("datadir",
                cfg.getDatadirectory().toString());
      stores = provider.openStores(props);
    }
    return stores;
  }

  public static Epoch.BuilderFactory getEpochBuilderFactory()
  {
    return Lookup.getDefault().lookup(Epoch.BuilderFactory.class);
  }

  public static Entity.BuilderFactory getEntityBuilderFactory()
  {
    return Lookup.getDefault().lookup(Entity.BuilderFactory.class);
  }

  public static Gauge.BuilderFactory getGaugeBuilderFactory()
  {
    return Lookup.getDefault().lookup(Gauge.BuilderFactory.class);
  }

  public static Contact.BuilderFactory getContactBuilderFactory()
  {
    return Lookup.getDefault().lookup(Contact.BuilderFactory.class);
  }

  public static Decoder.BuilderFactory getDecoderBuilderFactory()
  {
    return Lookup.getDefault().lookup(Decoder.BuilderFactory.class);
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
