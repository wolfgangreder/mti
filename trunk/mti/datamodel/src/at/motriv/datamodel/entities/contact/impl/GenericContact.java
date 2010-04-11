/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact.impl;

import java.util.UUID;

/**
 *
 * @author wolfi
 */
public class GenericContact extends AbstractContact
{

  public GenericContact(UUID id,
          String name,
          String address1,
          String address2,
          String zip,
          String city,
          String country,
          String email,
          String www,
          String memo,
          String phone1,
          String phone2,
          String fax)
  {
    super(id, name, address1, address2, zip, city, country, email, www, memo, phone1, phone2, fax);
  }
}
