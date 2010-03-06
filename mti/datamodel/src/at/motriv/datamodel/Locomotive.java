/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

/**
 *
 * @author wolfi
 */
public interface Locomotive extends ServiceableObject, ScaledObject
{

  public String getWheelArragement();

  public String getKind();

  public String getLocomotiveClass();

  public String getCompany();

  public String getCountry();
}
