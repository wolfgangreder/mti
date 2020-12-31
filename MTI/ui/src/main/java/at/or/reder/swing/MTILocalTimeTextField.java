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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Objects;
import org.openide.util.WeakListeners;

public final class MTILocalTimeTextField extends MTITextField
{

  private static final long serialVersionUID = 1L;

  private DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
  private String formatPattern;
  private FormatStyle style = FormatStyle.SHORT;
  private LocalTime _minTime;
  private LocalTime _maxTime;
  private LocalTime commited;
  private MTILocalTimeTextField minField;
  private MTILocalTimeTextField maxField;
  private final ActionListener minMaxListener = this::minMaxListen;
  private ActionListener weakMinListener;
  private ActionListener weakMaxListener;

  public MTILocalTimeTextField()
  {
    getMTIDocument().setValidChecker(this::checkDataValid);
    getMTIDocument().setChangedChecker(this::checkDataChanged);
  }

  private void minMaxListen(ActionEvent evt)
  {
    getMTIDocument().checkFlags();
  }

  public MTILocalTimeTextField getMinField()
  {
    return minField;
  }

  public void setMinField(MTILocalTimeTextField minField)
  {
    if (this.minField != minField && minField != this) {
      if (this.minField != null && weakMinListener != null) {
        this.minField.removeActionListener(weakMinListener);
      }
      this.minField = minField;
      if (this.minField != null) {
        weakMinListener = WeakListeners.create(ActionListener.class,
                                               minMaxListener,
                                               this.minField);
        this.minField.addActionListener(weakMinListener);
      } else {
        weakMinListener = null;
      }
      getMTIDocument().checkFlags();
    }
  }

  public MTILocalTimeTextField getMaxField()
  {
    return maxField;
  }

  public void setMaxField(MTILocalTimeTextField maxField)
  {
    if (this.maxField != maxField && maxField != this) {
      if (this.maxField != null && weakMaxListener != null) {
        this.maxField.removeActionListener(weakMaxListener);
      }
      this.maxField = maxField;
      if (this.maxField != null) {
        weakMaxListener = WeakListeners.create(ActionListener.class,
                                               minMaxListener,
                                               this.maxField);
        this.maxField.addActionListener(weakMaxListener);
      } else {
        weakMaxListener = null;
      }
      getMTIDocument().checkFlags();
    }
  }

  private LocalTime parse(String text) throws DateTimeParseException
  {
    if (text == null || text.trim().isEmpty()) {
      return null;
    }
    if (dtf == null) {
      return LocalTime.parse(text);
    } else {
      return LocalTime.parse(text,
                             dtf);
    }
  }

  public LocalTime getValue()
  {
    try {
      return parse(getText());
    } catch (DateTimeParseException ex) {
//      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  public void setValue(LocalTime lt)
  {
    if (lt == null) {
      setText(null);
    } else if (dtf != null) {
      setText(lt.format(dtf));
    } else {
      setText(lt.toString());
    }
  }

  public void setCommitedValue(LocalTime lt)
  {
    setValue(lt);
    commit();
  }

  @Override
  public void revert()
  {
    setValue(commited);
    getMTIDocument().checkFlags();
  }

  @Override
  public void commit()
  {
    commited = getValue();
    getMTIDocument().checkFlags();
  }

  private boolean checkDataValid(MTIDocument doc,
                                 String text)
  {
    try {
      LocalTime current = parse(text);
      if (current == null && !isNullAllowed()) {
        return false;
      }
      LocalTime mt = getMinTime();
      if (mt != null && (current != null && current.isBefore(mt))) {
        return false;
      }
      LocalTime at = getMaxTime();
      return !(at != null && (current != null && current.isAfter(at)));
    } catch (DateTimeParseException ex) {

    }
    return false;
  }

  public DateTimeFormatter getDateTimeFormatter()
  {
    return dtf;
  }

  public void setDateTimeFormatter(DateTimeFormatter dtf)
  {
    if (this.dtf != dtf) {
      this.dtf = dtf;
      formatPattern = null;
      style = null;
      getMTIDocument().checkFlags();
    }
  }

  public String getFormatString()
  {
    return formatPattern;
  }

  public void setFormatString(String fmt)
  {
    if (!Objects.equals(fmt,
                        formatPattern)) {
      formatPattern = fmt;
      style = null;
      if (fmt != null) {
        style = null;
        dtf = DateTimeFormatter.ofPattern(fmt);
      } else if (style != null) {
        dtf = DateTimeFormatter.ofLocalizedTime(style);
      } else {
        style = FormatStyle.SHORT;
        dtf = DateTimeFormatter.ofLocalizedTime(style);
      }
      getMTIDocument().checkFlags();
    }
  }

  public FormatStyle getFormatStyle()
  {
    return style;
  }

  public void setFormatStyle(FormatStyle style)
  {
    if (this.style != style) {
      this.style = style;
      if (style != null) {
        dtf = DateTimeFormatter.ofLocalizedTime(style);
      } else if (formatPattern != null) {
        dtf = DateTimeFormatter.ofPattern(formatPattern);
      } else {
        style = FormatStyle.SHORT;
        dtf = DateTimeFormatter.ofLocalizedTime(style);
      }
      getMTIDocument().checkFlags();
    }
  }

  public LocalTime getMinTime()
  {
    return minField != null ? minField.getValue() : _minTime;
  }

  public void setMinTime(LocalTime minTime)
  {
    if (!Objects.equals(minTime,
                        this._minTime)) {
      this._minTime = minTime;
      getMTIDocument().checkFlags();
    }
  }

  public LocalTime getMaxTime()
  {
    return maxField != null ? maxField.getValue() : _maxTime;
  }

  public void setMaxTime(LocalTime maxTime)
  {
    if (!Objects.equals(this._maxTime,
                        maxTime)) {
      this._maxTime = maxTime;
      getMTIDocument().checkFlags();
    }
  }

  private boolean checkDataChanged(MTIDocument doc,
                                   String text)
  {
    return !Objects.equals(commited,
                           getValue());
  }

}
