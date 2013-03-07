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
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
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
    private final URI email;
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
                        URI email,
                        URI www,
                        URI shopAddress,
                        String memo,
                        Collection<ContactType> types,
                        Lookup lookup)
    {
      this.address1 = address1;
      this.address2 = address2;
      this.city = city;
      this.country = country;
      this.email = email;
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
    public URI getEmail()
    {
      return email;
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
    public int hashCode()
    {
      int hash = 5;
      hash = 23 * hash + Objects.hashCode(this.id);
      return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (obj == null) {
        return false;
      }
      if (this == obj) {
        return true;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final ContactImpl other = (ContactImpl) obj;
      if (!Objects.equals(this.id, other.id)) {
        return false;
      }
      return true;
    }

    @Override
    public String toString()
    {
      return "ContactImpl{" + "id=" + id + ", name=" + name + '}';
    }

  }

  public static class ContactBuilder implements Contact.Builder<Contact>
  {

    private String address1 = "";
    private String address2 = "";
    private String city = "";
    private String country = "";
    private URI email;
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
    private Set<Object> lookupContent = new HashSet<>();

    @Override
    public Contact.Builder<? extends Contact> copy(Contact contact) throws NullPointerException
    {
      if (contact == null) {
        throw new NullPointerException("contact==null");
      }
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
      this.shopAddress = contact.getShopAddress();
      this.types.clear();
      this.types.addAll(contact.getTypes());
      this.www = contact.getWWW();
      this.zip = contact.getZip();
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> id(UUID id) throws NullPointerException
    {
      if (id == null) {
        throw new NullPointerException("id==null");
      }
      this.id = id;
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> name(String name) throws NullPointerException, IllegalArgumentException
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
    public Contact.Builder<? extends Contact> address1(String address1)
    {
      if (address1 == null) {
        this.address1 = "";
      } else {
        this.address1 = address1.trim();
      }
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> address2(String address2)
    {
      if (address2 == null) {
        this.address2 = "";
      } else {
        this.address2 = address2.trim();
      }
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> city(String city)
    {
      if (city == null) {
        this.city = "";
      } else {
        this.city = city.trim();
      }
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> zip(String zip)
    {
      if (zip == null) {
        this.zip = "";
      } else {
        this.zip = zip.trim();
      }
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> country(String country)
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
        } catch (Throwable th) {
          th.getMessage();
        }
      }
      return null;
    }

    @Override
    public Contact.Builder<? extends Contact> email(URI email) throws IllegalArgumentException
    {
      String tmp = testEmail(email);
      if (tmp != null) {
        throw new IllegalArgumentException(tmp);
      }
      this.email = email;
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
    public Contact.Builder<? extends Contact> www(URI www) throws IllegalArgumentException
    {
      String tmp = testWWW(www);
      if (tmp != null) {
        throw new IllegalArgumentException(tmp);
      }
      this.www = www;
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> shopAddress(URI shopAddress) throws IllegalArgumentException
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
    public Contact.Builder<? extends Contact> phone1(String phone) throws IllegalArgumentException
    {
      String tmp = testPhone(phone);
      if (tmp != null) {
        throw new IllegalArgumentException(tmp);
      }
      this.phone1 = phone != null ? phone.trim() : "";
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> phone2(String phone) throws IllegalArgumentException
    {
      String tmp = testPhone(phone);
      if (tmp != null) {
        throw new IllegalArgumentException(tmp);
      }
      this.phone2 = phone != null ? phone.trim() : "";
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> fax(String fax) throws IllegalArgumentException
    {
      String tmp = testPhone(fax);
      if (tmp != null) {
        throw new IllegalArgumentException(tmp);
      }
      this.fax = fax != null ? fax.trim() : "";
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> addType(ContactType type) throws NullPointerException
    {
      if (type == null) {
        throw new NullPointerException("type==null");
      }
      this.types.add(type);
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> removeType(ContactType type)
    {
      if (type != null) {
        this.types.remove(type);
      }
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> clearTypes()
    {
      this.types.clear();
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> setTypes(Collection<ContactType> types) throws NullPointerException
    {
      if (types == null || types.contains(null)) {
        throw new NullPointerException("types==null");
      }
      this.types.addAll(types);
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> addLookupItem(Object item) throws NullPointerException
    {
      if (item == null) {
        throw new NullPointerException("item==null");
      }
      lookupContent.add(item);
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> removeLookupItem(Object item)
    {
      if (item != null) {
        lookupContent.remove(item);
      }
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> removeInstancesOfFromLookup(Class<?> clazz) throws NullPointerException
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
    public Contact.Builder<? extends Contact> clearLookup()
    {
      lookupContent.clear();
      return this;
    }

    @Override
    public Contact.Builder<? extends Contact> memo(String memo)
    {
      this.memo = memo != null ? memo : "";
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
      tmp = testEmail(email);
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
                             email,
                             www,
                             shopAddress,
                             memo,
                             types,
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
  public Contact.Builder<? extends Contact> createBuilder()
  {
    return new ContactBuilder();
  }

}
