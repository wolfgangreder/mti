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

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class DefaultEnumRadioButtonsAdapter<E extends Enum<E>> implements EnumRadioButtonsAdapter<E>
{

  @Override
  public Function<E, Boolean> getFilter()
  {
    return null;
  }

  @Override
  public Function<E, String> getNameAdapter()
  {
    return Enum<E>::toString;
  }

  @Override
  public Comparator<E> getComparator()
  {
    return null;
  }

  @Override
  public List<E> getValues()
  {
    return null;
  }

}
