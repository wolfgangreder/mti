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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public final class MonthComboBoxModel implements ComboBoxModel<MonthComboBoxModel.Item>
{

  public static final class Item
  {

    private final int key;
    private final String name;

    public Item(int key,
                String name)
    {
      this.key = key;
      this.name = name;
    }

    public int getKey()
    {
      return key;
    }

    public String getName()
    {
      return name;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final Item other = (Item) obj;
      if (this.key != other.key) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode()
    {
      int hash = 3;
      hash = 97 * hash + this.key;
      return hash;
    }

    @Override
    public String toString()
    {
      return name;
    }

  }
  private final Set<ListDataListener> listeners = new CopyOnWriteArraySet<>();
  private final List<Item> data;
  private Item selected;

  public MonthComboBoxModel()
  {
    List<Item> tmp = new ArrayList<>(12);
    SimpleDateFormat fmt = new SimpleDateFormat("MMMM");
    Calendar cal = Calendar.getInstance();
    for (int m = Calendar.JANUARY; m <= Calendar.DECEMBER; ++m) {
      cal.set(Calendar.MONTH,
              m);
      tmp.add(new Item(m,
                       fmt.format(cal.getTime())));
    }
    data = Collections.unmodifiableList(tmp);
    selected = data.get(0);
  }

  @Override
  public Item getSelectedItem()
  {
    return selected;
  }

  @Override
  public void setSelectedItem(Object anItem)
  {
    Item old = selected;
    if (anItem instanceof Item) {
      int index = data.indexOf(anItem);
      if (index >= 0) {
        selected = data.get(index);
      }
    } else if (anItem instanceof Number) {
      int index = data.indexOf(new Item(((Number) anItem).intValue(),
                                        ""));
      if (index >= 0) {
        selected = data.get(index);
      }
    } else {
      selected = null;
    }
    if (!Objects.equals(old,
                        selected)) {
      fireSelectionChanged();
    }
  }

  @Override
  public void addListDataListener(ListDataListener l)
  {
    if (l != null) {
      listeners.add(l);
    }
  }

  @Override
  public Item getElementAt(int index)
  {
    return data.get(index);
  }

  @Override
  public int getSize()
  {
    return data.size();
  }

  @Override
  public void removeListDataListener(ListDataListener l)
  {
    listeners.remove(l);
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

}
