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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public interface ErrorFlagable extends Validateable
{

  public static final Border DEFAULT_ERROR_BORDER = BorderFactory.createLineBorder(new Color(0xcc,
                                                                                             0,
                                                                                             0),
                                                                                   1);
  public static final String PROP_ERRORBORDER = "errorBorder";

  public Border getBorder();

  public Border getErrorBorder();

  public void setErrorBorder(Border newErrorBorder);

  public static void paintBorder(Component comp,
                                 ErrorFlagable ef,
                                 Graphics g)
  {
    paintBorder(comp,
                ef::getBorder,
                ef::isDataValid,
                ef::getErrorBorder,
                g);
  }

  public static void paintBorder(Component comp,
                                 Supplier<Border> borderProvider,
                                 Supplier<Boolean> isValidProvider,
                                 Supplier<Border> errorBorderProvider,
                                 Graphics g)
  {
    Border border;
    Border errorBorder = errorBorderProvider.get();
    if (errorBorder != null && !isValidProvider.get()) {
      border = errorBorder;
    } else {
      border = borderProvider.get();
    }
    if (border != null) {
      border.paintBorder(comp,
                         g,
                         0,
                         0,
                         comp.getWidth(),
                         comp.getHeight());
    }
  }

}
