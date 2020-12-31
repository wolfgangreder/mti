/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.controls;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface EnumCheckboxModel<E extends Enum>
{

  public List<E> getSelectionValues();

  public Set<E> getSelection();

  public void setSelection(Collection<E> newSelection);

  public void setCommitedSelection(Collection<E> newSelection);

  public boolean isDataValid();

  public boolean isDataChanged();

  public void commit();

  public void revert();

  public void select(E e, boolean sel);

  public void addEnumCheckboxModelListener(EnumCheckboxModelListener<E> listener);

  public void removeEnumCheckboxModelListener(EnumCheckboxModelListener<E> listener);

  public String getLabel(E e);

  public boolean isOneRequired();

  public void setOneRequired(boolean or);

}
