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

import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class DateListCellRenderer extends DefaultListCellRenderer
{

  private static final long serialVersionUID = 1L;
  private final DateFormat format;

  public DateListCellRenderer()
  {
    this(DateFormat.getDateTimeInstance());
  }

  public DateListCellRenderer(String fmt) throws IllegalArgumentException, NullPointerException
  {
    this(fmt != null ? new SimpleDateFormat(fmt) : null);
  }

  public DateListCellRenderer(DateFormat fmt) throws NullPointerException
  {
    if (fmt == null) {
      throw new NullPointerException("format==null");
    }
    format = fmt;
  }

  @Override
  public Component getListCellRendererComponent(JList<?> list,
                                                Object value,
                                                int index,
                                                boolean isSelected,
                                                boolean cellHasFocus)
  {
    String val = (value instanceof Date) ? format.format((Date) value) : null;
    return super.getListCellRendererComponent(list,
                                              val,
                                              index,
                                              isSelected,
                                              cellHasFocus);
  }

}
