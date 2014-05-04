/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.controls;

import java.awt.Graphics;
import java.util.Objects;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.border.Border;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public final class MTIComboBox<T> extends JComboBox<T> implements Commitable, ErrorFlagable
{

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
    }

    @Override
    public void intervalRemoved(ListDataEvent e)
    {
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

  public void setCommitedSelection(Object sel)
  {
    ComboBoxModel m = getModel();
    if (m instanceof MTIComboBoxModel) {
      ((MTIComboBoxModel) m).setCommitedSelectedItem(sel);
    } else {
      m.setSelectedItem(sel);
    }
    checkFlags();
  }

  @Override
  public void setModel(ComboBoxModel<T> aModel)
  {
    ComboBoxModel oldModel = getModel();
    if (oldModel != null) {
      oldModel.removeListDataListener(myListener);
    }
    if (aModel instanceof MTIComboBoxModel) {
      super.setModel(aModel);
    } else {
      super.setModel(new MTIComboBoxModel<>(aModel));
    }
    ComboBoxModel newModel = getModel();
    if (newModel != null) {
      newModel.addListDataListener(myListener);
    }
    checkFlags();
  }

  @Override
  public void commit()
  {
    ComboBoxModel<T> m = getModel();
    if (m instanceof MTIComboBoxModel) {
      ((MTIComboBoxModel) m).commit();
    }
    checkFlags();
  }

  @Override
  public void revert()
  {
    ComboBoxModel<T> m = getModel();
    if (m instanceof MTIComboBoxModel) {
      MTIComboBoxModel mm = (MTIComboBoxModel) m;
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
      modified = !Objects.equals(((MTIComboBoxModel) m).commited, m.getSelectedItem());
      dataValid = !dataRequired || m.getSelectedItem() != null;
    } else {
      modified = false;
    }
    if (modified != wasModified) {
      firePropertyChange(PROP_DATACHANGED, wasModified, modified);
    }
    if (dataValid != wasValid) {
      repaint();
      firePropertyChange(PROP_DATAVALID, wasValid, dataValid);
    }
  }

  @Override
  public boolean isDataChanged()
  {
    return modified;
  }

  @Override
  public Border getErrorBorder()
  {
    return errorBorder;
  }

  @Override
  public void setErrorBorder(Border newErrorBorder)
  {
    Border oldErrorBorder = getErrorBorder();
    errorBorder = newErrorBorder;
    if (getErrorBorder() != oldErrorBorder) {
      repaint();
      firePropertyChange(PROP_ERRORBORDER, oldErrorBorder, getErrorBorder());
    }
  }

  @Override
  public boolean isDataValid()
  {
    return dataValid;
  }

  @Override
  protected void paintBorder(Graphics g)
  {
    ErrorFlagable.paintBorder(this, this, g);
  }

  public boolean isDataRequired()
  {
    return dataRequired;
  }

  public void setDataRequired(boolean dataRequired)
  {
    if (dataRequired != dataRequired) {
      this.dataRequired = dataRequired;
      firePropertyChange("dataRequired", !dataRequired, dataRequired);
    }
  }

}
