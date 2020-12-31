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
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.datamodel.xml.XDecoder;
import at.reder.mti.api.utils.Money;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author wolfi
 */
@ServiceProvider(service = Decoder.BuilderFactory.class)
public final class DefaultDecoderBuilderFactory implements Decoder.BuilderFactory
{

  public static final class DefaultDecoder extends AbstractInventoryObject implements Decoder
  {

    private DefaultDecoder(UUID id,
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
                           Collection<? extends Entity> entities)
    {
      super(id, name, condition, dateOfPurchase, description, lastModified, manufacturer, masterImage, price, productNumber,
            retailer, entities);
    }

  }

  public static final class DefaultDecoderBuilder implements Decoder.Builder
  {

    private static final Collection<? extends Class<? extends Decoder>> implementingClasses = Collections.singleton(
            DefaultDecoder.class);
    protected final Set<Entity> entities = new HashSet<>();
    protected ModelCondition condition;
    protected LocalDate dateOfPurchase;
    protected String description;
    protected UUID id;
    protected Instant lastModified;
    protected Contact manufacturer;
    protected Entity masterImage;
    protected String name;
    protected Money price;
    protected String productNumber;
    protected Contact retailer;

    @Override
    public Decoder.Builder copy(XDecoder decoder) throws NullPointerException
    {
      if (decoder == null) {
        throw new NullPointerException("decoder==null");
      }
      this.entities.clear();
      this.entities.addAll(decoder.getEntities());
      this.condition = decoder.getCondition();
      this.dateOfPurchase = decoder.getDateOfPurchase();
      this.description = decoder.getDescription();
      this.id = decoder.getId();
      this.lastModified = decoder.getLastModified();
      this.manufacturer = decoder.getManufacturer();
      this.masterImage = decoder.getMasterImage();
      this.name = decoder.getName();
      this.price = decoder.getPrice();
      this.productNumber = decoder.getProductNumber();
      this.retailer = decoder.getRetailer();
      return this;
    }

    @Override
    public Decoder.Builder copy(Decoder decoder) throws NullPointerException
    {
      if (decoder == null) {
        throw new NullPointerException("decoder==null");
      }
      this.entities.clear();
      this.entities.addAll(decoder.getEntities());
      this.condition = decoder.getCondition();
      this.dateOfPurchase = decoder.getDateOfPurchase();
      this.description = decoder.getDescription();
      this.id = decoder.getId();
      this.lastModified = decoder.getLastModified();
      this.manufacturer = decoder.getManufacturer();
      this.masterImage = decoder.getMasterImage();
      this.name = decoder.getName();
      this.price = decoder.getPrice();
      this.productNumber = decoder.getProductNumber();
      this.retailer = decoder.getRetailer();
      return this;
    }

    protected void checkState() throws IllegalStateException
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
    }

    @Override
    public Decoder.Builder condition(ModelCondition cond) throws NullPointerException
    {
      if (cond == null) {
        throw new NullPointerException("condition==null");
      }
      this.condition = cond;
      return this;
    }

    @Override
    public Decoder.Builder dateOfPurchase(LocalDate ts)
    {
      this.dateOfPurchase = ts;
      return this;
    }

    @Override
    public Decoder.Builder description(String descr)
    {
      if (descr == null) {
        description = "";
      } else {
        description = descr;
      }
      return this;
    }

    @Override
    public Decoder.Builder addEntity(Entity e) throws NullPointerException
    {
      if (e == null) {
        throw new NullPointerException("entity==null");
      }
      entities.add(e);
      return this;
    }

    @Override
    public Decoder.Builder removeEntity(Entity e)
    {
      if (e != null) {
        entities.remove(e);
      }
      return this;
    }

    @Override
    public Decoder.Builder addEntities(
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
    public Decoder.Builder clearEntities()
    {
      this.entities.clear();
      return this;
    }

    @Override
    public Decoder.Builder id(UUID id) throws NullPointerException
    {
      if (id == null) {
        throw new NullPointerException("id==null");
      }
      this.id = id;
      return this;
    }

    @Override
    public Decoder.Builder lastModified(Instant ts) throws NullPointerException
    {
      if (ts == null) {
        throw new NullPointerException("ts==null");
      }
      lastModified = ts;
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
      this.masterImage = e;
      if (e != null) {
        addEntity(e);
      }
      return this;
    }

    @Override
    public Decoder.Builder name(String name) throws NullPointerException,
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
    public Decoder build() throws IllegalStateException
    {
      checkState();
      return new DefaultDecoder(id, name, condition, dateOfPurchase, description, lastModified, manufacturer, masterImage, price,
                                productNumber, retailer, entities);
    }

    @Override
    public Collection<? extends Class<? extends Decoder>> getImplementingClasses()
    {
      return implementingClasses;
    }

    @Override
    public Class<?> getXmlClass()
    {
      return XDecoder.class;
    }

  }

  @Override
  public Decoder.Builder createBuilder()
  {
    return new DefaultDecoderBuilder();
  }

}
