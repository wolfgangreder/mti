/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Scale;
import at.reder.mti.api.datamodel.xml.XScale;
import at.reder.mti.api.utils.Fract;
import java.util.Collection;
import java.util.UUID;
import org.openide.util.Lookup;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author wolfi
 */
public class DefaultScaleBuilderFactoryNGTest
{

  @Test
  public void testLookup()
  {
    Collection<? extends Scale.BuilderFactory> factories = Lookup.getDefault().lookupAll(Scale.BuilderFactory.class);
    for (Scale.BuilderFactory f : factories) {
      if (f instanceof DefaultScaleBuilderFactory) {
        return;
      }
    }
    fail("no instanceof " + DefaultScaleBuilderFactory.class.getName() + " found");
  }

  @Test
  public void testCreateBuilder()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> result = instance.createBuilder();
    assertNotNull(result);
    Scale.Builder<?> expResult = instance.createBuilder();
    assertNotSame(result, expResult);
  }

  @Test
  public void testImplementingClasses()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    Collection<? extends Class<? extends Scale>> implClasses = builder.getImplementingClasses();
    assertNotNull(implClasses);
    assertEquals(implClasses.size(), 1);
    assertTrue(implClasses.contains(DefaultScaleBuilderFactory.Impl.class));
  }

  @Test
  public void testXmlClass()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    Class<?> clazz = builder.getXmlClass();
    assertTrue(clazz == XScale.class);
  }

  @Test
  public void testBuilderIdValid()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    Scale.Builder<?> result = builder.id(UUID.randomUUID());
    assertSame(result, builder);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testBuilderIdInvalid()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    builder.id(null); // hier soll eine NullPointerException ausgelöst werden.
  }

  @Test
  public void testBuilderNameValid()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    Scale.Builder result = builder.name("wolfi");
    assertSame(result, builder);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testBuilderNameInvalid1()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    builder.name(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuilderNameInvalid2()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    builder.name(" ");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuilderNameInvalid3()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    builder.name("");
  }

  @Test
  public void testBuilderScaleValid()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    Scale.Builder<?> result = builder.scale(Fract.HO);
    assertSame(result, builder);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testBuilderScaleInvalid1()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    builder.scale(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuilderScaleInvalid2()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    builder.scale(Fract.ZERO);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuilderScaleInvalid3()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    builder.scale(Fract.valueOf(-1, 87));
  }

  @Test
  public void testBuilderTrackWidthValid()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    Scale.Builder<?> result = builder.trackWidth(16.5);
    assertSame(result, builder);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuilderTrackWidthInvalid1()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    builder.trackWidth(0);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuilderTrackWdithInvalid2()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    builder.trackWidth(-1);

  }

  @Test
  public void testBuilderBuild()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    UUID id = UUID.randomUUID();
    builder.id(id);
    builder.name("wolfi");
    builder.scale(Fract.HO);
    builder.trackWidth(16.5);
    Scale scale = builder.build();
    assertNotNull(scale);
    assertSame(scale.getClass(), DefaultScaleBuilderFactory.Impl.class);
    assertEquals(scale.getId(), id);
    assertEquals(scale.getName(), "wolfi");
    assertEquals(scale.getScale(), Fract.HO);
    assertEquals(scale.getTrackWidth(), 16.5);
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildInvalid1()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    builder.build();
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildInvalid2()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    builder.id(UUID.randomUUID());
    builder.build();
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildInvalid3()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    builder.build();
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildInvalid4()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder = instance.createBuilder();
    builder.id(UUID.randomUUID());
    builder.name("wolfi");
    builder.scale(Fract.ONE);
    builder.build();
  }

  @Test
  public void testBuilderCopyValid()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder1 = instance.createBuilder();
    UUID id = UUID.randomUUID();
    Scale scale1 = builder1.id(id).name("wolfi").scale(Fract.HO).trackWidth(16.5).build();
    Scale.Builder<?> builder2 = instance.createBuilder();
    Scale.Builder<?> builder3 = builder2.copy(scale1);
    assertSame(builder3, builder2);
    Scale scale2 = builder2.build();
    assertNotSame(scale1, scale2);
    assertEquals(scale1, scale2);
    assertEquals(scale1.hashCode(), scale2.hashCode());
    assertEquals(scale1.getId(), scale2.getId());
    assertEquals(scale1.getName(), scale2.getName());
    assertEquals(scale1.getScale(), scale2.getScale());
    assertEquals(scale1.getTrackWidth(), scale2.getTrackWidth());
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testBuilderCopyInvalid()
  {
    DefaultScaleBuilderFactory instance = new DefaultScaleBuilderFactory();
    Scale.Builder<?> builder1 = instance.createBuilder();
    builder1.copy(null);
  }

}