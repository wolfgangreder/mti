/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact.impl;

import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactXMLSupport;
import at.motriv.datamodel.entities.contact.Manufacturer;
import at.motriv.datamodel.entities.contact.Retailer;
import at.mountainsd.util.XMLPrintWriter;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public class ContactXMLPrintWriter extends XMLPrintWriter
{

  private static class ContactWrapper
  {

    public final Contact contact;
    public boolean isManufacturer;
    public boolean isRetailer;

    public ContactWrapper(Contact contact)
    {
      this.contact = contact;
    }
  }

  public ContactXMLPrintWriter(OutputStream os)
  {
    super(os);
  }

  public void printHeader()
  {
    Map<String, String> attr = new HashMap<String, String>();
    attr.put(ContactXMLSupport.ATTR_VERSION, "1.0");
    super.printHeader(ContactXMLSupport.Elements.contacts.name(),
            attr,
            ContactXMLSupport.DTD_SYSTEM_ID,
            ContactXMLSupport.DTD_PUBLIC_ID);
  }

  private void printElement(String element, String value)
  {
    if (value != null && value.trim().length() > 0) {
      openElement(element);
      write(encodeXML(value));
      closeElement();
    }
  }

  private void print(ContactWrapper w)
  {
    Contact contact = w.contact;
    Map<String, String> attr = new HashMap<String, String>();
    attr.put(ContactXMLSupport.ATTR_ID, contact.getId().toString());
    attr.put(ContactXMLSupport.ATTR_IS_MANUFACTURER, Boolean.toString(w.isManufacturer));
    attr.put(ContactXMLSupport.ATTR_IS_RETAILER, Boolean.toString(w.isRetailer));
    openElement(ContactXMLSupport.Elements.contact.name(), attr);
    printElement(ContactXMLSupport.Elements.name.name(), contact.getName());
    printElement(ContactXMLSupport.Elements.address1.name(), contact.getAddress1());
    printElement(ContactXMLSupport.Elements.address2.name(), contact.getAddress2());
    printElement(ContactXMLSupport.Elements.city.name(), contact.getCity());
    printElement(ContactXMLSupport.Elements.country.name(), contact.getCountry());
    printElement(ContactXMLSupport.Elements.email.name(), contact.getEmail());
    printElement(ContactXMLSupport.Elements.memo.name(), contact.getMemo());
    printElement(ContactXMLSupport.Elements.www.name(), contact.getWWW());
    printElement(ContactXMLSupport.Elements.zip.name(), contact.getZip());
    printElement(ContactXMLSupport.Elements.phone1.name(),contact.getPhone1());
    printElement(ContactXMLSupport.Elements.phone2.name(),contact.getPhone2());
    printElement(ContactXMLSupport.Elements.fax.name(),contact.getFax());
    closeElement();
  }

  public void print(Iterable<? extends Contact> contacts)
  {
    Map<UUID, ContactWrapper> tmp = new HashMap<UUID, ContactWrapper>();
    Iterator<? extends Contact> iter = contacts.iterator();
    while (iter.hasNext()) {
      Contact c = iter.next();
      ContactWrapper w = tmp.get(c.getId());
      if (w == null) {
        w = new ContactWrapper(c);
        w.isManufacturer = c instanceof Manufacturer;
        w.isRetailer = c instanceof Retailer;
        tmp.put(c.getId(), w);
      } else {
        if (c instanceof Manufacturer) {
          w.isManufacturer = true;
        }
        if (c instanceof Retailer) {
          w.isRetailer = true;
        }
      }
    }
    for (ContactWrapper w : tmp.values()) {
      print(w);
    }
  }
}
