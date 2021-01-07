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

import at.or.reder.mti.model.ModelCondition;
import at.or.reder.mti.model.TractionSystem;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Wolfgang Reder
 */
public class XmlLocomotive
{

  @XmlAttribute(name = "number")
  private String number;
  @XmlAttribute(name = "wheel-arrangement")
  private String wheelArr;
  @XmlAttribute(name = "kind")
  private String kind;
  @XmlAttribute(name = "class")
  private String clazz;
  @XmlAttribute(name = "company")
  private String company;
  @XmlAttribute(name = "country")
  private String country;
  @XmlAttribute(name = "traction-system")
  private TractionSystem tractionSystem;
  @XmlAttribute(name = "epoch")
  @XmlIDREF
  private XmlEpoch epoch;
  @XmlAttribute(name = "length")
  private double length;
  @XmlAttribute(name = "width")
  private double width;
  @XmlAttribute(name = "height")
  private double height;
  @XmlAttribute(name = "weight")
  private double weight;
  @XmlElement(name = "decoder")
  @XmlElementWrapper(name = "decoder-list")
  private List<XmlDecoder> deocder;
  @XmlAttribute(name = "gauge")
  @XmlIDREF
  private XmlGauge gauge;
  @XmlAttribute(name = "id")
  @XmlID
  private String id;
  @XmlAttribute(name = "name")
  private String name;
  @XmlElement(name = "description")
  private String description;
  @XmlAttribute(name = "price")
  private BigDecimal price;
  @XmlAttribute(name = "purchase-date")
  @XmlJavaTypeAdapter(XmlLocalDateAdapter.class)
  private LocalDate purchase;
  @XmlAttribute(name = "product-number")
  private String productNumber;
  @XmlElement(name = "manufacturer")
  private XmlContact manufacturer;
  @XmlElement(name = "retailer")
  private XmlContact retailer;
  @XmlAttribute(name = "condition")
  private ModelCondition condition;
  @XmlElement(name = "entity")
  @XmlElementWrapper(name = "entities")
  private List<XmlEntity> entities;
  @XmlAttribute(name = "master-image")
  private String masterImage;
  @XmlAttribute(name = "last-modified")
  @XmlJavaTypeAdapter(XmlZonedDateTimeAdapter.class)
  private ZonedDateTime lastModified;
  @XmlElement(name = "service-entry")
  @XmlElementWrapper(name = "service-entries")
  private List<XmlServiceEntriy> serviceEntries;
  @XmlElement(name = "defect")
  @XmlElementWrapper(name = "defects")
  private List<XmlDefect> defects;

}
