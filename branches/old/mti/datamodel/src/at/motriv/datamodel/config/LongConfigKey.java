/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.config;

/**
 *
 * @author wolfi
 */
public class LongConfigKey extends ConfigKey<Long>
{

  public LongConfigKey(String key, Long defaultValue)
  {
    super(key, defaultValue);
  }

  @Override
  public Class<? extends Long> getValueClass()
  {
    return Long.class;
  }

  @Override
  public Long parseValue(String strVal)
  {
    if (strVal != null) {
      return Long.parseLong(strVal);
    }
    return null;
  }

  @Override
  public String valueToString(Object val)
  {
    if (val instanceof Number) {
      return Long.toString(((Number) val).longValue(), 10);
    }
    return null;
  }
}
