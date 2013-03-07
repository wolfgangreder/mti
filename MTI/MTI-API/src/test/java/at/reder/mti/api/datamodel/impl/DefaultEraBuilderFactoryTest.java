/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Era;
import at.reder.mti.api.datamodel.Era.BuilderFactory;
import at.reder.mti.api.datamodel.xml.XEra;
import java.util.Collection;
import java.util.UUID;
import static org.testng.Assert.*;
import org.testng.annotations.Test;
import org.openide.util.Lookup;

public class DefaultEraBuilderFactoryTest
{

  public DefaultEraBuilderFactoryTest()
  {
  }

  @Test
  public void testLookup()
  {
    System.out.println("lookup");
    Collection<? extends BuilderFactory> factories = Lookup.getDefault().lookupAll(BuilderFactory.class);
    boolean oneFound = false;
    for (BuilderFactory f : factories) {
      if (f instanceof DefaultEraBuilderFactory) {
        oneFound = true;
        break;
      }
    }
    assertTrue(oneFound, "no instanceof DefaultEraBuilderFactory found");
  }

  @Test
  public void testCreateBuilder()
  {
    System.out.println("createBuilder");

    BuilderFactory instance = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> result = instance.createBuilder();
    assertNotNull(result);
    assertTrue(result instanceof DefaultEraBuilderFactory.EraBuilder);
    Era.Builder<? extends Era> result2 = instance.createBuilder();
    assertNotSame(result, result2);
    assertSame(result.getClass(), result2.getClass());
  }

  @Test
  public void testBuilderImplementingClasses()
  {
    System.out.println("builderImplementingClasses");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    Collection<? extends Class<? extends Era>> i = builder.getImplementingClasses();
    assertNotNull(i);
    assertEquals(1, i.size());
    assertSame(DefaultEraBuilderFactory.Impl.class, i.iterator().next());
  }

  @Test
  public void testBuilderXmlClass()
  {
    System.out.println("builderXmlClass");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    Class<?> xclass = builder.getXmlClass();
    assertNotNull(xclass);
    assertSame(XEra.class, xclass);
  }

