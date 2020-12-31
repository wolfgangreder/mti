/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel;

import at.reder.mti.api.utils.MTIUtils;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author wolfi
 */
public class ContactTypeNGTest
{

  @Test
  public void testNameLength()
  {
    for (ContactType ct : ContactType.values()) {
      assertTrue(ct.name().length() <= MTIUtils.MAX_ENUM_LENGTH);
    }
  }

}