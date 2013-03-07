/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Decoder;
import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.datamodel.impl.dummies.DummyContact;
import at.reder.mti.api.datamodel.impl.dummies.DummyEntity;
import at.reder.mti.api.datamodel.xml.XDecoder;
import at.reder.mti.api.utils.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import org.openide.util.Lookup;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author wolfi
 */
public class DefaultDecoderBuilderFactoryNGTest
{

  public DefaultDecoderBuilderFactoryNGTest()
  {
  }

  @BeforeClass
  public static void setUpClass() throws Exception
  {
  }

  @AfterClass
  public static void tearDownClass() throws Exception
  {
  }

  @BeforeMethod
  public void setUpMethod() throws Exception
  {
  }

  @AfterMethod
  public void tearDownMethod() throws Exception
  {
  }

  private DefaultDecoderBuilderFactory factory;

  @Test
  public void testLookup()
  {
    for (Decoder.BuilderFactory f : Lookup.getDefault().lookupAll(Decoder.BuilderFactory.class)) {
      if (f.getClass() == DefaultDecoderBuilderFactory.class) {
        factory = (DefaultDecoderBuilderFactory) f;
        return;
      }
    }
    fail("no instanceof DefaultDecoderBuilderFactory found");
  }

  @Test(dependsOnMethods = "testLookup")
  public void testCreateBuilder()
  {
    Decoder.Builder<?> builder = factory.createBuilder();
    Decoder.Builder<?> builder2 = factory.createBuilder();
    assertNotNull(builder);
    assertNotNull(builder2);
    assertNotSame(builder, builder2);
    assertSame(builder.getClass(), DefaultDecoderBuilderFactory.DefaultDecoderBuilder.class);
    assertSame(builder2.getClass(), DefaultDecoderBuilderFactory.DefaultDecoderBuilder.class);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderImplementingClass()
  {
    Decoder.Builder<?> builder = factory.createBuilder();
    assertNotNull(builder.getImplementingClasses());
    assertEquals(builder.getImplementingClasses().size(), 1);
    assertSame(builder.getImplementingClasses().iterator().next(), DefaultDecoderBuilderFactory.DefaultDecoder.class);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderXmlClass()
  {
    Decoder.Builder<?> builder = factory.createBuilder();
    assertSame(builder.getXmlClass(), XDecoder.class);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderReturnsThis()
  {
    Decoder.Builder<?> builder = factory.createBuilder();
    Collection<? extends Entity> emptyEntities = Collections.emptyList();
    assertSame(builder.addEntities(emptyEntities), builder);
    assertSame(builder.addEntity(null), builder);
    assertSame(builder.clearEntities(), builder);
    assertSame(builder.condition(ModelCondition.NEW), builder);
    assertSame(builder.dateOfPurchase(null), builder);
    assertSame(builder.description(null), builder);
    assertSame(builder.id(UUID.randomUUID()), builder);
    assertSame(builder.lastModified(new Timestamp()), builder);
    assertSame(builder.manufacturer(new DummyContact()), builder);
    assertSame(builder.masterImage(null), builder);
    assertSame(builder.name("name"), builder);
    assertSame(builder.price(null), builder);
    assertSame(builder.productNumber(null), builder);
    assertSame(builder.removeEntity(null), builder);
    assertSame(builder.retailer(null), builder);
    fail("umbauen !!!!!");
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderIdFail()
  {
    Decoder.Builder<?> builder = factory.createBuilder();
    builder.id(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderConditionFail()
  {
    Decoder.Builder<?> builder = factory.createBuilder();
    builder.condition(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderLastModifiedFail()
  {
    Decoder.Builder<?> builder = factory.createBuilder();
    builder.lastModified(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderNameFail1()
  {
    Decoder.Builder<?> builder = factory.createBuilder();
    builder.name(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalArgumentException.class)
  public void testBuilderNameFail2()
  {
    Decoder.Builder<?> builder = factory.createBuilder();
    builder.name("");
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalArgumentException.class)
  public void testBuilderNameFail3()
  {
    Decoder.Builder<?> builder = factory.createBuilder();
    builder.name(" ");
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderAddEntityFail()
  {
    Decoder.Builder<?> builder = factory.createBuilder();
    builder.addEntity(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderAddEntitiesFail1()
  {
    Decoder.Builder<?> builder = factory.createBuilder();
    builder.addEntities(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalArgumentException.class)
  public void testBuilderAddEntitiesFail2()
  {
    Decoder.Builder<?> builder = factory.createBuilder();

    builder.addEntities(Arrays.asList(new DummyEntity(), null));
  }

}