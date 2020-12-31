/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Locomotive;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

/**
 *
 * @author wolfi
 */
public class DefaultLocomotiveBuilderFactoryNGTest
{

  public DefaultLocomotiveBuilderFactoryNGTest()
  {
  }

  @Test(enabled = false)
  public void testCreateBuilder()
  {
    System.out.println("createBuilder");
    DefaultLocomotiveBuilderFactory instance = new DefaultLocomotiveBuilderFactory();
    Locomotive.Builder expResult = null;
    Locomotive.Builder result = instance.createBuilder();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

}
