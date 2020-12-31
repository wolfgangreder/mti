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

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JComboBox;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboPopup;

public class JComboBox2ComboPopup extends BasicComboPopup
{

  private static final long serialVersionUID = 1L;
  private static final Border LIST_BORDER = new LineBorder(Color.BLACK,
                                                           1);

  public JComboBox2ComboPopup(JComboBox<Object> combo)
  {
    super(combo);
  }

  @Override
  protected void configurePopup()
  {
    setLayout(new BorderLayout());
    setBorderPainted(true);
    setBorder(LIST_BORDER);
    setOpaque(false);
    add(scroller,
        BorderLayout.CENTER);
    setDoubleBuffered(true);
    setFocusable(false);
  }

}
