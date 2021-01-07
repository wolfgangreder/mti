/*
 * Copyright 2021 Wolfgang Reder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.or.reder.mti.model.impl;

import at.or.reder.dcc.util.Predicates;
import at.or.reder.dcc.util.Utils;
import at.or.reder.mti.model.Contact;
import at.or.reder.mti.model.ContactType;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wolfgang Reder
 */
@ServiceProvider(service = Contact.BuilderFactory.class)
public final class DefaultContactBuilderFactory implements Contact.BuilderFactory
{

  @Override
  public Contact.Builder createContactBuilder()
  {
    return new BuilderImpl();
  }

  private final class BuilderImpl implements Contact.Builder
  {

    private UUID id;
    private String lastName;
    private String firstName;
    private String address1;
    private String address2;
    private String city;
    private String zip;
    private String country;
    private URL emailShop;
    private URL emailService;
    private URL www;
    private URL shopAddress;
    private String phone1;
    private String phone2;
    private final Set<ContactType> types = EnumSet.noneOf(ContactType.class);
    private String memo;
    private ZonedDateTime lastModified;

    @Override
    public Contact.Builder copy(Contact contact) throws NullPointerException
    {
      this.id = Objects.requireNonNull(contact,
                                       "contact is null").getId();
      this.lastName = contact.getLastName();
      this.firstName = contact.getFirstName();
      this.address1 = contact.getAddress1();
      this.address2 = contact.getAddress2();
      this.city = contact.getCity();
      this.zip = contact.getZip();
      this.country = contact.getCountry();
      this.emailShop = contact.getEmailShop();
      this.emailService = contact.getEmailService();
      this.www = contact.getWWW();
      this.shopAddress = contact.getShopAddress();
      this.phone1 = contact.getPhone1();
      this.phone2 = contact.getPhone2();
      this.types.clear();
      this.types.addAll(contact.getTypes());
      this.memo = contact.getMemo();
      this.lastModified = contact.getLastModified();
      return this;
    }

    @Override
    public Contact.Builder id(UUID id) throws NullPointerException
    {
      this.id = id;
      return this;
    }

    @Override
    public Contact.Builder lastName(String name) throws NullPointerException, IllegalArgumentException
    {
      this.lastName = name;
      return this;
    }

    @Override
    public Contact.Builder firstName(String name)
    {
      this.firstName = name;
      return this;
    }

    @Override
    public Contact.Builder address1(String address1)
    {
      this.address1 = address1;
      return this;
    }

    @Override
    public Contact.Builder address2(String address2)
    {
      this.address2 = address2;
      return this;
    }

    @Override
    public Contact.Builder city(String city)
    {
      this.city = city;
      return this;
    }

    @Override
    public Contact.Builder zip(String zip)
    {
      this.zip = zip;
      return this;
    }

    @Override
    public Contact.Builder country(String country)
    {
      this.country = country;
      return this;
    }

    @Override
    public Contact.Builder emailShop(URL email) throws IllegalArgumentException
    {
      this.emailShop = email;
      return this;
    }

    @Override
    public Contact.Builder emailService(URL email) throws IllegalArgumentException
    {
      this.emailService = email;
      return this;
    }

    @Override
    public Contact.Builder www(URL www) throws IllegalArgumentException
    {
      this.www = www;
      return this;
    }

    @Override
    public Contact.Builder shopAddress(URL shopAddress) throws IllegalArgumentException
    {
      this.shopAddress = shopAddress;
      return this;
    }

    @Override
    public Contact.Builder phone1(String phone) throws IllegalArgumentException
    {
      this.phone1 = phone;
      return this;
    }

    @Override
    public Contact.Builder phone2(String phone) throws IllegalArgumentException
    {
      this.phone2 = phone;
      return this;
    }

    @Override
    public Contact.Builder addType(ContactType type)
    {
      if (type != null) {
        types.add(type);
      }
      return this;
    }

    @Override
    public Contact.Builder removeType(ContactType type)
    {
      if (type != null) {
        types.remove(type);
      }
      return this;
    }

    @Override
    public Contact.Builder clearTypes()
    {
      types.clear();
      return this;
    }

    @Override
    public Contact.Builder setTypes(Collection<ContactType> types)
    {
      if (types != null) {
        types.stream().filter(Predicates::isNotNull).forEach(this.types::add);
      }
      return this;
    }

    @Override
    public Contact.Builder memo(String memo)
    {
      this.memo = memo;
      return this;
    }

    @Override
    public Contact.Builder lastModified(ZonedDateTime lm) throws NullPointerException
    {
      lastModified = lm;
      return this;
    }

    @Override
    public Contact build() throws IllegalStateException, NullPointerException
    {
      return new Impl(id,
                      lastName,
                      firstName,
                      address1,
                      address2,
                      city,
                      zip,
                      country,
                      emailShop,
                      emailService,
                      www,
                      shopAddress,
                      phone1,
                      phone2,
                      types,
                      memo,
                      lastModified);
    }

  }

  private static final class Impl implements Contact
  {

    private final UUID id;
    private final String lastName;
    private final String firstName;
    private final String address1;
    private final String address2;
    private final String city;
    private final String zip;
    private final String country;
    private final URL emailShop;
    private final URL emailService;
    private final URL www;
    private final URL shopAddress;
    private final String phone1;
    private final String phone2;
    private final Set<ContactType> types;
    private final String memo;
    private final ZonedDateTime lastModified;

    public Impl(UUID id,
                String lastName,
                String firstName,
                String address1,
                String address2,
                String city,
                String zip,
                String country,
                URL emailShop,
                URL emailService,
                URL www,
                URL shopAddress,
                String phone1,
                String phone2,
                Collection<ContactType> types,
                String memo,
                ZonedDateTime lastModified)
    {
      this.id = id != null ? id : UUID.randomUUID();
      this.lastName = lastName;
      this.firstName = firstName;
      this.address1 = address1;
      this.address2 = address2;
      this.city = city;
      this.zip = zip;
      this.country = country;
      this.emailShop = emailShop;
      this.emailService = emailService;
      this.www = www;
      this.shopAddress = shopAddress;
      this.phone1 = phone1;
      this.phone2 = phone2;
      this.types = Utils.copyToUnmodifiableEnumSet(types,
                                                   ContactType.class,
                                                   Predicates::isNotNull);
      this.memo = memo;
      this.lastModified = lastModified != null ? lastModified : ZonedDateTime.now();
    }

    @Override
    public UUID getId()
    {
      return id;
    }

    @Override
    public String getLastName()
    {
      return lastName;
    }

    @Override
    public String getFirstName()
    {
      return firstName;
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
    public String getCity()
    {
      return city;
    }

    @Override
    public String getZip()
    {
      return zip;
    }

    @Override
    public String getCountry()
    {
      return country;
    }

    @Override
    public URL getEmailShop()
    {
      return emailShop;
    }

    @Override
    public URL getEmailService()
    {
      return emailService;
    }

    @Override
    public URL getWWW()
    {
      return www;
    }

    @Override
    public URL getShopAddress()
    {
      return shopAddress;
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
    public Set<ContactType> getTypes()
    {
      return types;
    }

    @Override
    public String getMemo()
    {
      return memo;
    }

    @Override
    public ZonedDateTime getLastModified()
    {
      return lastModified;
    }

    @Override
    public Lookup getLookup()
    {
      return Lookup.EMPTY;
    }

  }
}
