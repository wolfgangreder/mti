/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.Decoder;
import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.Era;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.datamodel.Scale;
import at.reder.mti.api.datamodel.ServiceEntry;
import at.reder.mti.api.datamodel.Wagon;
import at.reder.mti.api.datamodel.xml.XWagon;
import at.reder.mti.api.utils.Money;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = Wagon.BuilderFactory.class)
public final class DefaultWagonBuilderFactory implements Wagon.BuilderFactory
{

  public static final class DefaultWagon extends AbstractVehicle implements Wagon
  {

    private final String number;
    private final int wheelcount;
    private final String kind;
    private final String clazz;
    private final String company;
    private final String country;

    private DefaultWagon(UUID id,
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
                         Era era,
                         double length,
                         double width,
                         double height,
                         double weight,
                         Collection<? extends ServiceEntry> serviceEntries,
                         Collection<? extends Decoder> decoder,
                         Scale scale,
                         Collection<? extends Object> lookupContent,
                         String number,
                         int wheelcount,
                         String kind,
                         String clazz,
                         String company,
                         String country)
    {
      super(id, name, condition, dateOfPurchase, description, lastModified, manufacturer, masterImage, price, productNumber,
            retailer, entities, era, length, width, height, weight, serviceEntries, decoder, scale, lookupContent);
      this.number = number;
      this.wheelcount = wheelcount;
      this.kind = kind;
      this.clazz = clazz;
      this.company = company;
      this.country = country;
    }

    @Override
    public String getWagonNumber()
    {
      return number;
    }

    @Override
    public int getWheelCount()
    {
      return wheelcount;
    }

    @Override
    public String getKind()
    {
      return kind;
    }

    @Override
    public String getWagonClass()
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

  public static final class DefaultBuilder implements Wagon.Builder
  {

    private static final Collection<? extends Class<? extends Wagon>> implementingClasses = Collections.singleton(
            DefaultWagon.class);
    private String number;
    private int wheelcount;
    private String kind;
    private String clazz;
    private String company;
    private String country;
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
    private Era era;
    private double length;
    private double width;
    private double height;
    private double weight;
    private final Set<Decoder> decoder = new HashSet<>();
    private Scale scale;
    private final Set<ServiceEntry> serviceEntries = new HashSet<>();
    private final Set<Object> lookupContent = new HashSet<>();

    @Override
    public Wagon.Builder copy(XWagon w) throws NullPointerException
    {
      if (w == null) {
        throw new NullPointerException("wagon==null");
      }
      number = w.getNumber();
      wheelcount = w.getWheelCount();
      kind = w.getKind();
      clazz = w.getClazz();
      company = w.getCompany();
      country = w.getCountry();
      entities.clear();
      entities.addAll(w.getEntities());
      condition = w.getCondition();
      dateOfPurchase = w.getDateOfPurchase();
      description = w.getDescription();
      id = w.getId();
      lastModified = w.getLastModified();
      manufacturer = w.getManufacturer();
      masterImage(w.getMasterImage());
      name = w.getName();
      price = w.getPrice();
      productNumber = w.getProductNumber();
      retailer = w.getRetailer();
      era = w.getEra();
      length = w.getLength();
      width = w.getWidth();
      height = w.getHeight();
      weight = w.getWeight();
      decoder.clear();
      decoder.addAll(w.getDecoder());
      scale = w.getScale();
      serviceEntries.clear();
      serviceEntries.addAll(w.getServiceEntries());
      lookupContent.clear();
      return this;
    }

