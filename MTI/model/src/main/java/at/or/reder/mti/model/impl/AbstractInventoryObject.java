/*
 * Copyright 2020-2021 Wolfgang Reder.
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
import at.or.reder.mti.model.Entity;
import at.or.reder.mti.model.InventoryObject;
import at.or.reder.mti.model.ModelCondition;
import at.or.reder.mti.model.utils.Money;
import java.time.LocalDate;
import java.time.ZonedDateTime;
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
  protected final ZonedDateTime lastModified;
  protected final Contact manufacturer;
  protected final UUID masterImage;
  protected final String name;
  protected final Money price;
  protected final String productNumber;
  protected final Contact retailer;

  protected AbstractInventoryObject(UUID id,
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
    if (masterImage != null) {
      return entities.stream().filter((e) -> e.getId().equals(masterImage)).findAny().orElse(
          null);
    }
    return null;
  }

  @Override
  public ZonedDateTime getLastModified()
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
    return Objects.equals(this.id,
                          other.id);
  }

}
