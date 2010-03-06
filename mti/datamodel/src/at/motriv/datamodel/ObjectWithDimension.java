/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import at.motriv.datamodel.entities.era.Era;

/**
 *
 * @author wolfi
 */
public interface ObjectWithDimension extends InventoryObject
{

  public Era getEra();

  public double getLength();

  public double getWidth();

  public double getHeight();

  public double getWeight();
}
