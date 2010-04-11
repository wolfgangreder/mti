package at.motriv.datamodel.config;

import java.util.UUID;

public class UUIDConfigKey extends ConfigKey<UUID>
{

  UUIDConfigKey(String key, UUID defaultValue)
  {
    super(key, defaultValue);
  }

  @Override
  public Class<? extends UUID> getValueClass()
  {
    return UUID.class;
  }

  @Override
  public UUID parseValue(String strVal)
  {
    if (strVal != null) {
      return UUID.fromString(strVal);
    }
    return null;
  }

  @Override
  public String valueToString(Object val)
  {
    if (val instanceof UUID) {
      return ((UUID) val).toString();
    }
    return null;
  }
}
