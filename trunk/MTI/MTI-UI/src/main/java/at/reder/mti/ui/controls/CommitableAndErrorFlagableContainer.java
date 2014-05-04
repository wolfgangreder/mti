/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.controls;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.border.Border;

/**
 *
 * @author Wolfgang Reder
 */
public final class CommitableAndErrorFlagableContainer implements ErrorFlagable, Commitable
{

  private final Container container;
  private final List<Commitable> commitables = new ArrayList<>();
  private final List<ErrorFlagable> errorFlagables = new ArrayList<>();
  private final PropertyChangeSupport propSupport = new PropertyChangeSupport(this);
  private final ContainerListener cl = new ContainerListener()
  {

    @Override
    public void componentAdded(ContainerEvent e)
    {
      CommitableAndErrorFlagableContainer.this.componentAdded(e.getChild());
    }

    @Override
    public void componentRemoved(ContainerEvent e)
    {
      CommitableAndErrorFlagableContainer.this.componentRemoved(e.getChild());
    }

  };
  private boolean dataChanged;
  private boolean dataValid;

  public CommitableAndErrorFlagableContainer(Container c)
  {
    this.container = c;
    c.addContainerListener(cl);
  }

  private void checkDataChanged(PropertyChangeEvent event)
  {
    boolean wasChanged = dataChanged;
    if (event.getSource() instanceof Commitable) {
      dataChanged = ((Commitable) event.getSource()).isDataChanged();
    } else {
      dataChanged = false;
    }
    Iterator<Commitable> iter = commitables.iterator();
    while (!dataChanged && iter.hasNext()) {
      dataChanged |= iter.next().isDataChanged();
    }
    if (wasChanged != dataChanged) {
      propSupport.firePropertyChange(PROP_DATACHANGED, wasChanged, dataChanged);
    }
  }

  private void checkDataValid(PropertyChangeEvent event)
  {
    boolean wasValid = dataValid;
    if (event.getSource() instanceof ErrorFlagable) {
      dataValid = ((ErrorFlagable) event.getSource()).isDataValid();
    } else {
      dataValid = true;
    }
    Iterator<ErrorFlagable> iter = errorFlagables.iterator();
    while (dataValid && iter.hasNext()) {
      dataValid &= iter.next().isDataValid();
    }
    if (wasValid != dataValid) {
      propSupport.firePropertyChange(PROP_DATAVALID, wasValid, dataValid);
    }
  }

  private void componentAdded(Component c)
  {
    if (c instanceof Commitable) {
      Commitable cc = ((Commitable) c);
      if (!commitables.contains(cc)) {
        cc.addPropertyChangeListener(PROP_DATACHANGED, (PropertyChangeListener) this::checkDataChanged);
        commitables.add(cc);
      }
    }
    if (c instanceof ErrorFlagable) {
      ErrorFlagable ef = (ErrorFlagable) c;
      if (!errorFlagables.contains(ef)) {
        ef.addPropertyChangeListener(PROP_DATAVALID, (PropertyChangeListener) this::checkDataValid);
        errorFlagables.add(ef);
      }
    }
  }

  private void componentRemoved(Component c)
  {
    if (c instanceof Commitable) {
      ((Commitable) c).removePropertyChangeListener(PROP_DATACHANGED, (PropertyChangeListener) this::checkDataChanged);
      commitables.remove((Commitable) c);
    }
    if (c instanceof ErrorFlagable) {
      ((ErrorFlagable) c).removePropertyChangeListener(PROP_DATAVALID, (PropertyChangeListener) this::checkDataValid);
      errorFlagables.remove((ErrorFlagable) c);
    }
  }

  @Override
  public Border getBorder()
  {
    throw new UnsupportedOperationException("Not supported.");
  }

  @Override
  public Border getErrorBorder()
  {
    throw new UnsupportedOperationException("Not supported.");
  }

  @Override
  public void setErrorBorder(Border newErrorBorder)
  {
    errorFlagables.stream().
            forEach((ef) -> {
              ef.setErrorBorder(newErrorBorder);
            });
  }

  @Override
  public boolean isDataValid()
  {
    return dataValid;
  }

  @Override
  public void addPropertyChangeListener(String property, PropertyChangeListener l)
  {
    propSupport.addPropertyChangeListener(property, l);
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener l)
  {
    propSupport.addPropertyChangeListener(l);
  }

  @Override
  public void removePropertyChangeListener(String property, PropertyChangeListener l)
  {
    propSupport.removePropertyChangeListener(property, l);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener l)
  {
    propSupport.removePropertyChangeListener(l);
  }

  @Override
  public void commit()
  {
    commitables.stream().forEach((c) -> c.commit());
  }

  @Override
  public void revert()
  {
    commitables.stream().forEach((c) -> c.revert());
  }

  @Override
  public boolean isDataChanged()
  {
    return dataChanged;
  }

  public Container getContainer()
  {
    return container;
  }

  public List<Commitable> getCommitables()
  {
    return Collections.unmodifiableList(commitables);
  }

  public List<ErrorFlagable> getErrorFlagables()
  {
    return Collections.unmodifiableList(errorFlagables);
  }

}
