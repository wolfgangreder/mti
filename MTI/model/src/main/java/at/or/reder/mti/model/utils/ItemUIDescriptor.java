/*
 * Copyright 2020 Wolfgang Reder.
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
package at.or.reder.mti.model.utils;

import java.awt.Image;
import java.net.URL;
import javax.swing.Icon;
import org.openide.util.NbBundle;

public interface ItemUIDescriptor
{

  public String name();

  public default String getName()
  {
    try {
      return NbBundle.getMessage(getClass(),
                                 getClass().getSimpleName() + "_name_" + name());
    } catch (Throwable th) {
      System.err.println(th.getMessage());
//      Exceptions.printStackTrace(th);
    }
    return name();
  }

  public default String getDescription()
  {
    try {
      return NbBundle.getMessage(getClass(),
                                 getClass().getSimpleName() + "_name_" + name());
    } catch (Throwable th) {
      System.err.println(th.getMessage());
      //    Exceptions.printStackTrace(th);
    }
    return "";
  }

  public Icon getIcon(IconDimension dim);

  public default URL getIconURL(IconDimension dim)
  {
    return null;
  }

  public Image getImage(int index);

  public default URL getImageURL(int index)
  {
    return null;
  }

  public default int getImageCount()
  {
    return 0;
  }

}
