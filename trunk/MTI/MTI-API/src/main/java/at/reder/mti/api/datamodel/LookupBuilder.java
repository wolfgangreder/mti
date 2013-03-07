/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel;

import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public interface LookupBuilder<L extends Lookup.Provider>
{

  /**
   * Fügt ein Object zum Lookup hinzu
   *
   * @param item
   * @return {@code this}
   * @throws NullPointerException wenn {@code item==null}
   */
  public LookupBuilder<? extends L> addLookupItem(Object item) throws NullPointerException;

  /**
   * Entfernt {@code item} vom Lookup
   *
   * @param item
   * @return {@code this}
   */
  public LookupBuilder<? extends L> removeLookupItem(Object item);

  /**
   * Entfernt alle Objekte für die gilt {@code clazz.isInstance(item)==true}
   *
   * @param clazz
   * @return {@code this}
   * @throws NullPointerException wenn {@code clazz==null}
   * @see Class#isInstance(java.lang.Object)
   */
  public LookupBuilder<? extends L> removeInstancesOfFromLookup(Class<?> clazz) throws NullPointerException;

  /**
   * Entfernt alle Objecte aus dem Lookup
   *
   * @return {@code this}
   */
  public LookupBuilder<? extends L> clearLookup();

}
