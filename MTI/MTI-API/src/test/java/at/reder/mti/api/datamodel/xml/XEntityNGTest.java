/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.EntityKind;
import java.io.InputStream;
import java.net.URI;
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
public class XEntityNGTest
{
  
  public XEntityNGTest()
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
  public void testToEntity()
  {
    System.out.println("toEntity");
    XEntity instance = new XEntity();
    Entity expResult = null;
    Entity result = instance.toEntity();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetKind()
  {
    System.out.println("getKind");
    XEntity instance = new XEntity();
    EntityKind expResult = null;
    EntityKind result = instance.getKind();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetId()
  {
    System.out.println("getId");
    XEntity instance = new XEntity();
    String expResult = "";
    String result = instance.getId();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetUri()
  {
    System.out.println("getUri");
    XEntity instance = new XEntity();
    URI expResult = null;
    URI result = instance.getUri();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetDescription()
  {
    System.out.println("getDescription");
    XEntity instance = new XEntity();
    String expResult = "";
    String result = instance.getDescription();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetMimeType()
  {
    System.out.println("getMimeType");
    XEntity instance = new XEntity();
    String expResult = "";
    String result = instance.getMimeType();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetFileName()
  {
    System.out.println("getFileName");
    XEntity instance = new XEntity();
    String expResult = "";
    String result = instance.getFileName();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetSize()
  {
    System.out.println("getSize");
    XEntity instance = new XEntity();
    int expResult = 0;
    int result = instance.getSize();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testGetData() throws Exception
  {
    System.out.println("getData");
    XEntity instance = new XEntity();
    InputStream expResult = null;
    InputStream result = instance.getData();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetData() throws Exception
  {
    System.out.println("setData");
    InputStream is = null;
    XEntity instance = new XEntity();
    instance.setData(is);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetKind()
  {
    System.out.println("setKind");
    EntityKind kind = null;
    XEntity instance = new XEntity();
    instance.setKind(kind);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetUri()
  {
    System.out.println("setUri");
    URI uri = null;
    XEntity instance = new XEntity();
    instance.setUri(uri);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetDescription()
  {
    System.out.println("setDescription");
    String description = "";
    XEntity instance = new XEntity();
    instance.setDescription(description);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetMimeType()
  {
    System.out.println("setMimeType");
    String mimeType = "";
    XEntity instance = new XEntity();
    instance.setMimeType(mimeType);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetFileName()
  {
    System.out.println("setFileName");
    String fileName = "";
    XEntity instance = new XEntity();
    instance.setFileName(fileName);
    fail("The test case is a prototype.");
  }

  @Test
  public void testSetSize()
  {
    System.out.println("setSize");
    int size = 0;
    XEntity instance = new XEntity();
    instance.setSize(size);
    fail("The test case is a prototype.");
  }
  
}