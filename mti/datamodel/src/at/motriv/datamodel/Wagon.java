/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

/**
 *
 * @author wolfi
 */
public interface Wagon extends ServiceableObject, ScaledObject
{

  public String getKind();

  public String getWagonClass();

  public String getCompany();

  public String getCountry();
}
