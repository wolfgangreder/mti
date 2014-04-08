/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.contact.controls;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class EnumCheckbox<E extends Enum> extends JPanel
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

  public EnumCheckbox()
  {
    FlowLayout layout = new FlowLayout();
    layout.setAlignment(FlowLayout.LEADING);
    layout.setHgap(5);
    super.setLayout(layout);
  }

  @Override
  public final void setLayout(LayoutManager mgr)
  {

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

  private void selectionChanged(EnumCheckboxModel<E> model)
  {
    Set<E> selection = model.getSelection();
    buttons.stream().
            forEach((b) -> {
              b.button.setSelected(selection.contains(b.value));
            });
    changeListener.stream().
            forEach((cl) -> {
              cl.stateChanged(changeEvent);
            });
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

}
