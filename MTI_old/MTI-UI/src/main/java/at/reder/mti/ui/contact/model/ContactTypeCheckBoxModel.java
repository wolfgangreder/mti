/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.contact.model;

import at.reder.mti.api.datamodel.ContactType;
import at.reder.mti.ui.controls.DefaultEnumCheckboxModel;
import java.util.Collection;

public final class ContactTypeCheckBoxModel extends DefaultEnumCheckboxModel<ContactType>
{

  public ContactTypeCheckBoxModel()
  {
    super(ContactType.class);
    setOneRequired(true);
  }

  public ContactTypeCheckBoxModel(Collection<ContactType> vals)
  {
    super(ContactType.class, vals);
    setOneRequired(true);
  }

}
