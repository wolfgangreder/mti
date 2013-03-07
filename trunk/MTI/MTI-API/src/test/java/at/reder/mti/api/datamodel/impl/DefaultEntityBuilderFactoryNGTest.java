/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.EntityKind;
import at.reder.mti.api.datamodel.impl.dummies.DummyEntity;
import at.reder.mti.api.datamodel.xml.XEntity;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import javax.imageio.ImageIO;
import org.openide.util.Lookup;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author wolfi
 */
public class DefaultEntityBuilderFactoryNGTest
{

  private static BufferedImage cubaCar;
  private static Path dummyFile;
  private static URI cubaCarURI;

  public DefaultEntityBuilderFactoryNGTest()
  {
  }

  @BeforeClass
  public static void setUpClass() throws Exception
  {
    cubaCarURI = DefaultEntityBuilderFactoryNGTest.class.getResource("cubacar.jpg").toURI();
    cubaCar = ImageIO.read(cubaCarURI.toURL());
    dummyFile = Files.createTempFile("entityTest", ".tmp");
    dummyFile.toFile().deleteOnExit();
  }

  @AfterClass
  public static void tearDownClass() throws Exception
  {
    Files.delete(dummyFile);
  }

  private DefaultEntityBuilderFactory factory;

  @Test
  public void testLookup()
  {
    for (Entity.BuilderFactory f : Lookup.getDefault().lookupAll(Entity.BuilderFactory.class)) {
      if (f.getClass() == DefaultEntityBuilderFactory.class) {
        factory = (DefaultEntityBuilderFactory) f;
        return;
      }
    }
    fail("found no instanceof DefaultEntitiyBuilderFactory");
  }

  @Test(dependsOnMethods = "testLookup")
  public void testCreateBuilder()
  {
    Entity.Builder<?> expResult = factory.createBuilder();
    Entity.Builder<?> result = factory.createBuilder();
    assertNotNull(result);
    assertNotNull(expResult);
    assertNotSame(result, expResult);
    assertSame(DefaultEntityBuilderFactory.DefaultEntityBuilder.class, result.getClass());
    assertSame(DefaultEntityBuilderFactory.DefaultEntityBuilder.class, expResult.getClass());
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderImplementingClasses()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    Collection<? extends Class<? extends Entity>> ic = builder.getImplementingClasses();
    assertNotNull(ic);
    assertEquals(ic.size(), 1);
    assertSame(ic.iterator().next(), DefaultEntityBuilderFactory.DefaultEntity.class);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderXmlClass()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    Class<?> xc = builder.getXmlClass();
    assertSame(xc, XEntity.class);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderReturnsThis()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    assertSame(builder.copy(new DummyEntity()), builder);
    assertSame(builder.data(null), builder);
    assertSame(builder.data(dummyFile), builder);
    assertSame(builder.description(null), builder);
    assertSame(builder.description("descr"), builder);
    assertSame(builder.fileName(null), builder);
    assertSame(builder.fileName("filename"), builder);
    assertSame(builder.kind(EntityKind.IMAGE), builder);
    assertSame(builder.mimeType("text/plain"), builder);
    assertSame(builder.size(-1), builder);
    assertSame(builder.uri(cubaCarURI), builder);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderCopy()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    Entity e = new DummyEntity(cubaCarURI, "image/jpeg");
    builder.copy(e);
    Entity r = builder.build();
    assertNotSame(e, r);
    assertEquals(r.getDescription(), e.getDescription());
    assertEquals(r.getFileName(), e.getFileName());
    assertEquals(r.getId(), e.getId());
    assertEquals(r.getKind(), e.getKind());
    assertEquals(r.getMimeType(), e.getMimeType());
    assertEquals(r.getSize(), e.getSize());
    assertEquals(r.getURI(), e.getURI());
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderCopyFail()
  {
    factory.createBuilder().copy(null);
  }

  @Test(dependsOnMethods = {"testLookup", "testBuilderCopy"})
  public void testBuilderData()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.copy(new DummyEntity());
    builder.data(dummyFile);
    Entity e = builder.build();
    Entity.Data d = e.getLookup().lookup(Entity.Data.class);
    assertNotNull(d);
    assertEquals(d.getTmpFile(), dummyFile);
    builder = factory.createBuilder().copy(new DummyEntity());
    builder.data(dummyFile);
    builder.data(null);
    e = builder.build();
    assertNull(e.getLookup().lookup(Entity.Data.class));
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderDescription()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.description(null);
    builder.description("description");
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderFilename()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.fileName(null);
    builder.fileName("description");
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderKind()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.kind(EntityKind.DECODER_DESCRIPTION);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderKindFail()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.kind(null);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderMimeType()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.mimeType("text/plain");
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalArgumentException.class)
  public void testBuilderMimeTypeFail1()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.mimeType(" ");
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderMimeTypeFail2()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.mimeType(null);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderSize()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.size(0);
    builder.size(-1);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalArgumentException.class)
  public void testBuilderSizeFail()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.size(-2);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderURI()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.uri(cubaCarURI);
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderURIFail()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.uri(null);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderBuild() throws IOException
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.uri(cubaCarURI);
    builder.mimeType("image/jpeg");
    builder.kind(EntityKind.IMAGE);
    Entity e = builder.build();
    assertNotNull(e);
    assertEquals(e.getDescription(), "");
    assertNull(e.getFileName());
    assertEquals(e.getURI(), cubaCarURI);
    assertSame(e.getKind(), EntityKind.IMAGE);
    assertEquals(e.getMimeType(), "image/jpeg");
    assertEquals(e.getSize(), -1);
    try (InputStream is = e.getData()) {
      BufferedImage tmp = ImageIO.read(is);
      assertEquals(tmp.getColorModel(), cubaCar.getColorModel());
      assertEquals(tmp.getWidth(), cubaCar.getWidth());
      assertEquals(tmp.getHeight(), cubaCar.getHeight());
    }
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildFail1()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
//    builder.uri(cubaCarURI);
    builder.mimeType("image/jpeg");
    builder.kind(EntityKind.IMAGE);
    builder.build();
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildFail2()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.uri(cubaCarURI);
//    builder.mimeType("image/jpeg");
    builder.kind(EntityKind.IMAGE);
    builder.build();
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildFail3()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.uri(cubaCarURI);
    builder.mimeType("image/jpeg");
//    builder.kind(EntityKind.IMAGE);
    builder.build();
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void testBuilderBuildFail4()
  {
    Entity.Builder<? extends Entity> builder = factory.createBuilder();
    builder.build();
  }

}