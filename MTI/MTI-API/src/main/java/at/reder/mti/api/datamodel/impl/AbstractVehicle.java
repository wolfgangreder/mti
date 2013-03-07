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
import at.reder.mti.api.datamodel.Decoder;
import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.Era;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.datamodel.Scale;
import at.reder.mti.api.datamodel.ServiceEntry;
import at.reder.mti.api.datamodel.Vehicle;
import at.reder.mti.api.utils.Money;
import at.reder.mti.api.utils.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author wolfi
 */
public abstract class AbstractVehicle extends AbstractInventoryObject implements Vehicle
{

  private final Era era;
  private final double length;
  private final double width;
  private final double height;
  private final double weight;
  private final List<? extends Decoder> decoder;
  private final Scale scale;
  private final List<? extends ServiceEntry> serviceEntries;
  private final Lookup lookup;

  protected AbstractVehicle(UUID id,
                            String name,
                            ModelCondition condition,
                            Timestamp dateOfPurchase,
                            String description,
                            Timestamp lastModified,
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
    this.era = era;
    this.length = length;
    this.width = width;
    this.height = height;
    this.weight = weight;
    if (serviceEntries.isEmpty()) {
      this.serviceEntries = Collections.emptyList();
    } else {
      this.serviceEntries = Collections.unmodifiableList(new ArrayList<>(serviceEntries));
    }
    if (decoder.isEmpty()) {
      this.decoder = Collections.emptyList();
    } else {
      this.decoder = Collections.unmodifiableList(new ArrayList<>(decoder));
    }
    this.scale = scale;
    if (lookupContent.isEmpty()) {
      lookup = Lookup.EMPTY;
    } else {
      lookup = Lookups.fixed(lookupContent.toArray());
    }
  }

  @Override
  public Era getEra()
  {
    return era;
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
  public List<? extends Decoder> getDecoder()
  {
    return decoder;
  }

  @Override
  public Scale getScale()
  {
    return scale;
  }

  @Override
  public List<? extends ServiceEntry> getServiceEntries()
  {
    return serviceEntries;
  }

  @Override
  public Lookup getLookup()
  {
    return lookup;
  }

}
