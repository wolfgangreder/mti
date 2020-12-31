/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.controls;

import at.reder.mti.api.utils.MTIUtils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import org.openide.util.Exceptions;

/**
 *
 * @author Wolfgang Reder
 */
public class MTIDocument extends PlainDocument
{

  private String original;
  private boolean changed;
  private boolean valid;
  private boolean trimText;
  private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
  private int minLength = 0;
  private int maxLength = MTIUtils.MAX_NAME_LENGTH;
  private boolean nullAllowed = true;
  private final DocumentFilter docFilter = new DocumentFilter()
  {

    @Override
    public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws
            BadLocationException
    {
      String toInsert = string;
      String text = getText(false);
      int textLength = text != null ? text.length() : 0;
      if (string != null && (textLength + string.length() > maxLength)) {
        int delta = maxLength - textLength;
        if (delta > 0) {
          toInsert = string.substring(0, delta);
        } else {
          UIManager.getLookAndFeel().provideErrorFeedback(null);
          return;
        }
      }
      fb.insertString(offset, toInsert, attr);
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attrs) throws
            BadLocationException
    {
      String toInsert = string;
      String text = getText(false);
      int textLength = text != null ? text.length() : 0;
      if (string != null && ((textLength + string.length() - length) > maxLength)) {
        int delta = maxLength - textLength + length;
        if (delta > 0) {
          toInsert = string.substring(0, delta);
        } else {
          UIManager.getLookAndFeel().provideErrorFeedback(null);
          return;
        }
      }
      fb.replace(offset, length, toInsert, attrs);
    }

  };

  public MTIDocument()
  {
    checkFlags();
  }

  public MTIDocument(AbstractDocument.Content c)
  {
    super(c);
    original = getText();
    checkFlags();
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
      replace(0, getLength(), original, null);
    } catch (BadLocationException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  private String getText()
  {
    return getText(trimText);
  }

  private String getText(boolean tt)
  {
    try {
      String tmp = getText(0, getLength());
      if (tmp != null && tt) {
        tmp = tmp.trim();
        if (tmp.length() > maxLength) {
          return tmp.substring(0, maxLength);
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

  private boolean checkFlagsAndFire()
  {
    Collection<PropertyChangeEvent> events;
    if (!(events = checkFlags()).isEmpty()) {
      fireChange(events);
      return true;
    }
    return false;
  }

  protected boolean testValid(String text)
  {
    return true;
  }

  protected boolean testChanged(String text, String original)
  {
    return !Objects.equals(text, original);
  }

  private List<PropertyChangeEvent> checkFlags()
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
    changed = testChanged(text, original);
    List<PropertyChangeEvent> result = new LinkedList<>();
    if (wasChanged != changed) {
      result.add(new PropertyChangeEvent(this, "changed", wasChanged, changed));
    }
    if (wasValid != valid) {
      result.add(new PropertyChangeEvent(this, "valid", wasValid, valid));
    }
    return result;
  }

  public void addPropertyChangeListener(String propName, PropertyChangeListener l)
  {
    changeSupport.addPropertyChangeListener(propName, l);
  }

  public void addPropertyChangeListener(PropertyChangeListener cl)
  {
    changeSupport.addPropertyChangeListener(cl);
  }

  public void removePropertyChangeListener(String propName, PropertyChangeListener cl)
  {
    changeSupport.removePropertyChangeListener(propName, cl);
  }

  public void removePropertyChangeListener(PropertyChangeListener cl)
  {
    changeSupport.removePropertyChangeListener(cl);
  }

  private void fireChange(Collection<PropertyChangeEvent> events)
  {
    events.stream().
            forEach((e) -> {
              changeSupport.firePropertyChange(e);
            });
  }

  @Override
  protected void fireRemoveUpdate(DocumentEvent e)
  {
    Collection<PropertyChangeEvent> events = checkFlags();
    super.fireRemoveUpdate(e);
    if (!events.isEmpty()) {
      fireChange(events);
    }
  }

  @Override
  protected void fireChangedUpdate(DocumentEvent e)
  {
    Collection<PropertyChangeEvent> events = checkFlags();
    super.fireChangedUpdate(e);
    if (!events.isEmpty()) {
      fireChange(events);
    }
  }

  @Override
  protected void fireInsertUpdate(DocumentEvent e)
  {
    Collection<PropertyChangeEvent> events = checkFlags();
    super.fireInsertUpdate(e);
    if (!events.isEmpty()) {
      fireChange(events);
    }
  }

  @Override
  public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
  {
    super.insertString(offs, str, a);
  }

}
