/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.controls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public interface ErrorFlagable
{

  public static final Border DEFAULT_ERROR_BORDER = BorderFactory.createLineBorder(new Color(0xcc, 0, 0), 1);
  public static final String PROP_DATAVALID = "dataValid";
  public static final String PROP_ERRORBORDER = "errorBorder";

  public Border getBorder();

  public Border getErrorBorder();

  public void setErrorBorder(Border newErrorBorder);

  public boolean isDataValid();

  public void addPropertyChangeListener(String property, PropertyChangeListener l);

  public void addPropertyChangeListener(PropertyChangeListener l);

  public void removePropertyChangeListener(String property, PropertyChangeListener l);

  public void removePropertyChangeListener(PropertyChangeListener l);

  public static void paintBorder(Component comp, ErrorFlagable ef, Graphics g)
  {
    Border border;
    Border errorBorder = ef.getErrorBorder();
    if (errorBorder != null && !ef.isDataValid()) {
      border = errorBorder;
    } else {
      border = ef.getBorder();
    }
    if (border != null) {
      border.paintBorder(comp, g, 0, 0, comp.getWidth(), comp.getHeight());
    }
  }

}
