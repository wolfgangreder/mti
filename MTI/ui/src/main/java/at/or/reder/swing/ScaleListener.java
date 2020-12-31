/*
 * $Header: /CVSROOT/controls/msdswing/src/at/mountainsd/msdswing/ScaleListener.java,v 1.1 2008/01/19 15:56:27 wolfi Exp $
 *
 * ScaleListener.java
 *
 * Author Wolfgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import java.util.EventListener;

@FunctionalInterface
public interface ScaleListener extends EventListener
{

  public void scaleChanged(ScaleModel model);

}
