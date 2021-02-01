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

import at.or.reder.swing.model.Commitable;
import at.or.reder.swing.model.ErrorFlagable;
import java.awt.Graphics;
import java.beans.BeanProperty;
import java.util.Objects;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MTISpinner extends JSpinner implements ErrorFlagable, Commitable
{

  private static final long serialVersionUID = 1L;
  private Object commited;
  private Border errorBorder = DEFAULT_ERROR_BORDER;
  private boolean dataValid;
  private boolean dataChanged;
  private boolean valueRequired = true;
  private final ChangeListener changeListener = this::modelChanged;

  public MTISpinner()
  {
    commited = getValue();
    checkDataChanged();
    checkDataValid();
  }

  public MTISpinner(SpinnerModel model)
  {
    super(model);
    commited = getValue();
    checkDataChanged();
    checkDataValid();
  }

  private void modelChanged(ChangeEvent ce)
  {
    checkDataChanged();
    checkDataValid();
  }

  @BeanProperty(bound = true, preferred = true, visualUpdate = true)
  public boolean isValueRequired()
  {
    return valueRequired;
  }

  public void setValueRequired(boolean vr)
  {
    if (vr != valueRequired) {
      this.valueRequired = vr;
      checkDataValid();
    }
  }

  private void checkDataValid()
  {
    boolean wasValid = dataValid;
    dataValid = !valueRequired || getValue() != null;
    if (wasValid != dataValid) {
      repaint();
      firePropertyChange(PROP_DATAVALID,
                         wasValid,
                         dataValid);
    }
  }

  @Override
  public void setModel(SpinnerModel model)
  {
    SpinnerModel m = getModel();
    if (m != null) {
      m.removeChangeListener(changeListener);
    }
    super.setModel(model);
    m = getModel();
    if (m != null) {
      m.addChangeListener(changeListener);
    }
  }

  public void setCommited(Object hcp)
  {
    setValue(hcp);
    commited = hcp;
    checkDataChanged();
    checkDataValid();
  }

  private void checkDataChanged()
  {
    boolean wasChanged = dataChanged;
    dataChanged = commited != getValue();
    if (wasChanged != dataChanged) {
      firePropertyChange(PROP_DATACHANGED,
                         wasChanged,
                         dataChanged);
    }
  }

  public Object getCommited()
  {
    return commited;
  }

  @Override
  @BeanProperty(bound = true, preferred = true, visualUpdate = true)
  public Border getErrorBorder()
  {
    return errorBorder;
  }

  @Override
  public void setErrorBorder(Border eb)
  {
    if (!Objects.equals(errorBorder,
                        eb)) {
      Border oldErrorBorder = errorBorder;
      errorBorder = eb;
      repaint();
      firePropertyChange(PROP_ERRORBORDER,
                         oldErrorBorder,
                         errorBorder);
    }
  }

  @Override
  protected void paintBorder(Graphics g)
  {
    ErrorFlagable.paintBorder(this,
                              this,
                              g);
  }

  @Override
  public boolean isDataValid()
  {
    return dataValid;
  }

  @Override
  public void commit()
  {
    commited = getValue();
    checkDataChanged();
  }

  @Override
  public void revert()
  {
    setValue(commited);
  }

  @Override
  public boolean isDataChanged()
  {
    return dataChanged;
  }

}
