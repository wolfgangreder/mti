/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact;

import at.mountainsd.util.XMLSupport;

/**
 *
 * @author wolfi
 */
public interface ContactXMLSupport extends XMLSupport<Contact>
{

  public static enum Elements
  {

    contacts,
    contact,
    name,
    address1,
    address2,
    zip,
    city,
    country,
    email,
    www,
    memo,
    phone1,
    phone2,
    fax,
    type,
    shopaddress
  }
  public static final String ATTR_ID = "id";
  public static final String ATTR_VERSION = "version";
  public static final String ATTR_NAME = "name";
  public static final String DTD_PUBLIC_ID = "-//at.motriv//contacts//EN";
  public static final String DTD_SYSTEM_ID = "http://www.motriv.at/dtd/contacts.dtd";
}
