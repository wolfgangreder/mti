/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.datamodel.SparePart;
import at.reder.mti.api.datamodel.impl.dummies.DummyContact;
import at.reder.mti.api.datamodel.impl.dummies.DummyEntity;
import at.reder.mti.api.datamodel.impl.dummies.DummySparePart;
import at.reder.mti.api.datamodel.xml.XSparePart;
import at.reder.mti.api.utils.Money;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import javax.xml.bind.annotation.XmlRootElement;
import org.openide.util.Lookup;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

/**
 *
 * @author wolfi
 */
public class DefaultSparePartBuilderFactoryNGTest
{

  private final Random random = new Random();
  private final UUID id = UUID.randomUUID();
  private final Instant lastModified = Instant.now();
  private final String name = "spareDummy";
  private final ModelCondition condition = ModelCondition.values()[random.nextInt(ModelCondition.values().length)];
  private final LocalDate dateOfPurchase = LocalDate.now();
  private final String description = "spareDescription";
  private final Contact manufacturer = new DummyContact();
  private final Contact retailer = new DummyContact();
  private final String productNumber = "spareNumber";
  private final Money price = Money.valueOf(random.nextInt());
  private final BigDecimal amount = new BigDecimal(random.nextInt());
  private final Entity masterImage;
  private final SparePart dummy;

  public DefaultSparePartBuilderFactoryNGTest() throws URISyntaxException
  {
    masterImage = new DummyEntity(DefaultSparePartBuilderFactoryNGTest.class.getResource("cubacar.jpg").toURI(), "image/jpeg");
    Collection<? extends Entity> et = Collections.emptyList();
    dummy = new DummySparePart(id,
                               name,
                               condition,
                               dateOfPurchase,
                               description,
                               lastModified,
                               manufacturer,
                               masterImage,
                               price,
                               productNumber,
                               retailer,
                               et,
                               amount);
  }

  private DefaultSparePartBuilderFactory factory;

  @Test
  public void testLookup()
  {
    for (SparePart.BuilderFactory f : Lookup.getDefault().lookupAll(SparePart.BuilderFactory.class)) {
      if (f.getClass() == DefaultSparePartBuilderFactory.class) {
        factory = (DefaultSparePartBuilderFactory) f;
        return;
      }
    }
    fail("found no instance of DefaultSparePartBuilderFactory");
  }

