/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.ContactType;
import at.reder.mti.api.datamodel.xml.XContact;
import java.net.URI;
import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author wolfi
 */
@ServiceProvider(service = Contact.BuilderFactory.class)
public class DefaultContactBuilderFactory implements Contact.BuilderFactory
{

  private static final Collection<? extends Class<? extends Contact>> implementingClasses = Collections.singleton(
          ContactImpl.class);

  public static class ContactImpl implements Contact
  {

    private final String address1;
    private final String address2;
    private final String city;
    private final String country;
    private final URI shopEmail;
    private final URI serviceEmail;
    private final String fax;
    private final UUID id;
    private final Lookup lookup;
    private final String memo;
    private final String name;
    private final String phone1;
    private final String phone2;
    private final URI shopAddress;
    private final Set<ContactType> types;
    private final URI www;
    private final String zip;
    private final Instant lastModified;

    private ContactImpl(UUID id,
                        String name,
                        String address1,
                        String address2,
                        String city,
                        String zip,
                        String country,
                        String phone1,
                        String phone2,
                        String fax,
                        URI shopEmail,
                        URI serviceEmail,
                        URI www,
                        URI shopAddress,
                        String memo,
                        Collection<ContactType> types,
                        Instant lastModified,
                        Lookup lookup)
    {
      this.address1 = address1;
      this.address2 = address2;
      this.city = city;
      this.country = country;
      this.shopEmail = shopEmail;
      this.serviceEmail = serviceEmail;
      this.fax = fax;
      this.id = id;
      this.lookup = lookup;
      this.memo = memo;
      this.name = name;
      this.phone1 = phone1;
      this.phone2 = phone2;
      this.shopAddress = shopAddress;
      this.types = Collections.unmodifiableSet(EnumSet.copyOf(types));
      this.www = www;
      this.zip = zip;
      this.lastModified = lastModified;
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
    public URI getEmailShop()
    {
      return shopEmail;
    }

    @Override
    public URI getEmailService()
    {
      return serviceEmail;
    }

    @Override
    public URI getWWW()
    {
      return www;
    }

    @Override
    public URI getShopAddress()
    {
      return shopAddress;
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
      return lookup;
    }

    @Override
    public Instant getLastModified()
    {
      return lastModified;
    }

    @Override
    public int hashCode()
    {
      int hash = 7;
      hash = 61 * hash + Objects.hashCode(this.address1);
      hash = 61 * hash + Objects.hashCode(this.address2);
      hash = 61 * hash + Objects.hashCode(this.city);
      hash = 61 * hash + Objects.hashCode(this.country);
      hash = 61 * hash + Objects.hashCode(this.shopEmail);
      hash = 61 * hash + Objects.hashCode(this.serviceEmail);
      hash = 61 * hash + Objects.hashCode(this.fax);
      hash = 61 * hash + Objects.hashCode(this.id);
      hash = 61 * hash + Objects.hashCode(this.memo);
      hash = 61 * hash + Objects.hashCode(this.name);
      hash = 61 * hash + Objects.hashCode(this.phone1);
      hash = 61 * hash + Objects.hashCode(this.phone2);
      hash = 61 * hash + Objects.hashCode(this.shopAddress);
      hash = 61 * hash + Objects.hashCode(this.types);
      hash = 61 * hash + Objects.hashCode(this.www);
      hash = 61 * hash + Objects.hashCode(this.zip);
      return hash;
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
      final ContactImpl other = (ContactImpl) obj;
      if (!Objects.equals(this.address1, other.address1)) {
        return false;
      }
      if (!Objects.equals(this.address2, other.address2)) {
        return false;
      }
      if (!Objects.equals(this.city, other.city)) {
        return false;
      }
      if (!Objects.equals(this.country, other.country)) {
        return false;
      }
      if (!Objects.equals(this.shopEmail, other.shopEmail)) {
        return false;
      }
      if (!Objects.equals(this.serviceEmail, other.serviceEmail)) {
        return false;
      }
      if (!Objects.equals(this.fax, other.fax)) {
        return false;
      }
      if (!Objects.equals(this.id, other.id)) {
        return false;
      }
      if (!Objects.equals(this.memo, other.memo)) {
        return false;
      }
      if (!Objects.equals(this.name, other.name)) {
        return false;
      }
      if (!Objects.equals(this.phone1, other.phone1)) {
        return false;
      }
      if (!Objects.equals(this.phone2, other.phone2)) {
        return false;
      }
      if (!Objects.equals(this.shopAddress, other.shopAddress)) {
        return false;
      }
      if (!Objects.equals(this.types, other.types)) {
        return false;
      }
      if (!Objects.equals(this.www, other.www)) {
        return false;
      }
      return Objects.equals(this.zip, other.zip);
    }

    @Override
    public String toString()
    {
      return "ContactImpl{" + "id=" + id + ", name=" + name + '}';
    }

  }

  public static class ContactBuilder implements Contact.Builder
  {

