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

import at.or.reder.mti.ui.Utils;
import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

public class ColorTableCellRenderer extends JLabel
        implements TableCellRenderer
{

  private Border unselectedBorder = null;
  private Border selectedBorder = null;
  private final boolean isBordered;

  public ColorTableCellRenderer(boolean isBordered)
  {
    this.isBordered = isBordered;
    super.setOpaque(true); //MUST do this for background to show up.
  }

  @Override
  public Component getTableCellRendererComponent(
          JTable table,
          Object color,
          boolean isSelected,
          boolean hasFocus,
          int row,
          int column)
  {
    Color newColor = (Color) color;
    setBackground(newColor);
    if (isBordered) {
      if (isSelected) {
        if (selectedBorder == null) {
          selectedBorder = BorderFactory.createMatteBorder(2,
                                                           5,
                                                           2,
                                                           5,
                                                           table.getSelectionBackground());
        }
        setBorder(selectedBorder);
      } else {
        if (unselectedBorder == null) {
          unselectedBorder = BorderFactory.createMatteBorder(2,
                                                             5,
                                                             2,
                                                             5,
                                                             table.getBackground());
        }
        setBorder(unselectedBorder);
      }
    }
    if (newColor != null) {
      setToolTipText("#" + Utils.color2String(newColor));
    } else {
      setToolTipText(null);
    }
    return this;
  }

}
