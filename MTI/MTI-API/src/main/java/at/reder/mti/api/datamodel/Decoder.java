/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel;

/**
 *
 * @author wolfi
 */
public interface Decoder extends InventoryObject
{

  public static interface Builder<D extends Decoder> extends InventoryObject.Builder<D>,
                                                             BaseBuilder<D>
  {
  }

  public static interface BuilderFactory
  {

    public Decoder.Builder<? extends Decoder> createBuilder();

  }
}
