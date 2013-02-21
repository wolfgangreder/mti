/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact.impl;

import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactType;
import at.motriv.datamodel.entities.contact.ContactXMLSupport;
import at.mountainsd.util.XMLPrintWriter;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

  private void print(Contact contact)
  {
    Map<String, String> attr = new HashMap<String, String>();
    attr.put(ContactXMLSupport.ATTR_ID, contact.getId().toString());
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
    printElement(ContactXMLSupport.Elements.phone1.name(), contact.getPhone1());
    printElement(ContactXMLSupport.Elements.phone2.name(), contact.getPhone2());
    printElement(ContactXMLSupport.Elements.fax.name(), contact.getFax());
    printElement(ContactXMLSupport.Elements.shopaddress.name(), contact.getShopAddress());
    for (ContactType c : contact.getTypes()) {
      write("<");
      write(ContactXMLSupport.Elements.type.name());
      printProperty(ContactXMLSupport.ATTR_NAME, c.name());
      write("/>");
      writeNewLine();
    }
    closeElement();
  }

  public void print(Iterable<? extends Contact> contacts)
  {
    Iterator<? extends Contact> iter = contacts.iterator();
    while (iter.hasNext()) {
      print(iter.next());
    }
  }
}
