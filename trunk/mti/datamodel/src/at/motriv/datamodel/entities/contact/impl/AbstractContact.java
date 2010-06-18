/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact.impl;

import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.MutableContact;

/**
 *
 * @author wolfi
 */
public abstract class AbstractContact implements Contact
{

  @Override
  public MutableContact getMutator()
  {
    return new DefaultMutableContact(this);
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Contact other = (Contact) obj;
    if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 47 * hash + this.getId().hashCode();
    return hash;
  }

  @Override
  public String toString()
  {
    return getName();
  }
}
