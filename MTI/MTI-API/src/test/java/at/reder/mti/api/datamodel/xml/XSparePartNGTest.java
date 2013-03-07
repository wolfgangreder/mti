/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.SparePart;
import java.math.BigDecimal;
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
public class XSparePartNGTest
{
  
  public XSparePartNGTest()
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
  public void testToSparePart()
  {
    System.out.println("toSparePart");
    XSparePart instance = null;
    SparePart expResult = null;
    SparePart result = instance.toSparePart();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetAmount()
  {
    System.out.println("getAmount");
    XSparePart instance = null;
    BigDecimal expResult = null;
    BigDecimal result = instance.getAmount();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetAmount()
  {
    System.out.println("setAmount");
    BigDecimal amount = null;
    XSparePart instance = null;
    instance.setAmount(amount);
    fail("The test case is a prototype.");
  }
  
}