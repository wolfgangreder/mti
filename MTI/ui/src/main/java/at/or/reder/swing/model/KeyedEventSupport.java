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

import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class KeyedEventSupport<S, K, L>
{

  private final Set<L> commonListener = new CopyOnWriteArraySet<>();
  private final Map<K, Set<L>> propertyListener = new HashMap<>();
  private final S source;

  public KeyedEventSupport(S source)
  {
    this.source = source;
  }

  protected S getSource()
  {
    return source;
  }

  protected void fire(K key,
                      Consumer<Iterable<? extends L>> c)
  {
    Set<L> namedListener;
    synchronized (propertyListener) {
      namedListener = propertyListener.get(key);
    }
    if (namedListener != null && !namedListener.isEmpty()) {
      c.accept(Collections.unmodifiableSet(namedListener));
    }
    if (!commonListener.isEmpty()) {
      c.accept(Collections.unmodifiableSet(commonListener));
    }
  }

  protected <E extends EventObject> void fire(K key,
                                              BiConsumer<Iterable<? extends L>, E> c,
                                              Supplier<E> createEventObject)
  {
    Set<L> namedListener;
    synchronized (propertyListener) {
      namedListener = propertyListener.get(key);
    }
    if ((namedListener == null || namedListener.isEmpty()) && commonListener.isEmpty()) {
      return;
    }
    E e = createEventObject != null ? createEventObject.get() : null;
    if (namedListener != null && !namedListener.isEmpty()) {
      c.accept(Collections.unmodifiableSet(namedListener),
               e);
    }
    if (!commonListener.isEmpty()) {
      c.accept(Collections.unmodifiableSet(commonListener),
               e);
    }
  }

  public void addListener(K propertyName,
                          L l)
  {
    if (l != null && propertyName != null) {
      Set<L> listener;
      synchronized (propertyListener) {
        listener = propertyListener.computeIfAbsent(propertyName,
                                                    (K s) -> {
                                                      return new CopyOnWriteArraySet<>();
                                                    });
      }
      listener.add(l);
    }
  }

  public void addListener(L l)
  {
    if (l != null) {
      commonListener.add(l);
    }
  }

  public void removeListener(K propertyName,
                             L l)
  {
    if (propertyName != null) {
      synchronized (propertyListener) {
        Set<L> listener = propertyListener.get(propertyName);
        if (listener != null) {
          listener.remove(l);
          if (listener.isEmpty()) {
            propertyListener.remove(propertyName);
          }
        }
      }
    }
  }

  public void removeListener(L l)
  {
    commonListener.remove(l);
  }

}
