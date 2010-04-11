/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact.impl;

import at.motriv.datamodel.entities.contact.Contact;
import at.mountainsd.util.Builder;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public abstract class ContactBuilder<C extends Contact> extends AbstractContact implements Builder<C>
{

  public void setAddress1(String address1)
  {
    this.address1 = address1;
  }

  public ContactBuilder<C> address1(String address1)
  {
    this.address1 = address1;
    return this;
  }

  public void setAddress2(String address2)
  {
    this.address2 = address2;
  }

  public ContactBuilder<C> address2(String address2)
  {
    this.address2 = address2;
    return this;
  }

  public void setCity(String city)
  {
    this.city = city;
  }

  public ContactBuilder<C> city(String city)
  {
    this.city = city;
    return this;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  public ContactBuilder<C> country(String country)
  {
    this.country = country;
    return this;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public ContactBuilder<C> email(String email)
  {
    this.email = email;
    return this;
  }

  public void setId(UUID id)
  {
    this.id = id;
  }

  public ContactBuilder<C> id(UUID id)
  {
    this.id = id;
    return this;

  }

  public void setMemo(String memo)
  {
    this.memo = memo;
  }

  public ContactBuilder<C> memo(String memo)
  {
    this.memo = memo;
    return this;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public ContactBuilder<C> name(String name)
  {
    this.name = name;
    return this;
  }

  public void setWWW(String www)
  {
    this.www = www;
  }

  public ContactBuilder<C> www(String www)
  {
    this.www = www;
    return this;
  }

  public void setZip(String zip)
  {
    this.zip = zip;
  }

  public ContactBuilder<C> zip(String zip)
  {
    this.zip = zip;
    return this;
  }

  public void setPhone1(String phone1)
  {
    this.phone1 = phone1;
  }

  public ContactBuilder<C> phone1(String phone1)
  {
    this.phone1 = phone1;
    return this;
  }

  public void setPhone2(String phone2)
  {
    this.phone2 = phone2;
  }

  public ContactBuilder<C> phone2(String phone2)
  {
    this.phone2 = phone2;
    return this;
  }

  public void setFax(String fax)
  {
    this.fax = fax;
  }

  public ContactBuilder<C> fax(String fax)
  {
    this.fax = fax;
    return this;
  }
}
