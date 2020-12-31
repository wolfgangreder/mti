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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public final class ColorTableCellEditor extends AbstractCellEditor
        implements TableCellEditor
{

  private Color currentColor;
  private final JButton button;
  private final JColorChooser colorChooser;
  private final JDialog dialog;
  protected static final String EDIT = "edit";
  private final ActionListener actionListener = ColorTableCellEditor.this::actionPerformed;

  public ColorTableCellEditor()
  {
    //Set up the editor (from the table's point of view),
    //which is a button.
    //This button brings up the color chooser dialog,
    //which is the editor from the user's point of view.
    button = new JButton();
    button.setActionCommand(EDIT);
    button.addActionListener(actionListener);
    button.setBorderPainted(false);

    //Set up the dialog that the button brings up.
    colorChooser = new JColorChooser();
    dialog = JColorChooser.createDialog(button,
                                        "Pick a Color",
                                        true,  //modal
                                        colorChooser,
                                        actionListener,  //OK button handler
                                        null); //no CANCEL button handler
  }

  /**
   * Handles events from the editor button and from the dialog's OK button.
   */
  private void actionPerformed(ActionEvent e)
  {
    if (EDIT.equals(e.getActionCommand())) {
      //The user has clicked the cell, so
      //bring up the dialog.
      button.setBackground(currentColor);
      colorChooser.setColor(currentColor);
      dialog.setVisible(true);

      //Make the renderer reappear.
      fireEditingStopped();

    } else { //User pressed dialog's "OK" button.
      currentColor = colorChooser.getColor();
    }
  }

  //Implement the one CellEditor method that AbstractCellEditor doesn't.
  @Override
  public Object getCellEditorValue()
  {
    return currentColor;
  }

  //Implement the one method defined by TableCellEditor.
  @Override
  public Component getTableCellEditorComponent(JTable table,
                                               Object value,
                                               boolean isSelected,
                                               int row,
                                               int column)
  {
    currentColor = (Color) value;
    return button;
  }

}
