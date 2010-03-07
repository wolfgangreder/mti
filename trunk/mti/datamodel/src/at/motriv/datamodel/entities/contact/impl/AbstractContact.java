/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact.impl;

import at.motriv.datamodel.entities.contact.Contact;
import java.util.UUID;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public class AbstractContact implements Contact
{

  protected UUID id;
  protected String name;
  protected String address1;
  protected String address2;
  protected String zip;
  protected String city;
  protected String country;
  protected String email;
  protected String www;
  protected String memo;

  protected AbstractContact()
  {
  }

  protected AbstractContact(UUID id, String name, String address1, String address2,
          String zip, String city, String country, String email,
          String www, String memo)
  {
    this.id = id;
    this.name = name;
    this.address1 = address1;
    this.address2 = address2;
    this.zip = zip;
    this.city = city;
    this.country = country;
    this.email = email;
    this.www = www;
    this.memo = memo;
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
  public String getMemo()
  {
    return memo;
  }

  @Override
  public Lookup getLookup()
  {
    return Lookup.EMPTY;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final AbstractContact other = (AbstractContact) obj;
    if (this.id != other.id && (this.id == null || !this.id.equals(id))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 47 * hash + this.id.hashCode();
    return hash;
  }

  @Override
  public String toString()
  {
    return name;
  }
}
