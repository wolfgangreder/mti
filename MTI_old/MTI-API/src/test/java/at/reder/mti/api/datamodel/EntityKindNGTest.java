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
public class EntityKindNGTest
{

  @Test
  public void testIdentiferLength()
  {
    for (EntityKind ek : EntityKind.values()) {
      assertTrue(ek.name().length() <= MTIUtils.MAX_ENUM_LENGTH);
    }
  }

}