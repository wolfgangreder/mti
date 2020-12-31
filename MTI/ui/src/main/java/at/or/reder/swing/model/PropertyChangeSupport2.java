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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public final class PropertyChangeSupport2 extends KeyedEventSupport<Object, String, PropertyChangeListener>
{

  public PropertyChangeSupport2(Object source)
  {
    super(source);
  }

  public void addPropertyChangeListener(PropertyChangeListener l)
  {
    super.addListener(l);
  }

  public void addPropertyChangeListener(String propName,
                                        PropertyChangeListener l)
  {
    super.addListener(propName,
                      l);
  }

  public void removePropertyChangeListener(PropertyChangeListener l)
  {
    super.removeListener(l);
  }

  public void removePropertyChangeListener(String propName,
                                           PropertyChangeListener l)
  {
    super.removeListener(propName,
                         l);
  }

  public void firePropertyChange(PropertyChangeEvent event)
  {
    if (event == null || Objects.equals(event.getOldValue(),
                                        event.getNewValue())) {
      return;
    }
    fire(event.getPropertyName(),
         (Iterable<? extends PropertyChangeListener> listener) -> {
           for (PropertyChangeListener l : listener) {
             l.propertyChange(event);
           }
         });
  }

  private void internalFirePropertyChange(String propertyName,
                                          Object oldValue,
                                          Object newValue)
  {
    if (Objects.equals(oldValue,
                       newValue)) {
      return;
    }
    fire(propertyName,
         (Iterable<? extends PropertyChangeListener> listener, PropertyChangeEvent event) -> {
           for (PropertyChangeListener l : listener) {
             l.propertyChange(event);
           }
         },
         () -> {
           return new PropertyChangeEvent(getSource(),
                                          propertyName,
                                          oldValue,
                                          newValue);
         });
  }

  public void firePropertyChange(String propertyName,
                                 Object oldValue,
                                 Object newValue)
  {
    internalFirePropertyChange(propertyName,
                               oldValue,
                               newValue);
  }

  public void firePropertyChange(String propertyName,
                                 int oldValue,
                                 int newValue)
  {
    internalFirePropertyChange(propertyName,
                               oldValue,
                               newValue);
  }

  public void firePropertyChange(String propertyName,
                                 boolean oldValue,
                                 boolean newValue)
  {
    internalFirePropertyChange(propertyName,
                               oldValue,
                               newValue);
  }

  public void firePropertyChange(String propertyName,
                                 String oldValue,
                                 String newValue)
  {
    internalFirePropertyChange(propertyName,
                               oldValue,
                               newValue);
  }

}