    private String address1 = "";
    private String address2 = "";
    private String city = "";
    private String country = "";
    private URI shopEmail;
    private URI serviceEmail;
    private Instant lastModified;
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
    private final Set<Object> lookupContent = new HashSet<>();

    @Override
    public Contact.Builder copy(Contact contact) throws NullPointerException
    {
      if (contact == null) {
        throw new NullPointerException("contact==null");
      }
      this.address1 = contact.getAddress1();
      this.address2 = contact.getAddress2();
      this.city = contact.getCity();
      this.country = contact.getCountry();
      this.shopEmail = contact.getEmailShop();
      this.serviceEmail = contact.getEmailService();
      this.lastModified = contact.getLastModified();
      this.fax = contact.getFax();
      this.id = contact.getId();
      this.memo = contact.getMemo();
      this.name = contact.getName();
      this.phone1 = contact.getPhone1();
      this.phone2 = contact.getPhone2();
      this.shopAddress = contact.getShopAddress();
      this.types.clear();
      this.types.addAll(contact.getTypes());
      this.www = contact.getWWW();
      this.zip = contact.getZip();
      return this;
    }

    @Override
    public Contact.Builder id(UUID id) throws NullPointerException
    {
      if (id == null) {
        throw new NullPointerException("id==null");
      }
      this.id = id;
      return this;
    }

    @Override
    public Contact.Builder name(String name) throws NullPointerException, IllegalArgumentException
    {
      if (name == null) {
        throw new NullPointerException("name==null");
      }
      if (name.trim().isEmpty()) {
        throw new IllegalArgumentException("name is empty");
      }
      this.name = name.trim();
      return this;
    }

    @Override
    public Contact.Builder address1(String address1)
    {
      if (address1 == null) {
        this.address1 = "";
      } else {
        this.address1 = address1.trim();
      }
      return this;
    }

    @Override
    public Contact.Builder address2(String address2)
    {
      if (address2 == null) {
        this.address2 = "";
      } else {
        this.address2 = address2.trim();
      }
      return this;
    }

    @Override
    public Contact.Builder city(String city)
    {
      if (city == null) {
        this.city = "";
      } else {
        this.city = city.trim();
      }
      return this;
    }

    @Override
    public Contact.Builder zip(String zip)
    {
      if (zip == null) {
        this.zip = "";
      } else {
        this.zip = zip.trim();
      }
      return this;
    }

    @Override
    public Contact.Builder country(String country)
    {
      if (country == null) {
        this.country = "";
      } else {
        this.country = country.trim();
      }
      return this;
    }

    private static String testEmail(URI email)
    {
      if (email != null) {
        if (!"mailto".equals(email.getScheme())) {
          return "illegal scheme in email address";
        }
        try {
          InternetAddress.parse(email.getSchemeSpecificPart(), true);
        } catch (AddressException th) {
          return th.getMessage();
        }
      }
      return null;
    }

    @Override
    public Contact.Builder emailShop(URI email) throws IllegalArgumentException
    {
      String tmp = testEmail(email);
      if (tmp != null) {
        throw new IllegalArgumentException(tmp);
      }
      this.shopEmail = email;
      return this;
    }

    @Override
    public Contact.Builder emailService(URI email) throws IllegalArgumentException
    {
      String tmp = testEmail(email);
      if (tmp != null) {
        throw new IllegalArgumentException(tmp);
      }
      this.serviceEmail = email;
      return this;
    }

    private static String testWWW(URI www)
    {
      if (www != null) {
        if (!"http".equals(www.getScheme()) && !"https".equals(www.getScheme())) {
          return "illegal scheme";
        }
        if (www.getHost() == null || www.getHost().trim().isEmpty()) {
          return "no host";
        }
      }
      return null;
    }

    @Override
    public Contact.Builder www(URI www) throws IllegalArgumentException
    {
      String tmp = testWWW(www);
      if (tmp != null) {
        throw new IllegalArgumentException(tmp);
      }
      this.www = www;
      return this;
    }

    @Override
    public Contact.Builder shopAddress(URI shopAddress) throws IllegalArgumentException
    {
      String tmp = testWWW(shopAddress);
      if (tmp != null) {
        throw new IllegalArgumentException(tmp);
      }
      this.shopAddress = shopAddress;
      return this;
    }

    private static final Pattern phonePattern = Pattern.compile("(((\\+)|0|00)?)[1-9]+\\d*");

    private static String testPhone(String phone)
    {
      if (phone != null && !phone.trim().isEmpty()) {
        synchronized (phonePattern) {
          if (!phonePattern.matcher(phone.trim()).matches()) {
            return "illegal phonenumber";
          }
        }
      }
      return null;
    }

    @Override
    public Contact.Builder phone1(String phone) throws IllegalArgumentException
    {
      String tmp = testPhone(phone);
      if (tmp != null) {
        throw new IllegalArgumentException(tmp);
      }
      this.phone1 = phone != null ? phone.trim() : "";
      return this;
    }