  @Test
  public void testBuilderIdValid()
  {
    System.out.println("builderIdValid");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    Era.Builder<? extends Era> result = builder.id(UUID.randomUUID());
    assertSame(builder, result);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testBuilderIdInvalid()
  {
    System.out.println("builderIdInvalid");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.id(null); // hier soll eine NullPointerException geworfen werden.
  }

  @Test
  public void testBuilderNameValid()
  {
    System.out.println("builderNameValid");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    Era.Builder<? extends Era> result = builder.name("wolfi");
    assertSame(builder, result);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testBuilderNameInvalid1()
  {
    System.out.println("builderNameInvalid1");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.name(null); // NullPointerException
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuilderNameInvalid2()
  {
    System.out.println("builderNameInvalid2");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.name(""); // IllegalArgumentException
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuilderNameInvalid3()
  {
    System.out.println("builderNameInvalid3");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.name(" "); // IllegalArgumentException
  }

  @Test
  public void testBuilderYearFrom()
  {
    System.out.println("builderYearFrom");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    Era.Builder<? extends Era> result = builder.yearFrom(0);
    assertSame(builder, result);
  }

  @Test
  public void testBuilderYearTo()
  {
    System.out.println("builderYearTo");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    Era.Builder<? extends Era> result = builder.yearTo(0);
    assertSame(builder, result);
    result = builder.yearTo(null);
    assertSame(builder, result);
  }

  @Test
  public void testBuilderAddCountry()
  {
    System.out.println("builderAddCountry");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    Era.Builder<? extends Era> result = builder.addCountry("AT");
    assertSame(builder, result);
    result = builder.addCountry("027");
    assertSame(builder, result);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testBuilderAddCountryNull()
  {
    System.out.println("builderAddCountryNull");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.addCountry(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuilderAddCountryInvalid1()
  {
    System.out.println("builderAddCountryInvalid1");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.addCountry("at");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuilderAddCountryInvalid2()
  {
    System.out.println("builderAddCountryInvalid2");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.addCountry("AUT");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuilderAddCountryInvalid3()
  {
    System.out.println("builderAddCountryInvalid3");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.addCountry("27");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuilderAddCountryInvalid4()
  {
    System.out.println("builderAddCountryInvalid4");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.addCountry("");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuilderAddCountryInvalid5()
  {
    System.out.println("builderAddCountryInvalid5");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.addCountry("2700");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuilderAddCountryInvalid6()
  {
    System.out.println("builderAddCountryInvalid6");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.addCountry("A");
  }

  @Test
  public void testBuilderComment()
  {
    System.out.println("builderComment");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    Era.Builder<? extends Era> result = builder.comment("wolfi");
    assertSame(builder, result);
    builder.comment(null);
    builder.comment("");
  }

  @Test
  public void testBuilderRemoveCountry()
  {
    System.out.println("builderRemoveCountry");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.addCountry("AT");
    builder.addCountry("DE");
    builder.removeCountry(null);
    Era.Builder<? extends Era> result = builder.removeCountry("DE");
    assertSame(builder, result);
    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    builder.yearFrom(2000);
    Era era = builder.build();
    assertEquals(1, era.getCountries().size());
    assertTrue(era.getCountries().contains("AT"));
  }

  @Test
  public void testBuilderClearCountry()
  {
    System.out.println("builderClearCountry");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.addCountry("AT");
    builder.addCountry("DE");
    Era.Builder<? extends Era> result = builder.clearCountries();
    assertSame(builder, result);
    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    builder.yearFrom(2000);
    Era era = builder.build();
    assertTrue(era.getCountries().isEmpty());
  }

  @Test
  public void testBuilderBuild()
  {
    System.out.println("builderBuild");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    UUID id = UUID.randomUUID();
    builder.id(id);
    builder.addCountry("AT");
    builder.addCountry("DE");
    builder.name(" wolfi ");
    builder.comment(null);
    builder.yearFrom(1900);
    builder.yearTo(null);
    Era era = builder.build();
    assertNotNull(era);
    assertSame(era.getClass(), DefaultEraBuilderFactory.Impl.class);
    assertEquals(id, era.getId());
    assertEquals("wolfi", era.getName());
    assertEquals("", era.getComment());
    assertEquals(2, era.getCountries().size());
    assertTrue(era.getCountries().contains("AT"));
    assertTrue(era.getCountries().contains("DE"));
    assertEquals(1900, era.getYearFrom());
    assertNull(era.getYearTo());
    builder = factory.createBuilder();
    id = UUID.randomUUID();
    builder.id(id);
    builder.addCountry("AT");
    builder.addCountry("DE");
    builder.name("wolfi");
    builder.comment("");
    builder.yearFrom(1900);
    builder.yearTo(2000);
    era = builder.build();
    assertEquals(id, era.getId());
    assertEquals("wolfi", era.getName());
    assertEquals("", era.getComment());
    assertEquals(2, era.getCountries().size());
    assertTrue(era.getCountries().contains("AT"));
    assertTrue(era.getCountries().contains("DE"));
    assertEquals(1900, era.getYearFrom());
    assertEquals(new Integer(2000), era.getYearTo());
    id = UUID.randomUUID();
    builder.id(id);
    builder.addCountry("AT");
    builder.addCountry("DE");
    builder.name("wolfi");
    builder.comment(" comment ");
    builder.yearFrom(1900);
    builder.yearTo(2000);
    era = builder.build();
    assertEquals(id, era.getId());
    assertEquals("wolfi", era.getName());
    assertEquals("comment", era.getComment());
    assertEquals(2, era.getCountries().size());
    assertTrue(era.getCountries().contains("AT"));
    assertTrue(era.getCountries().contains("DE"));
    assertEquals(1900, era.getYearFrom());
    assertEquals(new Integer(2000), era.getYearTo());
  }

  public void testBuilderCopy()
  {
    System.out.println("builderCopy");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    UUID id = UUID.randomUUID();
    builder.id(id);
    builder.addCountry("AT");
    builder.addCountry("DE");
    builder.name(" wolfi ");
    builder.comment(null);
    builder.yearFrom(1900);
    builder.yearTo(null);
    Era era = builder.build();
    builder = factory.createBuilder();
    Era.Builder<? extends Era> result = builder.copy(era);
    assertSame(builder, result);
    Era eraResult = builder.build();
    assertSame(era, eraResult);
    builder = factory.createBuilder();
    builder.copy(era);
    builder.name(era.getName());
    eraResult = builder.build();
    assertNotSame(era, eraResult);
    assertEquals(era.getName(), eraResult.getName());
    builder = factory.createBuilder();
    builder.copy(era);
    builder.id(id);
    eraResult = builder.build();
    assertNotSame(era, eraResult);
    assertEquals(era.getId(), eraResult.getId());
    builder = factory.createBuilder();
    builder.copy(era);
    builder.comment(era.getComment());
    eraResult = builder.build();
    assertNotSame(era, eraResult);
    assertEquals(era.getComment(), eraResult.getComment());
    builder = factory.createBuilder();
    builder.copy(era);
    builder.removeCountry("DE");
    builder.addCountry("AT");
    builder.addCountry("DE");
    eraResult = builder.build();
    assertNotSame(era, eraResult);
    assertEquals(era.getCountries(), eraResult.getCountries());
    builder = factory.createBuilder();
    builder.copy(era);
    builder.clearCountries();
    builder.addCountries(era.getCountries());
    eraResult = builder.build();
    assertNotSame(era, eraResult);
    assertEquals(era.getCountries(), eraResult.getCountries());
    builder = factory.createBuilder();
    builder.copy(era);
    builder.yearFrom(era.getYearFrom());
    eraResult = builder.build();
    assertNotSame(era, eraResult);
    assertEquals(era.getYearFrom(), eraResult.getYearFrom());
    builder = factory.createBuilder();
    builder.copy(era);
    builder.yearTo(era.getYearTo());
    eraResult = builder.build();
    assertNotSame(era, eraResult);
    assertEquals(era.getYearTo(), eraResult.getYearTo());
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildFailure1()
  {
    System.out.println("builderBuilderFailure1");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.build();
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildFailure2()
  {
    System.out.println("builderBuilderFailure2");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.id(UUID.randomUUID());
//    builder.name("wolfi");
    builder.yearFrom(1900);
    builder.yearTo(null);
    builder.build();
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildFailure3()
  {
    System.out.println("builderBuilderFailure3");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
//    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    builder.yearFrom(1900);
    builder.yearTo(null);
    builder.build();
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildFailure4()
  {
    System.out.println("builderBuilderFailure4");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    builder.yearFrom(1699);
    builder.yearTo(null);
    builder.build();
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildFailure5()
  {
    System.out.println("builderBuilderFailure5");
    BuilderFactory factory = new DefaultEraBuilderFactory();
    Era.Builder<? extends Era> builder = factory.createBuilder();
    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    builder.yearFrom(1900);
    builder.yearTo(1899);
    builder.build();
  }

}