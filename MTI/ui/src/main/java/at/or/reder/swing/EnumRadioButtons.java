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
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.AbstractButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EnumRadioButtons<E extends Enum<E>> extends JPanel implements ErrorFlagable, Commitable
{

  private static final long serialVersionUID = 1L;

  private EnumButtonGroup<E> model;
  private Border errorBorder = DEFAULT_ERROR_BORDER;
  private boolean dataValid;
  private boolean dataChanged;
  private final Set<ActionListener> actionListener = new CopyOnWriteArraySet<>();
  private String command;
  private E commited;
  private EnumRadioButtonsAdapter<E> adapter;

  public EnumRadioButtons()
  {
    this(null);
  }

  public EnumRadioButtons(Class<E> clazz)
  {
    FlowLayout layout = new FlowLayout();
    layout.setAlignment(FlowLayout.LEADING);
    layout.setHgap(5);
    super.setLayout(layout);
    if (clazz != null) {
      setModel(new EnumButtonGroup<>(clazz));
    }
    updatePerferredSize();
  }

  public EnumRadioButtonsAdapter<E> getAdpater()
  {
    return adapter;
  }

  public void setAdpater(EnumRadioButtonsAdapter<E> ad)
  {
    if (adapter != ad) {
      adapter = ad;
      updateControls();
    }
  }

  private void updatePerferredSize()
  {
    Dimension dim;
    FlowLayout layout = (FlowLayout) getLayout();
    int vgap = 0;
    if (model == null || model.getButtonCount() == 0) {
      JRadioButton box = new JRadioButton("dummy");
      dim = box.getPreferredSize();
      vgap = 0;
      if (errorBorder != null) {
        Insets i = errorBorder.getBorderInsets(this);
        dim.height += i.bottom + i.top;
        dim.width *= 4;
        dim.width += i.left + i.right;
        vgap = i.top;
      }
    } else {
      dim = layout.preferredLayoutSize(this);
    }
    layout.setVgap(vgap);
    setPreferredSize(dim);
  }

  @Override
  public final void setLayout(LayoutManager mgr)
  {

  }

  public String getActionCommand()
  {
    return command;
  }

  public void setActionCommand(String ac)
  {
    command = ac;
  }

  public EnumButtonGroup<E> getModel()
  {
    return model;
  }

  public void setModel(EnumButtonGroup<E> newModel)
  {
    if (model != newModel) {
      if (model != null) {
        model.removeChangeListener(this::selectionChanged);
      }
      model = newModel;
      if (model != null) {
        model.addChangeListener(this::selectionChanged);
      }
      updateControls();
    }
  }

  public void setCommited(E c)
  {
    if (c != commited) {
      this.commited = c;
      if (model.getSelected() == null) {
        model.setSelected(commited);
      }
      selectionChanged(null);
    }
  }

  public E getCommited()
  {
    return commited;
  }

  public void setSelected(E s)
  {
    model.setSelected(s);
  }

  public E getSelected()
  {
    return model.getSelected();
  }

  @Override
  public void commit()
  {
    commited = model.getSelected();
    selectionChanged(null);
  }

  @Override
  public void revert()
  {
    model.setSelected(commited);
    selectionChanged(null);
  }

  @Override
  public boolean isDataChanged()
  {
    return dataChanged;
  }

  @Override
  public boolean isDataValid()
  {
    return dataValid;
  }

  public void addActionListener(ActionListener l)
  {
    if (l != null) {
      actionListener.add(l);
    }
  }

  public void removeActionListener(ActionListener l)
  {
    actionListener.remove(l);
  }

  private void fireActionEvent()
  {
    int modifiers = 0;
    AWTEvent currentEvent = EventQueue.getCurrentEvent();
    if (currentEvent instanceof InputEvent) {
      modifiers = ((InputEvent) currentEvent).getModifiersEx();
    } else if (currentEvent instanceof ActionEvent) {
      modifiers = ((ActionEvent) currentEvent).getModifiers();
    }
    ActionEvent e = new ActionEvent(this,
                                    ActionEvent.ACTION_PERFORMED,
                                    (command != null) ? command : getClass().getSimpleName(),
                                    EventQueue.getMostRecentEventTime(),
                                    modifiers);
    actionListener.stream().forEach((c) -> {
      c.actionPerformed(e);
    });
  }

  private void selectionChanged(ChangeEvent event)
  {
    boolean wasValid = dataValid;
    boolean wasChanged = dataChanged;
    dataValid = model.getSelected() != null;
    dataChanged = model.getSelected() != commited;
    fireActionEvent();
    if (wasValid != dataValid) {
      repaint();
      firePropertyChange(PROP_DATAVALID,
                         wasValid,
                         dataValid);
    }
    if (wasChanged != dataChanged) {
      repaint();
      firePropertyChange(PROP_DATACHANGED,
                         wasChanged,
                         dataChanged);
    }
  }

  private void updateControls()
  {
    removeAll();
    if (model != null) {
      for (AbstractButton btn : model.createButtons(adapter)) {
        add(btn);
      }
    }
    updatePerferredSize();
    invalidate();
  }

  public void addChangeListener(ChangeListener cl)
  {
    model.addChangeListener(cl);
  }

  public void removeChangeListener(ChangeListener cl)
  {
    model.removeChangeListener(cl);
  }

  @Override
  public Border getErrorBorder()
  {
    return errorBorder;
  }

  @Override
  public void setErrorBorder(Border newErrorBorder)
  {
    if (!Objects.equals(errorBorder,
                        newErrorBorder)) {
      Border oldErrorBorder = errorBorder;
      errorBorder = newErrorBorder;
      updatePerferredSize();
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

}
