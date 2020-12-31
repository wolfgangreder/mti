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
import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.InventoryObject;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.utils.Money;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public abstract class AbstractInventoryObject implements InventoryObject
{

  protected final List<Entity> entities;
  protected final ModelCondition condition;
  protected final LocalDate dateOfPurchase;
  protected final String description;
  protected final UUID id;
  protected final Instant lastModified;
  protected final Contact manufacturer;
  protected final Entity masterImage;
  protected final String name;
  protected final Money price;
  protected final String productNumber;
  protected final Contact retailer;

  protected AbstractInventoryObject(UUID id,
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
    this.condition = condition;
    this.dateOfPurchase = dateOfPurchase;
    this.description = description != null ? description : "";
    if (entities.isEmpty()) {
      this.entities = Collections.emptyList();
    } else {
      this.entities = Collections.unmodifiableList(new ArrayList<>(entities));
    }
    this.id = id;
    this.lastModified = lastModified;
    this.manufacturer = manufacturer;
    this.masterImage = masterImage;
    this.name = name;
    this.price = price;
    this.productNumber = productNumber != null ? productNumber : "";
    this.retailer = retailer;
  }

  @Override
  public UUID getId()
  {
    return id;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public String getDescription()
  {
    return description;
  }

  @Override
  public Money getPrice()
  {
    return price;
  }

  @Override
  public LocalDate getDateOfPurchase()
  {
    return dateOfPurchase;
  }

  @Override
  public String getProductNumber()
  {
    return productNumber;
  }

  @Override
  public Contact getManufacturer()
  {
    return manufacturer;
  }

  @Override
  public Contact getRetailer()
  {
    return retailer;
  }

  @Override
  public ModelCondition getCondition()
  {
    return condition;
  }

  @Override
  public List<Entity> getEntities()
  {
    return entities;
  }

  @Override
  public Entity getMasterImage()
  {
    return masterImage;
  }

  @Override
  public Instant getLastModified()
  {
    return lastModified;
  }

  @Override
  public int hashCode()
  {
    int hash = 3;
    hash = 29 * hash + Objects.hashCode(this.id);
    return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final AbstractInventoryObject other = (AbstractInventoryObject) obj;
    return Objects.equals(this.id, other.id);
  }

}
