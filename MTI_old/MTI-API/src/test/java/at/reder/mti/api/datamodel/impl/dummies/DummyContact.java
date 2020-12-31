/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */package at.reder.mti.api.datamodel.impl.dummies;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.ContactType;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public class DummyContact implements Contact
{

  private final UUID id = UUID.randomUUID();
  private final Instant lastModified = Instant.now();

  @Override
  public UUID getId()
  {
    return id;
  }

  @Override
  public String getLastName()
  {
    return "lastname_" + id.toString();
  }

  @Override
  public String getFirstName()
  {
    return "firstname_" + id.toString();
  }

  @Override
  public String getAddress1()
  {
    return "address1_" + id.toString();
  }

  @Override
  public String getAddress2()
  {
    return "address2_" + id.toString();
  }

  @Override
  public String getZip()
  {
    return "zip_" + id.toString();
  }

  @Override
  public String getCity()
  {
    return "city_" + id.toString();
  }

  @Override
  public String getCountry()
  {
    return "country_" + id.toString();
  }

  @Override
  public URI getEmailService()
  {
    try {
      return new URI(id.toString() + ".service@nowhere.no");
    } catch (URISyntaxException ex) {
      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  @Override
  public URI getEmailShop()
  {
    try {
      return new URI(id.toString() + ".shop@nowhere.no");
    } catch (URISyntaxException ex) {
      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  @Override
  public URI getWWW()
  {
    try {
      return new URI("www.nowhere.no/" + id.toString());
    } catch (URISyntaxException ex) {
      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  @Override
  public URI getShopAddress()
  {
    try {
      return new URI("shop.nowhere.no/" + id.toString());
    } catch (URISyntaxException ex) {
      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  @Override
  public String getMemo()
  {
    return "memo_" + id.toString();
  }

  @Override
  public String getPhone1()
  {
    return "phone1_" + id.toString();
  }

  @Override
  public String getPhone2()
  {
    return "phone21_" + id.toString();
  }

  @Override
  public String getFax()
  {
    return "fax_" + id.toString();
  }

  @Override
  public Set<ContactType> getTypes()
  {
    return ContactType.ALL;
  }

  @Override
  public Instant getLastModified()
  {
    return lastModified;
  }

  @Override
  public Lookup getLookup()
  {
    return Lookup.EMPTY;
  }

}
