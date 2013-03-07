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
import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.InventoryObject;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.datamodel.impl.dummies.AbstractInventoryObjectImpl;
import at.reder.mti.api.datamodel.impl.dummies.DummyContact;
import at.reder.mti.api.datamodel.impl.dummies.DummyEntity;
import at.reder.mti.api.utils.MTIUtils;
import at.reder.mti.api.utils.Money;
import at.reder.mti.api.utils.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author wolfi
 */
public class AbstractInventoryObjectBuilderNGTest
{

  private final UUID id = UUID.randomUUID();
  private final Timestamp ts = new Timestamp();
  private final Timestamp date = MTIUtils.getDayPart(ts);
  private final Contact retailer = new DummyContact();
  private final Contact manufacturer = new DummyContact();
  private final Entity image = new DummyEntity();
  private final Entity entity = new DummyEntity("http://www.sun.com");
  private InventoryObject testInstance = new AbstractInventoryObjectImpl(id,
                                                                         "name",
                                                                         ModelCondition.ORIGINAL,
                                                                         date,
                                                                         "description",
                                                                         ts,
                                                                         manufacturer,
                                                                         image,
                                                                         Money.TEN,
                                                                         "product",
                                                                         retailer,
                                                                         Arrays.asList(image, entity));

  public AbstractInventoryObjectBuilderNGTest()
  {
  }

  @Test
  public void testCopy()
  {
    System.out.println("copy");
    Object item = null;
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    instance.copy(testInstance);
    assertEquals(instance.condition, testInstance.getCondition());
    assertEquals(instance.dateOfPurchase, testInstance.getDateOfPurchase());
    assertEquals(instance.description, testInstance.getDescription());
    assertEquals(instance.entities.size(), testInstance.getEntities().size());
    assertTrue(instance.entities.containsAll(testInstance.getEntities()));
    assertEquals(instance.id, testInstance.getId());
    assertEquals(instance.lastModified, testInstance.getLastModified());
    assertEquals(instance.manufacturer, testInstance.getManufacturer());
    assertEquals(instance.masterImage, testInstance.getMasterImage());
    assertEquals(instance.name, testInstance.getName());
    assertEquals(instance.price, testInstance.getPrice());
    assertEquals(instance.productNumber, testInstance.getProductNumber());
    assertEquals(instance.retailer, testInstance.getRetailer());
  }