    @Override
    public Contact.Builder phone2(String phone) throws IllegalArgumentException
    {
      String tmp = testPhone(phone);
      if (tmp != null) {
        throw new IllegalArgumentException(tmp);
      }
      this.phone2 = phone != null ? phone.trim() : "";
      return this;
    }

    @Override
    public Contact.Builder fax(String fax) throws IllegalArgumentException
    {
      String tmp = testPhone(fax);
      if (tmp != null) {
        throw new IllegalArgumentException(tmp);
      }
      this.fax = fax != null ? fax.trim() : "";
      return this;
    }

    @Override
    public Contact.Builder addType(ContactType type) throws NullPointerException
    {
      if (type == null) {
        throw new NullPointerException("type==null");
      }
      this.types.add(type);
      return this;
    }

    @Override
    public Contact.Builder removeType(ContactType type)
    {
      if (type != null) {
        this.types.remove(type);
      }
      return this;
    }

    @Override
    public Contact.Builder clearTypes()
    {
      this.types.clear();
      return this;
    }

    @Override
    public Contact.Builder setTypes(Collection<ContactType> types) throws NullPointerException
    {
      if (types == null || types.contains(null)) {
        throw new NullPointerException("types==null");
      }
      this.types.addAll(types);
      return this;
    }

    @Override
    public Contact.Builder addLookupItem(Object item) throws NullPointerException
    {
      if (item == null) {
        throw new NullPointerException("item==null");
      }
      lookupContent.add(item);
      return this;
    }

    @Override
    public Contact.Builder removeLookupItem(Object item)
    {
      if (item != null) {
        lookupContent.remove(item);
      }
      return this;
    }

    @Override
    public Contact.Builder removeInstancesOfFromLookup(Class<?> clazz) throws NullPointerException
    {
      if (clazz == null) {
        throw new NullPointerException("clazz==null");
      }
      Iterator<Object> iter = lookupContent.iterator();
      while (iter.hasNext()) {
        Object tmp = iter.next();
        if (clazz.isInstance(tmp)) {
          iter.remove();
        }
      }
      return this;
    }

    @Override
    public Contact.Builder clearLookup()
    {
      lookupContent.clear();
      return this;
    }

    @Override
    public Contact.Builder memo(String memo)
    {
      this.memo = memo != null ? memo : "";
      return this;
    }

    @Override
    public Contact.Builder lastModified(Instant lm) throws NullPointerException
    {
      if (lm == null) {
        throw new NullPointerException("lastModified==null");
      }
      this.lastModified = lm;
      return this;
    }

    @Override
    public ContactImpl build() throws IllegalStateException
    {
      if (id == null) {
        throw new IllegalStateException("id=null");
      }
      if (name == null || name.trim().isEmpty()) {
        throw new IllegalStateException("name is empty or null");
      }
      if (address1 == null) {
        throw new IllegalStateException("address1==null");
      }
      if (address2 == null) {
        throw new IllegalStateException("address2==null");
      }
      if (city == null) {
        throw new IllegalStateException("city==null");
      }
      if (zip == null) {
        throw new IllegalStateException("zip==null");
      }
      if (country == null) {
        throw new IllegalStateException("country==null");
      }
      String tmp = testPhone(phone1);
      if (tmp != null) {
        throw new IllegalStateException(tmp);
      }
      tmp = testPhone(phone2);
      if (tmp != null) {
        throw new IllegalStateException(tmp);
      }
      tmp = testPhone(fax);
      if (tmp != null) {
        throw new IllegalStateException(tmp);
      }
      tmp = testEmail(shopEmail);
      if (tmp != null) {
        throw new IllegalStateException(tmp);
      }
      tmp = testEmail(serviceEmail);
      if (tmp != null) {
        throw new IllegalStateException(tmp);
      }
      tmp = testWWW(www);
      if (tmp != null) {
        throw new IllegalStateException(tmp);
      }
      tmp = testWWW(shopAddress);
      if (tmp != null) {
        throw new IllegalStateException(tmp);
      }
      if (memo == null) {
        throw new IllegalStateException("memo==null");
      }
      if (types.isEmpty()) {
        throw new IllegalStateException("no type set");
      }
      if (lastModified == null) {
        lastModified = Clock.systemUTC().instant();
      }
      Lookup lookup;
      if (lookupContent.isEmpty()) {
        lookup = Lookup.EMPTY;
      } else {
        lookup = Lookups.fixed(lookupContent.toArray());
      }
      return new ContactImpl(id,
                             name,
                             address1,
                             address2,
                             city,
                             zip,
                             country,
                             phone1,
                             phone2,
                             fax,
                             shopEmail,
                             serviceEmail,
                             www,
                             shopAddress,
                             memo,
                             types,
                             lastModified,
                             lookup);
    }

    @Override
    public Collection<? extends Class<? extends Contact>> getImplementingClasses()
    {
      return implementingClasses;
    }

    @Override
    public Class<?> getXmlClass()
    {
      return XContact.class;
    }

  }

  @Override
  public Contact.Builder createBuilder()
  {
    return new ContactBuilder();
  }

}
