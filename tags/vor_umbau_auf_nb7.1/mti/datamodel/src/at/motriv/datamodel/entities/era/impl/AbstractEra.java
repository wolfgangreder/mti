/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.era.impl;

import at.motriv.datamodel.entities.era.Era;

/**
 *
 * @author wolfi
 */
public abstract class AbstractEra implements Era
{

  @Override
  public boolean equals(Object obj)
  {
    if (obj instanceof Era) {
      return getId().equals(((Era) obj).getId());
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    return 79 * getId().hashCode();
  }

  @Override
  public String toString()
  {
    return getName() + "(" + getCountry() + ")";
  }
}
