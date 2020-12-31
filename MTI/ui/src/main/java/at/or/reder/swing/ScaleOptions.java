/*
 * $Header: /CVSROOT/controls/msdswing/src/at/mountainsd/msdswing/helper/ScaleOptions.java,v 1.3 2008/01/14 15:55:58 reder Exp $
 *
 * ScaleOptions.java
 *
 * Author Wolfgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import java.io.ObjectStreamException;

public enum ScaleOptions
{

  NO_SCALE,
  SCALE_CONTROL,
  SCALE_IMAGE;
  private static final long serialVersionUID = 1L;

  private Object readResolve() throws ObjectStreamException
  {
    return ScaleOptions.values()[ordinal()];
  }

}
