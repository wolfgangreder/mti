/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.controls;

import java.util.EventListener;

public interface EnumCheckboxModelListener<E extends Enum> extends EventListener
{

  public void selectionChanged(EnumCheckboxModel<E> model);

}
