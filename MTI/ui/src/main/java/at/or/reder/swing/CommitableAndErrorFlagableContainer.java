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

import java.awt.Container;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.RootPaneContainer;
import javax.swing.border.Border;
import org.netbeans.api.annotations.common.NonNull;
import org.openide.util.WeakSet;

public final class CommitableAndErrorFlagableContainer implements ErrorFlagable, Commitable
{

  private final JComponent container;
  private final Set<Commitable> commitables = new HashSet<>();
  private final Set<Validateable> validateables = new HashSet<>();
  private final WeakSet<Container> extraContainer = new WeakSet<>();
  private final ContainerListener cl = new ContainerListener()
  {

    @Override
    public void componentAdded(ContainerEvent e)
    {
      CommitableAndErrorFlagableContainer.this.add(e.getChild());
    }

    @Override
    public void componentRemoved(ContainerEvent e)
    {
      CommitableAndErrorFlagableContainer.this.remove(e.getChild());
    }

  };
  private boolean dataChanged;
  private boolean dataValid;
  private final Supplier<Boolean> testValid;
  private final Supplier<Boolean> testChanged;
  private Border errorBorder = ErrorFlagable.DEFAULT_ERROR_BORDER;
  private final String context;

  public CommitableAndErrorFlagableContainer(@NonNull JComponent c,
                                             Supplier<Boolean> testValid,
                                             Supplier<Boolean> testChanged)
  {
    this(c,
         testValid,
         testChanged,
         null);
  }

  public CommitableAndErrorFlagableContainer(@NonNull JComponent c,
                                             Supplier<Boolean> testValid,
                                             Supplier<Boolean> testChanged,
                                             String context)
  {
    this.container = Objects.requireNonNull(c,
                                            "container is null");
    c.addContainerListener(cl);
    if (c instanceof RootPaneContainer) {
      addExtraContainer(((RootPaneContainer) c).getContentPane());
    } else if (c instanceof GlassPaneProvider) {
      addExtraContainer(((GlassPaneProvider) c).getContentPanel());
    }
    this.testValid = testValid;
    this.testChanged = testChanged;
    if (context != null) {
      this.context = context;
    } else {
      this.context = c.getClass().getName();
    }
  }

  public CommitableAndErrorFlagableContainer(@NonNull JComponent c,
                                             String context)
  {
    this(c,
         null,
         null,
         context);
  }

  public CommitableAndErrorFlagableContainer(@NonNull JComponent c)
  {
    this(c,
         null,
         null,
         null);
  }

  public String getContext()
  {
    return context;
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
    Iterator<Validateable> iter = validateables.iterator();
    if (dataValid && iter.hasNext()) {
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

  public void add(Object c)
  {
    if (c instanceof Commitable) {
      Commitable cc = ((Commitable) c);
      if (!commitables.contains(cc)) {
        cc.addPropertyChangeListener(PROP_DATACHANGED,
                                     (PropertyChangeListener) this::checkDataChanged);
        commitables.add(cc);
      }
    }
    if (c instanceof Validateable) {
      Validateable vf = (Validateable) c;
      if (!validateables.contains(vf)) {
        vf.addPropertyChangeListener(PROP_DATAVALID,
                                     (PropertyChangeListener) this::checkDataValid);
        validateables.add(vf);
      }
    }
    if (c instanceof JScrollPane) {
      addExtraContainer(((JScrollPane) c).getViewport());
    } else if (c instanceof JSplitPane) {
      addExtraContainer((JSplitPane) c);
    } else if (c instanceof JTabbedPane) {
      addExtraContainer((JTabbedPane) c);
    } else if (c instanceof GlassPaneProvider) {
      addExtraContainer(((GlassPaneProvider) c).getContentPanel());
    } else if (c instanceof RootPaneContainer) {
      addExtraContainer(((RootPaneContainer) c).getContentPane());
    }
  }

  public void remove(Object c)
  {
    if (c instanceof Commitable) {
      ((Commitable) c).removePropertyChangeListener(PROP_DATACHANGED,
                                                    (PropertyChangeListener) this::checkDataChanged);
      commitables.remove((Commitable) c);
    }
    if (c instanceof Validateable) {
      ((Validateable) c).removePropertyChangeListener(PROP_DATAVALID,
                                                      (PropertyChangeListener) this::checkDataValid);
      validateables.remove((ErrorFlagable) c);
    }
    if (c instanceof JScrollPane) {
      removeExtraContainer(((JScrollPane) c).getViewport());
    } else if (c instanceof JSplitPane) {
      removeExtraContainer((JSplitPane) c);
    } else if (c instanceof JTabbedPane) {
      removeExtraContainer((JTabbedPane) c);
    } else if (c instanceof GlassPaneProvider) {
      removeExtraContainer(((GlassPaneProvider) c).getContentPanel());
    } else if (c instanceof RootPaneContainer) {
      removeExtraContainer(((RootPaneContainer) c).getContentPane());
    }
  }

  public void addExtraContainer(Container comp)
  {
    if (comp != null && comp != container && !extraContainer.contains(comp)) {
      extraContainer.add(comp);
      comp.addContainerListener(cl);
      if (comp instanceof JViewport) {
        add(((JViewport) comp).getView());
      } else if (comp instanceof JSplitPane) {
        add(((JSplitPane) comp).getLeftComponent());
        add(((JSplitPane) comp).getRightComponent());
      } else if (comp instanceof JTabbedPane) {
        JTabbedPane tab = (JTabbedPane) comp;
        for (int i = 0; i < tab.getTabCount(); ++i) {
          add(tab.getComponentAt(i));
        }
      }
    }
  }

  private void removeExtraContainer(Container comp)
  {
    if (comp != null && comp != container) {
      extraContainer.remove(comp);
      comp.removeContainerListener(cl);
      remove(comp);
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
    return Collections.unmodifiableList(new ArrayList<>(commitables));
  }

  public List<Validateable> getValidateables()
  {
    return Collections.unmodifiableList(new ArrayList<>(validateables));
  }

  @Override
  public String toString()
  {
    return "CommitableAndErrorFlagableContainer{" + "context=" + context + '}';
  }

}
