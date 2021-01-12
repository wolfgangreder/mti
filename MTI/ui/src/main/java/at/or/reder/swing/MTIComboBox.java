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

import java.awt.Graphics;
import java.beans.BeanProperty;
import java.util.Objects;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class MTIComboBox<T> extends JComboBox<T> implements Commitable, ErrorFlagable
{

  private static final long serialVersionUID = 1L;

  private static final class MTIComboBoxModel<C> implements ComboBoxModel<C>
  {

    private final ComboBoxModel<C> wrapped;
    private Object commited;

    private MTIComboBoxModel(ComboBoxModel<C> w)
    {
      wrapped = w;
    }

    public ComboBoxModel<C> getWrapped()
    {
      return wrapped;
    }

    public void setCommitedSelectedItem(Object anItem)
    {
      setSelectedItem(anItem);
      commit();
    }

    public void commit()
    {
      commited = getSelectedItem();
    }

    @Override
    public void setSelectedItem(Object anItem)
    {
      wrapped.setSelectedItem(anItem);
    }

    @Override
    public Object getSelectedItem()
    {
      return wrapped.getSelectedItem();
    }

    @Override
    public int getSize()
    {
      return wrapped.getSize();
    }

    @Override
    public C getElementAt(int index)
    {
      return wrapped.getElementAt(index);
    }

    @Override
    public void addListDataListener(ListDataListener l)
    {
      wrapped.addListDataListener(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l)
    {
      wrapped.removeListDataListener(l);
    }

  }
  private final ListDataListener myListener = new ListDataListener()
  {

    @Override
    public void intervalAdded(ListDataEvent e)
    {
      checkFlags();
    }

    @Override
    public void intervalRemoved(ListDataEvent e)
    {
      checkFlags();
    }

    @Override
    public void contentsChanged(ListDataEvent e)
    {
      if (e.getIndex0() == -1 && e.getIndex1() == -1) {
        checkFlags();
      }
    }

  };
  private boolean modified = false;
  private boolean dataValid = false;
  private boolean dataRequired = false;
  private Border errorBorder = DEFAULT_ERROR_BORDER;

  public MTIComboBox()
  {
    checkFlags();
  }

  public MTIComboBox(ComboBoxModel<T> aModel)
  {
    checkFlags();
    setModel(aModel);
  }

  public final void setCommitedSelection(Object sel)
  {
    ComboBoxModel<?> m = getModel();
    if (m instanceof MTIComboBoxModel) {
      ((MTIComboBoxModel) m).setCommitedSelectedItem(sel);
    } else {
      m.setSelectedItem(sel);
    }
    checkFlags();
  }

  @Override
  public final void setModel(ComboBoxModel<T> aModel)
  {
    ComboBoxModel<?> oldModel = getModel();
    if (oldModel != null) {
      oldModel.removeListDataListener(myListener);
    }
    if (aModel instanceof MTIComboBoxModel) {
      super.setModel(aModel);
    } else {
      super.setModel(new MTIComboBoxModel<>(aModel));
    }
    ComboBoxModel<?> newModel = getModel();
    if (newModel != null) {
      newModel.addListDataListener(myListener);
    }
    checkFlags();
  }

  @Override
  public final void commit()
  {
    ComboBoxModel<T> m = getModel();
    if (m instanceof MTIComboBoxModel) {
      ((MTIComboBoxModel) m).commit();
    }
    checkFlags();
  }

  @Override
  public final void revert()
  {
    ComboBoxModel<T> m = getModel();
    if (m instanceof MTIComboBoxModel) {
      MTIComboBoxModel<?> mm = (MTIComboBoxModel) m;
      mm.setSelectedItem(mm.commited);
    }
    checkFlags();
  }

  private void checkFlags()
  {
    boolean wasModified = modified;
    boolean wasValid = dataValid;
    ComboBoxModel<T> m = getModel();
    if (m instanceof MTIComboBoxModel) {
      modified = !Objects.equals(((MTIComboBoxModel) m).commited,
                                 m.getSelectedItem());
      dataValid = !dataRequired || m.getSelectedItem() != null;
    } else {
      modified = false;
    }
    if (modified != wasModified) {
      firePropertyChange(PROP_DATACHANGED,
                         wasModified,
                         modified);
    }
    if (dataValid != wasValid) {
      repaint();
      firePropertyChange(PROP_DATAVALID,
                         wasValid,
                         dataValid);
    }
  }

  @Override
  public final boolean isDataChanged()
  {
    return modified;
  }

  @Override
  @BeanProperty(bound = true, preferred = true, visualUpdate = true)
  public final Border getErrorBorder()
  {
    return errorBorder;
  }

  @Override
  public final void setErrorBorder(Border newErrorBorder)
  {
    Border oldErrorBorder = getErrorBorder();
    errorBorder = newErrorBorder;
    if (getErrorBorder() != oldErrorBorder) {
      repaint();
      firePropertyChange(PROP_ERRORBORDER,
                         oldErrorBorder,
                         getErrorBorder());
    }
  }

  @Override
  public final boolean isDataValid()
  {
    return dataValid;
  }

  @Override
  protected final void paintBorder(Graphics g)
  {
    ErrorFlagable.paintBorder(this,
                              this,
                              g);
  }

  public final boolean isDataRequired()
  {
    return dataRequired;
  }

  public final void setDataRequired(boolean dataRequired)
  {
    if (this.dataRequired != dataRequired) {
      this.dataRequired = dataRequired;
      checkFlags();
      firePropertyChange("dataRequired",
                         !dataRequired,
                         dataRequired);
    }
  }

}
