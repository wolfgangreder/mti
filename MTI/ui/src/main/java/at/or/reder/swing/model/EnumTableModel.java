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

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.netbeans.api.annotations.common.NonNull;

public interface EnumTableModel<E extends Enum<E>> extends TableModel
{

  public default TableCellRenderer getRenderer(E col)
  {
    if (col instanceof ColumnDescriptor) {
      return ((ColumnDescriptor) col).getRenderer();
    }
    return null;
  }

  public default TableCellEditor getEditor(E col)
  {
    if (col instanceof ColumnDescriptor) {
      return ((ColumnDescriptor) col).getEditor();
    }
    return null;
  }

  public String getColumnName(@NonNull E col);

  public Class<?> getColumnClass(@NonNull E col);

  public boolean isCellEditable(int rowIndex,
                                @NonNull E col);

  public Object getValueAt(int rowIndex,
                           @NonNull E col);

  public void setValueAt(Object aValue,
                         int rowIndex,
                         @NonNull E col);

}
