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

import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle.Messages;

@Messages({"CornerButtonController.resetAction.name=Reset"})
public final class CornerButtonController
{

  private final class ResetAction extends AbstractAction
  {

    private static final long serialVersionUID = 1L;

    private ResetAction()
    {
      super(Bundle.CornerButtonController_resetAction_name());
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
      resetColumns();
    }

  };
  private final JTable table;
  private String prefix;
  private final JButton cornerButton;
  private final Set<Integer> disabledColumns = new HashSet<>();
  private TableColumnModel colModel;
  private TableColumnFactory _factory;
  private ColumnFilter _filter;
  private final Runnable autoCreateColumns;
  private final TableColumnModelListener colModelListener = new TableColumnModelListener()
  {
    @Override
    public void columnAdded(TableColumnModelEvent e)
    {
      createColumns(null);
    }

    @Override
    public void columnMarginChanged(ChangeEvent e)
    {
    }

    @Override
    public void columnMoved(TableColumnModelEvent e)
    {
    }

    @Override
    public void columnRemoved(TableColumnModelEvent e)
    {
      createColumns(null);
    }

    @Override
    public void columnSelectionChanged(ListSelectionEvent e)
    {
    }

  };
  private boolean allowReset = true;

  public interface TableColumnFactory
  {

    public TableColumn createColumn(int column);

  }

  public interface TableColumnFactory2 extends TableColumnFactory
  {

    public TableColumn createColumn(int col,
                                    Object headerValue);

  }

  public static abstract class AbstractTableColumnFactory2 implements TableColumnFactory2
  {

    @Override
    public TableColumn createColumn(int column)
    {
      return createColumn(column,
                          null);
    }

  }

  public interface ColumnFilter
  {

    public boolean canCreateColumn(int modelIndex);

  }

  public CornerButtonController(JTable table,
                                String prefix)
  {
    this(table,
         prefix,
         null,
         null);
  }

  public CornerButtonController(JTable table,
                                String prefix,
                                TableColumnFactory factory,
                                ColumnFilter filter)
  {
    this(table,
         prefix,
         factory,
         filter,
         null);
  }

  public CornerButtonController(JTable table,
                                String prefix,
                                TableColumnFactory factory,
                                ColumnFilter filter,
                                Runnable autoCreateColumns)
  {
    this.table = table;
    this.prefix = prefix;
    this.autoCreateColumns = autoCreateColumns;
    setColumnFactory(factory);
    setColumnFilter(filter);
    this.cornerButton = createCornerButton();
    table.getModel().addTableModelListener(CornerButtonController.this::tableChanged);
    colModel = table.getColumnModel();
    colModel.addColumnModelListener(colModelListener);
  }

  private void tableChanged(TableModelEvent e)
  {
    TableColumnModel newModel = table.getColumnModel();
    if (colModel != newModel) {
      if (colModel != null) {
        colModel.removeColumnModelListener(colModelListener);
      }
      newModel.addColumnModelListener(colModelListener);
      colModel = newModel;
    }
  }

  public boolean isResetAllowed()
  {
    return allowReset;
  }

  public void setResetAllowed(boolean a)
  {
    allowReset = a;
  }

  public TableColumnFactory getColumnFactory()
  {
    return _factory;
  }

  public void addDisabledColumns(Collection<Integer> col)
  {
    disabledColumns.addAll(col);
  }

  public void removeDisabledColumns(Collection<Integer> col)
  {
    disabledColumns.removeAll(col);
  }

  public void setColumnFactory(TableColumnFactory factory)
  {
    this._factory = factory;
  }

  public ColumnFilter getColumnFilter()
  {
    return _filter;
  }

  public void setColumnFilter(ColumnFilter filter)
  {
    this._filter = filter;
  }

  public void setPrefix(String prefix)
  {
    this.prefix = prefix;
  }

  public String getPrefix()
  {
    return prefix;
  }

  private JButton createCornerButton()
  {
    JButton btn = new JButton(ImageUtilities.loadImageIcon("at/mountainsd/msdswing/models/colselector10.png",
                                                           true));
    btn.addActionListener(CornerButtonController.this::showColumnSelector);
    JScrollPane pane = getScrollPane();
    if (pane != null) {
      pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
      pane.setCorner(JScrollPane.UPPER_RIGHT_CORNER,
                     btn);

    }
    return btn;
  }

  public void writeProperties(Properties props)
  {
    props.setProperty(prefix + ".colCount",
                      Integer.toString(colModel.getColumnCount()));
    for (int i = 0; i < colModel.getColumnCount(); ++i) {
      TableColumn c = colModel.getColumn(i);
      props.setProperty(MessageFormat.format("{1}col.{0,number,#}.modelIndex",
                                             i,
                                             prefix),
                        Integer.toString(c.getModelIndex()));
      props.setProperty(MessageFormat.format("{1}col.{0,number,#}.width",
                                             i,
                                             prefix),
                        Integer.toString(c.getWidth()));
    }

  }

  public void readProperties(Properties props)
  {
    createColumns(props);
  }

