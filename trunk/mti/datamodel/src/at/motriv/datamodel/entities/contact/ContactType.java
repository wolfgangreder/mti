/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author wolfi
 */
public enum ContactType
{

  RETAILER,
  MANUFACTURER;

  public static final Set<ContactType> ALL = Collections.unmodifiableSet(new HashSet<ContactType>(Arrays.asList(ContactType.values())));
}
