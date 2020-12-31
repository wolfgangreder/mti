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

import java.awt.Component;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JViewport;
import javax.swing.border.Border;
import org.netbeans.api.annotations.common.NonNull;
import org.openide.util.WeakSet;

public final class CommitableAndErrorFlagableContainer implements ErrorFlagable, Commitable
{

  private final JComponent container;
  private final List<Commitable> commitables = new ArrayList<>();
  private final List<ErrorFlagable> errorFlagables = new ArrayList<>();
  private final WeakSet<JComponent> extraContainer = new WeakSet<>();
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
  private final Supplier<Boolean> testValid;
  private final Supplier<Boolean> testChanged;
  private Border errorBorder = ErrorFlagable.DEFAULT_ERROR_BORDER;

  public CommitableAndErrorFlagableContainer(@NonNull JComponent c,
                                             Supplier<Boolean> testValid,
                                             Supplier<Boolean> testChanged)
  {
    this.container = Objects.requireNonNull(c,
                                            "container is null");
    c.addContainerListener(cl);
    this.testValid = testValid;
    this.testChanged = testChanged;
  }

  public CommitableAndErrorFlagableContainer(@NonNull JComponent c)
  {
    this(c,
         null,
         null);
  }

  public void addExtraContainer(JComponent comp)
  {
    if (comp != null && comp != container && !extraContainer.contains(comp)) {
      extraContainer.add(comp);
      comp.addContainerListener(cl);
      if (comp instanceof JViewport) {
        componentAdded(((JViewport) comp).getView());
      } else if (comp instanceof JSplitPane) {
        componentAdded(((JSplitPane) comp).getLeftComponent());
        componentAdded(((JSplitPane) comp).getRightComponent());
      }
    }
  }

  public void removeExtraContainer(JComponent comp)
  {
    if (comp != null && comp != container) {
      extraContainer.remove(comp);
      comp.removeContainerListener(cl);
    }
  }

  public void checkFlags()
  {
    checkDataChanged(null);
    checkDataValid(null);
  }

  public void checkDataChanged()
  {
    checkDataChanged(null);
  }

  private void checkDataChanged(PropertyChangeEvent event)
  {
    boolean wasChanged = dataChanged;
    if (event != null && event.getSource() instanceof Commitable) {
      dataChanged = ((Commitable) event.getSource()).isDataChanged();
    } else {
      dataChanged = false;
    }
    Iterator<Commitable> iter = commitables.iterator();
    while (!dataChanged && iter.hasNext()) {
      dataChanged |= iter.next().isDataChanged();
    }
    if (!dataChanged && testChanged != null) {
      dataChanged |= testChanged.get();
    }
    if (wasChanged != dataChanged) {
      container.firePropertyChange(PROP_DATACHANGED,
                                   wasChanged,
                                   dataChanged);
    }
  }

  public void checkDataValid()
  {
    checkDataValid(null);
  }

  private void checkDataValid(PropertyChangeEvent event)
  {
    boolean wasValid = dataValid;
    if (event != null && event.getSource() instanceof ErrorFlagable) {
      dataValid = ((ErrorFlagable) event.getSource()).isDataValid();
    } else {
      dataValid = true;
    }
    Iterator<ErrorFlagable> iter = errorFlagables.iterator();
    while (dataValid && iter.hasNext()) {
      dataValid &= iter.next().isDataValid();
    }
    if (dataValid && testValid != null) {
      dataValid &= testValid.get();
    }
    if (wasValid != dataValid) {
      container.firePropertyChange(PROP_DATAVALID,
                                   wasValid,
                                   dataValid);
      container.repaint();
    }
  }

  public void componentAdded(Component c)
  {
    if (c instanceof Commitable) {
      Commitable cc = ((Commitable) c);
      if (!commitables.contains(cc)) {
        cc.addPropertyChangeListener(PROP_DATACHANGED,
                                     (PropertyChangeListener) this::checkDataChanged);
        commitables.add(cc);
      }
    }
    if (c instanceof ErrorFlagable) {
      ErrorFlagable ef = (ErrorFlagable) c;
      if (!errorFlagables.contains(ef)) {
        ef.addPropertyChangeListener(PROP_DATAVALID,
                                     (PropertyChangeListener) this::checkDataValid);
        errorFlagables.add(ef);
      }
    }
    if (c instanceof JScrollPane) {
      addExtraContainer(((JScrollPane) c).getViewport());
    } else if (c instanceof JSplitPane) {
      addExtraContainer((JSplitPane) c);
    }
  }

  public void componentRemoved(Component c)
  {
    if (c instanceof Commitable) {
      ((Commitable) c).removePropertyChangeListener(PROP_DATACHANGED,
                                                    (PropertyChangeListener) this::checkDataChanged);
      commitables.remove((Commitable) c);
    }
    if (c instanceof ErrorFlagable) {
      ((ErrorFlagable) c).removePropertyChangeListener(PROP_DATAVALID,
                                                       (PropertyChangeListener) this::checkDataValid);
      errorFlagables.remove((ErrorFlagable) c);
    }
  }

  /**
   *
   * @return container.getBorder()
   */
  @Override
  public Border getBorder()
  {
    return container.getBorder();
  }

  @Override
  public Border getErrorBorder()
  {
    return errorBorder;
  }

  @Override
  public void setErrorBorder(Border newErrorBorder)
  {
    if (errorBorder != newErrorBorder) {
      Border oldBorder = errorBorder;
      errorBorder = newErrorBorder;
      PropertyChangeListener[] listener = container.getPropertyChangeListeners();
      PropertyChangeEvent evt = null;
      if (listener != null && listener.length > 0) {
        evt = new PropertyChangeEvent(container,
                                      PROP_ERRORBORDER,
                                      oldBorder,
                                      errorBorder);
        for (PropertyChangeListener l : listener) {
          l.propertyChange(evt);
        }
      }
      listener = container.getPropertyChangeListeners(PROP_ERRORBORDER);
      if (listener != null && listener.length > 0) {
        if (evt == null) {
          evt = new PropertyChangeEvent(container,
                                        PROP_ERRORBORDER,
                                        oldBorder,
                                        errorBorder);
        }
        for (PropertyChangeListener l : listener) {
          l.propertyChange(evt);
        }
      }
      container.validate();
    }
  }

  @Override
  public boolean isDataValid()
  {
    return dataValid;
  }

  @Override
  public void addPropertyChangeListener(String property,
                                        PropertyChangeListener l)
  {
    container.addPropertyChangeListener(property,
                                        l);
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener l)
  {
    container.addPropertyChangeListener(l);
  }

  @Override
  public void removePropertyChangeListener(String property,
                                           PropertyChangeListener l)
  {
    container.removePropertyChangeListener(property,
                                           l);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener l)
  {
    container.removePropertyChangeListener(l);
  }

  @Override
  public void commit()
  {
    for (Commitable c : commitables) {
      c.commit();
    }
  }

  @Override
  public void revert()
  {
    for (Commitable c : commitables) {
      c.revert();
    }
  }

  @Override
  public boolean isDataChanged()
  {
    return dataChanged;
  }

  public JComponent getContainer()
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
