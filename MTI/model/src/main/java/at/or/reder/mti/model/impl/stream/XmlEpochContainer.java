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
import at.or.reder.mti.model.api.EpochContainer;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "epoch-container")
public final class XmlEpochContainer
{

  @XmlElement(name = "epoch")
  private List<XmlEpoch> data;

  public XmlEpochContainer()
  {
  }

  public XmlEpochContainer(Collection<? extends Epoch> e)
  {
    data = e.stream().map(XmlEpoch::new).collect(Collectors.toList());
  }

  public EpochContainer toEpochContainer()
  {
    if (data != null && !data.isEmpty()) {
      return data.stream().map(XmlEpoch::toEpoch).collect(Collectors.toCollection(EpochContainer::new));
    }
    return new EpochContainer();
  }

}
