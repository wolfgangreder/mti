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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.function.Function;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.text.DefaultFormatterFactory;

public class SwitchablePercentTableCellEditor extends DefaultCellEditor
{

  private final Function<Integer, Boolean> percentMode;

  private final class MyFormatter extends AbstractFormatter
  {

    private static final long serialVersionUID = 1L;

    @Override
    public Object stringToValue(String text) throws ParseException
    {
      return SwitchablePercentTableCellEditor.this.stringToValue(text);
    }

    @Override
    public String valueToString(Object value)
    {
      return SwitchablePercentTableCellEditor.this.valueToString(value);
    }

  };

  public SwitchablePercentTableCellEditor(Function<Integer, Boolean> percentMode)
  {
    this(percentMode,
         new JFormattedTextField());
  }

  private SwitchablePercentTableCellEditor(Function<Integer, Boolean> percentMode,
                                           JFormattedTextField field)
  {
    super(field);
    field.setFormatterFactory(new DefaultFormatterFactory(new MyFormatter()));
    this.percentMode = percentMode;
  }

  private static final long serialVersionUID = 1L;
  NumberFormat percentFmt = DecimalFormat.getPercentInstance();
  NumberFormat decimalFmt = DecimalFormat.getCurrencyInstance();
  private int rowIndex = -1;

  @Override
  public Component getTableCellEditorComponent(JTable table,
                                               Object value,
                                               boolean isSelected,
                                               int row,
                                               int column)
  {
    rowIndex = row;
    return super.getTableCellEditorComponent(table,
                                             value,
                                             isSelected,
                                             row,
                                             column);
  }

  @Override
  public Component getTreeCellEditorComponent(JTree tree,
                                              Object value,
                                              boolean isSelected,
                                              boolean expanded,
                                              boolean leaf,
                                              int row)
  {
    rowIndex = row;
    return super.getTreeCellEditorComponent(tree,
                                            value,
                                            isSelected,
                                            expanded,
                                            leaf,
                                            row);
  }

  private String valueToString(Object value)
  {
    String result = null;
    if (value instanceof Number) {
      if (percentMode.apply(rowIndex)) {
        result = percentFmt.format(((Number) value).doubleValue());
      } else {
        result = decimalFmt.format(((Number) value).doubleValue());
      }
    }
    return result;
  }

  private Object stringToValue(String strValue) throws ParseException
  {
    if (strValue == null || strValue.trim().isEmpty()) {
      return null;
    }
    if (percentMode.apply(rowIndex)) {
      return percentFmt.parse(strValue).doubleValue();
    } else {
      return decimalFmt.parse(strValue);
    }
  }

}
