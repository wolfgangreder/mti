/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact.impl;

import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactType;
import at.motriv.datamodel.entities.contact.MutableContact;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public class DefaultMutableContact extends AbstractContact implements MutableContact
{

  private UUID id;
  private String name;
  private String address1;
  private String address2;
  private String zip;
  private String city;
  private String country;
  private String email;
  private String www;
  private String memo;
  private String phone1;
  private String phone2;
  private String fax;
  private String shop;
  private Set<ContactType> types;

  public DefaultMutableContact(Set<ContactType> types)
  {
    this.types = new HashSet<ContactType>(types);
  }

  public DefaultMutableContact()
  {
    types = new HashSet<ContactType>();
  }

  public DefaultMutableContact(Contact contact)
  {
    this.address1 = contact.getAddress1();
    this.address2 = contact.getAddress2();
    this.city = contact.getCity();
    this.country = contact.getCountry();
    this.email = contact.getEmail();
    this.fax = contact.getFax();
    this.id = contact.getId();
    this.memo = contact.getMemo();
    this.name = contact.getName();
    this.phone1 = contact.getPhone1();
    this.phone2 = contact.getPhone2();
    this.shop = contact.getShopAddress();
    this.types = new HashSet<ContactType>(contact.getTypes());
    this.www = contact.getWWW();
    this.zip = contact.getZip();
  }

  @Override
  public UUID getId()
  {
    return id;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public String getAddress1()
  {
    return address1;
  }

  @Override
  public String getAddress2()
  {
    return address2;
  }

  @Override
  public String getZip()
  {
    return zip;
  }

  @Override
  public String getCity()
  {
    return city;
  }

  @Override
  public String getCountry()
  {
    return country;
  }

  @Override
  public String getEmail()
  {
    return email;
  }

  @Override
  public String getWWW()
  {
    return www;
  }

  @Override
  public String getShopAddress()
  {
    return shop;
  }

  @Override
  public String getMemo()
  {
    return memo;
  }

  @Override
  public String getPhone1()
  {
    return phone1;
  }

  @Override
  public String getPhone2()
  {
    return phone2;
  }

  @Override
  public String getFax()
  {
    return fax;
  }

  @Override
  public Set<ContactType> getTypes()
  {
    return types;
  }

  @Override
  public Lookup getLookup()
  {
    return Lookup.EMPTY;
  }

  @Override
  public void setId(UUID id)
  {
    this.id = id != null ? id : UUID.randomUUID();
  }

  @Override
  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public void setAddress1(String address1)
  {
    this.address1 = address1;
  }

  @Override
  public void setAddress2(String address2)
  {
    this.address2 = address2;
  }

  @Override
  public void setZip(String zip)
  {
    this.zip = zip;
  }

  @Override
  public void setCity(String city)
  {
    this.city = city;
  }

  @Override
  public void setCountry(String country)
  {
    this.country = country;
  }

  @Override
  public void setEmail(String email)
  {
    this.email = email;
  }

  @Override
  public void setWWW(String www)
  {
    this.www = www;
  }

  @Override
  public void setShopAddress(String shopAddress)
  {
    this.shop = shopAddress;
  }

  @Override
  public void setMemo(String memo)
  {
    this.memo = memo;
  }

  @Override
  public void setPhone1(String phone1)
  {
    this.phone1 = phone1;
  }

  @Override
  public void setPhone2(String phone2)
  {
    this.phone2 = phone2;
  }

  @Override
  public void setFax(String fax)
  {
    this.fax = fax;
  }

  @Override
  public void setTypes(Set<ContactType> types)
  {
    this.types = new HashSet<ContactType>(types);
  }

  @Override
  public Contact build()
  {
    return new DefaultContact(id, name, address1, address2, zip, city, country, email, www, memo, phone1, phone2, fax, shop, types);
  }
}
