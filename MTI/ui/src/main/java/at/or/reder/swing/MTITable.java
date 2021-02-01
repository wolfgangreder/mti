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
import at.or.reder.swing.model.ErrorAndCommitableTableModel;
import at.or.reder.swing.model.ErrorFlagable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.border.Border;
import javax.swing.table.TableModel;

public class MTITable extends JTable8 implements ErrorFlagable, Commitable
{

  private static final long serialVersionUID = 1L;
  private Border errorBorder = DEFAULT_ERROR_BORDER;
  private transient boolean cefModel = false;
  private final PropertyChangeListener propListener = this::cefChanged;

  private void cefChanged(PropertyChangeEvent e)
  {
    if (PROP_DATAVALID.equals(e.getPropertyName()) || PROP_DATACHANGED.equals(e.getPropertyName())) {
      firePropertyChange(e.getPropertyName(),
                         e.getOldValue(),
                         e.getNewValue());
    }
  }

  @Override
  public Border getErrorBorder()
  {
    return errorBorder;
  }

  @Override
  public void setErrorBorder(Border newErrorBorder)
  {
    if (newErrorBorder != errorBorder) {
      this.errorBorder = newErrorBorder;
      repaint();
    }
  }

  @Override
  public void setModel(TableModel dataModel)
  {
    TableModel model = getModel();
    if (model instanceof ErrorAndCommitableTableModel) {
      ((ErrorAndCommitableTableModel) model).removePropertyChangeListener(PROP_DATAVALID,
                                                                          propListener);
      ((ErrorAndCommitableTableModel) model).removePropertyChangeListener(PROP_DATACHANGED,
                                                                          propListener);
    }
    super.setModel(dataModel);
    model = getModel();
    cefModel = model instanceof ErrorAndCommitableTableModel;
    if (model != null && cefModel) {
      ((ErrorAndCommitableTableModel) model).addPropertyChangeListener(PROP_DATAVALID,
                                                                       propListener);
      ((ErrorAndCommitableTableModel) model).addPropertyChangeListener(PROP_DATACHANGED,
                                                                       propListener);
    }
  }

  @Override
  public boolean isDataValid()
  {
    if (cefModel) {
      return ((ErrorAndCommitableTableModel) getModel()).isDataValid();
    }
    return true;
  }

  @Override
  public void commit()
  {
    if (cefModel) {
      ((ErrorAndCommitableTableModel) getModel()).commit();
    }
  }

  @Override
  public void revert()
  {
    if (cefModel) {
      ((ErrorAndCommitableTableModel) getModel()).revert();
    }
  }

  @Override
  public boolean isDataChanged()
  {
    if (cefModel) {
      return ((ErrorAndCommitableTableModel) getModel()).isDataChanged();
    }
    return false;
  }

}
