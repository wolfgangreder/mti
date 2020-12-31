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

import at.or.reder.swing.AspectRatio;
import org.openide.util.NbBundle.Messages;

@Messages({"# {0} - x",
           "# {1} - y",
           "AspectListCellRenderer_fmt={0,number,0.#}/{1,number,0.#}",
           "AspectListCellRenderer_free=Frei",
           "AspectListCellRenderer_keep=Behalten"})
public class AspectListCellRenderer extends DefaultListCellRendererEx<AspectRatio>
{

  @Override
  protected String getStringValue(AspectRatio value)
  {
    if (value == AspectRatio.ASPECT_FREE) {
      return Bundle.AspectListCellRenderer_free();
    } else if (value == AspectRatio.ASPECT_KEEP) {
      return Bundle.AspectListCellRenderer_keep();
    } else {
      return Bundle.AspectListCellRenderer_fmt(value.getX(),
                                               value.getY());
    }
  }

}
