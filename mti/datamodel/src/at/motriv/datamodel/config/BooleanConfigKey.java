/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.config;

/**
 *
 * @author wolfi
 */
public class BooleanConfigKey extends ConfigKey<Boolean>
{

  public BooleanConfigKey(String key, Boolean defaultValue)
  {
    super(key, defaultValue);
  }

  @Override
  public Class<? extends Boolean> getValueClass()
  {
    return Boolean.class;
  }

  @Override
  public Boolean parseValue(String strVal)
  {
    return Boolean.parseBoolean(strVal);
  }

  @Override
  public String valueToString(Object val)
  {
    if (val instanceof Boolean) {
      return Boolean.toString((Boolean)val);
    }
    return null;
  }
}
