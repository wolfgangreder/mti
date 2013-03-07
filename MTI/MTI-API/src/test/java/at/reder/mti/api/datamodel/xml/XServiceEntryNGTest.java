/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.ServiceEntry;
import at.reder.mti.api.utils.Timestamp;
import java.util.List;
import java.util.UUID;
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
public class XServiceEntryNGTest
{
  
  public XServiceEntryNGTest()
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
  public void testToServiceEntry()
  {
    System.out.println("toServiceEntry");
    XServiceEntry instance = new XServiceEntry();
    ServiceEntry expResult = null;
    ServiceEntry result = instance.toServiceEntry();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetId()
  {
    System.out.println("getId");
    XServiceEntry instance = new XServiceEntry();
    UUID expResult = null;
    UUID result = instance.getId();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetStringId()
  {
    System.out.println("getStringId");
    XServiceEntry instance = new XServiceEntry();
    String expResult = "";
    String result = instance.getStringId();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetDate()
  {
    System.out.println("getDate");
    XServiceEntry instance = new XServiceEntry();
    Timestamp expResult = null;
    Timestamp result = instance.getDate();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetDefects()
  {
    System.out.println("getDefects");
    XServiceEntry instance = new XServiceEntry();
    List expResult = null;
    List result = instance.getDefects();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetParts()
  {
    System.out.println("getParts");
    XServiceEntry instance = new XServiceEntry();
    List expResult = null;
    List result = instance.getParts();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetDescription()
  {
    System.out.println("getDescription");
    XServiceEntry instance = new XServiceEntry();
    String expResult = "";
    String result = instance.getDescription();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetId()
  {
    System.out.println("setId");
    UUID id = null;
    XServiceEntry instance = new XServiceEntry();
    instance.setId(id);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetDate()
  {
    System.out.println("setDate");
    Timestamp date = null;
    XServiceEntry instance = new XServiceEntry();
    instance.setDate(date);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetDescription()
  {
    System.out.println("setDescription");
    String description = "";
    XServiceEntry instance = new XServiceEntry();
    instance.setDescription(description);
    fail("The test case is a prototype.");
  }
  
}