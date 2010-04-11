/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact;

import at.motriv.datamodel.entities.contact.Contact;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public class ContactLookupKey
{

  private final UUID id;
  private final Class<? extends Contact> classKey;

  public ContactLookupKey(UUID id, Class<? extends Contact> classKey)
  {
    this.id = id;
    this.classKey = classKey;
  }

  public UUID getId()
  {
    return id;
  }

  public Class<? extends Contact> getClassKey()
  {
    return classKey;
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
    final ContactLookupKey other = (ContactLookupKey) obj;
    if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
      return false;
    }
    if (this.classKey != other.classKey && (this.classKey == null || !this.classKey.equals(other.classKey))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
    hash = 37 * hash + (this.classKey != null ? this.classKey.hashCode() : 0);
    return hash;
  }
}