  @Test(dependsOnMethods = "testLookup")
  public void testCreateBuilder()
  {
    SparePart.Builder builder1 = factory.createBuilder();
    SparePart.Builder builder2 = factory.createBuilder();
    assertNotNull(builder1);
    assertNotNull(builder2);
    assertNotSame(builder1, builder2);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderImplementingClass()
  {
    SparePart.Builder builder = factory.createBuilder();
    Collection<? extends Class> impl = builder.getImplementingClasses();
    assertNotNull(impl);
    assertEquals(1, impl.size());
    assertSame(impl.iterator().next(), DefaultSparePartBuilderFactory.DefaultSparePart.class);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderXmlClass()
  {
    SparePart.Builder builder = factory.createBuilder();
    Class xc = builder.getXmlClass();
    assertNotNull(xc);
    assertNull(xc.getAnnotation(XmlRootElement.class));
    assertSame(xc, XSparePart.class);
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderReturnsThis()
  {
    SparePart.Builder builder = factory.createBuilder();
    assertSame(builder, builder.amount(BigDecimal.ZERO));
    assertSame(builder, builder.copy(dummy));
  }

  @Test(dependsOnMethods = "testLookup")
  public void testBuilderAmount()
  {
    SparePart.Builder builder = factory.createBuilder();
    BigDecimal testAmount = new BigDecimal(random.nextDouble());
    builder.copy(dummy);
    builder.amount(testAmount);
    SparePart part = builder.build();
    assertEquals(testAmount, part.getAmount());
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = NullPointerException.class)
  public void testBuilderAmountFail()
  {
    factory.createBuilder().amount(null);
  }

  @Test(dependsOnMethods = "testLookup")
  public void tesTBuilderBuild1()
  {
    SparePart.Builder builder = factory.createBuilder();
    builder.id(id);
    builder.name(name);
    builder.condition(condition);
    builder.amount(amount);
    builder.lastModified(lastModified);
    builder.dateOfPurchase(dateOfPurchase);
    builder.description(description);
    builder.manufacturer(retailer);
    builder.masterImage(masterImage);
    builder.price(price);
    builder.productNumber(productNumber);
    builder.retailer(manufacturer);
    SparePart part = builder.build();
    assertNotNull(part);
    assertSame(part.getClass(), DefaultSparePartBuilderFactory.DefaultSparePart.class);
    assertEquals(amount, part.getAmount());
    assertSame(condition, part.getCondition());
    assertEquals(dateOfPurchase, part.getDateOfPurchase());
    assertEquals(retailer, part.getManufacturer());
    assertEquals(masterImage.getId(), part.getMasterImage().getId());
    assertEquals(name, part.getName());
    assertEquals(price, part.getPrice());
    assertEquals(productNumber, part.getProductNumber());
    assertEquals(manufacturer, part.getRetailer());
    assertEquals(id, part.getId());
  }

  @Test(dependsOnMethods = "testLookup")
  public void tesTBuilderBuild2()
  {
    SparePart.Builder builder = factory.createBuilder();
    builder.copy(dummy);
    SparePart part = builder.build();
    assertNotNull(part);
    assertSame(part.getClass(), DefaultSparePartBuilderFactory.DefaultSparePart.class);
    assertNotSame(part, dummy);
    assertEquals(amount, part.getAmount());
    assertSame(condition, part.getCondition());
    assertEquals(dateOfPurchase, part.getDateOfPurchase());
    assertEquals(manufacturer, part.getManufacturer());
    assertEquals(masterImage.getId(), part.getMasterImage().getId());
    assertEquals(name, part.getName());
    assertEquals(price, part.getPrice());
    assertEquals(productNumber, part.getProductNumber());
    assertEquals(retailer, part.getRetailer());
    assertEquals(id, part.getId());
  }

  @Test(dependsOnMethods = "testLookup")
  public void tesTBuilderBuild3()
  {
    SparePart.Builder builder = factory.createBuilder();
    builder.id(id);
    builder.name(name);
    builder.condition(condition);
    builder.amount(amount);
    builder.lastModified(lastModified);
    SparePart part = builder.build();
    assertNotNull(part);
    assertSame(part.getClass(), DefaultSparePartBuilderFactory.DefaultSparePart.class);
    assertEquals(id, part.getId());
    assertEquals(amount, part.getAmount());
    assertSame(condition, part.getCondition());
    assertNull(part.getDateOfPurchase());
    assertNull(part.getManufacturer());
    assertNull(part.getMasterImage());
    assertEquals(name, part.getName());
    assertNull(part.getPrice());
    assertEquals("", part.getProductNumber());
    assertNull(part.getRetailer());
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void tesTBuilderBuildFail1()
  {
    SparePart.Builder builder = factory.createBuilder();
//    builder.id(id);
    builder.name(name);
    builder.condition(condition);
    builder.amount(amount);
    builder.lastModified(lastModified);
    builder.build();
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void tesTBuilderBuildFail2()
  {
    SparePart.Builder builder = factory.createBuilder();
    builder.id(id);
//    builder.name(name);
    builder.condition(condition);
    builder.amount(amount);
    builder.lastModified(lastModified);
    builder.build();
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void tesTBuilderBuildFail3()
  {
    SparePart.Builder builder = factory.createBuilder();
    builder.id(id);
    builder.name(name);
//    builder.condition(condition);
    builder.amount(amount);
    builder.lastModified(lastModified);
    builder.build();
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void tesTBuilderBuildFail4()
  {
    SparePart.Builder builder = factory.createBuilder();
    builder.id(id);
    builder.name(name);
    builder.condition(condition);
//    builder.amount(amount);
    builder.lastModified(lastModified);
    builder.build();
  }

  @Test(dependsOnMethods = "testLookup", expectedExceptions = IllegalStateException.class)
  public void tesTBuilderBuildFail5()
  {
    SparePart.Builder builder = factory.createBuilder();
    builder.id(id);
    builder.name(name);
    builder.condition(condition);
    builder.amount(amount);
//    builder.lastModified(lastModified);
    builder.build();
  }

}
