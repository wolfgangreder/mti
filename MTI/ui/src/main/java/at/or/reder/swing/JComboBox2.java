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

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.plaf.ComboBoxUI;

public class JComboBox2<T> extends JComboBox<T>
{

  private static final long serialVersionUID = 1L;

  public JComboBox2()
  {
  }

  public JComboBox2(ComboBoxModel<T> model)
  {
    super(model);
  }

  @Override
  public void updateUI()
  {
    super.updateUI();
    super.setUI(new JComboBox2UI());
  }

  public int getButtonWidth()
  {
    ComboBoxUI cui = getUI();
    if (cui instanceof JComboBox2UI) {
      return ((JComboBox2UI) cui).getButtonWidth();
    }
    return 0;
  }

  public void setButtonWidth(int buttonWidth)
  {
    ComboBoxUI cui = getUI();
    if (cui instanceof JComboBox2UI) {
      ((JComboBox2UI) cui).setButtonWidth(buttonWidth);
    }
  }

  public int getPopupWidth()
  {
    ComboBoxUI cui = getUI();
    if (cui instanceof JComboBox2UI) {
      return ((JComboBox2UI) cui).getListWidth();
    }
    return 0;
  }

  public void setPopupWidth(int w)
  {
    ComboBoxUI cui = getUI();
    if (cui instanceof JComboBox2UI) {
      ((JComboBox2UI) cui).setListWidth(w);
    }
  }

}
