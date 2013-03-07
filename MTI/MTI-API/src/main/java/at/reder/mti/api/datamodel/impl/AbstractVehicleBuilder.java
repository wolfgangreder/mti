/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Decoder;
import at.reder.mti.api.datamodel.Era;
import at.reder.mti.api.datamodel.LookupBuilder;
import at.reder.mti.api.datamodel.Scale;
import at.reder.mti.api.datamodel.ScaledObject;
import at.reder.mti.api.datamodel.ServiceEntry;
import at.reder.mti.api.datamodel.ServiceableObject;
import at.reder.mti.api.datamodel.Vehicle;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author wolfi
 */
public abstract class AbstractVehicleBuilder<V extends Vehicle> extends AbstractInventoryObjectBuilder<V> implements
        Vehicle.Builder<V>
{

  protected Era era;
  protected double length;
  protected double width;
  protected double height;
  protected double weight;
  protected final Set<Decoder> decoder = new HashSet<>();
  protected Scale scale;
  protected final Set<ServiceEntry> serviceEntries = new HashSet<>();
  protected Set<Object> lookupContent = new HashSet<>();

  protected void copy(Vehicle v) throws NullPointerException
  {
    super.copy(v);
    this.era = v.getEra();
    this.length = v.getLength();
    this.width = v.getWidth();
    this.height = v.getHeight();
    this.weight = v.getWeight();
    this.decoder.clear();
    this.decoder.addAll(v.getDecoder());
    this.scale = v.getScale();
    this.serviceEntries.addAll(v.getServiceEntries());
  }

  @Override
  public Vehicle.Builder<? extends Vehicle> era(Era era)
  {
    this.era = era;
    return this;
  }

  @Override
  public Vehicle.Builder<? extends Vehicle> length(double len)
  {
    this.length = len;
    return this;
  }

  @Override
  public Vehicle.Builder<? extends Vehicle> width(double width)
  {
    this.width = width;
    return this;
  }

  @Override
  public Vehicle.Builder<? extends Vehicle> height(double height)
  {
    this.height = height;
    return this;
  }

  @Override
  public Vehicle.Builder<? extends Vehicle> weight(double weight)
  {
    this.weight = weight;
    return this;
  }

  @Override
  public Vehicle.Builder<? extends Vehicle> addDecoder(Decoder d) throws NullPointerException
  {
    if (d == null) {
      throw new NullPointerException("d==null");
    }
    decoder.add(d);
    return this;
  }

  @Override
  public Vehicle.Builder<? extends Vehicle> removeDecoder(Decoder d) throws NullPointerException
  {
    if (d != null) {
      decoder.remove(d);
    }
    return this;
  }

  @Override
  public Vehicle.Builder<? extends Vehicle> addDecoder(Collection<? extends Decoder> d) throws NullPointerException,
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
  public Vehicle.Builder<? extends Vehicle> clearDecoder()
  {
    decoder.clear();
    return this;
  }

  @Override
  public ScaledObject.Builder<? extends ScaledObject> scale(Scale scale) throws NullPointerException
  {
    if (scale == null) {
      throw new NullPointerException("scale==null");
    }
    this.scale = scale;
    return this;
  }

  @Override
  public ServiceableObject.Builder<? extends ServiceableObject> addServiceEntry(ServiceEntry e) throws NullPointerException
  {
    if (e == null) {
      throw new NullPointerException("e==null");
    }
    serviceEntries.add(e);
    return this;
  }

  @Override
  public ServiceableObject.Builder<? extends ServiceableObject> removeServiceEntry(ServiceEntry e)
  {
    if (e != null) {
      serviceEntries.remove(e);
    }
    return this;
  }

  @Override
  public ServiceableObject.Builder<? extends ServiceableObject> addServiceEntries(
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
  public ServiceableObject.Builder<? extends V> clearServiceEntries()
  {
    serviceEntries.clear();
    return this;
  }

  @Override
  public LookupBuilder<? extends V> addLookupItem(Object item) throws NullPointerException
  {
    if (item == null) {
      throw new NullPointerException("item==null");
    }
    lookupContent.add(item);
    return this;
  }

  @Override
  public LookupBuilder<? extends V> removeLookupItem(Object item)
  {
    if (item != null) {
      lookupContent.remove(item);
    }
    return this;
  }

  @Override
  public LookupBuilder<? extends V> removeInstancesOfFromLookup(
          Class<?> clazz) throws NullPointerException
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
  public LookupBuilder<? extends V> clearLookup()
  {
    lookupContent.clear();
    return this;
  }

  @Override
  protected void checkState() throws IllegalStateException
  {
    super.checkState();
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

}
