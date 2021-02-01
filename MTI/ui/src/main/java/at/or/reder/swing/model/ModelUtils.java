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

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public final class ModelUtils
{

  private static final ComboBoxModel<?> emptyComboBox = new ComboBoxModel<Object>()
  {

    @Override
    public void setSelectedItem(Object anItem)
    {
    }

    @Override
    public Object getSelectedItem()
    {
      return null;
    }

    @Override
    public int getSize()
    {
      return 0;
    }

    @Override
    public Object getElementAt(int index)
    {
      throw new IndexOutOfBoundsException();
    }

    @Override
    public void addListDataListener(ListDataListener l)
    {
    }

    @Override
    public void removeListDataListener(ListDataListener l)
    {
    }

  };

  @SuppressWarnings("unchecked")
  public static <T> ComboBoxModel<T> getEmptyComboBoxModel()
  {
    return (ComboBoxModel<T>) emptyComboBox;
  }

  private ModelUtils()
  {
  }

}
