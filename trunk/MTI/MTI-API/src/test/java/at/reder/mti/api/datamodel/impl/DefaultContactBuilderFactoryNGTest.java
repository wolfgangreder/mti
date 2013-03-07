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
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import org.openide.util.Lookup;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 *
 * @author wolfi
 */
public class DefaultContactBuilderFactoryNGTest
{

  private Contact.BuilderFactory factory;

  @Test
  public void testLookup()
  {
    Collection<? extends Contact.BuilderFactory> factories = Lookup.getDefault().lookupAll(Contact.BuilderFactory.class);
    for (Contact.BuilderFactory f : factories) {
      if (f instanceof DefaultContactBuilderFactory) {
        factory = f;
        return;
      }
    }
    fail("No matching Contact.BuilderFactory found");
  }

  @Test(dependsOnMethods = "testLookup")
  public void testCreateBuilder()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    assertTrue(builder instanceof DefaultContactBuilderFactory.ContactBuilder);
    Contact.Builder<?> builder2 = factory.createBuilder();
    assertNotSame(builder, builder2);
    assertNotNull(builder2);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testImplementingClasses()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    Collection<? extends Class<?>> implClasses = builder.getImplementingClasses();
    assertNotNull(implClasses);
    assertEquals(implClasses.size(), 1);
    assertSame(implClasses.iterator().next(), DefaultContactBuilderFactory.ContactImpl.class);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testXmlClass()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    assertSame(builder.getXmlClass(), XContact.class);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderReturnThis() throws URISyntaxException
  {
    Contact.Builder<? extends Contact> builder = factory.createBuilder();
    Contact.Builder<? extends Contact> result = builder.id(UUID.randomUUID());
    assertSame(result, builder);
    result = builder.addLookupItem(new Object());
    assertSame(result, builder);
    result = builder.addType(ContactType.RETAILER);
    assertSame(result, builder);
    result = builder.address1("address1");
    assertSame(result, builder);
    result = builder.address1(null);
    assertSame(result, builder);
    result = builder.address2("address2");
    assertSame(result, builder);
    result = builder.address2(null);
    assertSame(result, builder);
    result = builder.city(null);
    assertSame(result, builder);
    result = builder.city("city");
    assertSame(result, builder);
    result = builder.clearLookup();
    assertSame(result, builder);
    result = builder.clearTypes();
    assertSame(result, builder);
    result = builder.email(new URI("mailto:w.reder@mountain-sd.at"));
    assertSame(result, builder);
    result = builder.email(null);
    assertSame(result, builder);
    result = builder.fax("46");
    assertSame(result, builder);
    result = builder.fax(null);
    assertSame(result, builder);
    result = builder.id(UUID.randomUUID());
    assertSame(result, builder);
    result = builder.memo("memo");
    assertSame(result, builder);
    result = builder.memo(null);
    assertSame(result, builder);
    result = builder.name("wolfi");
    assertSame(result, builder);
    result = builder.phone1("46");
    assertSame(result, builder);
    result = builder.phone1(null);
    assertSame(result, builder);
    result = builder.phone2("543");
    assertSame(result, builder);
    result = builder.phone2(null);
    assertSame(result, builder);
    result = builder.removeInstancesOfFromLookup(Object.class);
    assertSame(result, builder);
    result = builder.removeLookupItem(new Object());
    assertSame(result, builder);
    result = builder.removeLookupItem(null);
    assertSame(result, builder);
    result = builder.removeType(ContactType.RETAILER);
    assertSame(result, builder);
    result = builder.removeType(null);
    assertSame(result, builder);
    result = builder.setTypes(Collections.singleton(ContactType.RETAILER));
    assertSame(result, builder);
    result = builder.setTypes(EnumSet.noneOf(ContactType.class));
    assertSame(result, builder);
    result = builder.shopAddress(new URI("http://www.mountain-sd.at"));
    assertSame(result, builder);
    result = builder.shopAddress(null);
    assertSame(result, builder);
    result = builder.www(new URI("https://www.mountain-sd.at"));
    assertSame(result, builder);
    result = builder.www(null);
    assertSame(result, builder);
    result = builder.zip("zip");
    assertSame(result, builder);
    result = builder.zip(null);
    assertSame(result, builder);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderIdFail()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.id(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderNameFail1()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.name(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalArgumentException.class)
  public void testBuilderNameFail2()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.name(" ");
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalArgumentException.class)
  public void testBuilderNameFail3()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.name("");
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testAddToLookupFail()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.addLookupItem(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testAddTypeFail1()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.addType(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testCopyFail()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.copy(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalArgumentException.class)
  public void testEmailFail1() throws URISyntaxException
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.email(new URI("w.reder@mountain-sd.at"));
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalArgumentException.class)
  public void testEmailFail2() throws URISyntaxException
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.email(new URI("http://w.reder@mountain-sd.at"));
  }

  @Test(dependsOnMethods = "testLookup")
  public void testPhonePattern()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone1("+43676842803817");
    builder.phone1("0043676842803817");
    builder.phone1("0676842803817");
    builder.phone1("84280317");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testPhonePatternInvalid1()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone1("wolfi");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testPhonePatternInvalid2()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone1("++4367685226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testPhonePatternInvalid3()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone1("0004367685226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testPhonePatternInvalid4()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone1("00w43670685226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testPhonePatternInvalid5()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone1("004367068w226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testPhonePatternInvalid6()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone1("w004367068226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testPhonePatternInvalid7()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone1("004367068226w");
  }

  @Test(dependsOnMethods = "testLookup")
  public void testPhone2Pattern()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone2("+43676842803817");
    builder.phone2("0043676842803817");
    builder.phone2("0676842803817");
    builder.phone2("84280317");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testPhone2PatternInvalid1()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone2("wolfi");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testPhone2PatternInvalid2()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone2("++4367685226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testPhone2PatternInvalid3()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone2("0004367685226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testPhone2PatternInvalid4()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone2("00w43670685226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testPhone2PatternInvalid5()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone2("004367068w226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testPhone2PatternInvalid6()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone2("w004367068226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testPhone2PatternInvalid7()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.phone2("004367068226w");
  }

  @Test(dependsOnMethods = "testLookup")
  public void testFaxPattern()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.fax("+43676842803817");
    builder.fax("0043676842803817");
    builder.fax("0676842803817");
    builder.fax("84280317");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testFaxPatternInvalid1()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.fax("wolfi");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testFaxPatternInvalid2()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.fax("++4367685226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testFaxPatternInvalid3()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.fax("0004367685226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testFaxPatternInvalid4()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.fax("00w43670685226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testFaxPatternInvalid5()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.fax("004367068w226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testFaxPatternInvalid6()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.fax("w004367068226");
  }

  @Test(expectedExceptions = IllegalArgumentException.class, dependsOnMethods = "testLookup")
  public void testFaxPatternInvalid7()
  {
    DefaultContactBuilderFactory.ContactBuilder builder = new DefaultContactBuilderFactory.ContactBuilder();
    builder.fax("004367068226w");
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testIdFail()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.id(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testNameFail1()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.name(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalArgumentException.class)
  public void testNameFail2()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.name("");
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalArgumentException.class)
  public void testNameFail3()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.name(" ");
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testRemoveInstancesOfFromLookupFail()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.removeInstancesOfFromLookup(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testSetTypesFail1()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.setTypes(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testSetTypesFail2()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    List<ContactType> types = Arrays.asList(ContactType.RETAILER, null, ContactType.MANUFACTURER);
    builder.setTypes(types);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testSetTypesFail3()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    List<ContactType> types = Arrays.asList(null, ContactType.MANUFACTURER);
    builder.setTypes(types);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testSetTypesFail4()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    List<ContactType> types = Arrays.asList(ContactType.MANUFACTURER, null);
    builder.setTypes(types);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testSetTypesFail5()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    Collection<ContactType> types = Collections.singletonList(null);
    builder.setTypes(types);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalArgumentException.class)
  public void testShopAddressFail1() throws URISyntaxException
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.shopAddress(new URI("gopher://www.mountain-sd.at"));
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalArgumentException.class)
  public void testWWWFail1() throws URISyntaxException
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.www(new URI("gopher://www.mountain-sd.at"));
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilder1()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    UUID id = UUID.randomUUID();
    builder.addType(ContactType.RETAILER);
    builder.id(id);
    builder.name(" wolfi ");
    Contact contact = builder.build();
    assertNotNull(contact);
    assertTrue(builder.getImplementingClasses().contains(contact.getClass()));
    assertEquals(contact.getAddress1(), "");
    assertEquals(contact.getAddress2(), "");
    assertEquals(contact.getCity(), "");
    assertEquals(contact.getCountry(), "");
    assertNull(contact.getEmail());
    assertEquals(contact.getFax(), "");
    assertEquals(contact.getId(), id);
    assertEquals(contact.getMemo(), "");
    assertEquals(contact.getName(), "wolfi");
    assertEquals(contact.getPhone1(), "");
    assertEquals(contact.getPhone2(), "");
    assertNull(contact.getShopAddress());
    assertNotNull(contact.getTypes());
    assertEquals(contact.getTypes().size(), 1);
    assertTrue(contact.getTypes().contains(ContactType.RETAILER));
    assertNull(contact.getWWW());
    assertEquals(contact.getZip(), "");
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilder2() throws URISyntaxException
  {
    Contact.Builder<? extends Contact> builder = factory.createBuilder();
    UUID id = UUID.randomUUID();
    URI email = new URI("mailto:w.reder@mountain-sd.at");
    URI shop = new URI("http://www.roco.co.at");
    URI www = new URI("http://www.zimo.at");
    builder.addLookupItem(new Integer(5));
    builder.addLookupItem("wolfi");
    builder.addLookupItem("reder");
    builder.removeInstancesOfFromLookup(String.class);
    builder.setTypes(ContactType.ALL);
    builder.removeType(ContactType.RETAILER);
    builder.address1("address 1 ");
    builder.address2(" address 2");
    builder.city(" city ");
    builder.country(" country ");
    builder.email(email);
    builder.fax("1");
    builder.id(id);
    builder.memo(" memo ");
    builder.name("wolfi ");
    builder.phone1("2");
    builder.phone2("3");
    builder.shopAddress(shop);
    builder.www(www);
    builder.zip("zip ");
    Contact contact = builder.build();
    assertNotNull(contact);
    assertEquals(contact.getAddress1(), "address 1");
    assertEquals(contact.getAddress2(), "address 2");
    assertEquals(contact.getCity(), "city");
    assertEquals(contact.getCountry(), "country");
    assertEquals(contact.getEmail(), email);
    assertEquals(contact.getFax(), "1");
    assertEquals(contact.getId(), id);
    assertEquals(contact.getMemo(), " memo ");
    assertEquals(contact.getName(), "wolfi");
    assertEquals(contact.getPhone1(), "2");
    assertEquals(contact.getPhone2(), "3");
    assertEquals(contact.getShopAddress(), shop);
    assertEquals(contact.getWWW(), www);
    assertEquals(contact.getZip(), "zip");
    assertEquals(contact.getTypes().size(), 1);
    assertTrue(contact.getTypes().contains(ContactType.MANUFACTURER));
    Integer i = 5;
    assertEquals(contact.getLookup().lookup(Integer.class), i);
    assertNull(contact.getLookup().lookup(String.class));
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderCopy() throws URISyntaxException
  {
    Contact.Builder<? extends Contact> builder = factory.createBuilder();
    UUID id = UUID.randomUUID();
    URI email = new URI("mailto:w.reder@mountain-sd.at");
    URI shop = new URI("http://www.roco.co.at");
    URI www = new URI("http://www.zimo.at");
    builder.addLookupItem(new Integer(5));
    builder.addLookupItem("wolfi");
    builder.addLookupItem("reder");
    builder.removeInstancesOfFromLookup(String.class);
    builder.setTypes(ContactType.ALL);
    builder.removeType(ContactType.RETAILER);
    builder.address1("address 1 ");
    builder.address2(" address 2");
    builder.city(" city ");
    builder.country(" country ");
    builder.email(email);
    builder.fax("1");
    builder.id(id);
    builder.memo(" memo ");
    builder.name("wolfi ");
    builder.phone1("2");
    builder.phone2("3");
    builder.shopAddress(shop);
    builder.www(www);
    builder.zip("zip ");
    Contact contact = builder.build();
    Contact.Builder<? extends Contact> builder2 = factory.createBuilder();
    Contact.Builder<? extends Contact> builder3 = builder2.copy(contact);
    assertSame(builder3, builder2);
    Contact result = builder2.build();
    assertNotSame(result, contact);
    assertEquals(result.getAddress1(), contact.getAddress1());
    assertEquals(result.getAddress2(), contact.getAddress2());
    assertEquals(result.getCity(), contact.getCity());
    assertEquals(result.getCountry(), contact.getCountry());
    assertEquals(result.getEmail(), contact.getEmail());
    assertEquals(result.getFax(), contact.getFax());
    assertEquals(result.getId(), contact.getId());
    assertEquals(result.getMemo(), contact.getMemo());
    assertEquals(result.getName(), contact.getName());
    assertEquals(result.getPhone1(), contact.getPhone1());
    assertEquals(result.getPhone2(), contact.getPhone2());
    assertEquals(result.getShopAddress(), contact.getShopAddress());
    assertEquals(result.getWWW(), contact.getWWW());
    assertEquals(result.getZip(), contact.getZip());
    assertEquals(result.getTypes().size(), contact.getTypes().size());
    assertTrue(contact.getTypes().containsAll(result.getTypes()));
    assertNull(result.getLookup().lookup(Object.class));
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderLookup1()
  {
    Contact.Builder<? extends Contact> builder = factory.createBuilder();
    builder.addType(ContactType.RETAILER);
    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    builder.addLookupItem("wolfi");
    builder.addLookupItem(UUID.randomUUID());
    builder.addLookupItem("reder");
    Contact contact = builder.build();
    Collection<? extends Object> tmp = contact.getLookup().lookupAll(String.class);
    assertEquals(tmp.size(), 2);
    assertTrue(tmp.contains("wolfi"));
    assertTrue(tmp.contains("reder"));
    assertNotNull(contact.getLookup().lookup(UUID.class));
    assertNull(contact.getLookup().lookup(Integer.class));
    tmp = contact.getLookup().lookupAll(Object.class);
    assertEquals(tmp.size(), 3);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderLookup2()
  {
    Contact.Builder<? extends Contact> builder = factory.createBuilder();
    builder.addType(ContactType.RETAILER);
    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    builder.addLookupItem("wolfi");
    builder.addLookupItem(UUID.randomUUID());
    builder.addLookupItem("reder");
    builder.clearLookup();
    builder.addLookupItem("string");
    Contact contact = builder.build();
    Collection<? extends Object> tmp = contact.getLookup().lookupAll(String.class);
    assertEquals(tmp.size(), 1);
    assertTrue(tmp.contains("string"));
    assertNull(contact.getLookup().lookup(Integer.class));
    tmp = contact.getLookup().lookupAll(Object.class);
    assertEquals(tmp.size(), 1);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void testBuilderContactTypeFail1()
  {
    Contact.Builder<? extends Contact> builder = factory.createBuilder();
    builder.addType(ContactType.RETAILER);
    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    builder.clearTypes();
    builder.build();
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderContactType1()
  {
    Contact.Builder<? extends Contact> builder = factory.createBuilder();
    builder.addType(ContactType.RETAILER);
    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    builder.clearTypes();
    builder.addType(ContactType.MANUFACTURER);
    Contact contact = builder.build();
    assertEquals(contact.getTypes().size(), 1);
    assertTrue(contact.getTypes().contains(ContactType.MANUFACTURER));
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderContactType2()
  {
    Contact.Builder<? extends Contact> builder = factory.createBuilder();
    builder.setTypes(ContactType.ALL);
    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    Contact contact = builder.build();
    assertEquals(contact.getTypes().size(), ContactType.ALL.size());
    assertTrue(contact.getTypes().containsAll(ContactType.ALL));
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = UnsupportedOperationException.class)
  public void testBuilderContactTypeFail2()
  {
    Contact.Builder<? extends Contact> builder = factory.createBuilder();
    builder.setTypes(ContactType.ALL);
    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    Contact contact = builder.build();
    contact.getTypes().remove(ContactType.MANUFACTURER);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void testBuilderFail1()
  {
    factory.createBuilder().build();
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void testBuilderFail2()
  {
    Contact.Builder<?> builder = factory.createBuilder();
//    builder.addType(ContactType.RETAILER);
    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    builder.build();
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void testBuilderFail3()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.addType(ContactType.RETAILER);
//    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    builder.build();
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void testBuilderFail4()
  {
    Contact.Builder<?> builder = factory.createBuilder();
    builder.addType(ContactType.RETAILER);
    builder.id(UUID.randomUUID());
//    builder.name("wolfi");
    builder.build();
  }

}