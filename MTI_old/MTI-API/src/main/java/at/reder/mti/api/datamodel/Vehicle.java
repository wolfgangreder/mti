/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel;

import java.util.List;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public interface Vehicle extends ScaledObject, InventoryObject, ServiceableObject, Lookup.Provider
{

  public Era getEra();

  public double getLength();

  public double getWidth();

  public double getHeight();

  public double getWeight();

  public List<Decoder> getDecoder();

}
