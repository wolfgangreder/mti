/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.config;

/**
 *
 * @author wolfi
 */
public class StringConfigKey extends ConfigKey<String>
{

  public StringConfigKey(String key, String defaultValue)
  {
    super(key, defaultValue);
  }

  @Override
  public Class<? extends String> getValueClass()
  {
    return String.class;
  }

  @Override
  public String parseValue(String strVal)
  {
    return strVal;
  }

  @Override
  public String valueToString(Object val)
  {
    if (val!=null) {
      return val.toString();
    }
    return null;
  }
}
