/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Wagon;
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
public class XWagonNGTest
{
  
  public XWagonNGTest()
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
  public void testToWagon()
  {
    System.out.println("toWagon");
    XWagon instance = new XWagon();
    Wagon expResult = null;
    Wagon result = instance.toWagon();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetNumber()
  {
    System.out.println("getNumber");
    XWagon instance = new XWagon();
    String expResult = "";
    String result = instance.getNumber();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetNumber()
  {
    System.out.println("setNumber");
    String number = "";
    XWagon instance = new XWagon();
    instance.setNumber(number);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetWheelCount()
  {
    System.out.println("getWheelCount");
    XWagon instance = new XWagon();
    int expResult = 0;
    int result = instance.getWheelCount();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetWheelCount()
  {
    System.out.println("setWheelCount");
    int wheelCount = 0;
    XWagon instance = new XWagon();
    instance.setWheelCount(wheelCount);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetKind()
  {
    System.out.println("getKind");
    XWagon instance = new XWagon();
    String expResult = "";
    String result = instance.getKind();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetKind()
  {
    System.out.println("setKind");
    String kind = "";
    XWagon instance = new XWagon();
    instance.setKind(kind);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetClazz()
  {
    System.out.println("getClazz");
    XWagon instance = new XWagon();
    String expResult = "";
    String result = instance.getClazz();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetClazz()
  {
    System.out.println("setClazz");
    String clazz = "";
    XWagon instance = new XWagon();
    instance.setClazz(clazz);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetCompany()
  {
    System.out.println("getCompany");
    XWagon instance = new XWagon();
    String expResult = "";
    String result = instance.getCompany();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetCompany()
  {
    System.out.println("setCompany");
    String company = "";
    XWagon instance = new XWagon();
    instance.setCompany(company);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetCountry()
  {
    System.out.println("getCountry");
    XWagon instance = new XWagon();
    String expResult = "";
    String result = instance.getCountry();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetCountry()
  {
    System.out.println("setCountry");
    String country = "";
    XWagon instance = new XWagon();
    instance.setCountry(country);
    fail("The test case is a prototype.");
  }
  
}