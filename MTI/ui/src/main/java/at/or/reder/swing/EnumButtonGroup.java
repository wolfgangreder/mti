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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JRadioButton;
import org.openide.util.Exceptions;

public final class EnumButtonGroup<E extends Enum<E>> extends ObservableButtonGroup
{

  private static final long serialVersionUID = 1L;
  private Class<E> myClass;
  private Method methValues;
  private final Map<ButtonModel, E> modelMap = new HashMap<>();

  public EnumButtonGroup()
  {
  }

  public EnumButtonGroup(Class<E> clazz)
  {
    setClass(clazz);
  }

  public Class<E> getEnumClass()
  {
    return myClass;
  }

  public void setClass(Class<E> clazz)
  {
    if (clazz != myClass) {
      myClass = clazz;
      methValues = null;
      if (myClass != null) {
        try {
          methValues = myClass.getMethod("values");
        } catch (NoSuchMethodException | SecurityException ex) {
          // Sollte nie passieren weil ja Enum !
          Exceptions.printStackTrace(ex);
          myClass = null;
        }
      }
    }
  }

  public List<E> getValues()
  {
    if (methValues != null) {
      try {
        List<E> result = new ArrayList<>();
        @SuppressWarnings("unchecked")
        E[] a = (E[]) methValues.invoke(null);
        result.addAll(Arrays.asList(a));
        return result;
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return Collections.emptyList();
  }

  @SuppressWarnings("unchecked")
  public E getSelected()
  {
    ButtonModel model = getSelection();
    return modelMap.get(model);
  }

  public void setSelected(E selected)
  {
    for (AbstractButton btn : buttons) {
      setSelected(btn.getModel(),
                  modelMap.get(btn.getModel()) == selected);
    }
  }

  public List<AbstractButton> createButtons()
  {
    return createButtons(E::toString,
                         null,
                         null,
                         getValues());
  }

  public List<AbstractButton> createButtons(Function<E, String> nameAdapter)
  {
    return createButtons(nameAdapter,
                         null,
                         null,
                         getValues());
  }

  public List<AbstractButton> createSortedButtons(Comparator<E> comp)
  {
    return createButtons(E::toString,
                         null,
                         comp,
                         getValues());
  }

  public List<AbstractButton> createFilteredButtons(Function<E, Boolean> filter)
  {
    return createButtons(E::toString,
                         filter,
                         null,
                         getValues());
  }

  public List<AbstractButton> createButtons(EnumRadioButtonsAdapter<E> adapter)
  {
    if (adapter != null) {
      List<E> values = adapter.getValues();
      if (values == null) {
        values = getValues();
      }
      return createButtons(adapter.getNameAdapter(),
                           adapter.getFilter(),
                           adapter.getComparator(),
                           values);
    } else {
      return createButtons();
    }
  }

  public List<AbstractButton> createButtons(Function<E, String> nameAdapter,
                                            Function<E, Boolean> filter,
                                            Comparator<E> comparator,
                                            List<E> values)
  {
    List<AbstractButton> result = new ArrayList<>();
    if (comparator != null) {
      Collections.sort(values,
                       comparator);
    }
    for (E e : values) {
      if (filter == null || filter.apply(e)) {
        JRadioButton rb;
        if (nameAdapter != null) {
          rb = new MyRadioButton(nameAdapter.apply(e));
        } else {
          rb = new MyRadioButton(e.toString());
        }
        ButtonModel bm = rb.getModel();
        modelMap.put(bm,
                     e);
        add(rb);
        result.add(rb);
      }
    }
    return result;
  }

  private final class MyRadioButton extends JRadioButton
  {

    private static final long serialVersionUID = 1L;

    private MyRadioButton(String text)
    {
      super(text);
    }

    @Override
    public void setModel(ButtonModel newModel)
    {
      if (getModel() == null) {
        super.setModel(newModel);
      }
    }

  }
}
