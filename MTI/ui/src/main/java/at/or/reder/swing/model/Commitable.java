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

import java.beans.PropertyChangeListener;

public interface Commitable
{

  public static final String PROP_DATACHANGED = "dataChanged";

  public void commit();

  public void revert();

  public boolean isDataChanged();

  public void addPropertyChangeListener(String property,
                                        PropertyChangeListener l);

  public void addPropertyChangeListener(PropertyChangeListener l);

  public void removePropertyChangeListener(String property,
                                           PropertyChangeListener l);

  public void removePropertyChangeListener(PropertyChangeListener l);

}