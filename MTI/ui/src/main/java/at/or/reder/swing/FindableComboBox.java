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

public class FindableComboBox<I> extends JComboBox2<I>
{

  private static final long serialVersionUID = 1L;

  @Override
  @SuppressWarnings("unchecked")
  public int getSelectedIndex()
  {
    int result = -1;
    if (getModel() instanceof FindableComboBoxModel) {
      result = ((FindableComboBoxModel) getModel()).getIndexOf(getModel().getSelectedItem());
    }
    if (result == -1 && getModel().getSelectedItem() != null) {
      result = super.getSelectedIndex();
    }
    return result;
  }

}
