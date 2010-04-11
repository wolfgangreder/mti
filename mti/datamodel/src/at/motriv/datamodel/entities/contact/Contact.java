/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact;

import java.util.UUID;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public interface Contact extends Lookup.Provider
{

  public UUID getId();

  public String getName();

  public String getAddress1();

  public String getAddress2();

  public String getZip();

  public String getCity();

  public String getCountry();

  public String getEmail();

  public String getWWW();

  public String getMemo();

  public String getPhone1();

  public String getPhone2();

  public String getFax();
}
