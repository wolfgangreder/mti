/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel;

import java.util.Collections;
import java.util.EnumSet;
import java.util.MissingResourceException;
import java.util.Set;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author wolfi
 */
@Messages({"ContactType_RETAILER=Händler",
           "ContactType_MANUFACTURER=Hersteller",
           "ContactType_CLUB=Club",
           "ContactType_PERSONAL=Person"})
public enum ContactType
{

  RETAILER,
  MANUFACTURER,
  CLUB,
  PERSONAL;
  public static final Set<ContactType> ALL = Collections.unmodifiableSet(EnumSet.allOf(ContactType.class));

  @Override
  public String toString()
  {
    try {
      switch (this) {
        case CLUB:
          return Bundle.ContactType_CLUB();
        case MANUFACTURER:
          return Bundle.ContactType_MANUFACTURER();
        case PERSONAL:
          return Bundle.ContactType_PERSONAL();
        case RETAILER:
          return Bundle.ContactType_RETAILER();
      }
    } catch (MissingResourceException ex) {

    }
    return super.toString();
  }

}
