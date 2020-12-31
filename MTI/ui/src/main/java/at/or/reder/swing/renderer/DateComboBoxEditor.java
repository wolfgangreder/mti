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
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.ComboBoxEditor;
import org.jdesktop.swingx.JXDatePicker;

public class DateComboBoxEditor implements ComboBoxEditor
{

  private final JXDatePicker picker = new JXDatePicker();
  private final Set<ActionListener> listeners = new CopyOnWriteArraySet<>();

  @Override
  public Component getEditorComponent()
  {
    return picker;
  }

  @Override
  public void setItem(Object anObject)
  {
    Date old = picker.getDate();
    if (anObject instanceof Date) {
      picker.setDate((Date) anObject);
    } else {
      picker.setDate(null);
    }
    if (!Objects.equals(old,
                        picker.getDate())) {
      fireChanged();
    }
  }

  @Override
  public Object getItem()
  {
    return picker.getDate();
  }

  @Override
  public void selectAll()
  {
  }

  private void fireChanged()
  {
    ActionEvent evt = new ActionEvent(this,
                                      ActionEvent.ACTION_PERFORMED,
                                      "setItem");
    for (ActionListener l : listeners) {
      l.actionPerformed(evt);
    }
  }

  @Override
  public void addActionListener(ActionListener l)
  {
    if (l != null) {
      listeners.add(l);
    }
  }

  @Override
  public void removeActionListener(ActionListener l)
  {
    listeners.remove(l);
  }

}
