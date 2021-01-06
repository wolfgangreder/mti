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
import at.or.reder.mti.model.api.Streamer;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wolfgang Reder
 */
@ServiceProvider(service = Streamer.class, path = "mti/streamer")
public final class EpochXmlStreamer extends AbstractXmlStreamer<Epoch, XmlEpoch> implements Streamer<Epoch>
{

  public EpochXmlStreamer()
  {
    super(XmlEpoch::new);
  }

  @Override
  public Class<Epoch> getStreamableClass()
  {
    return Epoch.class;
  }

}
