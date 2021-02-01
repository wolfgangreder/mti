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

import java.util.Collection;
import java.util.stream.Stream;

public class DefaultListModel<E> extends AbstractListModel<E>
{

  public DefaultListModel()
  {
  }

  public DefaultListModel(Collection<? extends E> data)
  {
    super(data);
  }

  public final boolean isEmpty()
  {
    return data.isEmpty();
  }

  public final Stream<E> stream()
  {
    return data.stream();
  }

  public final void add(E item)
  {
    if (data.add(item)) {
      fireContentsChanged();
    }
  }

  public final void add(int index,
                        E item)
  {
    data.add(index,
             item);
    fireContentsChanged();
  }

  public final void addAll(Collection<? extends E> items)
  {
    data.addAll(items);
    fireContentsChanged();
  }

  public final void addAll(int index,
                           Collection<? extends E> items)
  {
    data.addAll(index,
                items);
    fireContentsChanged();
  }

  public final void remove(E item)
  {
    data.remove(item);
    fireContentsChanged();
  }

  public final void remove(int index)
  {
    data.remove(index);
    fireContentsChanged();
  }

  public final void clear()
  {
    data.clear();
    fireContentsChanged();
  }

  public final void set(Collection<? extends E> newContent)
  {
    data.clear();
    data.addAll(newContent);
    fireContentsChanged();
  }

}
