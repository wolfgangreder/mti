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

import at.or.reder.dcc.util.Predicates;
import at.or.reder.mti.model.Contact;
import at.or.reder.mti.model.Defect;
import at.or.reder.mti.model.Entity;
import at.or.reder.mti.model.Epoch;
import at.or.reder.mti.model.Gauge;
import at.or.reder.mti.model.Locomotive;
import at.or.reder.mti.model.ModelCondition;
import at.or.reder.mti.model.ServiceEntry;
import at.or.reder.mti.model.TractionSystem;
import at.or.reder.mti.model.utils.Money;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = Locomotive.BuilderFactory.class)
public final class DefaultLocomotiveBuilderFactory implements Locomotive.BuilderFactory
{

  public static final class DefaultLocomotive extends AbstractVehicle implements Locomotive
  {

    private final String number;
    private final String arrangement;
    private final String clazz;
    private final String company;
    private final String country;
    private final TractionSystem tractionSystem;

    private DefaultLocomotive(UUID id,
                              String name,
                              ModelCondition condition,
                              LocalDate dateOfPurchase,
                              String description,
                              ZonedDateTime lastModified,
                              Contact manufacturer,
                              UUID masterImage,
                              Money price,
                              String productNumber,
                              Contact retailer,
                              Collection<? extends Entity> entities,
                              Epoch epoch,
                              double length,
                              double width,
                              double height,
                              double weight,
                              Collection<? extends ServiceEntry> serviceEntries,
                              Collection<? extends Defect> defect,
                              String decoder,
                              int address,
                              int consistsAddress,
                              Gauge gauge,
                              Collection<? extends Object> lookupContent,
                              String number,
                              String arrangement,
                              String clazz,
                              String company,
                              String country,
                              TractionSystem tractionSystem)
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
            epoch,
            length,
            width,
            height,
            weight,
            serviceEntries,
            decoder,
            address,
            consistsAddress,
            gauge,
            defect,
            lookupContent);
      this.number = number;
      this.arrangement = arrangement;
      this.clazz = clazz;
      this.company = company;
      this.country = country;
      this.tractionSystem = tractionSystem != null ? tractionSystem : TractionSystem.OTHER;
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

    @Override
    public TractionSystem getTractionSystem()
    {
      return tractionSystem;
    }

  }

  public static final class DefaultBuilder implements Locomotive.Builder
  {

    private final Set<Entity> entities = new HashSet<>();
    private ModelCondition condition;
    private LocalDate dateOfPurchase;
    private String description;
    private UUID id;
    private ZonedDateTime lastModified;
    private Contact manufacturer;
    private UUID masterImage;
    private String name;
    private Money price;
    private String productNumber;
    private Contact retailer;
    private Epoch epoch;
    private double length;
    private double width;
    private double height;
    private double weight;
    private String decoder;
    private int address;
    private int consistsAddress;
    private Gauge gauge;
    private final Set<ServiceEntry> serviceEntries = new HashSet<>();
    private final Set<Defect> defects = new HashSet<>();
    private final Set<Object> lookupContent = new HashSet<>();
    private String number;
    private String arrangement;
    private String clazz;
    private String company;
    private String country;
    private TractionSystem tractionSystem;

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
      this.masterImage = locomotive.getMasterImage() != null ? locomotive.getMasterImage().getId() : null;
      this.name = locomotive.getName();
      this.price = locomotive.getPrice();
      this.productNumber = locomotive.getProductNumber();
      this.retailer = locomotive.getRetailer();
      this.epoch = locomotive.getEpoch();
      this.length = locomotive.getLength();
      this.height = locomotive.getHeight();
      this.weight = locomotive.getWeight();
      this.decoder = locomotive.getDecoder();
      this.address = locomotive.getAddress();
      this.consistsAddress = locomotive.getConsistsAddress();
      this.gauge = locomotive.getGauge();
      serviceEntries.clear();
      this.serviceEntries.addAll(locomotive.getServiceEntries());
      defects.clear();;
      this.defects.addAll(locomotive.getDefect());
      lookupContent.clear();
      this.number = locomotive.getLocomotiveNumber();
      this.arrangement = locomotive.getWheelArrangement();
      this.clazz = locomotive.getLocomotiveClass();
      this.company = locomotive.getCompany();
      this.country = locomotive.getCountry();
      this.tractionSystem = locomotive.getTractionSystem();
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

    @Override
    public Locomotive.Builder tractionSystem(TractionSystem ts)
    {
      this.tractionSystem = ts;
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
      if (gauge == null) {
        throw new IllegalStateException("scale==null");
      }
      if (serviceEntries.contains(null)) {
        throw new IllegalStateException("serviceEntries contains null");
      }
      if (lookupContent.contains(null)) {
        throw new IllegalStateException("lookupContent contains null");
      }
      if (defects.contains(null)) {
        throw new IllegalStateException("defects contains null");
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
    public Locomotive.Builder lastModified(ZonedDateTime ts) throws NullPointerException
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
    public Locomotive.Builder masterImage(UUID e)
    {
      this.masterImage = e;
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
    public Locomotive.Builder epoch(Epoch epoch)
    {
      this.epoch = epoch;
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
    public Locomotive.Builder address(int address)
    {
      this.address = address;
      return this;
    }

    @Override
    public Locomotive.Builder consistsAddress(int address)
    {
      this.consistsAddress = address;
      return this;
    }

    @Override
    public Locomotive.Builder decoder(String decoder)
    {
      this.decoder = decoder;
      return this;
    }

    @Override
    public Locomotive.Builder gauge(Gauge gauge) throws NullPointerException
    {
      if (gauge == null) {
        throw new NullPointerException("gauge==null");
      }
      this.gauge = gauge;
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
    public Locomotive.Builder addDefect(Defect d)
    {
      if (d != null) {
        defects.add(d);
      }
      return this;
    }

    @Override
    public Locomotive.Builder addDefects(Collection<? extends Defect> d)
    {
      if (d != null) {
        d.stream().filter(Predicates::isNotNull).forEach(defects::add);
      }
      return this;
    }

    @Override
    public Locomotive.Builder removeDefect(Defect d)
    {
      if (d != null) {
        defects.remove(d);
      }
      return this;
    }

    @Override
    public Locomotive.Builder removeDefects(Collection<? extends Defect> d)
    {
      if (d != null) {
        defects.removeAll(d);
      }
      return this;
    }

    @Override
    public Locomotive.Builder clearDefects()
    {
      defects.clear();
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
                                   epoch,
                                   length,
                                   width,
                                   height,
                                   weight,
                                   serviceEntries,
                                   defects,
                                   decoder,
                                   address,
                                   consistsAddress,
                                   gauge,
                                   lookupContent,
                                   number,
                                   arrangement,
                                   clazz,
                                   company,
                                   country,
                                   tractionSystem);
    }

  }

  @Override
  public Locomotive.Builder createBuilder()
  {
    return new DefaultBuilder();
  }

}
