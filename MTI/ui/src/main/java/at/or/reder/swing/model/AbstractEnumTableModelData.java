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

public abstract class AbstractEnumTableModelData<E extends Enum<E>, D> extends AbstractEnumTableModel<E>
{

  protected final List<D> data = new ArrayList<>();

  protected AbstractEnumTableModelData(Class<E> clazz)
  {
    super(clazz);
  }

  @Override
  public final boolean isEmpty()
  {
    return data.isEmpty();
  }

  protected void replaceDataItem(int index,
                                 D newItem)
  {
    data.set(index,
             newItem);
    fireRowUpdated(index);
  }

  protected D getDataItem(int index)
  {
    return data.get(index);
  }

  @Override
  public final int getRowCount()
  {
    return data.size();
  }

  protected abstract Object getValue(D data,
                                     E col);

  @Override
  protected Object uncheckedGetValueAt(int rowIndex,
                                       E col)
  {
    return getValue(data.get(rowIndex),
                    col);
  }

  /**
   * Dient zum Ändern des Tabelleninhalts. Die Standardimplementation wirft eine {@code UnsupportedOperationException}. Soll ein
   * TableModel mutierbar sein, muss entweder diese Methode oder {@link at.mountainsd.msdswing.models.AbstractEnumTableModel#setValueAt(java.lang.Object, int, java.lang.Enum)
   * } überschrienben werden.
   *
   * @param newValue newvalue
   * @param data data
   * @param rowIndex rowIndex
   * @param col column
   * @return {@code true} falls ein Event ausgelöst werden soll.
   * @see at.mountainsd.msdswing.models.AbstractEnumTableModel#setValueAt(java.lang.Object, int, java.lang.Enum)
   */
  protected boolean setValueAt(Object newValue,
                               D data,
                               int rowIndex,
                               E col)
  {
    throw new UnsupportedOperationException("Not supported.");
  }

  @Override
  public void setValueAt(Object newValue,
                         int row,
                         E col)
  {
    if (isCellEditable(row,
                       col)) {
      D d = data.get(row);
      if (setValueAt(newValue,
                     d,
                     row,
                     col)) {
        fireCellUpdated(row,
                        col);
      }
    }
  }

}
