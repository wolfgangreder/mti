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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author Wolfgang Reder
 */
public abstract class AbstractComboBoxModel<E> implements ComboBoxModel<E>
{

  protected final List<E> data = new ArrayList<>();
  private final Set<ListDataListener> listener = new CopyOnWriteArraySet<>();
  protected E selected;

  @Override
  public final E getSelectedItem()
  {
    return selected;
  }

  @Override
  public final int getSize()
  {
    return data.size();
  }

  @Override
  public final E getElementAt(int index)
  {
    return data.get(index);
  }

  @Override
  public final void addListDataListener(ListDataListener l)
  {
    if (l != null) {
      listener.add(l);
    }
  }

  @Override
  public final void removeListDataListener(ListDataListener l)
  {
    listener.remove(l);
  }

  protected final void fireContentsChanged()
  {
    if (listener.isEmpty()) {
      return;
    }
    ListDataEvent evt = new ListDataEvent(this,
                                          ListDataEvent.CONTENTS_CHANGED,
                                          0,
                                          data.size());
    for (ListDataListener l : listener) {
      l.contentsChanged(evt);
    }
  }

  protected final void fireSelectionChanged()
  {
    if (listener.isEmpty()) {
      return;
    }
    ListDataEvent evt = new ListDataEvent(this,
                                          ListDataEvent.CONTENTS_CHANGED,
                                          -1,
                                          -1);
    for (ListDataListener l : listener) {
      l.contentsChanged(evt);
    }
  }

  protected final void fireIntervalAdded(int indexFrom,
                                         int indexTo)
  {
    if (listener.isEmpty()) {
      return;
    }
    ListDataEvent evt = new ListDataEvent(this,
                                          ListDataEvent.INTERVAL_ADDED,
                                          indexFrom,
                                          indexTo);
    for (ListDataListener l : listener) {
      l.intervalAdded(evt);
    }
  }

  protected final void fireIntervalRemoved(int indexFrom,
                                           int indexTo)
  {
    if (listener.isEmpty()) {
      return;
    }
    ListDataEvent evt = new ListDataEvent(this,
                                          ListDataEvent.INTERVAL_REMOVED,
                                          indexFrom,
                                          indexTo);
    for (ListDataListener l : listener) {
      l.intervalRemoved(evt);
    }
  }

}
