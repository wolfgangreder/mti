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

import at.or.reder.osplatform.FileSizeFormatter;
import at.or.reder.osplatform.FileSizeFormatter.Flags;
import at.or.reder.osplatform.PlatformFolders;
import java.util.function.Function;
import javax.swing.table.DefaultTableCellRenderer;

public class FileSizeTableCellRenderer extends DefaultTableCellRenderer
{

  private final Function<Long, String> formatter;

  public FileSizeTableCellRenderer()
  {
    this.formatter = PlatformFolders.getInstance()::formatFileSize;
  }

  public FileSizeTableCellRenderer(int numDigits,
                                   Flags... flags)
  {
    this.formatter = (s) -> FileSizeFormatter.formatFileSize(s,
                                                             numDigits,
                                                             flags);
  }

  @Override
  protected void setValue(Object value)
  {
    String val = null;
    if (value instanceof Number) {
      Number size = (Number) value;
      val = formatter.apply(size.longValue());
    }
    super.setValue(val);
  }

}
