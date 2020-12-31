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

import java.util.EventObject;
import javax.swing.ButtonModel;
import javax.swing.JCheckBox;

public class MTICheckBox extends JCheckBox implements Commitable
{

  private static final long serialVersionUID = 1L;
  private boolean commited;
  private boolean dataChanged;

  @Override
  public void setModel(ButtonModel newModel)
  {
    ButtonModel m = getModel();
    if (m != null) {
      m.removeActionListener(this::checkDataChanged);
    }
    super.setModel(newModel);
    m = getModel();
    if (m != null) {
      m.addActionListener(this::checkDataChanged);
    }
  }

  public void setCommited(boolean sel)
  {
    commited = sel;
    setSelected(sel);
  }

  public boolean getCommited()
  {
    return commited;
  }

  @Override
  public void commit()
  {
    commited = isSelected();
    checkDataChanged(null);
  }

  @Override
  public void revert()
  {
    setSelected(commited);
  }

  @Override
  public boolean isDataChanged()
  {
    return dataChanged;
  }

  private void checkDataChanged(EventObject ev)
  {
    boolean wasChanged = dataChanged;
    dataChanged = commited != isSelected();
    if (wasChanged != dataChanged) {
      firePropertyChange(PROP_DATACHANGED,
                         wasChanged,
                         dataChanged);
    }
  }

}
