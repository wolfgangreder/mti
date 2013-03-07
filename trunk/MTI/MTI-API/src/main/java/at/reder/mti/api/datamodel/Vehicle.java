/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel;

import java.util.Collection;
import java.util.List;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public interface Vehicle extends ScaledObject, InventoryObject, ServiceableObject, Lookup.Provider
{

  public static interface Builder<V extends Vehicle> extends InventoryObject.Builder<V>,
                                                             ScaledObject.Builder<V>,
                                                             ServiceableObject.Builder<V>,
                                                             LookupBuilder<V>
  {

    public Vehicle.Builder<? extends Vehicle> era(Era era);

    public Vehicle.Builder<? extends Vehicle> length(double len);

    public Vehicle.Builder<? extends Vehicle> width(double width);

    public Vehicle.Builder<? extends Vehicle> height(double height);

    public Vehicle.Builder<? extends Vehicle> weight(double weight);

    public Vehicle.Builder<? extends Vehicle> addDecoder(Decoder d) throws NullPointerException;

    public Vehicle.Builder<? extends Vehicle> removeDecoder(Decoder d) throws NullPointerException;

    public Vehicle.Builder<? extends Vehicle> addDecoder(Collection<? extends Decoder> d) throws NullPointerException,
                                                                                                 IllegalArgumentException;

    public Vehicle.Builder<? extends Vehicle> clearDecoder();

  }

  public Era getEra();

  public double getLength();

  public double getWidth();

  public double getHeight();

  public double getWeight();

  public List<? extends Decoder> getDecoder();

}
