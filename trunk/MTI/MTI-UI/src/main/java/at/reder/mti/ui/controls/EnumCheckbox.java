/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.controls;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EnumCheckbox<E extends Enum> extends JPanel implements ErrorFlagable, Commitable
{

  private final class BoxActionListener implements ActionListener
  {

    private final E value;
    private final AbstractButton button;

    private BoxActionListener(E value, AbstractButton button)
    {
      this.value = value;
      this.button = button;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
      model.select(value, button.isSelected());
    }

  }
  private EnumCheckboxModel<E> model;
  private final Set<ChangeListener> changeListener = new CopyOnWriteArraySet<>();
  private transient final ChangeEvent changeEvent = new ChangeEvent(this);
  private final List<BoxActionListener> buttons = new LinkedList<>();
  private Border errorBorder = DEFAULT_ERROR_BORDER;
  private boolean dataValid;
  private boolean dataChanged;
  private final Set<ActionListener> actionListener = new CopyOnWriteArraySet<>();
  private String command;

  public EnumCheckbox()
  {
    FlowLayout layout = new FlowLayout();
    layout.setAlignment(FlowLayout.LEADING);
    layout.setHgap(5);
    super.setLayout(layout);
    updatePerferredSize();
  }

  private void updatePerferredSize()
  {
    JCheckBox box = new JCheckBox("dummy");
    Dimension dim = box.getPreferredSize();
    int vgap = 0;
    if (errorBorder != null) {
      Insets i = errorBorder.getBorderInsets(this);
      dim.height += i.bottom + i.top;
      dim.width *= 4;
      dim.width += i.left + i.right;
      vgap = i.top;
    }
    FlowLayout layout = (FlowLayout) getLayout();
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

  public EnumCheckboxModel<E> getModel()
  {
    return model;
  }

  public void setModel(EnumCheckboxModel<E> newModel)
  {
    if (model != newModel) {
      if (model != null) {
        model.removeEnumCheckboxModelListener(this::selectionChanged);
      }
      model = newModel;
      if (model != null) {
        model.addEnumCheckboxModelListener(this::selectionChanged);
      }
      updateControls();
    }
  }

  @Override
  public void commit()
  {
    model.commit();
  }

  @Override
  public void revert()
  {
    model.revert();
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
      modifiers = ((InputEvent) currentEvent).getModifiers();
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

  private void selectionChanged(EnumCheckboxModel<E> model)
  {
    boolean wasValid = dataValid;
    boolean wasChanged = dataChanged;
    dataValid = model.isDataValid();
    dataChanged = model.isDataChanged();
    Set<E> selection = model.getSelection();
    buttons.stream().
            forEach((b) -> {
              b.button.setSelected(selection.contains(b.value));
            });
    changeListener.stream().
            forEach((cl) -> {
              cl.stateChanged(changeEvent);
            });
    fireActionEvent();
    if (wasValid != dataValid) {
      repaint();
      firePropertyChange(PROP_DATAVALID, wasValid, dataValid);
    }
    if (wasChanged != dataChanged) {
      repaint();
      firePropertyChange(PROP_DATACHANGED, wasChanged, dataChanged);
    }
  }

  private void updateControls()
  {
    removeAll();
    if (model != null) {
      List<E> values = model.getSelectionValues();
      if (values != null && !values.isEmpty()) {
        values.stream().
                filter((e) -> (e != null)).
                forEach((e) -> {
                  JCheckBox box = new JCheckBox(model.getLabel(e));
                  BoxActionListener boxListener = new BoxActionListener(e, box);
                  box.addActionListener(boxListener);
                  add(box);
                  buttons.add(boxListener);
                });
      }
    }
    validate();
  }

  public void addChangeListener(ChangeListener cl)
  {
    if (cl != null) {
      changeListener.add(cl);
    }
  }

  public void removeChangeListener(ChangeListener cl)
  {
    changeListener.remove(cl);
  }

  @Override
  public Border getErrorBorder()
  {
    return errorBorder;
  }

  @Override
  public void setErrorBorder(Border newErrorBorder)
  {
    if (!Objects.equals(errorBorder, newErrorBorder)) {
      Border oldErrorBorder = errorBorder;
      errorBorder = newErrorBorder;
      updatePerferredSize();
      repaint();
      firePropertyChange(PROP_ERRORBORDER, oldErrorBorder, errorBorder);
    }
  }

  @Override
  protected void paintBorder(Graphics g)
  {
    ErrorFlagable.paintBorder(this, this, g);
  }

}
