/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.locomotive.impl;

import at.motriv.datamodel.entities.locomotive.Locomotive;

/**
 *
 * @author wolfi
 */
public abstract class AbstractLocomotive implements Locomotive
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
    final AbstractLocomotive other = (AbstractLocomotive) obj;
    if (this.getId() != other.getId() && !this.getId().equals(other.getId())) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 67 * hash + this.getId().hashCode();
    return hash;
  }

  @Override
  public String toString()
  {
    return getName();
  }
}
