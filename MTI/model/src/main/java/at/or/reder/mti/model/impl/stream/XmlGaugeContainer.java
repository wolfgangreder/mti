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

import at.or.reder.mti.model.Gauge;
import at.or.reder.mti.model.api.GaugeContainer;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Wolfgang Reder
 */
@XmlRootElement(name = "gauge-container")
public class XmlGaugeContainer implements XmlObject<GaugeContainer>
{

  @XmlElement(name = "gauge")
  private List<XmlGauge> gauges;

  public XmlGaugeContainer()
  {
  }

  public XmlGaugeContainer(Collection<? extends Gauge> scales)
  {
    this.gauges = scales.stream().map(XmlGauge::new).collect(Collectors.toList());
  }

  @Override
  public GaugeContainer toModel()
  {
    if (gauges != null) {
      return gauges.stream().filter((f) -> f != null).map(XmlGauge::toModel).collect(Collectors.toCollection(GaugeContainer::new));
    }
    return new GaugeContainer();
  }

}
