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
import java.util.List;
import java.util.Set;

public interface EnumCheckboxModel<E extends Enum<E>>
{

  public List<E> getSelectionValues();

  public Set<E> getSelection();

  public void setSelection(Collection<E> newSelection);

  public void setCommitedSelection(Collection<E> newSelection);

  public boolean isDataValid();

  public boolean isDataChanged();

  public void commit();

  public void revert();

  public void select(E e,
                     boolean sel);

  public void addEnumCheckboxModelListener(EnumCheckboxModelListener<E> listener);

  public void removeEnumCheckboxModelListener(EnumCheckboxModelListener<E> listener);

  public String getLabel(E e);

  public boolean isOneRequired();

  public void setOneRequired(boolean or);

}
