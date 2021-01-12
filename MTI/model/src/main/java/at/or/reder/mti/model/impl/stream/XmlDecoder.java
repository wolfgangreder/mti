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
import at.or.reder.mti.model.Decoder;
import at.or.reder.mti.model.Entity;
import at.or.reder.mti.model.ModelCondition;
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
public class XmlDecoder implements XmlObject<Decoder>
{

  @XmlAttribute(name = "condition")
  private ModelCondition condition;
  @XmlAttribute(name = "purchase-date")
  @XmlJavaTypeAdapter(XmlLocalDateAdapter.class)
  private LocalDate dateOfPurchase;
  @XmlElement(name = "description")
  private String description;
  @XmlElement(name = "entity")
  @XmlElementWrapper(name = "entities")
  private List<XmlEntity> entities;
  @XmlAttribute(name = "id")
  @XmlID
  private UUID id;
  @XmlAttribute(name = "last-modified")
  @XmlJavaTypeAdapter(XmlZonedDateTimeAdapter.class)
  private ZonedDateTime lastModified;
  @XmlAttribute(name = "manufacturer")
  @XmlIDREF
  private XmlContact manufacturer;
  @XmlAttribute(name = "master-image")
  private UUID masterImage;
  @XmlAttribute(name = "name")
  private String name;
  @XmlAttribute(name = "price")
  private BigDecimal price;
  @XmlAttribute(name = "product-number")
  private String productNumber;
  @XmlAttribute(name = "retailer")
  @XmlIDREF
  private XmlContact retailer;

  public XmlDecoder()
  {
  }

  public XmlDecoder(Decoder decoder)
  {
    condition = decoder.getCondition();
    dateOfPurchase = decoder.getDateOfPurchase();
    description = decoder.getDescription();
    entities = decoder.getEntities().stream().filter(Predicates::isNotNull).map(XmlEntity::new).collect(Collectors.toList());
    id = decoder.getId();
    lastModified = decoder.getLastModified();
    manufacturer = decoder.getManufacturer() != null ? new XmlContact(decoder.getManufacturer()) : null;
    masterImage = decoder.getMasterImage() != null ? decoder.getMasterImage().getId() : null;
    name = decoder.getName();
    price = decoder.getPrice() != null ? decoder.getPrice().toBigDecimal() : null;
    productNumber = decoder.getProductNumber();
    retailer = decoder.getRetailer() != null ? new XmlContact(decoder.getRetailer()) : null;
  }

  @Override
  public Decoder toModel()
  {
    Decoder.Builder builder = Factories.getDecoderBuilderFactory().createDecoderBuilder();
    builder.condition(condition);
    builder.dateOfPurchase(dateOfPurchase);
    builder.description(description);
    builder.id(id);
    builder.lastModified(lastModified);
    if (manufacturer != null) {
      builder.manufacturer(manufacturer.toModel());
    }
    builder.name(name);
    builder.price(Money.valueOf(price));
    builder.productNumber(productNumber);
    if (retailer != null) {
      builder.retailer(retailer.toModel());
    }
    List<Entity> e = entities.stream().map(XmlEntity::toModel).filter(Predicates::isNotNull).collect(Collectors.toList());
    Entity mi = e.stream().filter((en) -> en.getId().equals(this.masterImage)).findAny().orElse(null);
    builder.addEntities(e);
    builder.masterImage(mi);
    return builder.build();
  }

}
