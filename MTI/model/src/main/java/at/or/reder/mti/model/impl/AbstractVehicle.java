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

import at.or.reder.dcc.util.Predicates;
import at.or.reder.dcc.util.Utils;
import at.or.reder.mti.model.Contact;
import at.or.reder.mti.model.Decoder;
import at.or.reder.mti.model.Defect;
import at.or.reder.mti.model.Entity;
import at.or.reder.mti.model.Epoch;
import at.or.reder.mti.model.Gauge;
import at.or.reder.mti.model.ModelCondition;
import at.or.reder.mti.model.ServiceEntry;
import at.or.reder.mti.model.Vehicle;
import at.or.reder.mti.model.utils.Money;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

public abstract class AbstractVehicle extends AbstractInventoryObject implements Vehicle
{

  private final Epoch epoch;
  private final double length;
  private final double width;
  private final double height;
  private final double weight;
  private final List<Decoder> decoder;
  private final Gauge gauge;
  private final List<ServiceEntry> serviceEntries;
  private final List<Defect> defect;
  private final Lookup lookup;

  protected AbstractVehicle(UUID id,
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
                            Collection<? extends Decoder> decoder,
                            Gauge gauge,
                            Collection<? extends Defect> defect,
                            Collection<? extends Object> lookupContent)
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
    this.epoch = epoch;
    this.length = length;
    this.width = width;
    this.height = height;
    this.weight = weight;
    this.serviceEntries = Utils.copyToUnmodifiableList(serviceEntries,
                                                       Predicates::isNotNull);
    this.decoder = Utils.copyToUnmodifiableList(decoder,
                                                Predicates::isNotNull);
    this.defect = Utils.copyToUnmodifiableList(defect,
                                               Predicates::isNotNull);
    this.gauge = gauge;
    if (lookupContent.isEmpty()) {
      lookup = Lookup.EMPTY;
    } else {
      lookup = Lookups.fixed(lookupContent.toArray());
    }
  }

  @Override
  public Epoch getEpoch()
  {
    return epoch;
  }

  @Override
  public double getLength()
  {
    return length;
  }

  @Override
  public double getWidth()
  {
    return width;
  }

  @Override
  public double getHeight()
  {
    return height;
  }

  @Override
  public double getWeight()
  {
    return weight;
  }

  @Override
  public List<Decoder> getDecoder()
  {
    return decoder;
  }

  @Override
  public Gauge getGauge()
  {
    return gauge;
  }

  @Override
  public List<ServiceEntry> getServiceEntries()
  {
    return serviceEntries;
  }

  @Override
  public List<Defect> getDefect()
  {
    return defect;
  }

  @Override
  public Lookup getLookup()
  {
    return lookup;
  }

}
