/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.InventoryObject;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.utils.MTIUtils;
import at.reder.mti.api.utils.Money;
import at.reder.mti.api.utils.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public abstract class AbstractInventoryObjectBuilder<I extends InventoryObject> implements
        InventoryObject.Builder<I>
{

  protected final Set<Entity> entities = new HashSet<>();
  protected ModelCondition condition;
  protected Timestamp dateOfPurchase;
  protected String description;
  protected UUID id;
  protected Timestamp lastModified;
  protected Contact manufacturer;
  protected Entity masterImage;
  protected String name;
  protected Money price;
  protected String productNumber;
  protected Contact retailer;

  protected <IT extends InventoryObject> void copy(IT item) throws NullPointerException
  {
    if (item == null) {
      throw new NullPointerException("item==null");
    }
    this.entities.clear();
    this.entities.addAll(item.getEntities());
    this.condition = item.getCondition();
    this.dateOfPurchase = item.getDateOfPurchase();
    this.description = item.getDescription();
    this.id = item.getId();
    this.lastModified = item.getLastModified();
    this.manufacturer = item.getManufacturer();
    this.masterImage = item.getMasterImage();
    this.name = item.getName();
    this.price = item.getPrice();
    this.productNumber = item.getProductNumber();
    this.retailer = item.getRetailer();
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
  public InventoryObject.Builder<? extends InventoryObject> condition(ModelCondition cond) throws
          NullPointerException
  {
    if (cond == null) {
      throw new NullPointerException("condition==null");
    }
    this.condition = cond;
    return this;
  }

  @Override
  public InventoryObject.Builder<? extends InventoryObject> dateOfPurchase(Timestamp ts)
  {
    this.dateOfPurchase = MTIUtils.getDayPart(ts);
    return this;
  }

  @Override
  public InventoryObject.Builder<? extends InventoryObject> description(String descr)
  {
    if (descr == null) {
      description = "";
    } else {
      description = descr;
    }
    return this;
  }

  @Override
  public InventoryObject.Builder<? extends InventoryObject> addEntity(Entity e) throws NullPointerException
  {
    if (e == null) {
      throw new NullPointerException("entity==null");
    }
    entities.add(e);
    return this;
  }

  @Override
  public InventoryObject.Builder<? extends InventoryObject> removeEntity(Entity e)
  {
    if (e != null) {
      entities.remove(e);
    }
    return this;
  }

  @Override
  public InventoryObject.Builder<? extends InventoryObject> addEntities(
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
  public InventoryObject.Builder<? extends InventoryObject> clearEntities()
  {
    this.entities.clear();
    return this;
  }

  @Override
  public InventoryObject.Builder<? extends InventoryObject> id(UUID id) throws NullPointerException
  {
    if (id == null) {
      throw new NullPointerException("id==null");
    }
    this.id = id;
    return this;
  }

  @Override
  public InventoryObject.Builder<? extends InventoryObject> lastModified(Timestamp ts) throws NullPointerException
  {
    if (ts == null) {
      throw new NullPointerException("ts==null");
    }
    lastModified = ts;
    return this;
  }

  @Override
  public InventoryObject.Builder<? extends InventoryObject> manufacturer(Contact contact)
  {
    this.manufacturer = contact;
    return this;
  }

  @Override
  public InventoryObject.Builder<? extends InventoryObject> masterImage(Entity e)
  {
    this.masterImage = e;
    if (e != null) {
      addEntity(e);
    }
    return this;
  }

  @Override
  public InventoryObject.Builder<? extends InventoryObject> name(String name) throws NullPointerException,
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
  public InventoryObject.Builder<? extends InventoryObject> price(Money price)
  {
    this.price = price;
    return this;
  }

  @Override
  public InventoryObject.Builder<? extends InventoryObject> productNumber(String productNumber)
  {
    this.productNumber = productNumber;
    return this;
  }

  @Override
  public InventoryObject.Builder<? extends InventoryObject> retailer(Contact contact)
  {
    this.retailer = contact;
    return this;
  }

}
