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
package at.or.reder.mti.model.impl.stream;

import at.or.reder.mti.model.Contact;
import at.or.reder.mti.model.ContactType;
import at.or.reder.mti.model.api.Factories;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Wolfgang Reder
 */
public class XmlContact implements XmlObject<Contact>
{

  @XmlAttribute(name = "id")
  @XmlID
  private String id;
  @XmlAttribute(name = "last-name")
  private String lastName;
  @XmlAttribute(name = "first-name")
  private String firstName;
  @XmlAttribute(name = "address-1")
  private String address1;
  @XmlAttribute(name = "address-2")
  private String address2;
  @XmlAttribute(name = "city")
  private String city;
  @XmlAttribute(name = "zip")
  private String zip;
  @XmlAttribute(name = "country")
  private String country;
  @XmlAttribute(name = "email-shop")
  private URL emailShop;
  @XmlAttribute(name = "email-service")
  private URL emailService;
  @XmlAttribute(name = "www")
  private URL www;
  @XmlAttribute(name = "shop")
  private URL shopAddress;
  @XmlAttribute(name = "phone-1")
  private String phone1;
  @XmlAttribute(name = "phone-2")
  private String phone2;
  @XmlElement(name = "types")
  private String types;
  @XmlElement(name = "memo")
  private String memo;
  @XmlElement(name = "last-modified")
  @XmlJavaTypeAdapter(XmlZonedDateTimeAdapter.class)
  private ZonedDateTime lastModified;

  public XmlContact()
  {
  }

  public XmlContact(Contact c)
  {
    id = c.getId().toString();
    lastName = c.getLastName();
    firstName = c.getFirstName();
    address1 = c.getAddress1();
    address2 = c.getAddress2();
    city = c.getCity();
    zip = c.getZip();
    country = c.getCountry();
    emailShop = c.getEmailShop();
    emailService = c.getEmailService();
    www = c.getWWW();
    shopAddress = c.getShopAddress();
    phone1 = c.getPhone1();
    phone2 = c.getPhone2();
    memo = c.getMemo();
    lastModified = c.getLastModified();
    types = c.getTypes().stream().map(ContactType::name).collect(Collectors.joining(" "));
  }

  @Override
  public Contact toModel()
  {
    Contact.Builder builder = Factories.getContactBuilderFactory().createContactBuilder();
    builder.address1(address1);
    builder.address2(address2);
    builder.city(city);
    builder.country(country);
    builder.emailService(emailService);
    builder.emailShop(emailShop);
    builder.firstName(firstName);
    builder.id(UUID.fromString(id));
    builder.lastModified(lastModified);
    builder.lastName(lastName);
    builder.memo(memo);
    builder.phone1(phone1);
    builder.phone2(phone1);
    builder.shopAddress(shopAddress);
    builder.www(www);
    builder.zip(zip);
    if (types != null) {
      String[] tmp = types.split(" ");
      for (String s : tmp) {
        try {
          ContactType t = ContactType.valueOf(s);
          builder.addType(t);
        } catch (Throwable th) {
        }
      }
    }
    return builder.build();
  }

}
