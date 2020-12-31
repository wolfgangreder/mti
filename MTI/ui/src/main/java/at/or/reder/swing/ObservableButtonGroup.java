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

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;

public class ObservableButtonGroup extends ButtonGroup
{

  private static final long serialVersionUID = 1L;
  private final ChangeSupport changeSupport = new ChangeSupport(this);

  @Override
  public void clearSelection()
  {
    ButtonModel oldSelection = getSelection();
    super.clearSelection();
    if (oldSelection != getSelection()) {
      fireChanged();
    }
  }

  @Override
  public void setSelected(ButtonModel m,
                          boolean b)
  {
    ButtonModel oldSelection = getSelection();
    super.setSelected(m,
                      b);
    if (oldSelection != getSelection()) {
      fireChanged();
    }
  }

  private void fireChanged()
  {
    changeSupport.fireChange();
  }

  public void addChangeListener(ChangeListener l)
  {
    changeSupport.addChangeListener(l);
  }

  public void removeChangeListener(ChangeListener l)
  {
    changeSupport.removeChangeListener(l);
  }

}
