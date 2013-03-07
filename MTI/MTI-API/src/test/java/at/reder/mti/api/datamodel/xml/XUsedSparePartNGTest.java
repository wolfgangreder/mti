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
import at.reder.mti.api.datamodel.UsedSparePart;
import java.math.BigDecimal;
import java.util.Set;
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
public class XUsedSparePartNGTest
{
  
  public XUsedSparePartNGTest()
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
  public void testToUsedSparePart()
  {
    System.out.println("toUsedSparePart");
    XUsedSparePart instance = new XUsedSparePart();
    UsedSparePart expResult = null;
    UsedSparePart result = instance.toUsedSparePart();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetDefects()
  {
    System.out.println("getDefects");
    XUsedSparePart instance = new XUsedSparePart();
    Set expResult = null;
    Set result = instance.getDefects();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetPart()
  {
    System.out.println("getPart");
    XUsedSparePart instance = new XUsedSparePart();
    SparePart expResult = null;
    SparePart result = instance.getPart();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetAmount()
  {
    System.out.println("getAmount");
    XUsedSparePart instance = new XUsedSparePart();
    BigDecimal expResult = null;
    BigDecimal result = instance.getAmount();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetMemo()
  {
    System.out.println("getMemo");
    XUsedSparePart instance = new XUsedSparePart();
    String expResult = "";
    String result = instance.getMemo();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetPart()
  {
    System.out.println("setPart");
    SparePart part = null;
    XUsedSparePart instance = new XUsedSparePart();
    instance.setPart(part);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetAmount()
  {
    System.out.println("setAmount");
    BigDecimal amount = null;
    XUsedSparePart instance = new XUsedSparePart();
    instance.setAmount(amount);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetMemo()
  {
    System.out.println("setMemo");
    String memo = "";
    XUsedSparePart instance = new XUsedSparePart();
    instance.setMemo(memo);
    fail("The test case is a prototype.");
  }
  
}