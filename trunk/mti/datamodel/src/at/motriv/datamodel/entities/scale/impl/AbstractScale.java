/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.scale.impl;

import at.motriv.datamodel.entities.scale.Scale;

/**
 *
 * @author wolfi
 */
public abstract class AbstractScale implements Scale
{

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final AbstractScale other = (AbstractScale) obj;
    if (this.getId() != other.getId() && !this.getId().equals(other.getId())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 97 * hash + this.getId().hashCode();
    return hash;
  }

  @Override
  public String toString()
  {
    return getName();
  }
}
