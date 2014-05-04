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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openide.util.Lookup;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 *
 * @author wolfi
 */
public class XContactTest
{

  private final URI emailShop;
  private final URI emailService;
  private final UUID id = UUID.randomUUID();
  private final URI shop;
  private final URI www;
  private final Contact.BuilderFactory factory = Lookup.getDefault().lookup(Contact.BuilderFactory.class);
  private final String address1 = "address1";
  private final String address2 = "address2";
  private final String city = "city";
  private final String country = "country";
  private final String fax = "1";
  private final String memo = "memo";
  private final String lastName = "reder";
  private final String firstName = "wolfi";
  private final String phone1 = "2";
  private final String phone2 = "3";
  private final String zip = "zip";
  private final Instant lastModified = Instant.now();

  public XContactTest() throws URISyntaxException
  {
    emailShop = new URI("mailto:w.reder@mountain-sd.at");
    emailService = new URI("mailto:wolfgang.reder@mountain-sd.at");
    shop = new URI("http://www.roco.co.at");
    www = new URI("http://www.zimo.at");
  }

  private JAXBContext context;

  private Contact.Builder createBuilder()
  {
    Contact.Builder builder = factory.createBuilder();
    builder.setTypes(ContactType.ALL);
    builder.address1(address1);
    builder.address2(address2);
    builder.city(city);
    builder.country(country);
    builder.emailShop(emailShop);
    builder.emailService(emailService);
    builder.fax(fax);
    builder.id(id);
    builder.memo(memo);
    builder.lastName(lastName);
    builder.firstName(firstName);
    builder.phone1(phone1);
    builder.phone2(phone2);
    builder.shopAddress(shop);
    builder.www(www);
    builder.zip(zip);
    builder.lastModified(lastModified);
    return builder;
  }

  @Test
  public void testJAXB() throws JAXBException
  {
    context = JAXBContext.newInstance(XContact.class);
    assertNotNull(context);
  }

  private void testProperties(Contact result, Contact contact)
  {
    assertNotSame(result, contact);
    assertEquals(result.getAddress1(), contact.getAddress1());
    assertEquals(result.getAddress2(), contact.getAddress2());
    assertEquals(result.getCity(), contact.getCity());
    assertEquals(result.getCountry(), contact.getCountry());
    assertEquals(result.getEmailShop(), contact.getEmailShop());
    assertEquals(result.getEmailService(), contact.getEmailService());
    assertEquals(result.getFax(), contact.getFax());
    assertEquals(result.getId(), contact.getId());
    assertEquals(result.getMemo(), contact.getMemo());
    assertEquals(result.getLastName(), contact.getLastName());
    assertEquals(result.getFirstName(), contact.getFirstName());
    assertEquals(result.getPhone1(), contact.getPhone1());
    assertEquals(result.getPhone2(), contact.getPhone2());
    assertEquals(result.getShopAddress(), contact.getShopAddress());
    assertEquals(result.getWWW(), contact.getWWW());
    assertEquals(result.getZip(), contact.getZip());
    assertEquals(result.getTypes().size(), contact.getTypes().size());
    assertTrue(contact.getTypes().containsAll(result.getTypes()));
    InstantXmlAdapter adapter = new InstantXmlAdapter();
    assertEquals(adapter.marshal(result.getLastModified()), adapter.marshal(contact.getLastModified()));
    assertNull(result.getLookup().lookup(Object.class));
  }

  @Test(dependsOnMethods = "testJAXB")
  public void testStreaming() throws JAXBException
  {
    Contact contact = createBuilder().build();
    XContact xContact = new XContact(contact);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    context.createMarshaller().marshal(xContact, os);
    ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
    Object tmp = context.createUnmarshaller().unmarshal(is);
    assertNotNull(tmp);
    assertSame(tmp.getClass(), XContact.class);
    assertNotSame(tmp, xContact);
    Contact result = ((XContact) tmp).toContact();
    assertNotNull(result);
    testProperties(result, contact);
  }

  @XmlRootElement
  public static final class Wrapper
  {

    private Contact contact;
    private Contact nullContact;

    public Wrapper()
    {
    }

    ;

    @XmlElement
    @XmlJavaTypeAdapter(value = XContact.Adapter.class)
    public Contact getContact()
    {
      return contact;
    }

    public void setContact(Contact contact)
    {
      this.contact = contact;
    }

    @XmlElement
    @XmlJavaTypeAdapter(value = XContact.Adapter.class)
    public Contact getNullContact()
    {
      return nullContact;
    }

    public void setNullContact(Contact nullContact)
    {
      this.nullContact = nullContact;
    }

  }

  @Test
  public void testWrappedStreaming() throws JAXBException
  {
    JAXBContext ctx = JAXBContext.newInstance(Wrapper.class);
    final Contact contact = createBuilder().build();
    final Wrapper wrapper = new Wrapper();
    wrapper.setContact(contact);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ctx.createMarshaller().marshal(wrapper, os);
    ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
    Object tmp = ctx.createUnmarshaller().unmarshal(is);
    assertTrue(tmp instanceof Wrapper);
    assertNotSame(tmp, wrapper);
    final Contact result = ((Wrapper) tmp).getContact();
    assertNull(((Wrapper) tmp).getNullContact());
    assertNotNull(result);
    assertNotSame(result, contact);
    testProperties(result, contact);
  }

}
