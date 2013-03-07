/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 *
 * @author wolfi
 */
public enum ContactType
{

  RETAILER,
  MANUFACTURER;
  public static final Set<ContactType> ALL = Collections.unmodifiableSet(EnumSet.allOf(ContactType.class));
}
