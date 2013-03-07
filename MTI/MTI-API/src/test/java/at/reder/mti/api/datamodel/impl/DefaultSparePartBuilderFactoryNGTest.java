/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.SparePart;
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
public class DefaultSparePartBuilderFactoryNGTest
{
  
  public DefaultSparePartBuilderFactoryNGTest()
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

  @Test
  public void testCreateBuilder()
  {
    System.out.println("createBuilder");
    DefaultSparePartBuilderFactory instance = new DefaultSparePartBuilderFactory();
    SparePart.Builder expResult = null;
    SparePart.Builder result = instance.createBuilder();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }
  
}