    @Override
    public Wagon.Builder copy(Wagon w) throws NullPointerException
    {
      if (w == null) {
        throw new NullPointerException("wagon==null");
      }
      number = w.getWagonNumber();
      wheelcount = w.getWheelCount();
      kind = w.getKind();
      clazz = w.getWagonClass();
      company = w.getCompany();
      country = w.getCountry();
      entities.clear();
      entities.addAll(w.getEntities());
      condition = w.getCondition();
      dateOfPurchase = w.getDateOfPurchase();
      description = w.getDescription();
      id = w.getId();
      lastModified = w.getLastModified();
      manufacturer = w.getManufacturer();
      masterImage(w.getMasterImage());
      name = w.getName();
      price = w.getPrice();
      productNumber = w.getProductNumber();
      retailer = w.getRetailer();
      era = w.getEra();
      length = w.getLength();
      width = w.getWidth();
      height = w.getHeight();
      weight = w.getWeight();
      decoder.clear();
      decoder.addAll(w.getDecoder());
      scale = w.getScale();
      serviceEntries.clear();
      serviceEntries.addAll(w.getServiceEntries());
      lookupContent.clear();
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
    public Wagon.Builder condition(ModelCondition cond) throws NullPointerException
    {
      if (cond == null) {
        throw new NullPointerException("condition==null");
      }
      this.condition = cond;
      return this;
    }

    @Override
    public Wagon.Builder dateOfPurchase(LocalDate ts)
    {
      this.dateOfPurchase = ts;
      return this;
    }

    @Override
    public Wagon.Builder description(String descr)
    {
      if (descr == null) {
        description = "";
      } else {
        description = descr;
      }
      return this;
    }

    @Override
    public Wagon.Builder addEntity(Entity e) throws NullPointerException
    {
      if (e == null) {
        throw new NullPointerException("entity==null");
      }
      entities.add(e);
      return this;
    }

    @Override
    public Wagon.Builder removeEntity(Entity e)
    {
      if (e != null) {
        entities.remove(e);
      }
      return this;
    }

    @Override
    public Wagon.Builder addEntities(
            Collection<? extends Entity> e) throws
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
    public Wagon.Builder clearEntities()
    {
      this.entities.clear();
      return this;
    }

    @Override
    public Wagon.Builder id(UUID id) throws NullPointerException
    {
      if (id == null) {
        throw new NullPointerException("id==null");
      }
      this.id = id;
      return this;
    }

    @Override
    public Wagon.Builder lastModified(Instant ts) throws NullPointerException
    {
      if (ts == null) {
        throw new NullPointerException("ts==null");
      }
      lastModified = ts;
      return this;
    }

    @Override
    public Wagon.Builder manufacturer(Contact contact)
    {
      this.manufacturer = contact;
      return this;
    }

    @Override
    public Wagon.Builder masterImage(Entity e)
    {
      this.masterImage = e;
      if (e != null) {
        addEntity(e);
      }
      return this;
    }

    @Override
    public Wagon.Builder name(String name) throws NullPointerException,
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
    public Wagon.Builder price(Money price)
    {
      this.price = price;
      return this;
    }

    @Override
    public Wagon.Builder productNumber(String productNumber)
    {
      this.productNumber = productNumber;
      return this;
    }

    @Override
    public Wagon.Builder retailer(Contact contact)
    {
      this.retailer = contact;
      return this;
    }

    @Override
    public Wagon.Builder era(Era era)
    {
      this.era = era;
      return this;
    }

    @Override
    public Wagon.Builder length(double len)
    {
      this.length = len;
      return this;
    }

    @Override
    public Wagon.Builder width(double width)
    {
      this.width = width;
      return this;
    }

    @Override
    public Wagon.Builder height(double height)
    {
      this.height = height;
      return this;
    }

    @Override
    public Wagon.Builder weight(double weight)
    {
      this.weight = weight;
      return this;
    }

    @Override
    public Wagon.Builder addDecoder(Decoder d) throws NullPointerException
    {
      if (d == null) {
        throw new NullPointerException("d==null");
      }
      decoder.add(d);
      return this;
    }

    @Override
    public Wagon.Builder removeDecoder(Decoder d) throws NullPointerException
    {
      if (d != null) {
        decoder.remove(d);
      }
      return this;
    }

    @Override
    public Wagon.Builder addDecoder(Collection<? extends Decoder> d) throws NullPointerException,
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
    public Wagon.Builder clearDecoder()
    {
      decoder.clear();
      return this;
    }

    @Override
    public Wagon.Builder scale(Scale scale) throws NullPointerException
    {
      if (scale == null) {
        throw new NullPointerException("scale==null");
      }
      this.scale = scale;
      return this;
    }

    @Override
    public Wagon.Builder addServiceEntry(ServiceEntry e) throws NullPointerException
    {
      if (e == null) {
        throw new NullPointerException("e==null");
      }
      serviceEntries.add(e);
      return this;
    }

    @Override
    public Wagon.Builder removeServiceEntry(ServiceEntry e)
    {
      if (e != null) {
        serviceEntries.remove(e);
      }
      return this;
    }

    @Override
    public Wagon.Builder addServiceEntries(
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
    public Wagon.Builder clearServiceEntries()
    {
      serviceEntries.clear();
      return this;
    }

    @Override
    public Wagon.Builder addLookupItem(Object item) throws NullPointerException
    {
      if (item == null) {
        throw new NullPointerException("item==null");
      }
      lookupContent.add(item);
      return this;
    }

    @Override
    public Wagon.Builder removeLookupItem(Object item)
    {
      if (item != null) {
        lookupContent.remove(item);
      }
      return this;
    }

    @Override
    public Wagon.Builder removeInstancesOfFromLookup(Class<?> clazz) throws NullPointerException
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
    public Wagon.Builder clearLookup()
    {
      lookupContent.clear();
      return this;
    }

    @Override
    public Wagon.Builder wagonNumber(String number)
    {
      this.number = number;
      return this;
    }

    @Override
    public Wagon.Builder wheelCount(int wheelCount)
    {
      this.wheelcount = wheelCount;
      return this;
    }

    @Override
    public Wagon.Builder kind(String kind)
    {
      this.kind = kind;
      return this;
    }

    @Override
    public Wagon.Builder wagonClass(String clazz)
    {
      this.clazz = clazz;
      return this;
    }

    @Override
    public Wagon.Builder company(String company)
    {
      this.company = company;
      return this;
    }

    @Override
    public Wagon.Builder country(String country)
    {
      this.country = country;
      return this;
    }

    @Override
    public Wagon build() throws IllegalStateException
    {
      checkState();
      return new DefaultWagon(id, name, condition, dateOfPurchase, description, lastModified, manufacturer, masterImage,
                              price, productNumber, retailer, entities, era, length, width, height, weight, serviceEntries,
                              decoder, scale, lookupContent, number, wheelcount, kind, clazz, company, country);
    }

    @Override
    public Collection<? extends Class<? extends Wagon>> getImplementingClasses()
    {
      return implementingClasses;
    }

    @Override
    public Class<?> getXmlClass()
    {
      return XWagon.class;
    }

  }

  @Override
  public Wagon.Builder createBuilder()
  {
    return new DefaultBuilder();
  }

}
