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
package at.or.reder.swing.renderer;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.EventObject;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import org.jdesktop.swingx.JXDatePicker;

public class DateTableCellEditor implements TableCellEditor
{

  private final JXDatePicker picker = new JXDatePicker();
  private int clickCountStart = 1;
  private final Set<CellEditorListener> listeners = new CopyOnWriteArraySet<>();

  public DateTableCellEditor()
  {
    picker.addActionListener((ActionEvent e) -> {
      stopCellEditing();
    });
  }

  @Override
  public Component getTableCellEditorComponent(JTable table,
                                               Object value,
                                               boolean isSelected,
                                               int row,
                                               int column)
  {
    if (value instanceof Date) {
      picker.setDate((Date) value);
    } else {
      picker.setDate(null);
    }
    return picker;
  }

  public int getClickCountStart()
  {
    return clickCountStart;
  }

  public void setClickCountStart(int ccs)
  {
    this.clickCountStart = ccs;
  }

  @Override
  public Object getCellEditorValue()
  {
    return picker.getDate();
  }

  @Override
  public boolean isCellEditable(EventObject anEvent)
  {
    if (anEvent instanceof MouseEvent) {
      return ((MouseEvent) anEvent).getClickCount() >= clickCountStart;
    }
    return true;
  }

  @Override
  public boolean shouldSelectCell(EventObject anEvent)
  {
    return true;
  }

  @Override
  public boolean stopCellEditing()
  {
    fireEditingStopped();
    return true;
  }

  @Override
  public void cancelCellEditing()
  {
    fireEditingCanceled();
  }

  @Override
  public void addCellEditorListener(CellEditorListener l)
  {
    if (l != null) {
      listeners.add(l);
    }
  }

  @Override
  public void removeCellEditorListener(CellEditorListener l)
  {
    listeners.remove(l);
  }

  private void fireEditingStopped()
  {
    ChangeEvent evt = new ChangeEvent(this);
    for (CellEditorListener l : listeners) {
      l.editingStopped(evt);
    }
  }

  private void fireEditingCanceled()
  {
    ChangeEvent evt = new ChangeEvent(this);
    for (CellEditorListener l : listeners) {
      l.editingCanceled(evt);
    }
  }

}
