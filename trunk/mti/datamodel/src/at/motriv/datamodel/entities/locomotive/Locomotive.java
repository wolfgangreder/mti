/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.locomotive;

import at.motriv.datamodel.ObjectWithDimension;
import at.motriv.datamodel.ScaledObject;
import at.motriv.datamodel.ServiceableObject;
import at.mountain_sd.objects.MutatorProvider;

/**
 *
 * @author wolfi
 */
public interface Locomotive extends ObjectWithDimension, ServiceableObject, ScaledObject, MutatorProvider<MutableLocomotive>
{

  public String getWheelArragement();

  public String getKind();

  public String getLocomotiveClass();

  public String getCompany();

  public String getCountry();
}
