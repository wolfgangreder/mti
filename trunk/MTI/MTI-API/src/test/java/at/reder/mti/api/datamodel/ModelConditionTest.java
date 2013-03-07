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
import org.junit.Test;
import static org.junit.Assert.*;

public class ModelConditionTest
{

  @Test
  public void testNameLength()
  {
    for (ModelCondition c : ModelCondition.values()) {
      assertTrue(c.name() + "is longer than " + MTIUtils.MAX_ENUM_LENGTH + " characters.",
                 c.name().length() < MTIUtils.MAX_ENUM_LENGTH);
      assertFalse(c.name().equals(c.toString()));
    }
  }

}