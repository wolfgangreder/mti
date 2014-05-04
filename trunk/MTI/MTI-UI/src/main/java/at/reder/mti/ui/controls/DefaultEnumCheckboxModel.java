/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.controls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class DefaultEnumCheckboxModel<E extends Enum> implements EnumCheckboxModel<E>
{

  private final List<E> values;
  private final Set<E> selection;
  private final Set<EnumCheckboxModelListener<E>> listener = new CopyOnWriteArraySet<>();
  private boolean oneRequired;
  private Set<E> commited;
  private boolean dataValid;
  private boolean dataChanged;

  public DefaultEnumCheckboxModel(Class<E> clazz)
  {
    List<E> tmp = null;
    try {
      Method methValues = clazz.getMethod("values", (Class<?>[]) null);
      @SuppressWarnings("unchecked")
      E[] vals = (E[]) methValues.invoke(null, (Object[]) null);
      tmp = Collections.unmodifiableList(Arrays.asList(vals));
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
      throw new IllegalArgumentException(ex);
    }
    if (tmp == null) {
      tmp = Collections.emptyList();
    }
    values = tmp;
    selection = EnumSet.noneOf(clazz);
  }

  public DefaultEnumCheckboxModel(Class<E> clazz, Collection<E> vals)
  {
    values = Collections.unmodifiableList(new ArrayList<>(vals));
    selection = EnumSet.of(values.get(0));
    selection.clear();
  }

  @Override
  public List<E> getSelectionValues()
  {
    return values;
  }

  @Override
  public Set<E> getSelection()
  {
    return Collections.unmodifiableSet(selection);
  }

  @Override
  public void setSelection(Collection<E> newSelection)
  {
    if (oneRequired && (newSelection == null || newSelection.isEmpty())) {
      return;
    }
    Set<E> oldSelection = EnumSet.copyOf(selection);
    selection.clear();
    if (newSelection != null) {
      selection.addAll(newSelection);
    }
    if (!oldSelection.equals(selection)) {
      fireSelectionChanged();
    }
  }

  @Override
  public void setCommitedSelection(Collection<E> newSelection)
  {
    setSelection(newSelection);
    commit();
  }

  @Override
  public void commit()
  {
    if (selection.isEmpty()) {
      commited = Collections.emptySet();
    } else {
      commited = EnumSet.copyOf(selection);
    }
    fireSelectionChanged();
  }

  @Override
  public void revert()
  {
    selection.clear();
    if (commited != null && !commited.isEmpty()) {
      selection.addAll(commited);
    }
    fireSelectionChanged();
  }

  @Override
  public boolean isDataValid()
  {
    return dataValid;
  }

  public void setDataValid(boolean dataValid)
  {
    this.dataValid = dataValid;
  }

  @Override
  public boolean isDataChanged()
  {
    return dataChanged;
  }

  public void setDataChanged(boolean dataChanged)
  {
    this.dataChanged = dataChanged;
  }

  private void checkFlags()
  {
    if (commited == null || commited.isEmpty()) {
      dataChanged = !selection.isEmpty();
    } else {
      dataChanged = selection.size() != commited.size() || !commited.containsAll(selection);
    }
    dataValid = !oneRequired || !selection.isEmpty();
  }

  private void fireSelectionChanged()
  {
    checkFlags();
    listener.stream().
            forEach((l) -> {
              l.selectionChanged(this);
            });
  }

  @Override
  public void select(E e, boolean sel)
  {
    if (e != null) {
      if (selection.contains(e) != sel) {
        if (sel) {
          selection.add(e);
        } else {
          if (!oneRequired || selection.size() > 1) {
            selection.remove(e);
          }
        }
        fireSelectionChanged();
      }
    }
  }

  @Override
  public void addEnumCheckboxModelListener(EnumCheckboxModelListener<E> l)
  {
    if (l != null) {
      listener.add(l);
    }
  }

  @Override
  public void removeEnumCheckboxModelListener(EnumCheckboxModelListener<E> l)
  {
    listener.remove(l);
  }

  @Override
  public String getLabel(E e)
  {
    return e.toString();
  }

  @Override
  public boolean isOneRequired()
  {
    return oneRequired;
  }

  @Override
  public void setOneRequired(boolean or)
  {
    if (oneRequired != or) {
      oneRequired = or;
      fireSelectionChanged();
    }
  }

}
