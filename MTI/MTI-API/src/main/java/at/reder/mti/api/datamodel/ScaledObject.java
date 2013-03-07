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
 * Das Objekt hat einen Verkleinerungsmasstab.
 *
 * @author wolfi
 */
public interface ScaledObject
{

  public interface Builder<S extends ScaledObject>
  {

    /**
     * Setzt den Masstab
     *
     * @param scale
     * @return {@code this}
     * @throws NullPointerException wenn {@code scale==null}
     */
    public ScaledObject.Builder<? extends ScaledObject> scale(Scale scale) throws NullPointerException;

  }

  /**
   * Der Masstab des Modells
   *
   * @return niemals {@code null}
   */
  public Scale getScale();

}