  @Test
  public void testCheckState()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    instance.id(UUID.randomUUID());
    instance.condition(ModelCondition.DEFECT);
    instance.lastModified(new Timestamp());
    instance.name("name");
    instance.checkState();
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testCheckStateFail1()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    //instance.id(UUID.randomUUID());
    instance.condition(ModelCondition.DEFECT);
    instance.lastModified(new Timestamp());
    instance.name("name");
    instance.checkState();
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testCheckStateFail2()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    instance.id(UUID.randomUUID());
//    instance.condition(ModelCondition.DEFECT);
    instance.lastModified(new Timestamp());
    instance.name("name");
    instance.checkState();
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testCheckStateFail3()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    instance.id(UUID.randomUUID());
    instance.condition(ModelCondition.DEFECT);
//    instance.lastModified(new Timestamp());
    instance.name("name");
    instance.checkState();
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testCheckStateFail4()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    instance.id(UUID.randomUUID());
    instance.condition(ModelCondition.DEFECT);
    instance.lastModified(new Timestamp());
//    instance.name("name");
    instance.checkState();
  }

  @Test
  public void testCondition()
  {
    ModelCondition cond = ModelCondition.DEFECT;
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.condition(cond);
    assertSame(result, expResult);
    assertEquals(instance.condition, cond);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testConditionFail()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    InventoryObject.Builder result = instance.condition(null);
  }

  @Test
  public void testDateOfPurchase()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.dateOfPurchase(ts);
    assertSame(result, expResult);
    assertEquals(instance.dateOfPurchase, date);
    result = instance.dateOfPurchase(null);
    assertSame(result, expResult);
    assertNull(instance.dateOfPurchase);
  }

  @Test
  public void testDescription()
  {
    String descr = " descr ";
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.description(descr);
    assertSame(result, expResult);
    assertEquals(instance.description, descr);
    instance.description(null);
    assertEquals(instance.description, "");
  }

  @Test
  public void testAddEntity()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.addEntity(entity);
    assertSame(result, expResult);
    assertEquals(instance.entities.size(), 1);
    assertTrue(instance.entities.contains(entity));
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testAddEntityFail()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    instance.addEntity(null);
  }

  @Test
  public void testRemoveEntity()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    instance.addEntity(image);
    instance.addEntity(entity);
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.removeEntity(image);
    assertSame(result, expResult);
    assertEquals(instance.entities.size(), 1);
    assertTrue(instance.entities.contains(entity));
    instance.removeEntity(null);
    assertEquals(instance.entities.size(), 1);
    assertTrue(instance.entities.contains(entity));
  }

  @Test
  public void testAddEntities()
  {
    Collection<? extends Entity> e = Arrays.asList(image, entity);
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.addEntities(e);
    assertSame(result, expResult);
    assertEquals(instance.entities.size(), e.size());
    assertTrue(instance.entities.containsAll(e));
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testAddEntitiesFail1()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    instance.addEntities(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testAddEntitiesFail2()
  {
    System.out.println("addEntities");
    Collection<? extends Entity> e = Arrays.asList(image, null);
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    instance.addEntities(e);
  }

  @Test
  public void testClearEntities()
  {
    Collection<? extends Entity> e = Arrays.asList(image, entity);
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    instance.addEntities(e);
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.clearEntities();
    assertSame(result, expResult);
    assertTrue(instance.entities.isEmpty());
  }

  @Test
  public void testId()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.id(id);
    assertSame(result, expResult);
    assertEquals(instance.id, id);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testIdFail()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    instance.id(null);
  }

  @Test
  public void testLastModified()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.lastModified(ts);
    assertSame(result, expResult);
    assertEquals(instance.lastModified, ts);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testLastModifiedFail()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    instance.lastModified(null);
  }

  @Test
  public void testManufacturer()
  {
    Contact contact = null;
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.manufacturer(contact);
    assertSame(result, expResult);
    assertSame(instance.manufacturer, contact);
    contact = retailer;
    result = instance.manufacturer(contact);
    assertSame(result, expResult);
    assertSame(instance.manufacturer, contact);
  }

  @Test
  public void testMasterImage()
  {
    Entity e = image;
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.masterImage(e);
    assertSame(result, expResult);
    assertEquals(instance.masterImage, e);
    assertTrue(instance.entities.contains(e));
    assertEquals(instance.entities.size(), 1);
    result = instance.masterImage(null);
    assertSame(result, expResult);
    assertNull(instance.masterImage);
    assertTrue(instance.entities.contains(e));
    assertEquals(instance.entities.size(), 1);
  }

  @Test
  public void testName()
  {
    String name = "name";
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.name(name);
    assertSame(result, expResult);
    assertEquals(instance.name, name);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testNameFail1()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    instance.name(null);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNameFail2()
  {
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    instance.name(" ");
  }

  @Test
  public void testPrice()
  {
    System.out.println("price");
    Money price = Money.ONE;
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.price(price);
    assertSame(result, expResult);
    assertEquals(instance.price, price);
    result = instance.price(null);
    assertSame(result, expResult);
    assertNull(instance.price);
  }

  @Test
  public void testProductNumber()
  {
    String productNumber = "p";
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.productNumber(productNumber);
    assertSame(result, expResult);
    assertEquals(instance.productNumber, productNumber);
    result = instance.productNumber(null);
    assertSame(result, expResult);
    assertNull(instance.productNumber);
  }

  @Test
  public void testRetailer()
  {
    Contact contact = retailer;
    AbstractInventoryObjectBuilder instance = new AbstractInventoryObjectBuilderImpl();
    InventoryObject.Builder expResult = instance;
    InventoryObject.Builder result = instance.retailer(contact);
    assertSame(result, expResult);
    assertEquals(instance.retailer, retailer);
    result = instance.retailer(null);
    assertSame(result, expResult);
    assertNull(instance.retailer);
  }

  public class AbstractInventoryObjectBuilderImpl extends AbstractInventoryObjectBuilder
  {
  }
}