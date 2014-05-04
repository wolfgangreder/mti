/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.controls;

import java.beans.PropertyChangeListener;

public interface Commitable
{

  public static final String PROP_DATACHANGED = "dataChanged";

  public void commit();

  public void revert();

  public boolean isDataChanged();

  public void addPropertyChangeListener(String property, PropertyChangeListener l);

  public void addPropertyChangeListener(PropertyChangeListener l);

  public void removePropertyChangeListener(String property, PropertyChangeListener l);

  public void removePropertyChangeListener(PropertyChangeListener l);

}
