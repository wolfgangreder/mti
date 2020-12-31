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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Objects;
import org.openide.util.WeakListeners;

public final class MTILocalDateTextField extends MTITextField
{

  private static final long serialVersionUID = 1L;

  private DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
  private String formatPattern;
  private FormatStyle style = FormatStyle.SHORT;
  private LocalDate minDate;
  private LocalDate maxDate;
  private LocalDate commited;
  private MTILocalDateTextField minField;
  private MTILocalDateTextField maxField;
  private final ActionListener minMaxListener = this::minMaxListen;
  private ActionListener weakMinListener;
  private ActionListener weakMaxListener;

  public MTILocalDateTextField()
  {
    getMTIDocument().setValidChecker(this::checkDataValid);
    getMTIDocument().setChangedChecker(this::checkDataChanged);
  }

  private void minMaxListen(ActionEvent evt)
  {
    getMTIDocument().checkFlags();
  }

  public MTILocalDateTextField getMinField()
  {
    return minField;
  }

  public void setMinField(MTILocalDateTextField minField)
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

  public MTILocalDateTextField getMaxField()
  {
    return maxField;
  }

  public void setMaxField(MTILocalDateTextField maxField)
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

  private LocalDate parse(String text) throws DateTimeParseException
  {
    if (text == null || text.trim().isEmpty()) {
      return null;
    }
    if (dtf == null) {
      return LocalDate.parse(text);
    } else {
      return LocalDate.parse(text,
                             dtf);
    }
  }

  public LocalDate getValue()
  {
    try {
      return parse(getText());
    } catch (DateTimeParseException ex) {
//      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  public void setValue(LocalDate lt)
  {
    if (lt == null) {
      setText(null);
    } else if (dtf != null) {
      setText(lt.format(dtf));
    } else {
      setText(lt.toString());
    }
  }

  public void setCommitedValue(LocalDate lt)
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

  @Override
  public boolean isNullAllowed()
  {
    return super.isNullAllowed() && minDate == null && maxDate == null;
  }

  private boolean checkDataValid(MTIDocument doc,
                                 String text)
  {
    try {
      LocalDate current = parse(text);
      if (current == null && !isNullAllowed()) {
        return false;
      }
      if (minDate != null && (current == null || current.isBefore(minDate))) {
        return false;
      }
      return !(maxDate != null && (current == null || current.isAfter(maxDate)));
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
        dtf = DateTimeFormatter.ofLocalizedDate(style);
      } else {
        style = FormatStyle.SHORT;
        dtf = DateTimeFormatter.ofLocalizedDate(style);
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
        dtf = DateTimeFormatter.ofLocalizedDate(style);
      } else if (formatPattern != null) {
        dtf = DateTimeFormatter.ofPattern(formatPattern);
      } else {
        style = FormatStyle.SHORT;
        dtf = DateTimeFormatter.ofLocalizedDate(style);
      }
      getMTIDocument().checkFlags();
    }
  }

  public LocalDate getMinTime()
  {
    return minDate;
  }

  public void setMinTime(LocalDate minTime)
  {
    if (!Objects.equals(minTime,
                        this.minDate)) {
      this.minDate = minTime;
      getMTIDocument().checkFlags();
    }
  }

  public LocalDate getMaxTime()
  {
    return maxDate;
  }

  public void setMaxTime(LocalDate maxTime)
  {
    if (!Objects.equals(this.maxDate,
                        maxTime)) {
      this.maxDate = maxTime;
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
