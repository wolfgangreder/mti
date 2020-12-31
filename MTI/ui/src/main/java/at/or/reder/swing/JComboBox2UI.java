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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxButton;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import javax.swing.plaf.metal.MetalComboBoxUI;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class JComboBox2UI extends MetalComboBoxUI
{

  private BasicComboPopup lastCreated;
  private int listWidth;
  private int buttonWidth = 10;

  @Override
  protected ComboPopup createPopup()
  {
    lastCreated = new JComboBox2ComboPopup(comboBox);
//    Dimension dim = lastCreated.getPreferredSize();
//    if (listWidth > 0) {
//      dim.width = listWidth;
//    }
//    lastCreated.setPreferredSize(dim);
//    lastCreated.revalidate();
//    lastCreated.validate();
    return lastCreated;
  }

  public int getButtonWidth()
  {
    return buttonWidth;
  }

  public void setButtonWidth(int buttonWidth)
  {
    if (buttonWidth >= 10) {
      this.buttonWidth = buttonWidth;
      if (comboBox != null) {
        comboBox.revalidate();
        comboBox.validate();
      }
    }
  }

  public int getListWidth()
  {
    return listWidth;
  }

  public void setListWidth(int listWidth)
  {
    if (comboBox != null && listWidth < comboBox.getMinimumSize().width) {
      this.listWidth = comboBox.getMinimumSize().width;
    } else {
      this.listWidth = listWidth;
    }
    if (lastCreated != null) {
      Dimension dim = lastCreated.getPreferredSize();
      dim.width = listWidth;
      lastCreated.setPreferredSize(dim);
      lastCreated.revalidate();
    }
  }

  @Override
  protected JButton createArrowButton()
  {
    boolean iconOnly = true;
    JButton button = new MetalComboBoxButton(comboBox,
                                             new MetalComboBoxIcon()
                                     {
                                       @Override
                                       public void paintIcon(Component c,
                                                             Graphics g,
                                                             int x,
                                                             int y)
                                       {
                                         JComponent component = (JComponent) c;
                                         int iconWidth = getIconWidth();
                                         int hiw = iconWidth / 2;
                                         g.translate(x,
                                                     y);

                                         g.setColor(component.isEnabled()
                                                            ? MetalLookAndFeel.getControlInfo()
                                                            : MetalLookAndFeel.getControlShadow());
                                         g.fillPolygon(new int[]{0, hiw, iconWidth - hiw, iconWidth},
                                                       new int[]{0, hiw, hiw, 0},
                                                       4);
                                         g.translate(-x,
                                                     -y);
                                       }

                                       /**
                                        * Created a stub to satisfy the interface.
                                        */
                                       @Override
                                       public int getIconWidth()
                                       {
                                         return JComboBox2UI.this.getButtonWidth();
                                       }

                                       /**
                                        * Created a stub to satisfy the interface.
                                        */
                                       @Override
                                       public int getIconHeight()
                                       {
                                         return 10;
                                       }

                                     },
                                             iconOnly,
                                             currentValuePane,
                                             listBox);
    button.setMargin(new Insets(0,
                                1,
                                1,
                                3));
    return button;
  }

}
