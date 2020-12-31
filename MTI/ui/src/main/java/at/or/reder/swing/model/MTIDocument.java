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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;

/**
 *
 * @author Wolfgang Reder
 */
public class MTIDocument extends PlainDocument
{

  private static final long serialVersionUID = 1L;

  private String original;
  private boolean changed;
  private boolean valid;
  private boolean trimText;
  private final PropertyChangeSupport2 propChangeSupport = new PropertyChangeSupport2(this);
  private final ChangeSupport changeSupport = new ChangeSupport(this);
  private int minLength = 0;
  private int maxLength = Integer.MAX_VALUE;
  private boolean nullAllowed = true;
  private final DocumentFilter docFilter = new DocumentFilter()
  {

    @Override
    public void insertString(DocumentFilter.FilterBypass fb,
                             int offset,
                             String string,
                             AttributeSet attr) throws
            BadLocationException
    {
      String toInsert = string;
      String text = getText(false);
      int textLength = text != null ? text.length() : 0;
      if (string != null && (textLength + string.length() > maxLength)) {
        int delta = maxLength - textLength;
        if (delta > 0) {
          toInsert = string.substring(0,
                                      delta);
        } else {
          UIManager.getLookAndFeel().provideErrorFeedback(null);
          return;
        }
      }
      fb.insertString(offset,
                      toInsert,
                      attr);
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb,
                        int offset,
                        int length,
                        String string,
                        AttributeSet attrs) throws
            BadLocationException
    {
      String toInsert = string;
      String text = getText(false);
      int textLength = text != null ? text.length() : 0;
      if (string != null && ((textLength + string.length() - length) > maxLength)) {
        int delta = maxLength - textLength + length;
        if (delta > 0) {
          toInsert = string.substring(0,
                                      delta);
        } else {
          UIManager.getLookAndFeel().provideErrorFeedback(null);
          return;
        }
      }
      fb.replace(offset,
                 length,
                 toInsert,
                 attrs);
    }

  };
  private BiFunction<MTIDocument, String, Boolean> validChecker;
  private BiFunction<MTIDocument, String, Boolean> changedChecker;
  private final ChangeDocumentListener cdl = new ChangeDocumentListener()
  {
    @Override
    protected void changed(DocumentEvent e)
    {
      changeSupport.fireChange();
    }

  };

  @SuppressWarnings("OverridableMethodCallInConstructor")
  public MTIDocument()
  {
    innerCheckFlags();
    addDocumentListener(cdl);
  }

  @SuppressWarnings("OverridableMethodCallInConstructor")
  public MTIDocument(AbstractDocument.Content c)
  {
    super(c);
    addDocumentListener(cdl);
    original = getText();
    innerCheckFlags();
  }

  public String getOriginal()
  {
    return original;
  }

  public boolean isChanged()
  {
    return changed;
  }

  public boolean isValid()
  {
    return valid;
  }

  public int getMinLength()
  {
    return minLength;
  }

  public void setMinLength(int minLength)
  {
    if (this.minLength != minLength && minLength >= 0) {
      this.minLength = minLength;
      checkFlagsAndFire();
    }
  }

  public int getMaxLength()
  {
    return maxLength;
  }

  public void setMaxLength(int maxLength)
  {
    if (this.maxLength != maxLength && maxLength > minLength) {
      this.maxLength = maxLength;
      if (this.maxLength < Integer.MAX_VALUE) {
        setDocumentFilter(docFilter);
      } else {
        setDocumentFilter(null);
      }
      checkFlagsAndFire();
    }
  }

  public boolean isNullAllowed()
  {
    return nullAllowed;
  }

  public void setNullAllowed(boolean nullAllowed)
  {
    if (this.nullAllowed != nullAllowed) {
      this.nullAllowed = nullAllowed;
      checkFlagsAndFire();
    }
  }

  public boolean isTrimText()
  {
    return trimText;
  }

  public void setTrimText(boolean trimText)
  {
    if (this.trimText != trimText) {
      this.trimText = trimText;
      checkFlagsAndFire();
    }
  }

  public void commit()
  {
    original = getText();
    checkFlagsAndFire();
  }

