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
package at.or.reder.mti.model.api;

import at.or.reder.dcc.cv.CVAddress;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openide.util.Lookup;

/**
 *
 * @author Wolfgang Reder
 */
public interface CVSource extends Lookup.Provider
{

  public Map<String, String> getMetaMap();

  public String getMeta(String key);

  public void setMeta(String key,
                      String value);

  public String getName();

  public Set<CVSourceAttributes> getAttributes();

  public CVResult readCV(int decoderAddress,
                         CVAddress address,
                         long timeout,
                         TimeUnit unit) throws IOException, StoreException;

  public void readCV(int decoderAddress,
                     CVAddress address) throws IOException, StoreException;

  public void setCV(int decoderAddress,
                    CVAddress address,
                    int value) throws IOException, StoreException;

  public void flush();

  public void addCVSourceListener(CVSourceListener listener);

  public void removeCVSourceListener(CVSourceListener listener);

}
