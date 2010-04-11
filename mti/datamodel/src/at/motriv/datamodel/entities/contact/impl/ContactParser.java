/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact.impl;

import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactXMLSupport;
import at.motriv.datamodel.entities.contact.ManufacturerBuilder;
import at.motriv.datamodel.entities.contact.RetailerBuilder;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

/**
 *
 * @author wolfi
 */
public class ContactParser extends DefaultHandler2
{

  private final List<Contact> items = new LinkedList<Contact>();
  private final StringBuilder builder = new StringBuilder();
  private UUID id;
  private String name;
  private String memo;
  private String address1;
  private String address2;
  private String city;
  private String country;
  private String zip;
  private String www;
  private String email;
  private String phone1;
  private String phone2;
  private String fax;
  private boolean isManufacturer;
  private boolean isRetailer;
  private final Deque<ContactXMLSupport.Elements> elementStack = new LinkedList<ContactXMLSupport.Elements>();

  public List<? extends Contact> getItems()
  {
    return items;
  }

  @Override
  public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws SAXException, IOException
  {
    if (ContactXMLSupport.DTD_PUBLIC_ID.equals(publicId)) {
      FileObject fo = FileUtil.getConfigFile("xml/entities/Motriv/DTD_Contacts");
      if (fo != null) {
        return new InputSource(fo.getInputStream());
      }
    }
    return super.resolveEntity(name, publicId, baseURI, systemId);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    ContactXMLSupport.Elements current = popElement();
    switch (current) {
      case memo:
        memo = builder.toString();
        break;
      case name:
        name = builder.toString();
        break;
      case address1:
        address1 = builder.toString();
        break;
      case address2:
        address2 = builder.toString();
        break;
      case city:
        city = builder.toString();
        break;
      case country:
        country = builder.toString();
        break;
      case email:
        email = builder.toString();
        break;
      case www:
        www = builder.toString();
        break;
      case zip:
        zip = builder.toString();
        break;
      case phone1:
        phone1 = builder.toString();
        break;
      case phone2:
        phone2 = builder.toString();
        break;
      case fax:
        fax = builder.toString();
        break;
      case contact:
        if (isManufacturer) {
          items.add(new ManufacturerBuilder().address1(address1).address2(address2).city(city).country(country).email(email).id(id).
                  memo(memo).name(name).www(www).zip(zip).phone1(phone1).phone2(phone2).fax(fax).build());
        }
        if (isRetailer) {
          items.add(new RetailerBuilder().address1(address1).address2(address2).city(city).country(country).email(email).id(id).
                  memo(memo).name(name).www(www).zip(zip).phone1(phone1).phone2(phone2).fax(fax).build());
        }
        name = null;
        memo = null;
        id = null;
        address1 = null;
        address2 = null;
        city = null;
        zip = null;
        country = null;
        email = null;
        www = null;
        phone1 = null;
        phone2 = null;
        fax = null;
        isManufacturer = false;
        isRetailer = false;
        break;
    }
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    ContactXMLSupport.Elements current = pushElement(qName);
    builder.setLength(0);
    switch (current) {
      case contact:
        processStartContact(attributes);
        break;
    }
  }

  private void processStartContact(Attributes attributes) throws SAXException
  {
    try {
      id = UUID.fromString(attributes.getValue(ContactXMLSupport.ATTR_ID));
      isManufacturer = Boolean.parseBoolean(attributes.getValue(ContactXMLSupport.ATTR_IS_MANUFACTURER));
      isRetailer = Boolean.parseBoolean(attributes.getValue(ContactXMLSupport.ATTR_IS_RETAILER));
    } catch (IllegalArgumentException e) {
      throw new SAXException(e);
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    builder.append(ch, start, length);
  }

  private ContactXMLSupport.Elements pushElement(String qName) throws SAXException
  {
    try {
      ContactXMLSupport.Elements result = ContactXMLSupport.Elements.valueOf(qName);
      elementStack.addFirst(result);
      return result;
    } catch (IllegalArgumentException e) {
      throw new SAXException(e);
    }
  }

  private ContactXMLSupport.Elements popElement()
  {
    return elementStack.removeFirst();
  }
}
