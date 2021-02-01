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

import at.or.reder.dcc.cv.CVAddress;
import at.or.reder.mti.model.api.CVResult;
import at.or.reder.mti.model.api.CVSource;
import at.or.reder.mti.model.api.CVSourceAttributes;
import at.or.reder.mti.model.api.CVSourceFactory;
import at.or.reder.mti.model.api.CVSourceListener;
import at.or.reder.mti.model.api.StoreException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Wolfgang Reder
 */
public abstract class AbstractCVSource implements CVSource
{

  private final Set<CVSourceListener> listener = new CopyOnWriteArraySet<>();
  private final Set<CVSourceAttributes> attributes;
  protected final InstanceContent ic = new InstanceContent();
  private final Lookup lookup = new AbstractLookup(ic);
  private final String name;

  protected AbstractCVSource(CVSourceFactory factory,
                             String name,
                             Collection<CVSourceAttributes> attributes)
  {
    ic.add(factory);
    this.name = name;
    if (attributes.isEmpty()) {
      this.attributes = Collections.emptySet();
    } else {
      this.attributes = Collections.unmodifiableSet(EnumSet.copyOf(attributes));
    }
  }

  @Override
  public Map<String, String> getMetaMap()
  {
    return Collections.emptyMap();
  }

  @Override
  public String getMeta(String key)
  {
    return null;
  }

  @Override
  public void setMeta(String key,
                      String value)
  {
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public Set<CVSourceAttributes> getAttributes()
  {
    return attributes;
  }

  @Override
  public void readCV(int decoderAddress,
                     CVAddress address) throws IOException, StoreException
  {
    CVResult result = readCV(decoderAddress,
                             address,
                             Long.MAX_VALUE,
                             TimeUnit.SECONDS);
    if (result != null) {
      fireCVEvent(result);
    }
  }

  @Override
  public void flush()
  {
  }

  @Override
  public void addCVSourceListener(CVSourceListener listener)
  {
    if (listener != null) {
      this.listener.add(listener);
    }
  }

  @Override
  public void removeCVSourceListener(CVSourceListener listener)
  {
    this.listener.remove(listener);
  }

  protected void fireCVEvent(CVResult result)
  {
    for (CVSourceListener l : listener) {
      l.onCVResult(result);
    }
  }

  @Override
  public Lookup getLookup()
  {
    return lookup;
  }

}
