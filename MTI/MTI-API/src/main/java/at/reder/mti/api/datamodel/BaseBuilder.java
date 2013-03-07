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

/**
 * Basisinterface für alle BaseBuilder.
 *
 * @author wolfi
 */
public interface BaseBuilder<E>
{

  /**
   * Erzeugt eine neue Instanz.
   *
   * @return Eine neue Instance der Klassa {@code E}
   * @throws IllegalStateException falls keine Instanz erzeugt werden kann.
   */
  public E build() throws IllegalStateException;

  /**
   * Dient zum ermitteln der tatsächlichen Klasse der erzeugten Instanzen.
   *
   * @return Klassen der erzeugten Instanzen.
   */
  public Collection<? extends Class<? extends E>> getImplementingClasses();

  /**
   * Dient zum ermitteln der Klasse die Instanzen von {@code E} mittels JAXB streamen kann.
   *
   * @return
   */
  public Class<?> getXmlClass();

}
