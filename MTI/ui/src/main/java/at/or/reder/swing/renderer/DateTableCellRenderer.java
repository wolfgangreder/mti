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
package at.or.reder.swing.renderer;

import at.or.reder.mti.ui.Utils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import javax.swing.table.DefaultTableCellRenderer;

public class DateTableCellRenderer extends DefaultTableCellRenderer
{

  private static final long serialVersionUID = 1L;
  private volatile FormatStyle style = FormatStyle.SHORT;
  private volatile DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(style);

  public final DateTableCellRenderer style(FormatStyle style)
  {
    this.style = style;
    formatter = DateTimeFormatter.ofLocalizedDate(style);
    return this;
  }

  public final DateTableCellRenderer formatter(DateTimeFormatter formatter)
  {
    this.formatter = formatter;
    return this;
  }

  public final DateTimeFormatter getFormatter()
  {
    return this.formatter;
  }

  @Override
  protected void setValue(Object value)
  {
    String strValue = null;
    if (value instanceof LocalDate) {
      strValue = ((LocalDate) value).format(formatter);
    } else if (value instanceof LocalDateTime) {
      strValue = ((LocalDateTime) value).format(formatter);
    } else if (value instanceof ZonedDateTime) {
      strValue = ((ZonedDateTime) value).format(formatter);
    } else if (value instanceof Date) {
      strValue = Utils.dateToLocalDateTime((Date) value).format(formatter);
    }
    super.setValue(strValue);
  }

}
