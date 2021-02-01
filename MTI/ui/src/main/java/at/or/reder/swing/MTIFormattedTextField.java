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
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.beans.BeanProperty;
import java.util.Objects;
import javax.swing.JFormattedTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 *
 * @author Wolfgang Reder
 * @param <V> v
 */
public class MTIFormattedTextField<V> extends JFormattedTextField implements Commitable, ErrorFlagable
{

  private static final long serialVersionUID = 1L;

  private boolean isNullAllowed;
  private boolean dataValid;
  private boolean dataChanged;
  private Border errorBorder = DEFAULT_ERROR_BORDER;
  private V commitedValue;
  private final Class<? extends V> valueClass;
  private final DocumentListener docListener = new DocumentListener()
  {

    @Override
    public void insertUpdate(DocumentEvent e)
    {
      checkFlags();
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
      checkFlags();
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
      checkFlags();
    }

  };

  public MTIFormattedTextField(Class<? extends V> valueClass)
  {
    this.valueClass = valueClass;
    assert valueClass != null;
    addActionListener(this::actionListener);
  }

  private void actionListener(ActionEvent evt)
  {
    checkFlags();
  }

  @Override
  public void setDocument(Document doc)
  {
    Document oldDoc = getDocument();
    if (oldDoc != null) {
      oldDoc.removeDocumentListener(docListener);
    }
    super.setDocument(doc);
    Document newDoc = getDocument();
    if (newDoc != null) {
      newDoc.addDocumentListener(docListener);
    }
  }

  private void checkFlags()
  {
    checkModified();
    checkDataValid();
  }

  private void checkModified()
  {
    boolean wasChanged = dataChanged;
    dataChanged = !Objects.equals(commitedValue,
                                  getValue());
    if (wasChanged != dataChanged) {
      firePropertyChange(PROP_DATACHANGED,
                         wasChanged,
                         dataChanged);
    }
  }

  @BeanProperty(bound = true, preferred = true, visualUpdate = true)
  public boolean isNullAllowed()
  {
    return isNullAllowed;
  }

  public void setNullAllowed(boolean isNullAllowed)
  {
    if (this.isNullAllowed != isNullAllowed) {
      this.isNullAllowed = isNullAllowed;
      checkDataValid();
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public V getValue()
  {
    return (V) super.getValue();
  }

  @Override
  public void setValue(Object value)
  {
    if (value == null || valueClass.isInstance(value)) {
      super.setValue(value);
      commitedValue = getValue();
      checkFlags();
    }
  }

  @Override
  public void commit()
  {
    commitedValue = getValue();
    checkFlags();
  }

  @Override
  public void revert()
  {
    setValue(commitedValue);
  }

  @Override
  public boolean isDataChanged()
  {
    return dataChanged;
  }

  @Override
  @BeanProperty(bound = true, preferred = true, visualUpdate = true)
  public Border getErrorBorder()
  {
    return errorBorder;
  }

  @Override
  public void setErrorBorder(Border newErrorBorder)
  {
    if (newErrorBorder != errorBorder) {
      Border oldBorder = errorBorder;
      errorBorder = newErrorBorder;
      firePropertyChange(PROP_ERRORBORDER,
                         oldBorder,
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

  @Override
  public boolean isDataValid()
  {
    return dataValid;
  }

  private void checkDataValid()
  {
    boolean wasValid = dataValid;
    dataValid = isNullAllowed || isEditValid();
    if (wasValid != dataValid) {
      repaint();
      firePropertyChange(PROP_DATAVALID,
                         wasValid,
                         dataValid);
    }
  }

}
