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

import at.or.reder.swing.model.MTIDocument;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 *
 * @author Wolfgang Reder
 */
public class MTITextField extends JTextField implements ErrorFlagable, Commitable
{

  public static final String PROP_NULL_ALLOWED = "nullAllowed";
  public static final String PROP_TRIM_TEXT = "trimText";
  public static final String PROP_MIN_LENGTH = "minLength";
  public static final String PROP_MAX_LENGTH = "maxLength";
  private static final long serialVersionUID = 1L;
  private boolean constructed = false;
  private Border errorBorder = DEFAULT_ERROR_BORDER;
  private final DocumentListener docListener = new DocumentListener()
  {

    @Override
    public void insertUpdate(DocumentEvent e)
    {
      fireActionPerformed();
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
      fireActionPerformed();
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
      fireActionPerformed();
    }

  };
  private final PropertyChangeListener modelListener = this::modelChanged;

  public MTITextField()
  {
    super(null,
          null,
          20);
    setDocument(new MTIDocument());
//    getMTIDocument().addPropertyChangeListener((PropertyChangeListener) this::modelChanged);
//    getDocument().addDocumentListener(docListener);
    constructed = true;
  }

  public void setCommitedText(String text)
  {
    super.setText(text);
    commit();
  }

  private void modelChanged(PropertyChangeEvent evt)
  {
    if (null != evt.getPropertyName()) {
      switch (evt.getPropertyName()) {
        case "valid":
          firePropertyChange(PROP_DATAVALID,
                             evt.getOldValue(),
                             evt.getNewValue());
          repaint();
          break;
        case "changed":
          firePropertyChange(PROP_DATACHANGED,
                             evt.getOldValue(),
                             evt.getNewValue());
          break;
      }
    }
  }

  @Override
  public final void setDocument(Document doc)
  {
    if (!constructed || doc instanceof MTIDocument) {
      Document tmpDoc = getDocument();
      if (tmpDoc instanceof MTIDocument) {
        ((MTIDocument) tmpDoc).removePropertyChangeListener(modelListener);
      }
      if (tmpDoc != null) {
        tmpDoc.removeDocumentListener(docListener);
      }
      super.setDocument(doc);
      tmpDoc = getDocument();
      if (tmpDoc instanceof MTIDocument) {
        ((MTIDocument) tmpDoc).addPropertyChangeListener(modelListener);
      }
      if (tmpDoc != null) {
        tmpDoc.addDocumentListener(docListener);
      }
    }
  }

  public final MTIDocument getMTIDocument()
  {
    return (MTIDocument) super.getDocument();
  }

  public boolean isNullAllowed()
  {
    return getMTIDocument().isNullAllowed();
  }

  public final void setNullAllowed(boolean nullAllowed)
  {
    boolean wasNullAllowed = isNullAllowed();
    getMTIDocument().setNullAllowed(nullAllowed);
    if (wasNullAllowed != isNullAllowed()) {
      firePropertyChange(PROP_NULL_ALLOWED,
                         wasNullAllowed,
                         !wasNullAllowed);
    }
  }

  public boolean isTrimText()
  {
    return getMTIDocument().isTrimText();
  }

  public void setTrimText(boolean tt)
  {
    boolean wasTT = isTrimText();
    getMTIDocument().setTrimText(tt);
    if (wasTT != isTrimText()) {
      firePropertyChange(PROP_TRIM_TEXT,
                         wasTT,
                         !wasTT);
    }
  }

  public int getMinLength()
  {
    return getMTIDocument().getMinLength();
  }

  public void setMinLength(int ml)
  {
    int oldMinLength = getMinLength();
    getMTIDocument().setMinLength(ml);
    ml = getMinLength();
    if (oldMinLength != ml) {
      firePropertyChange(PROP_MIN_LENGTH,
                         oldMinLength,
                         ml);
    }
  }

  public int getMaxLength()
  {
    return getMTIDocument().getMaxLength();
  }

  public void setMaxLength(int ml)
  {
    int oldMaxLength = getMaxLength();
    getMTIDocument().setMaxLength(ml);
    ml = getMaxLength();
    if (oldMaxLength != ml) {
      firePropertyChange(PROP_MAX_LENGTH,
                         oldMaxLength,
                         ml);
    }
  }

  @Override
  public void commit()
  {
    getMTIDocument().commit();
  }

  @Override
  public void revert()
  {
    getMTIDocument().revert();
  }

  @Override
  public boolean isDataValid()
  {
    return getMTIDocument().isValid();
  }

  @Override
  public boolean isDataChanged()
  {
    return getMTIDocument().isChanged();
  }

  @Override
  public Border getErrorBorder()
  {
    return errorBorder;
  }

  @Override
  public void setErrorBorder(Border eb)
  {
    if (!Objects.equals(errorBorder,
                        eb)) {
      Border oldErrorBorder = errorBorder;
      errorBorder = eb;
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
