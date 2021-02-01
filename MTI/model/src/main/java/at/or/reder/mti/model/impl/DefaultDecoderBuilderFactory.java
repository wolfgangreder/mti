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

import at.or.reder.dcc.util.Predicates;
import at.or.reder.dcc.util.DCCUtils;
import at.or.reder.mti.model.Contact;
import at.or.reder.mti.model.Decoder;
import at.or.reder.mti.model.Entity;
import at.or.reder.mti.model.ModelCondition;
import at.or.reder.mti.model.utils.Money;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wolfgang Reder
 */
@ServiceProvider(service = Decoder.BuilderFactory.class)
public final class DefaultDecoderBuilderFactory implements Decoder.BuilderFactory
{

  @Override
  public Decoder.Builder createDecoderBuilder()
  {
    return new Builder();
  }

  private static final class Builder implements Decoder.Builder
  {

    private ModelCondition condition;
    private LocalDate dateOfPurchase;
    private String description;
    private final List<Entity> entities = new ArrayList<>();
    private UUID id;
    private ZonedDateTime lastModified;
    private Contact manufacturer;
    private UUID masterImage;
    private String name;
    private Money price;
    private String productNumber;
    private Contact retailer;

    @Override
    public Decoder.Builder copy(Decoder decoder) throws NullPointerException
    {
      this.condition = Objects.requireNonNull(decoder,
                                              "decoder is null").getCondition();
      this.dateOfPurchase = decoder.getDateOfPurchase();
      this.description = decoder.getDescription();
      this.entities.clear();
      decoder.getEntities().stream().
          filter(Predicates::isNotNull).
          forEach(this.entities::add);
      id = decoder.getId();
      this.lastModified = decoder.getLastModified();
      this.manufacturer = decoder.getManufacturer();
      this.masterImage = decoder.getMasterImage() != null ? decoder.getMasterImage().getId() : null;
      this.name = decoder.getName();
      this.price = decoder.getPrice();
      this.productNumber = decoder.getProductNumber();
      this.retailer = decoder.getRetailer();
      return this;
    }

    @Override
    public Decoder.Builder condition(ModelCondition cond) throws NullPointerException
    {
      condition = cond;
      return this;
    }

    @Override
    public Decoder.Builder dateOfPurchase(LocalDate ts)
    {
      dateOfPurchase = ts;
      return this;
    }

    @Override
    public Decoder.Builder description(String descr)
    {
      this.description = descr;
      return this;
    }

    @Override
    public Decoder.Builder addEntity(Entity e)
    {
      if (e != null) {
        this.entities.add(e);
      }
      return this;
    }

    @Override
    public Decoder.Builder removeEntity(Entity e)
    {
      this.entities.remove(e);
      return this;
    }

    @Override
    public Decoder.Builder addEntities(Collection<? extends Entity> e)
    {
      if (e != null) {
        e.stream().filter(Predicates::isNotNull).forEach(this.entities::add);
      }
      return this;
    }

    @Override
    public Decoder.Builder clearEntities()
    {
      this.entities.clear();
      return this;
    }

    @Override
    public Decoder.Builder id(UUID id) throws NullPointerException
    {
      this.id = id;
      return this;
    }

    @Override
    public Decoder.Builder lastModified(ZonedDateTime ts) throws NullPointerException
    {
      this.lastModified = ts;
      return this;
    }

    @Override
    public Decoder.Builder manufacturer(Contact contact)
    {
      this.manufacturer = contact;
      return this;
    }

    @Override
    public Decoder.Builder masterImage(Entity e)
    {
      this.masterImage = e != null ? e.getId() : null;
      return this;
    }

    @Override
    public Decoder.Builder name(String name) throws NullPointerException, IllegalArgumentException
    {
      if (Objects.requireNonNull(name,
                                 "name is null").isBlank()) {
        throw new IllegalArgumentException("name is blank");
      }
      this.name = name;
      return this;
    }

    @Override
    public Decoder.Builder price(Money price)
    {
      this.price = price;
      return this;
    }

    @Override
    public Decoder.Builder productNumber(String productNumber)
    {
      this.productNumber = productNumber;
      return this;
    }

    @Override
    public Decoder.Builder retailer(Contact contact)
    {
      this.retailer = contact;
      return this;
    }

    @Override
    public Decoder build() throws NullPointerException, IllegalStateException
    {
      return new Impl(condition,
                      dateOfPurchase,
                      description,
                      entities,
                      id,
                      lastModified,
                      manufacturer,
                      masterImage,
                      name,
                      price,
                      productNumber,
                      retailer);
    }

  }

  private static final class Impl extends AbstractInventoryObject implements Decoder
  {

    private final ModelCondition condition;
    private final LocalDate dateOfPurchase;
    private final String description;
    private final List<Entity> entities;
    private final UUID id;
    private final ZonedDateTime lastModified;
    private final Contact manufacturer;
    private final UUID masterImage;
    private final String name;
    private final Money price;
    private final String productNumber;
    private final Contact retailer;

    public Impl(ModelCondition condition,
                LocalDate dateOfPurchase,
                String description,
                Collection<? extends Entity> entities,
                UUID id,
                ZonedDateTime lastModified,
                Contact manufacturer,
                UUID masterImage,
                String name,
                Money price,
                String productNumber,
                Contact retailer)
    {
      super(id,
            name,
            condition,
            dateOfPurchase,
            description,
            lastModified,
            manufacturer,
            masterImage,
            price,
            productNumber,
            retailer,
            entities);
      this.condition = condition;
      this.dateOfPurchase = dateOfPurchase;
      this.description = description;
      this.entities = DCCUtils.copyToUnmodifiableList(entities,
                                                   Predicates::isNotNull);
      this.id = id;
      this.lastModified = lastModified;
      this.manufacturer = manufacturer;
      this.masterImage = masterImage;
      this.name = name;
      this.price = price;
      this.productNumber = productNumber;
      this.retailer = retailer;
    }

  }
}
