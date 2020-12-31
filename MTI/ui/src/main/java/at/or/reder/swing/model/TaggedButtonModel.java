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
package at.or.reder.swing.model;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.event.ChangeListener;

/**
 * Ein Buttonmodel das auch einen zusätzliches Attribute tragen kann
 *
 * @author reder
 * @param <T> t
 */
public final class TaggedButtonModel<T> implements ButtonModel
{

  private final ButtonModel wrapped;
  private final T tag;

  public TaggedButtonModel(T tag)
  {
    this(new DefaultButtonModel(),
         tag);
  }

  public TaggedButtonModel(ButtonModel bm,
                           T tag)
  {
    wrapped = bm;
    this.tag = tag;
  }

  public ButtonModel getWrapped()
  {
    return wrapped;
  }

  public T getTag()
  {
    return tag;
  }

  @Override
  public boolean isArmed()
  {
    return wrapped.isArmed();
  }

  @Override
  public boolean isSelected()
  {
    return wrapped.isSelected();
  }

  @Override
  public boolean isEnabled()
  {
    return wrapped.isEnabled();
  }

  @Override
  public boolean isPressed()
  {
    return wrapped.isPressed();
  }

  @Override
  public boolean isRollover()
  {
    return wrapped.isRollover();
  }

  @Override
  public void setArmed(boolean b)
  {
    wrapped.setArmed(b);
  }

  @Override
  public void setSelected(boolean b)
  {
    wrapped.setSelected(b);
  }

  @Override
  public void setEnabled(boolean b)
  {
    wrapped.setEnabled(b);
  }

  @Override
  public void setPressed(boolean b)
  {
    wrapped.setPressed(b);
  }

  @Override
  public void setRollover(boolean b)
  {
    wrapped.setRollover(b);
  }

  @Override
  public void setMnemonic(int key)
  {
    wrapped.setMnemonic(key);
  }

  @Override
  public int getMnemonic()
  {
    return wrapped.getMnemonic();
  }

  @Override
  public void setActionCommand(String s)
  {
    wrapped.setActionCommand(s);
  }

  @Override
  public String getActionCommand()
  {
    return wrapped.getActionCommand();
  }

  @Override
  public void setGroup(ButtonGroup group)
  {
    wrapped.setGroup(group);
  }

  @Override
  public void addActionListener(ActionListener l)
  {
    wrapped.addActionListener(l);
  }

  @Override
  public void removeActionListener(ActionListener l)
  {
    wrapped.removeActionListener(l);
  }

  @Override
  public void addItemListener(ItemListener l)
  {
    wrapped.addItemListener(l);
  }

  @Override
  public void removeItemListener(ItemListener l)
  {
    wrapped.removeItemListener(l);
  }

  @Override
  public void addChangeListener(ChangeListener l)
  {
    wrapped.addChangeListener(l);
  }

  @Override
  public void removeChangeListener(ChangeListener l)
  {
    wrapped.removeChangeListener(l);
  }

  @Override
  public Object[] getSelectedObjects()
  {
    return wrapped.getSelectedObjects();
  }

}
