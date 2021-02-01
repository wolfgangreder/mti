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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.netbeans.api.annotations.common.NonNull;

/**
 * Ein TableModel das die Spalten mithilfe einer Enumeration organisiert.
 *
 * @author reder
 * @param <E> Enum der die Tabellenspalten darstellt.
 */
public abstract class AbstractEnumTableModel<E extends Enum<E>> extends AbstractTableModel implements EnumTableModel<E>
{

  protected final List<E> columns;

  protected AbstractEnumTableModel(@NonNull Class<E> clazz)
  {
    List<E> tmp = null;
    try {
      Method methValues = clazz.getMethod("values",
                                          (Class<?>[]) null);
      @SuppressWarnings("unchecked")
      E[] values = (E[]) methValues.invoke(null,
                                           (Object[]) null);
      tmp = Collections.unmodifiableList(Arrays.asList(values));
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
      throw new IllegalArgumentException(ex);
    }
    if (tmp == null) {
      tmp = Collections.emptyList();
    }
    columns = tmp;
  }

  @Override
  public E getColumnEnum(int columnIndex)
  {
    if (columnIndex < 0 || columnIndex >= columns.size()) {
      return null;
    }
    for (E e : columns) {
      if (e.ordinal() == columnIndex) {
        return e;
      }
    }
    return null;
  }

  @Override
  public int getColumnCount()
  {
    return columns.size();
  }

  public boolean isColumnRequired(@NonNull E col)
  {
    if (col instanceof ColumnDescriptor) {
      return ((ColumnDescriptor) col).isRequired();
    }
    return false;
  }

  @Override
  public final boolean isColumnRequired(int columnIndex)
  {
    return isColumnRequired(columns.get(columnIndex));
  }

  public boolean isColumnDefaultVisible(@NonNull E col)
  {
    if (col instanceof ColumnDescriptor) {
      return ((ColumnDescriptor) col).isDefaultVisible();
    }
    return true;
  }

  @Override
  public final boolean isColumnDefaultVisible(int columnIndex)
  {
    return isColumnDefaultVisible(columns.get(columnIndex));
  }

  public boolean isColumnDisplayable(@NonNull E col)
  {
    if (col instanceof ColumnDescriptor) {
      return ((ColumnDescriptor) col).isDisplayable();
    }
    return true;
  }

  @Override
  public final boolean isColumnDisplayable(int columnIndex)
  {
    return isColumnDisplayable(columns.get(columnIndex));
  }

  public int getDefaultColumnWidth(@NonNull E col)
  {
    if (col instanceof ColumnDescriptor) {
      return ((ColumnDescriptor) col).getDefaultWidth();
    }
    return 75;
  }

  @Override
  public final int getDefaultColumnWidth(int columnIndex)
  {
    return getDefaultColumnWidth(columns.get(columnIndex));
  }

  @Override
  public String getColumnName(@NonNull E col)
  {
    if (col instanceof ColumnDescriptor) {
      return ((ColumnDescriptor) col).getHeaderValue();
    } else {
      return col.toString();
    }
  }

  @Override
  public String getColumnName(int columnIndex)
  {
    return getColumnName(columns.get(columnIndex));
  }

  protected Class<?> fallbackGetColumnClass(@NonNull E col)
  {
    return Object.class;
  }

  @Override
  public final Class<?> getColumnClass(@NonNull E col)
  {
    Class<?> result = null;
    if (col instanceof ColumnDescriptor) {
      result = ((ColumnDescriptor) col).getValueClass();
    }
    if (result == null) {
      return fallbackGetColumnClass(col);
    }
    return result;
  }

  @Override
  public Class<?> getColumnClass(int columnIndex)
  {
    return getColumnClass(columns.get(columnIndex));
  }

  @Override
  public boolean isCellEditable(int rowIndex,
                                @NonNull E col)
  {
    return false;
  }

  @Override
  public boolean isCellEditable(int rowIndex,
                                int columnIndex)
  {
    return isCellEditable(rowIndex,
                          columns.get(columnIndex));
  }

  protected abstract Object uncheckedGetValueAt(int rowIndex,
                                                E col);

  @Override
  public final Object getValueAt(int rowIndex,
                                 @NonNull E col)
  {
    Object tmp = uncheckedGetValueAt(rowIndex,
                                     col);
    assert tmp == null || getColumnClass(col) == null || getColumnClass(col).isInstance(tmp) :
            getAssertMessage(rowIndex,
                             col.ordinal(),
                             getColumnClass(col),
                             tmp);
    return tmp;
  }

  @Override
  protected Object uncheckedGetValueAt(int rowIndex,
                                       int columnIndex)
  {
    return uncheckedGetValueAt(rowIndex,
                               columns.get(columnIndex));
  }

  @Override
  public void setValueAt(Object aValue,
                         int rowIndex,
                         @NonNull E col)
  {
    throw new UnsupportedOperationException("Not supported.");
  }

  @Override
  public void setValueAt(Object aValue,
                         int rowIndex,
                         int columnIndex)
  {
    setValueAt(aValue,
               rowIndex,
               columns.get(columnIndex));
  }

  /**
   * Erzeugt eine Tabellenspalte mit dem HeaderValue {@code getColumnName(col);} und dem Identifier {@code col}.
   *
   * @param col column
   * @param width width
   * @param header header
   * @return column
   */
  public TableColumn createColumn(@NonNull E col,
                                  int width,
                                  Object header)
  {
    if (width == -1) {
      width = getDefaultColumnWidth(col);
    }
    TableColumn result = new TableColumn(col.ordinal(),
                                         width);
    if (header != null) {
      result.setHeaderValue(header);
    } else {
      result.setHeaderValue(getColumnName(col));
    }
    result.setIdentifier(col);
    TableCellRenderer renderer = getRenderer(col);
    if (renderer != null) {
      result.setCellRenderer(renderer);
    }
    TableCellEditor editor = getEditor(col);
    if (editor != null) {
      result.setCellEditor(editor);
    }
    return result;
  }

  @Override
  public TableColumn createColumn(int col,
                                  int width,
                                  Object header)
  {
    return createColumn(columns.get(col),
                        width,
                        header);
  }

  protected void fireCellUpdated(int row,
                                 @NonNull E col)
  {
    fireCellUpdated(row,
                    col.ordinal());
  }

  protected void fireColChanged(@NonNull E col)
  {
    fireColChanged(col.ordinal());
  }

}