  public final void revert()
  {
    try {
      replace(0,
              getLength(),
              original,
              null);
    } catch (BadLocationException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  protected String getText()
  {
    return getText(trimText);
  }

  private String getText(boolean tt)
  {
    try {
      String tmp = getText(0,
                           getLength());
      if (tmp != null && tt) {
        tmp = tmp.trim();
        if (tmp.length() > maxLength) {
          return tmp.substring(0,
                               maxLength);
        } else {
          return tmp;
        }
      } else {
        return tmp;
      }
    } catch (BadLocationException ex) {
      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  public void checkFlags()
  {
    checkFlagsAndFire();
  }

  private boolean checkFlagsAndFire()
  {
    Collection<PropertyChangeEvent> events;
    if (!(events = innerCheckFlags()).isEmpty()) {
      fireChange(events);
      return true;
    }
    return false;
  }

  public BiFunction<MTIDocument, String, Boolean> getValidChecker()
  {
    return validChecker;
  }

  public void setValidChecker(BiFunction<MTIDocument, String, Boolean> validChecker)
  {
    if (this.validChecker != validChecker) {
      this.validChecker = validChecker;
      checkFlagsAndFire();
    }
  }

  protected boolean testValid(String text)
  {
    return validChecker == null || validChecker.apply(this,
                                                      text);
  }

  public BiFunction<MTIDocument, String, Boolean> getChangedChecker()
  {
    return changedChecker;
  }

  public void setChangedChecker(BiFunction<MTIDocument, String, Boolean> changedChecker)
  {
    if (this.changedChecker != changedChecker) {
      this.changedChecker = changedChecker;
      checkFlagsAndFire();
    }
  }

  protected boolean testChanged(String text,
                                String original)
  {
    if (changedChecker == null) {
      return !Objects.equals(text,
                             original);
    } else {
      return changedChecker.apply(this,
                                  text);
    }
  }

  private List<PropertyChangeEvent> innerCheckFlags()
  {
    boolean wasValid = valid;
    boolean wasChanged = changed;
    String text = getText();
    valid = true;
    if (!nullAllowed) {
      valid &= text != null;
    }
    if (valid && minLength > 0) {
      valid &= text != null && text.length() >= minLength;
    }
    if (valid) {
      valid = testValid(text);
    }
    changed = testChanged(text,
                          original);
    List<PropertyChangeEvent> result = new LinkedList<>();
    if (wasChanged != changed) {
      result.add(new PropertyChangeEvent(this,
                                         "changed",
                                         wasChanged,
                                         changed));
    }
    if (wasValid != valid) {
      result.add(new PropertyChangeEvent(this,
                                         "valid",
                                         wasValid,
                                         valid));
    }
    return result;
  }

  public void addChangeListener(ChangeListener l)
  {
    changeSupport.addChangeListener(l);
  }

  public void removeChangeLister(ChangeListener l)
  {
    changeSupport.removeChangeListener(l);
  }

  public void addPropertyChangeListener(String propName,
                                        PropertyChangeListener l)
  {
    propChangeSupport.addListener(propName,
                                  l);
  }

  public void addPropertyChangeListener(PropertyChangeListener cl)
  {
    propChangeSupport.addListener(cl);
  }

  public void removePropertyChangeListener(String propName,
                                           PropertyChangeListener cl)
  {
    propChangeSupport.removeListener(propName,
                                     cl);
  }

  public void removePropertyChangeListener(PropertyChangeListener cl)
  {
    propChangeSupport.removeListener(cl);
  }

  private void fireChange(Collection<PropertyChangeEvent> events)
  {
    for (PropertyChangeEvent e : events) {
      propChangeSupport.firePropertyChange(e);
    }
  }

  @Override
  protected void fireRemoveUpdate(DocumentEvent e)
  {
    Collection<PropertyChangeEvent> events = innerCheckFlags();
    super.fireRemoveUpdate(e);
    if (!events.isEmpty()) {
      fireChange(events);
    }
  }

  @Override
  protected void fireChangedUpdate(DocumentEvent e)
  {
    Collection<PropertyChangeEvent> events = innerCheckFlags();
    super.fireChangedUpdate(e);
    if (!events.isEmpty()) {
      fireChange(events);
    }
  }

  @Override
  protected void fireInsertUpdate(DocumentEvent e)
  {
    Collection<PropertyChangeEvent> events = innerCheckFlags();
    super.fireInsertUpdate(e);
    if (!events.isEmpty()) {
      fireChange(events);
    }
  }

  @Override
  public void insertString(int offs,
                           String str,
                           AttributeSet a) throws BadLocationException
  {
    super.insertString(offs,
                       str,
                       a);
  }

}
