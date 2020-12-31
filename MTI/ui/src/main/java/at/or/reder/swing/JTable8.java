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
package at.or.reder.swing;

import at.or.reder.swing.model.AbstractTableModel;
import at.or.reder.swing.renderer.ColorTableCellEditor;
import at.or.reder.swing.renderer.ColorTableCellRenderer;
import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIDefaults;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

public class JTable8 extends JTable
{

  private static final long serialVersionUID = 1L;
  private static final Map<Class<?>, UIDefaults.LazyValue> DEFAULT_RENDERER = new ConcurrentHashMap<>();
  private static final Map<Class<?>, UIDefaults.LazyValue> DEFAULT_EDITORS = new ConcurrentHashMap<>();

  static {

    DEFAULT_RENDERER.put(Color.class,
                         (UIDefaults.LazyValue) (UIDefaults table) -> new ColorTableCellRenderer(true));
    DEFAULT_EDITORS.put(Color.class,
                        (UIDefaults.LazyValue) (UIDefaults table) -> new ColorTableCellEditor());
  }

  public JTable8()
  {
  }

  public JTable8(TableModel dm)
  {
    super(dm);
  }

  public JTable8(TableModel dm,
                 TableColumnModel cm)
  {
    super(dm,
          cm);
  }

  public JTable8(TableModel dm,
                 TableColumnModel cm,
                 ListSelectionModel sm)
  {
    super(dm,
          cm,
          sm);
  }

  public JTable8(int numRows,
                 int numColumns)
  {
    super(numRows,
          numColumns);
  }

  public JTable8(Object[][] rowData,
                 Object[] columnNames)
  {
    super(rowData,
          columnNames);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void createDefaultRenderers()
  {
    super.createDefaultRenderers();
    defaultRenderersByColumnClass.putAll(DEFAULT_RENDERER);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void createDefaultEditors()
  {
    super.createDefaultEditors();
    defaultEditorsByColumnClass.putAll(DEFAULT_EDITORS);
  }

  private boolean canCreateColumn(int modelIndex,
                                  TableModel tableModel,
                                  boolean defaultColumns)
  {
    if (tableModel instanceof AbstractTableModel) {
      AbstractTableModel am = (AbstractTableModel) tableModel;
      return (!defaultColumns || am.isColumnDefaultVisible(modelIndex)) && am.isColumnDisplayable(modelIndex);
    }
    return true;
  }

  private TableColumn createColumn(int col,
                                   TableModel model,
                                   int width,
                                   Object header)
  {
    TableColumn result;
    Object h = null;
    if (header != null) {
      h = header;
    } else if (model != null) {
      h = model.getColumnName(col);
    }
    if (model instanceof AbstractTableModel) {
      result = ((AbstractTableModel) model).createColumn(col,
                                                         width,
                                                         h);
    } else {
      result = new TableColumn(col,
                               width);
      result.setHeaderValue(h);
    }
    return result;
  }

  @Override
  public void createDefaultColumnsFromModel()
  {
    TableModel model = getModel();
    if (model != null) {
      TableColumnModel colModel = getColumnModel();
      for (int i = 0; i < model.getColumnCount(); ++i) {
        if (canCreateColumn(i,
                            model,
                            true)) {
          TableColumn col = createColumn(i,
                                         model,
                                         -1,
                                         null);
          if (col != null) {
            colModel.addColumn(col);
          }
        }
      }
    } else {
      super.createDefaultColumnsFromModel();
    }
  }

  public static void installGlobalDefaultRenderer(Class<?> clazz,
                                                  final UIDefaults.LazyValue renderer)
  {
    if (renderer != null) {
      DEFAULT_RENDERER.put(clazz,
                           renderer);
    } else {
      DEFAULT_RENDERER.remove(clazz);
    }
  }

  public static void installGlobalDefaultEditor(Class<?> clazz,
                                                final UIDefaults.LazyValue editor)
  {
    if (editor != null) {
      DEFAULT_EDITORS.put(clazz,
                          editor);
    } else {
      DEFAULT_EDITORS.remove(clazz);
    }
  }

}