  private TableColumn createColumn(int col,
                                   TableModel model,
                                   Object header)
  {
    return createColumn(col,
                        model,
                        -1,
                        header);
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
    if (_factory instanceof TableColumnFactory2) {
      result = ((TableColumnFactory2) _factory).createColumn(col,
                                                             h);
    } else if (_factory != null) {
      result = _factory.createColumn(col);
      result.setHeaderValue(h);
    } else if (model instanceof AbstractTableModel) {
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

  private boolean isMenuItemEnabled(int modelIndex,
                                    TableModel model)
  {
    if (disabledColumns.contains(modelIndex)) {
      return false;
    } else if (model instanceof AbstractTableModel) {
      return !((AbstractTableModel) model).isColumnRequired(modelIndex);
    }
    return true;
  }

  private JCheckBoxMenuItem createMenuItem(final int modelCol)
  {
    final int tableColIndex = table.convertColumnIndexToView(modelCol);
    final String colName;
    if (tableColIndex >= 0) {
      colName = table.getColumnName(table.convertColumnIndexToView(modelCol));
    } else {
      colName = table.getModel().getColumnName(modelCol);
    }
    final JCheckBoxMenuItem item = new JCheckBoxMenuItem(new AbstractAction(colName)
    {

      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerformed(ActionEvent e)
      {
        boolean show = ((JCheckBoxMenuItem) e.getSource()).isSelected();
        if (!show) {
          TableColumn col = table.getColumnModel().getColumn(tableColIndex);
          table.removeColumn(col);
        } else {
          table.addColumn(createColumn(modelCol,
                                       table.getModel(),
                                       -1,
                                       null));
        }
      }

    }
    );
    item.setState(tableColIndex >= 0);
    item.setEnabled(isMenuItemEnabled(modelCol,
                                      table.getModel()));
    return item;
  }

  private JScrollPane getScrollPane()
  {
    Container container = table.getParent();
    while ((container != null) && !(container instanceof JScrollPane)) {
      container = container.getParent();
    }
    return container instanceof JScrollPane ? (JScrollPane) container : null;
  }

  private void showColumnSelector(ActionEvent evt)
  {
    JPopupMenu pop = new JPopupMenu();

    if (allowReset) {
      pop.add(new JMenuItem(new ResetAction()));
    }
    TableModel model = table.getModel();
    AbstractTableModel am = null;
    if (model instanceof AbstractTableModel) {
      am = (AbstractTableModel) model;
    }
    if (model != null) {
      for (int i = 0; i < model.getColumnCount(); ++i) {
        if (am == null || am.isColumnDisplayable(i)) {
          pop.add(createMenuItem(i));
        }
      }
    }
    JScrollPane pane = getScrollPane();
    Point point = cornerButton.getLocationOnScreen();
    SwingUtilities.convertPointFromScreen(point,
                                          pane);
    point.move(point.x,
               point.y + cornerButton.getHeight());
    pop.show(pane,
             point.x,
             point.y);
  }

  public void createColumns()
  {
    createColumns(null);
  }

  private void createDefaultColumns()
  {
    if (autoCreateColumns != null) {
      autoCreateColumns.run();
    } else {
      TableModel model = table.getModel();
      if (model != null) {
        for (int i = 0; i < model.getColumnCount(); ++i) {
          if (canCreateColumn(i,
                              model,
                              true)) {
            TableColumn col = createColumn(i,
                                           model,
                                           null);
            if (col != null) {
              colModel.addColumn(col);
            }
          }
        }
      } else {
        table.createDefaultColumnsFromModel();
      }
    }
  }

  private void resetColumns()
  {
    colModel.removeColumnModelListener(colModelListener);
    try {
      while (colModel.getColumnCount() > 0) {
        colModel.removeColumn(colModel.getColumn(0));
      }
    } finally {
      colModel.addColumnModelListener(colModelListener);
    }
    createColumns();
  }

  private boolean canCreateColumn(int modelIndex,
                                  TableModel tableModel,
                                  boolean defaultColumns)
  {
    if (_filter != null) {
      return _filter.canCreateColumn(modelIndex);
    } else if (tableModel instanceof AbstractTableModel) {
      AbstractTableModel am = (AbstractTableModel) tableModel;
      if (defaultColumns) {
        return am.isColumnDefaultVisible(modelIndex);
      } else {
        return am.isColumnDisplayable(modelIndex);
      }
    }
    return true;
  }

  @SuppressWarnings("UseSpecificCatch")
  private void createColumns(Properties props)
  {
    colModel.removeColumnModelListener(colModelListener);
    try {
      if (colModel.getColumnCount() == 0 || props != null) {
        if (props != null) {
          while (colModel.getColumnCount() > 0) {
            colModel.removeColumn(colModel.getColumn(0));
          }
          Object prop = props.getProperty(prefix + ".colCount");
          if (prop != null && table != null) {
            try {
              int colCount = Integer.valueOf(prop.toString());
              TableModel model = table.getModel();
              for (int i = 0; i < colCount; ++i) {
                int modelIndex = Integer.valueOf(props.getProperty(MessageFormat.format("{1}col.{0,number,#}.modelIndex",
                                                                                        i,
                                                                                        prefix),
                                                                   Integer.toString(i)));
                if (canCreateColumn(modelIndex,
                                    model,
                                    false)) {
                  int width = Integer.valueOf(
                          props.getProperty(MessageFormat.format("{1}col.{0,number,#}.width",
                                                                 i,
                                                                 prefix),
                                            "-1"));
                  TableColumn col = createColumn(modelIndex,
                                                 model,
                                                 width,
                                                 null);
                  colModel.addColumn(col);
                }
              }
            } catch (Throwable th) {
              Exceptions.printStackTrace(th);
            }
          }
        }
        if (colModel.getColumnCount() == 0) {
          createDefaultColumns();
        }
      }
    } finally {
      colModel.addColumnModelListener(colModelListener);
    }
  }

}
