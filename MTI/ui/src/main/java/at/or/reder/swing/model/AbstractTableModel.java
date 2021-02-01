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

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public abstract class AbstractTableModel implements TableModel
{

  private final Set<TableModelListener> _listener = new CopyOnWriteArraySet<>();

  public TableColumn createColumn(int col,
                                  int width,
                                  Object header)
  {
    TableColumn result = new TableColumn(col,
                                         width);
    if (header != null) {
      result.setHeaderValue(header);
    } else {
      result.setHeaderValue(getColumnName(col));
    }
    result.setIdentifier(col);
    return result;

  }

  /**
   * Liefert {@code true} wenn keine Elemente in der Tablle darzustellen sind.
   *
   * @return empty
   */
  public boolean isEmpty()
  {
    return getRowCount() == 0;
  }

  public boolean isColumnRequired(int columnIndex)
  {
    return false;
  }

  public boolean isColumnDefaultVisible(int columnIndex)
  {
    return true;
  }

  public boolean isColumnDisplayable(int columnIndex)
  {
    return true;
  }

  public int getDefaultColumnWidth(int columnIndex)
  {
    return 75;
  }

  @Override
  public boolean isCellEditable(int rowIndex,
                                int columnIndex)
  {
    return false;
  }

  protected abstract Object uncheckedGetValueAt(int rowIndex,
                                                int columnIndex);

  protected final String getAssertMessage(int rowIndex,
                                          int columnIndex,
                                          Class<?> clazz,
                                          Object val)
  {
    StringBuilder result = new StringBuilder("Assertion failed for tablemodel ");
    result.append(getClass().getName());
    result.append(" at row ");
    result.append(rowIndex);
    result.append(" and column ");
    result.append(columnIndex);
    result.append(" getColumnClass() returned ");
    if (clazz != null) {
      result.append(clazz.getName());
    } else {
      result.append("null");
    }
    result.append(", and value is of class ");
    if (val != null) {
      result.append(val.getClass().getName());
    } else {
      result.append("null");
    }
    return result.toString();
  }

  @Override
  public final Object getValueAt(int rowIndex,
                                 int columnIndex)
  {
    Object tmp = uncheckedGetValueAt(rowIndex,
                                     columnIndex);
    assert tmp == null || getColumnClass(columnIndex) == null || getColumnClass(columnIndex).isInstance(tmp) :
            getAssertMessage(rowIndex,
                             columnIndex,
                             getColumnClass(columnIndex),
                             tmp);
    return tmp;
  }

  @Override
  public void setValueAt(Object aValue,
                         int rowIndex,
                         int columnIndex)
  {
    throw new UnsupportedOperationException("Not supported.");
  }

  @Override
  public void addTableModelListener(TableModelListener l)
  {
    if (l != null) {
      _listener.add(l);
    }
  }

  @Override
  public void removeTableModelListener(TableModelListener l)
  {
    _listener.remove(l);
  }

  protected void fireHeaderChanged()
  {
    doFireEvent(new TableModelEvent(this,
                                    TableModelEvent.HEADER_ROW));
  }

  protected void fireColChanged(int col)
  {
    doFireEvent(new TableModelEvent(this,
                                    0,
                                    getRowCount() - 1,
                                    col));
  }

  protected final void doFireEvent(TableModelEvent evt)
  {
    beforeEventFired(evt);
    for (TableModelListener l : _listener) {
      l.tableChanged(evt);
    }
    afterEventFired(evt);
  }

  /**
   * Soll aufgerufen werden wenn sie die gesamte Tabellestruktur geändert hat.
   */
  protected void fireContentsChanged()
  {
    doFireEvent(new TableModelEvent(this));
  }

  /**
   * Soll aufgerufen werden, wenn sich die Spalten geändert haben.
   */
  protected void fireRowsChanged()
  {
    doFireEvent(new TableModelEvent(this,
                                    TableModelEvent.HEADER_ROW,
                                    TableModelEvent.HEADER_ROW,
                                    TableModelEvent.ALL_COLUMNS,
                                    TableModelEvent.UPDATE));
  }

  /**
   * Soll aufgerufen werden, wenn sich der Inhalt einer Zeile geändert hat.
   *
   * @param firstRow firstRow
   */
  protected void fireEvent(int firstRow)
  {
    doFireEvent(new TableModelEvent(this,
                                    firstRow));
  }

  /**
   * Soll aufgerufen werden, wenn sich der Inhalt mehrerer Zeilen geändert hat.
   *
   * @param firstRow firstRow
   * @param lastRow lastRow
   */
  protected void fireEvent(int firstRow,
                           int lastRow)
  {
    doFireEvent(new TableModelEvent(this,
                                    firstRow,
                                    lastRow));
  }

  protected void fireEvent(int firstRow,
                           int lastRow,
                           int column)
  {
    doFireEvent(new TableModelEvent(this,
                                    firstRow,
                                    lastRow,
                                    column));
  }

  protected void fireEvent(int firstRow,
                           int lastRow,
                           int column,
                           int type)
  {
    doFireEvent(new TableModelEvent(this,
                                    firstRow,
                                    lastRow,
                                    column,
                                    type));
  }

  protected void fireCellUpdated(int row,
                                 int column)
  {
    doFireEvent(new TableModelEvent(this,
                                    row,
                                    row,
                                    column,
                                    TableModelEvent.UPDATE));
  }

  protected void fireRowUpdated(int row)
  {
    doFireEvent(new TableModelEvent(this,
                                    row,
                                    row,
                                    TableModelEvent.ALL_COLUMNS,
                                    TableModelEvent.UPDATE));
  }

  /**
   * Wird aufgerufen, bevor die registierten Event-Listener aufgerufen werden.
   * Die Standardimplementierung hat keine funktion.
   *
   * @param evt event
   */
  protected void beforeEventFired(TableModelEvent evt)
  {

  }

  /**
   * Wird aufgerufen, nachdem die registierten Event-Listener aufgerufen wurden.
   * Die Standardimplementierung hat keine funktion.
   *
   * @param evt event
   */
  protected void afterEventFired(TableModelEvent evt)
  {

  }

  protected void fireRowsUpdated(int rowFrom,
                                 int rowTo)
  {
    doFireEvent(new TableModelEvent(this,
                                    rowFrom,
                                    rowTo,
                                    TableModelEvent.ALL_COLUMNS,
                                    TableModelEvent.UPDATE));
  }

}
