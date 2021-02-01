/*
 * Copyright 2017-2021 Wolfgang Reder.
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

import at.or.reder.swing.AspectRatio;
import java.util.Objects;

public final class AspectComboBoxModel extends AbstractComboBoxModel<AspectRatio>
{

  public AspectComboBoxModel()
  {
    data.add(AspectRatio.ASPECT_KEEP);
    data.add(AspectRatio.ASPECT_FREE);
    data.add(AspectRatio.ASPECT_3_2);
    data.add(AspectRatio.ASPECT_2_3);
    data.add(AspectRatio.ASPECT_4_3);
    data.add(AspectRatio.ASPECT_3_4);
    data.add(AspectRatio.ASPECT_16_9);
    data.add(AspectRatio.ASPECT_9_16);
    data.add(AspectRatio.ASPECT_3_1);
    data.add(AspectRatio.ASPECT_1_1);
    selected = AspectRatio.ASPECT_FREE;
  }

  @Override
  public void setSelectedItem(Object anItem)
  {
    AspectRatio old = selected;
    if (anItem instanceof AspectRatio) {
      selected = (AspectRatio) anItem;
    }
    if (!Objects.equals(old,
                        selected)) {
      fireSelectionChanged();
    }
  }

}
