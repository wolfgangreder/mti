/*
 * Copyright 2020 Wolfgang Reder.
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

import at.or.reder.mti.model.Contact;
import at.or.reder.mti.model.Decoder;
import at.or.reder.mti.model.Entity;
import at.or.reder.mti.model.Locomotive;
import at.or.reder.mti.model.ModelCondition;
import at.or.reder.mti.model.Scale;
import at.or.reder.mti.model.ServiceEntry;
import at.or.reder.mti.model.utils.Money;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;
import at.or.reder.mti.model.Epoch;

@ServiceProvider(service = Locomotive.BuilderFactory.class)
public final class DefaultLocomotiveBuilderFactory implements Locomotive.BuilderFactory
{

  public static final class DefaultLocomotive extends AbstractVehicle
          implements Locomotive
  {

    private final String number;
    private final String arrangement;
    private final String kind;
    private final String clazz;
    private final String company;
    private final String country;

    private DefaultLocomotive(UUID id,
                              String name,
                              ModelCondition condition,
                              LocalDate dateOfPurchase,
                              String description,
                              Instant lastModified,
                              Contact manufacturer,
                              Entity masterImage,
                              Money price,
                              String productNumber,
                              Contact retailer,
                              Collection<? extends Entity> entities,
                              Epoch era,
                              double length,
                              double width,
                              double height,
                              double weight,
                              Collection<? extends ServiceEntry> serviceEntries,
                              Collection<? extends Decoder> decoder,
                              Scale scale,
                              Collection<? extends Object> lookupContent,
                              String number,
                              String arrangement,
                              String kind,
                              String clazz,
                              String company,
                              String country)
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
            entities,
            era,
            length,
            width,
            height,
            weight,
            serviceEntries,
            decoder,
            scale,
            lookupContent);
      this.number = number;
      this.arrangement = arrangement;
      this.kind = kind;
      this.clazz = clazz;
      this.company = company;
      this.country = country;
    }

    @Override
    public String getLocomotiveNumber()
    {
      return number;
    }

    @Override
    public String getWheelArrangement()
    {
      return arrangement;
    }

    @Override
    public String getKind()
    {
      return kind;
    }

    @Override
    public String getLocomotiveClass()
    {
      return clazz;
    }

    @Override
    public String getCompany()
    {
      return company;
    }

    @Override
    public String getCountry()
    {
      return country;
    }

  }

  public static final class DefaultBuilder implements Locomotive.Builder
  {

    private final Set<Entity> entities = new HashSet<>();
    private ModelCondition condition;
    private LocalDate dateOfPurchase;
    private String description;
    private UUID id;
    private Instant lastModified;
    private Contact manufacturer;
    private Entity masterImage;
    private String name;
    private Money price;
    private String productNumber;
    private Contact retailer;
    private Epoch era;
    private double length;
    private double width;
    private double height;
    private double weight;
    private final Set<Decoder> decoder = new HashSet<>();
    private Scale scale;
    private final Set<ServiceEntry> serviceEntries = new HashSet<>();
    private final Set<Object> lookupContent = new HashSet<>();
    private String number;
    private String arrangement;
    private String kind;
    private String clazz;
    private String company;
    private String country;

    @Override
    public Locomotive.Builder copy(Locomotive locomotive) throws NullPointerException
    {
      if (locomotive == null) {
        throw new NullPointerException("locomotive==null");
      }
      this.entities.clear();
      this.entities.addAll(locomotive.getEntities());
      this.condition = locomotive.getCondition();
      this.dateOfPurchase = locomotive.getDateOfPurchase();
      this.description = locomotive.getDescription();
      this.id = locomotive.getId();
      this.lastModified = locomotive.getLastModified();
      this.manufacturer = locomotive.getManufacturer();
      this.masterImage = locomotive.getMasterImage();
      this.name = locomotive.getName();
      this.price = locomotive.getPrice();
      this.productNumber = locomotive.getProductNumber();
      this.retailer = locomotive.getRetailer();
      this.era = locomotive.getEra();
      this.length = locomotive.getLength();
      this.height = locomotive.getHeight();
      this.weight = locomotive.getWeight();
      this.decoder.clear();
      this.decoder.addAll(locomotive.getDecoder());
      this.scale = locomotive.getScale();
      serviceEntries.clear();
      this.serviceEntries.addAll(locomotive.getServiceEntries());
      lookupContent.clear();
      this.number = locomotive.getLocomotiveNumber();
      this.arrangement = locomotive.getWheelArrangement();
      this.kind = locomotive.getKind();
      this.clazz = locomotive.getLocomotiveClass();
      this.company = locomotive.getCompany();
      this.country = locomotive.getCountry();
      return this;
    }

    @Override
    public Locomotive.Builder locomotiveNumber(String number)
    {
      this.number = number;
      return this;
    }

    @Override
    public Locomotive.Builder wheelArrangement(String arrangement)
    {
      this.arrangement = arrangement;
      return this;
    }

    @Override
    public Locomotive.Builder kind(String kind)
    {
      this.kind = kind;
      return this;
    }

    @Override
    public Locomotive.Builder locomotiveClass(String clazz)
    {
      this.clazz = clazz;
      return this;
    }

    @Override
    public Locomotive.Builder company(String company)
    {
      this.company = company;
      return this;
    }

    @Override
    public Locomotive.Builder country(String country)
    {
      this.country = country;
      return this;
    }

    private void checkState() throws IllegalStateException
    {
      if (id == null) {
        throw new IllegalStateException("id==null");
      }
      if (name == null) {
        throw new IllegalStateException("name==null");
      }
      if (name.trim().isEmpty()) {
        throw new IllegalStateException("name is empty");
      }
      if (condition == null) {
        throw new IllegalStateException("condition==null");
      }
      if (lastModified == null) {
        throw new IllegalStateException("lastModified==null");
      }
      if (entities.contains(null)) {
        throw new IllegalStateException("entities contains null");
      }
      if (decoder.contains(null)) {
        throw new IllegalStateException("decoder contains null");
      }
      if (scale == null) {
        throw new IllegalStateException("scale==null");
      }
      if (serviceEntries.contains(null)) {
        throw new IllegalStateException("serviceEntries contains null");
      }
      if (lookupContent.contains(null)) {
        throw new IllegalStateException("lookupContent contains null");
      }

    }

    @Override
    public Locomotive.Builder condition(ModelCondition cond) throws NullPointerException
    {
      if (cond == null) {
        throw new NullPointerException("condition==null");
      }
      this.condition = cond;
      return this;
    }

    @Override
    public Locomotive.Builder dateOfPurchase(LocalDate ts)
    {
      this.dateOfPurchase = ts;
      return this;
    }

    @Override
    public Locomotive.Builder description(String descr)
    {
      if (descr == null) {
        description = "";
      } else {
        description = descr;
      }
      return this;
    }

    @Override
    public Locomotive.Builder addEntity(Entity e) throws NullPointerException
    {
      if (e == null) {
        throw new NullPointerException("entity==null");
      }
      entities.add(e);
      return this;
    }

    @Override
    public Locomotive.Builder removeEntity(Entity e)
    {
      if (e != null) {
        entities.remove(e);
      }
      return this;
    }

    @Override
    public Locomotive.Builder addEntities(Collection<? extends Entity> e) throws
            NullPointerException,
            IllegalArgumentException
    {
      if (e == null) {
        throw new NullPointerException("entities==null");
      }
      if (e.contains(null)) {
        throw new IllegalArgumentException("entities contains null");
      }
      this.entities.addAll(e);
      return this;
    }

    @Override
    public Locomotive.Builder clearEntities()
    {
      this.entities.clear();
      return this;
    }

    @Override
    public Locomotive.Builder id(UUID id) throws NullPointerException
    {
      if (id == null) {
        throw new NullPointerException("id==null");
      }
      this.id = id;
      return this;
    }

    @Override
    public Locomotive.Builder lastModified(Instant ts) throws NullPointerException
    {
      if (ts == null) {
        throw new NullPointerException("ts==null");
      }
      lastModified = ts;
      return this;
    }

    @Override
    public Locomotive.Builder manufacturer(Contact contact)
    {
      this.manufacturer = contact;
      return this;
    }

    @Override
    public Locomotive.Builder masterImage(Entity e)
    {
      this.masterImage = e;
      if (e != null) {
        addEntity(e);
      }
      return this;
    }

    @Override
    public Locomotive.Builder name(String name) throws NullPointerException,
                                                       IllegalArgumentException
    {
      if (name == null) {
        throw new NullPointerException("name==null");
      }
      if (name.trim().isEmpty()) {
        throw new IllegalArgumentException("name is empty");
      }
      this.name = name;
      return this;
    }

    @Override
    public Locomotive.Builder price(Money price)
    {
      this.price = price;
      return this;
    }

    @Override
    public Locomotive.Builder productNumber(String productNumber)
    {
      this.productNumber = productNumber;
      return this;
    }

    @Override
    public Locomotive.Builder retailer(Contact contact)
    {
      this.retailer = contact;
      return this;
    }

    @Override
    public Locomotive.Builder era(Epoch era)
    {
      this.era = era;
      return this;
    }

    @Override
    public Locomotive.Builder length(double len)
    {
      this.length = len;
      return this;
    }

    @Override
    public Locomotive.Builder width(double width)
    {
      this.width = width;
      return this;
    }

    @Override
    public Locomotive.Builder height(double height)
    {
      this.height = height;
      return this;
    }

    @Override
    public Locomotive.Builder weight(double weight)
    {
      this.weight = weight;
      return this;
    }

    @Override
    public Locomotive.Builder addDecoder(Decoder d) throws NullPointerException
    {
      if (d == null) {
        throw new NullPointerException("d==null");
      }
      decoder.add(d);
      return this;
    }

    @Override
    public Locomotive.Builder removeDecoder(Decoder d) throws NullPointerException
    {
      if (d != null) {
        decoder.remove(d);
      }
      return this;
    }

    @Override
    public Locomotive.Builder addDecoder(Collection<? extends Decoder> d) throws NullPointerException,
                                                                                 IllegalArgumentException
    {
      if (d == null) {
        throw new NullPointerException("d==null");
      }
      if (d.contains(null)) {
        throw new IllegalArgumentException("d contains null");
      }
      decoder.addAll(d);
      return this;
    }

    @Override
    public Locomotive.Builder clearDecoder()
    {
      decoder.clear();
      return this;
    }

    @Override
    public Locomotive.Builder scale(Scale scale) throws NullPointerException
    {
      if (scale == null) {
        throw new NullPointerException("scale==null");
      }
      this.scale = scale;
      return this;
    }

    @Override
    public Locomotive.Builder addServiceEntry(ServiceEntry e) throws NullPointerException
    {
      if (e == null) {
        throw new NullPointerException("e==null");
      }
      serviceEntries.add(e);
      return this;
    }

    @Override
    public Locomotive.Builder removeServiceEntry(ServiceEntry e)
    {
      if (e != null) {
        serviceEntries.remove(e);
      }
      return this;
    }

    @Override
    public Locomotive.Builder addServiceEntries(
            Collection<? extends ServiceEntry> e) throws
            NullPointerException,
            IllegalArgumentException
    {
      if (e == null) {
        throw new NullPointerException("e==null");
      }
      if (e.contains(null)) {
        throw new IllegalArgumentException("e contains null");
      }
      serviceEntries.addAll(e);
      return this;
    }

    @Override
    public Locomotive.Builder clearServiceEntries()
    {
      serviceEntries.clear();
      return this;
    }

    @Override
    public Locomotive.Builder addLookupItem(Object item) throws NullPointerException
    {
      if (item == null) {
        throw new NullPointerException("item==null");
      }
      lookupContent.add(item);
      return this;
    }

    @Override
    public Locomotive.Builder removeLookupItem(Object item)
    {
      if (item != null) {
        lookupContent.remove(item);
      }
      return this;
    }

    @Override
    public Locomotive.Builder removeInstancesOfFromLookup(Class<?> clazz) throws NullPointerException
    {
      if (clazz == null) {
        throw new NullPointerException("clazz==null");
      }
      Iterator<Object> iter = lookupContent.iterator();
      while (iter.hasNext()) {
        if (clazz.isInstance(iter.next())) {
          iter.remove();
        }
      }
      return this;
    }

    @Override
    public Locomotive.Builder clearLookup()
    {
      lookupContent.clear();
      return this;
    }

    @Override
    public Locomotive build() throws IllegalStateException
    {
      checkState();
      return new DefaultLocomotive(id,
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
                                   entities,
                                   era,
                                   length,
                                   width,
                                   height,
                                   weight,
                                   serviceEntries,
                                   decoder,
                                   scale,
                                   lookupContent,
                                   number,
                                   arrangement,
                                   kind,
                                   clazz,
                                   company,
                                   country);
    }

  }

  @Override
  public Locomotive.Builder createBuilder()
  {
    return new DefaultBuilder();
  }

}
