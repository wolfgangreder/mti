/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact.impl;

import at.motriv.datamodel.entities.contact.Retailer;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public class DefaultRetailer extends AbstractContact implements Retailer
{

  public DefaultRetailer(UUID id,
          String name,
          String address1,
          String address2,
          String zip,
          String city,
          String country,
          String email,
          String www,
          String memo)
  {
    super(id, name, address1, address2, zip, city, country, email, www, memo);
  }
}