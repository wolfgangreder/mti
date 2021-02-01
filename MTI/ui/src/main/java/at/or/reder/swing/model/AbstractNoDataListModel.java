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

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public abstract class AbstractNoDataListModel<I> implements ListModel<I>
{

  private final Set<ListDataListener> listeners = new CopyOnWriteArraySet<>();

  protected AbstractNoDataListModel()
  {
  }

  @Override
  public final void addListDataListener(ListDataListener l)
  {
    if (l != null) {
      listeners.add(l);
    }
  }

  @Override
  public final void removeListDataListener(ListDataListener l)
  {
    listeners.remove(l);
  }

  protected final void fireContentsChanged()
  {
    fireContentsChanged(0,
                        getSize() - 1);
  }

  protected final void fireContentsChanged(int indexFrom,
                                           int indexTo)
  {
    ListDataEvent evt = new ListDataEvent(this,
                                          ListDataEvent.CONTENTS_CHANGED,
                                          indexFrom,
                                          indexTo);
    for (ListDataListener l : listeners) {
      l.contentsChanged(evt);
    }
  }

  protected final void fireIntervalAdded(int indexFrom,
                                         int indexTo)
  {
    ListDataEvent evt = new ListDataEvent(this,
                                          ListDataEvent.INTERVAL_ADDED,
                                          indexFrom,
                                          indexTo);
    for (ListDataListener l : listeners) {
      l.intervalAdded(evt);
    }
  }

  protected final void fireIntervalRemoved(int indexFrom,
                                           int indexTo)
  {
    ListDataEvent evt = new ListDataEvent(this,
                                          ListDataEvent.INTERVAL_REMOVED,
                                          indexFrom,
                                          indexTo);
    for (ListDataListener l : listeners) {
      l.intervalRemoved(evt);
    }
  }

}
