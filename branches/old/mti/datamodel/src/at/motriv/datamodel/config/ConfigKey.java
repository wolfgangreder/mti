package at.motriv.datamodel.config;

public abstract class ConfigKey<V>
{

  private final String key;
  private final V defaultValue;

  public ConfigKey(String key, V defaultValue)
  {
    this.key = key;
    this.defaultValue = defaultValue;
  }

  public V getDefaultValue()
  {
    return defaultValue;
  }

  public String getKey()
  {
    return key;
  }

  public abstract Class<? extends V> getValueClass();

  public abstract V parseValue(String strVal);

  public abstract String valueToString(Object val);

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final ConfigKey<?> other = (ConfigKey<?>) obj;
    if ((this.key == null) ? (other.key != null) : !this.key.equals(other.key)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 3;
    hash = 61 * hash + (this.key != null ? this.key.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString()
  {
    return key;
  }
}
