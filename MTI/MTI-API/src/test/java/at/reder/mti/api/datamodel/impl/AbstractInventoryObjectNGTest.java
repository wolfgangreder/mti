/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.impl.dummies.AbstractInventoryObjectImpl;
import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.InventoryObject;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.datamodel.impl.dummies.DummyContact;
import at.reder.mti.api.datamodel.impl.dummies.DummyEntity;
import at.reder.mti.api.utils.MTIUtils;
import at.reder.mti.api.utils.Money;
import at.reder.mti.api.utils.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author wolfi
 */
public class AbstractInventoryObjectNGTest
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

  public AbstractInventoryObjectNGTest()
  {
  }

  @Test
  public void testGetId()
  {
    assertEquals(testInstance.getId(), id);
  }

  @Test
  public void testGetName()
  {
    assertEquals(testInstance.getName(), "name");
  }

  @Test
  public void testGetDescription()
  {
    assertEquals(testInstance.getDescription(), "description");
  }

  @Test
  public void testGetPrice()
  {
    assertEquals(testInstance.getPrice(), Money.TEN);
  }

  @Test
  public void testGetDateOfPurchase()
  {
    assertEquals(testInstance.getDateOfPurchase(), date);
  }

  @Test
  public void testGetProductNumber()
  {
    assertEquals(testInstance.getProductNumber(), "product");
  }

  @Test
  public void testGetManufacturer()
  {
    assertEquals(testInstance.getManufacturer(), manufacturer);
  }

  @Test
  public void testGetRetailer()
  {
    assertEquals(testInstance.getRetailer(), retailer);
  }

  @Test
  public void testGetCondition()
  {
    assertSame(testInstance.getCondition(), ModelCondition.ORIGINAL);
  }

  @Test
  public void testGetEntities()
  {
    List<? extends Entity> e = testInstance.getEntities();
    assertEquals(e.size(), 2);
    assertTrue(e.contains(entity));
    assertTrue(e.contains(image));
  }

  @Test
  public void testGetMasterImage()
  {
    assertEquals(testInstance.getMasterImage(), image);
  }

  @Test
  public void testGetLastModified()
  {
    assertEquals(testInstance.getLastModified(), ts);
  }
}