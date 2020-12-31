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

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public interface ColumnDescriptor
{

  public default boolean isRequired()
  {
    return true;
  }

  public default boolean isDefaultVisible()
  {
    return getDefaultWidth() > 0;
  }

  public default boolean isDisplayable()
  {
    return true;
  }

  public default int getDefaultWidth()
  {
    return 75;
  }

  public String getHeaderValue();

  public Class<?> getValueClass();

  public default TableCellRenderer getRenderer()
  {
    return null;
  }

  public default TableCellEditor getEditor()
  {
    return null;
  }

}
