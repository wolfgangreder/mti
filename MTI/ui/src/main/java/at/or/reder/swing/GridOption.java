/*
 * $Header: /CVSROOT/controls/msdswing/src/at/mountainsd/msdswing/GridOption.java,v 1.1 2008/01/19 15:57:17 wolfi Exp $
 *
 * GridOption.java
 *
 * Author Wolfgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import java.io.ObjectStreamException;

public enum GridOption
{

  NO_GRID,
  GRID_GOLDEN,
  GRID_THIRD,
  GRID_QUAD,
  GRID_DEC;
  private static final long serialVersionUID = 1L;

  private Object readResolve() throws ObjectStreamException
  {
    return GridOption.values()[ordinal()];
  }

}
