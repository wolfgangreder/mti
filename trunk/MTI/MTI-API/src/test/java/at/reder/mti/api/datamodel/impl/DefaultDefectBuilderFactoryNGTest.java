/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Defect;
import at.reder.mti.api.datamodel.xml.XDefect;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;
import org.openide.util.Lookup;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

public class DefaultDefectBuilderFactoryNGTest
{

  public DefaultDefectBuilderFactoryNGTest()
  {
  }

  private DefaultDefectBuilderFactory factory;

  @Test
  public void testLookup()
  {
    Collection<? extends Defect.BuilderFactory> allFactories = Lookup.getDefault().lookupAll(Defect.BuilderFactory.class);
    for (Defect.BuilderFactory f : allFactories) {
      if (f.getClass() == DefaultDefectBuilderFactory.class) {
        factory = (DefaultDefectBuilderFactory) f;
        return;
      }
    }
    fail("no matching Defect.BuilderFactory found");
  }

  @Test(dependsOnMethods = "testLookup")
  public void testCreateBuilder()
  {
    Defect.Builder builder = factory.createBuilder();
    assertNotNull(builder);
    assertNotSame(factory.createBuilder(), builder);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilder()
  {
    Defect.Builder builder = factory.createBuilder();
    Collection<? extends Class<? extends Defect>> implClasses = builder.getImplementingClasses();
    assertNotNull(implClasses);
    assertEquals(implClasses.size(), 1);
    assertTrue(implClasses.contains(DefaultDefectBuilderFactory.DefectImpl.class));
    Class<?> xClass = builder.getXmlClass();
    assertNotNull(xClass);
    assertSame(xClass, XDefect.class);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderReturnsThis()
  {
    Defect.Builder builder = factory.createBuilder();
    Defect.Builder result;
    result = builder.copy(new Defect()
    {
      @Override
      public UUID getId()
      {
        return UUID.randomUUID();
      }

      @Override
      public LocalDate getDate()
      {
        return LocalDate.now();
      }

      @Override
      public String getDescription()
      {
        return "deesc";
      }

    });
    assertSame(result, builder);
    result = builder.date(LocalDate.now());
    assertSame(result, builder);
    result = builder.description("descr");
    assertSame(result, builder);
    result = builder.id(UUID.randomUUID());
    assertSame(result, builder);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderBuild()
  {
    Defect.Builder builder = factory.createBuilder();
    UUID id = UUID.randomUUID();
    LocalDate resultTs = LocalDate.now();
    builder.date(resultTs);
    builder.description("descr");
    builder.id(id);
    Defect defect = builder.build();
    assertNotNull(defect);
    assertEquals(defect.getDate(), resultTs);
    assertEquals(defect.getDescription(), "descr");
    assertEquals(defect.getId(), id);
    Defect defect2 = builder.build();
    assertNotSame(defect2, defect);
    assertEquals(defect2.getDate(), defect.getDate());
    assertEquals(defect2.getDescription(), defect.getDescription());
    assertEquals(defect2.getId(), defect.getId());
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderDateFail()
  {
    Defect.Builder builder = factory.createBuilder();
    builder.date(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderDescriptionFail1()
  {
    Defect.Builder builder = factory.createBuilder();
    builder.description(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalArgumentException.class)
  public void testBuilderDescriptionFail2()
  {
    Defect.Builder builder = factory.createBuilder();
    builder.description("  ");
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderIdFail()
  {
    Defect.Builder builder = factory.createBuilder();
    builder.id(null);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildFail1()
  {
    Defect.Builder builder = factory.createBuilder();
    builder.build();
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildFail2()
  {
    Defect.Builder builder = factory.createBuilder();
//    builder.date(new Timestamp());
    builder.description("descr");
    builder.id(UUID.randomUUID());
    builder.build();
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildFail3()
  {
    Defect.Builder builder = factory.createBuilder();
    builder.date(LocalDate.now());
//    builder.description("descr");
    builder.id(UUID.randomUUID());
    builder.build();
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildFail4()
  {
    Defect.Builder builder = factory.createBuilder();
    builder.date(LocalDate.now());
    builder.description("descr");
    //builder.id(UUID.randomUUID());
    builder.build();
  }

}
