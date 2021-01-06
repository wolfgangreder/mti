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
import at.or.reder.mti.model.api.Factories;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Wolfgang Reder
 */
@XmlRootElement(name = "gauge")
public class XmlGauge implements XmlObject<Gauge>
{

  @XmlID
  @XmlAttribute(name = "id")
  private String id;
  @XmlAttribute(name = "name")
  private String name;
  @XmlAttribute(name = "scale")
  private double scale;
  @XmlAttribute(name = "track-width")
  private double trackWidth;

  public XmlGauge()
  {
  }

  public XmlGauge(Gauge scale)
  {
    id = scale.getId().toString();
    name = scale.getName();
    this.scale = scale.getScale();
    trackWidth = scale.getTrackWidth();
  }

  @Override
  public Gauge toModel()
  {
    Gauge.Builder builder = Factories.getGaugeBuilderFactory().createGaugeBuilder();
    builder.id(UUID.fromString(id));
    builder.name(name);
    builder.scale(scale);
    builder.trackWidth(trackWidth);
    return builder.build();
  }

}
