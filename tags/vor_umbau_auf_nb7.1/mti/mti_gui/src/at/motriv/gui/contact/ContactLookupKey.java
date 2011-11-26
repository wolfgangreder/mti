/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact;

import at.motriv.datamodel.entities.contact.ContactType;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public class ContactLookupKey
{

  private final UUID id;
  private final ContactType type;

  public ContactLookupKey(UUID id, ContactType type)
  {
    this.id = id;
    this.type = type;
  }

  public UUID getId()
  {
    return id;
  }

  public ContactType getType()
  {
    return type;
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
    if (this.type != other.type) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
    hash = 37 * hash + (this.type != null ? this.type.hashCode() : 0);
    return hash;
  }
}
