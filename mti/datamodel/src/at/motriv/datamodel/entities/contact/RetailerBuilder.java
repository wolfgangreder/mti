/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact;

import at.motriv.datamodel.entities.contact.impl.ContactBuilder;
import at.motriv.datamodel.entities.contact.impl.DefaultRetailer;

/**
 *
 * @author wolfi
 */
public class RetailerBuilder extends ContactBuilder<Retailer>
{

  @Override
  public Retailer build()
  {
    return new DefaultRetailer(id, name, address1, address2, zip, city, country, email, www, memo, phone1, phone2, fax);
  }
}
