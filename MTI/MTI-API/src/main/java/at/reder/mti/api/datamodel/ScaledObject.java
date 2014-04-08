/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
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

  /**
   * Der Masstab des Modells
   *
   * @return niemals {@code null}
   */
  public Scale getScale();

}
