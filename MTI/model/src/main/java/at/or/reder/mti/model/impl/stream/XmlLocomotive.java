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

import at.or.reder.dcc.util.Predicates;
import at.or.reder.mti.model.Locomotive;
import at.or.reder.mti.model.ModelCondition;
import at.or.reder.mti.model.TractionSystem;
import at.or.reder.mti.model.api.Factories;
import at.or.reder.mti.model.utils.Money;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
public class XmlLocomotive implements XmlObject<Locomotive>
{

  @XmlAttribute(name = "number")
  private String number;
  @XmlAttribute(name = "wheel-arrangement")
  private String wheelArr;
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
  @XmlAttribute(name = "address")
  private int address;
  @XmlAttribute(name = "consists-address")
  private int consistsAddress;
  @XmlAttribute(name = "decoder")
  private String decoder;
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
  @XmlAttribute(name = "manufacturer")
  @XmlIDREF
  private XmlContact manufacturer;
  @XmlAttribute(name = "retailer")
  @XmlIDREF
  private XmlContact retailer;
  @XmlAttribute(name = "condition")
  private ModelCondition condition;
  @XmlElement(name = "entity")
  @XmlElementWrapper(name = "entities")
  @XmlIDREF
  private List<XmlEntity> entities;
  @XmlAttribute(name = "master-image")
  private String masterImage;
  @XmlAttribute(name = "last-modified")
  @XmlJavaTypeAdapter(XmlZonedDateTimeAdapter.class)
  private ZonedDateTime lastModified;
//  @XmlElement(name = "service-entry")
//  @XmlElementWrapper(name = "service-entries")
//  private List<XmlServiceEntriy> serviceEntries;
//  @XmlElement(name = "defect")
//  @XmlElementWrapper(name = "defects")
//  private List<XmlDefect> defects;

  public XmlLocomotive()
  {
  }

  public XmlLocomotive(Locomotive l)
  {
    number = l.getProductNumber();
    wheelArr = l.getWheelArrangement();
    clazz = l.getLocomotiveClass();

    company = l.getCompany();
    country = l.getCountry();
    tractionSystem = l.getTractionSystem();
    epoch = new XmlEpoch(l.getEpoch());
    length = l.getLength();
    width = l.getWidth();
    height = l.getHeight();
    weight = l.getWeight();
    address = l.getAddress();
    consistsAddress = l.getConsistsAddress();
    decoder = l.getDecoder();
    gauge = new XmlGauge(l.getGauge());
    id = l.getId().toString();
    name = l.getName();
    description = l.getDescription();
    price = l.getPrice() != null ? l.getPrice().toBigDecimal() : null;
    purchase = l.getDateOfPurchase();
    productNumber = l.getProductNumber();
    manufacturer = l.getManufacturer() != null ? new XmlContact(l.getManufacturer()) : null;
    retailer = l.getRetailer() != null ? new XmlContact(l.getRetailer()) : null;
    condition = l.getCondition();
    entities = l.getEntities().stream().map(XmlEntity::new).collect(Collectors.toList());
    masterImage = l.getMasterImage() != null ? l.getMasterImage().getId().toString() : null;
    lastModified = l.getLastModified();
  }

  @Override
  public Locomotive toModel()
  {
    Locomotive.Builder builder = Factories.getLomotiveBuilderFactory().createBuilder();
    entities.stream().
            filter(Predicates::isNotNull).
            map(XmlEntity::toModel).
            filter(Predicates::isNotNull).
            forEach(builder::addEntity);
    builder.address(address);
    builder.company(company);
    builder.condition(condition);
    builder.consistsAddress(consistsAddress);
    builder.country(country);
    builder.dateOfPurchase(purchase);
    builder.decoder(decoder);
    builder.description(description);
    builder.epoch(epoch.toModel());
    builder.gauge(gauge.toModel());
    builder.height(height);
    builder.id(UUID.fromString(id));
    builder.lastModified(lastModified);
    builder.length(length);
    builder.locomotiveClass(clazz);
    builder.locomotiveNumber(number);
    if (manufacturer != null) {
      builder.manufacturer(manufacturer.toModel());
    }
    if (masterImage != null) {
      builder.masterImage(UUID.fromString(id));
    }
    builder.name(name);
    builder.price(Money.valueOf(price));
    builder.productNumber(productNumber);
    if (retailer != null) {
      builder.retailer(retailer.toModel());
    }
    builder.tractionSystem(tractionSystem);
    builder.weight(weight);
    builder.wheelArrangement(wheelArr);
    builder.width(width);
    return builder.build();
  }

}
