/*
 * Copyright 2017 Wolfgang Reder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.or.reder.swing.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.openide.util.Exceptions;

public final class EnumComboBoxModel<E extends Enum<E>> implements ComboBoxModel<E>
{

  private final List<E> items;
  private final Class<E> clazz;
  private final Method methValueOf;
  private final Set<ListDataListener> listeners = new CopyOnWriteArraySet<>();
  private E selected;

  public static <E extends Enum<E>> EnumComboBoxModel<E> getInstanceOf(Class<E> clazz)
  {
    return getInstanceOf(clazz,
                         null);
  }

  @SuppressWarnings("unchecked")
  public static <E extends Enum<E>> EnumComboBoxModel<E> getInstanceOf(Class<E> clazz,
                                                                       Comparator<E> comparator)
  {
    try {
      Method methValues = clazz.getMethod("values",
                                          (Class<?>[]) null);
      E[] values = (E[]) methValues.invoke(null,
                                           (Object[]) null);
      if (comparator != null) {
        Arrays.sort(values,
                    comparator);
      }
      return new EnumComboBoxModel<>(Arrays.asList(values),
                                     clazz);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
      Exceptions.printStackTrace(ex);
    }
    throw new IllegalStateException();
  }

  @SafeVarargs
  @SuppressWarnings("varargs")
  public static <E extends Enum<E>> EnumComboBoxModel<E> getInstanceOf(E firstValue,
                                                                       E... values)
  {
    Class<E> clazz = firstValue.getDeclaringClass();
    List<E> valueList;
    if (values == null || values.length == 0) {
      valueList = Collections.singletonList(firstValue);
    } else {
      valueList = new ArrayList<>(values.length + 1);
      valueList.add(firstValue);
      valueList.addAll(Arrays.asList(values));
    }
    return new EnumComboBoxModel<>(valueList,
                                   clazz);
  }

  public static <E extends Enum<E>> EnumComboBoxModel<E> getInstanceOf(Collection<E> values)
  {
    if (values.isEmpty()) {
      throw new IllegalArgumentException("values is empty");
    }
    Class<E> clazz = values.iterator().next().getDeclaringClass();
    return new EnumComboBoxModel<>(values,
                                   clazz);
  }

  private EnumComboBoxModel(Collection<E> it,
                            Class<E> clazz)
  {
    this.clazz = clazz;
    try {
      methValueOf = clazz.getMethod("valueOf",
                                    String.class);
    } catch (NoSuchMethodException | SecurityException ex) {
      throw new IllegalStateException();
    }
    items = Collections.unmodifiableList(new ArrayList<>(it));
  }

  @Override
  @SuppressWarnings("unchecked")
  public void setSelectedItem(Object anItem)
  {
    E oldSelection = selected;
    if (clazz.isInstance(anItem)) {
      selected = (E) anItem;
    } else if (anItem instanceof String) {
      try {
        Object tmp = methValueOf.invoke(null,
                                        anItem);
        if (clazz.isInstance(tmp)) {
          selected = (E) tmp;
        }
      } catch (IllegalAccessException | InvocationTargetException ex) {
      }
    }
    if (!Objects.equals(selected,
                        oldSelection)) {
      fireSelectionChanged();
    }
  }

  private void fireSelectionChanged()
  {
    ListDataEvent evt = new ListDataEvent(this,
                                          ListDataEvent.CONTENTS_CHANGED,
                                          -1,
                                          -1);
    for (ListDataListener l : listeners) {
      l.contentsChanged(evt);
    }
  }

  @Override
  public E getSelectedItem()
  {
    return selected;
  }

  @Override
  public int getSize()
  {
    return items.size();
  }

  @Override
  public E getElementAt(int index)
  {
    return items.get(index);
  }

  @Override
  public void addListDataListener(ListDataListener l)
  {
    if (l != null) {
      listeners.add(l);
    }
  }

  @Override
  public void removeListDataListener(ListDataListener l)
  {
    listeners.remove(l);
  }

}
