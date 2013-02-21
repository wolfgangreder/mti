/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.openide.util.Exceptions;

/**
 *
 * @author wolfi
 */
public class EnumConfigKey<E extends Enum<?>> extends ConfigKey<E>
{

  private final Class<? extends E> clazz;
  private final Method valueOf;

  public EnumConfigKey(String key, E defaultValue, Class<? extends E> clazz)
  {
    super(key, defaultValue);
    this.clazz = clazz;
    Method tmp = null;
    try {
      tmp = clazz.getMethod("valueOf", String.class);
    } catch (NoSuchMethodException ex) {
      Exceptions.printStackTrace(ex);
    } catch (SecurityException ex) {
      Exceptions.printStackTrace(ex);
    }
    valueOf = tmp;
  }

  @Override
  public Class<? extends E> getValueClass()
  {
    return clazz;
  }

  @Override
  @SuppressWarnings("unchecked")
  public E parseValue(String strVal)
  {
    if (strVal != null) {
      try {
        return (E) valueOf.invoke(null, strVal);
      } catch (IllegalAccessException ex) {
        Exceptions.printStackTrace(ex);
      } catch (IllegalArgumentException ex) {
        Exceptions.printStackTrace(ex);
      } catch (InvocationTargetException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public String valueToString(Object val)
  {
    if (clazz.isInstance(val)) {
      return ((E) val).name();
    }
    return null;
  }
}
