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
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public final class PercentTableCellRenderer extends DefaultTableCellRenderer
{

  private static final long serialVersionUID = 1L;
  NumberFormat fmt = DecimalFormat.getPercentInstance();

  @Override
  protected void setValue(Object value)
  {
    if (value instanceof Number) {
      value = fmt.format(((Number) value).doubleValue());
    } else {
      value = null;
    }
    super.setValue(value);
  }

  @Override
  public Component getTableCellRendererComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus,
                                                 int row,
                                                 int column)
  {
    super.getTableCellRendererComponent(table,
                                        value,
                                        isSelected,
                                        hasFocus,
                                        row,
                                        column);
    setHorizontalAlignment(SwingConstants.RIGHT);
    return this;
  }

}
