/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact.impl;

import at.motriv.datamodel.entities.contact.ContactType;
import at.mountainsd.util.Utils;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public class DefaultContact extends AbstractContact
{

  private final UUID id;
  private final String name;
  private final String address1;
  private final String address2;
  private final String zip;
  private final String city;
  private final String country;
  private final String email;
  private final String www;
  private final String memo;
  private final String phone1;
  private final String phone2;
  private final String fax;
  private final String shop;
  private final Set<ContactType> types;

  public DefaultContact(UUID id, String name, String address1, String address2,
                        String zip, String city, String country, String email,
                        String www, String memo, String phone1, String phone2, String fax, String shop, Set<ContactType> types)
  {
    this.id = id != null ? id : UUID.randomUUID();
    this.name = name;
    this.address1 = Utils.nullString2EmptyString(address1);
    this.address2 = Utils.nullString2EmptyString(address2);
    this.zip = Utils.nullString2EmptyString(zip);
    this.city = Utils.nullString2EmptyString(city);
    this.country = Utils.nullString2EmptyString(country);
    this.email = Utils.nullString2EmptyString(email);
    this.www = Utils.nullString2EmptyString(www);
    this.memo = Utils.nullString2EmptyString(memo);
    this.phone1 = Utils.nullString2EmptyString(phone1);
    this.phone2 = Utils.nullString2EmptyString(phone2);
    this.fax = Utils.nullString2EmptyString(fax);
    this.shop = Utils.nullString2EmptyString(shop);
    this.types = Collections.unmodifiableSet(new HashSet<ContactType>(types));
    if (types.isEmpty()) {
      throw new IllegalArgumentException("types cannot be empty");
    }
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
  public Lookup getLookup()
  {
    return Lookup.EMPTY;
  }

  @Override
  public Set<ContactType> getTypes()
  {
    return types;
  }
}
