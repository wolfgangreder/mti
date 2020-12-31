/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.ServiceEntry;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

/**
 *
 * @author wolfi
 */
public class DefaultServiceEntryBuilderFactoryNGTest
{

  public DefaultServiceEntryBuilderFactoryNGTest()
  {
  }

  @Test(enabled = false)
  public void testCreateBuilder()
  {
    System.out.println("createBuilder");
    DefaultServiceEntryBuilderFactory instance = new DefaultServiceEntryBuilderFactory();
    ServiceEntry.Builder expResult = null;
    ServiceEntry.Builder result = instance.createBuilder();
    assertEquals(result, expResult);
    fail("The test case is a prototype.");
  }

}
