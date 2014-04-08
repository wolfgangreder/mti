/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.ContactType;
import at.reder.mti.api.utils.xml.InstantXmlAdapter;
import java.net.URI;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openide.util.Lookup;

/**
 * JAXB kompatible Contact implementation
 *
 * @author wolfi
 */
@XmlRootElement(name = "contact", namespace = "mti")
public final class XContact
{

  public static final class Adapter extends XmlAdapter<XContact, Contact>
  {

    @Override
    public Contact unmarshal(XContact v)
    {
      if (v != null) {
        return v.toContact();
      }
      return null;
    }

    @Override
    public XContact marshal(Contact v)
    {
      if (v != null) {
        return new XContact(v);
      }
      return null;
    }

  }
  private String address1 = "";
  private String address2 = "";
  private String city = "";
  private String country = "";
  private URI emailShop;
  private URI emailService;
  private String fax = "";
  private UUID id;
  private String memo = "";
  private String name = "";
  private String phone1 = "";
  private String phone2 = "";
  private URI shopAddress;
  private final Set<ContactType> types = EnumSet.noneOf(ContactType.class);
  private URI www;
  private String zip = "";
  private Instant lastModified;

  public XContact()
  {
  }

  public XContact(Contact contact)
  {
    this.address1 = contact.getAddress1();
    this.address2 = contact.getAddress2();
    this.city = contact.getCity();
    this.country = contact.getCountry();
    this.emailShop = contact.getEmailShop();
    this.emailService = contact.getEmailService();
    this.fax = contact.getFax();
    this.id = contact.getId();
    this.memo = contact.getMemo();
    this.name = contact.getName();
    this.phone1 = contact.getPhone1();
    this.phone2 = contact.getPhone2();
    this.shopAddress = contact.getShopAddress();
    this.types.addAll(contact.getTypes());
    this.www = contact.getWWW();
    this.zip = contact.getZip();
    this.lastModified = contact.getLastModified();
  }

  public Contact toContact() throws NullPointerException, IllegalArgumentException, IllegalStateException
  {
    return Lookup.getDefault().lookup(Contact.BuilderFactory.class).createBuilder().setTypes(types).address1(address1).address2(
            address2).city(city).country(country).emailShop(emailShop).fax(fax).id(id).memo(memo).name(name).phone1(phone1).
            phone2(phone2).shopAddress(shopAddress).www(www).zip(zip).emailService(emailService).lastModified(
                    lastModified).build();
  }

  @XmlElement(name = "address1", namespace = "mti")
  public String getAddress1()
  {
    return address1;
  }

  public void setAddress1(String address1)
  {
    this.address1 = address1;
  }

  @XmlElement(name = "address2", namespace = "mti")
  public String getAddress2()
  {
    return address2;
  }

  public void setAddress2(String address2)
  {
    this.address2 = address2;
  }

  @XmlElement(name = "city", namespace = "mti")
  public String getCity()
  {
    return city;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  @XmlElement(name = "country", namespace = "mti")
  public String getCountry()
  {
    return country;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  @XmlElement(name = "emailShop", namespace = "mti")
  public URI getEmailShop()
  {
    return emailShop;
  }

  public void setEmailShop(URI email)
  {
    this.emailShop = email;
  }

  @XmlElement(name = "emailService", namespace = "mti")
  public URI getEmailService()
  {
    return emailService;
  }

  public void setEmailService(URI email)
  {
    this.emailService = email;
  }

  @XmlElement(name = "fax", namespace = "mti")
  public String getFax()
  {
    return fax;
  }

  public void setFax(String fax)
  {
    this.fax = fax;
  }

  @XmlTransient
  public UUID getId()
  {
    return id;
  }

  public void setId(UUID id)
  {
    this.id = id;
  }

  @XmlAttribute(name = "id", namespace = "mti")
  @XmlID
  public String getStringId()
  {
    return id != null ? id.toString() : null;
  }

  public void setStringId(String id)
  {
    if (id != null) {
      this.id = UUID.fromString(id);
    } else {
      this.id = null;
    }
  }

  @XmlElement(name = "memo", namespace = "mti")
  public String getMemo()
  {
    return memo;
  }

  public void setMemo(String memo)
  {
    this.memo = memo;
  }

  @XmlElement(name = "name", namespace = "mti", required = true)
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @XmlElement(name = "phone1", namespace = "mti")
  public String getPhone1()
  {
    return phone1;
  }

  public void setPhone1(String phone1)
  {
    this.phone1 = phone1;
  }

  @XmlElement(name = "phone2", namespace = "mti")
  public String getPhone2()
  {
    return phone2;
  }

  public void setPhone2(String phone2)
  {
    this.phone2 = phone2;
  }

  @XmlElement(name = "shop", namespace = "mti")
  public URI getShopAddress()
  {
    return shopAddress;
  }

  public void setShopAddress(URI shopAddress)
  {
    this.shopAddress = shopAddress;
  }

  @XmlElement(name = "www", namespace = "mti")
  public URI getWww()
  {
    return www;
  }

  public void setWww(URI www)
  {
    this.www = www;
  }

  @XmlElement(name = "zip", namespace = "mti")
  public String getZip()
  {
    return zip;
  }

  public void setZip(String zip)
  {
    this.zip = zip;
  }

  @XmlElement(name = "types", namespace = "mti")
  @XmlList
  public Set<ContactType> getTypes()
  {
    return types;
  }

  @XmlAttribute(name = "lastModified", namespace = "mti", required = true)
  @XmlJavaTypeAdapter(InstantXmlAdapter.class)
  public Instant getLastModified()
  {
    return lastModified;
  }

  public void setLastModified(Instant lm)
  {
    lastModified = lm;
  }

}
