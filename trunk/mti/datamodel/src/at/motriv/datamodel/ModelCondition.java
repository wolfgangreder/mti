/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import org.openide.util.NbBundle;

/**
 *
 * @author wolfi
 */
public enum ModelCondition
{

  NEW,
  USED,
  HEAVY_USED,
  DEFECT,
  NOT_FUNCTIONAL,
  DESTROYED;

  @Override
  public String toString()
  {
    return NbBundle.getMessage(ModelCondition.class, "ModelCondition."+name());
  }
}
