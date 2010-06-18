/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.filesystem.db;

import at.mountain_sd.objects.impl.SimpleClientPropertySupport;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public final class DefaultDBFileObject implements DBFileObject
{

  private final UUID id;
  private final String name;
  private final UUID parent;
  private final boolean folder;
  private final SimpleClientPropertySupport propSupport = new SimpleClientPropertySupport();

  public DefaultDBFileObject(UUID id, String name, UUID parent, boolean folder, Map<String, ?> props)
  {
    this.id = id;
    this.name = name;
    this.parent = parent;
    this.folder = folder;
    this.propSupport.initWith(props);
  }

  @Override
  public UUID getId()
  {
    return id;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public UUID getParent()
  {
    return parent;
  }

  @Override
  public boolean isFolder()
  {
    return folder;
  }

  @Override
  public String toString()
  {
    return name;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final DefaultDBFileObject other = (DefaultDBFileObject) obj;
    if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 83 * hash + (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }

  @Override
  public Object getClientProperty(String key)
  {
    return propSupport.getClientProperty(key);
  }

  @Override
  public <T> T getClientProperty(String key, T defaultValue)
  {
    return propSupport.getClientProperty(key, defaultValue);
  }

  @Override
  public Object setClientProperty(String key, Object value)
  {
    return propSupport.setClientProperty(key, value);
  }

  @Override
  public Map<String, ?> getClientProperites()
  {
    return propSupport.getClientProperites();
  }

  @Override
  public void clear()
  {
    propSupport.clear();
  }

  @Override
  public void initWith(Map<String, ?> init)
  {
    propSupport.initWith(init);
  }

  @Override
  public void addAll(Map<String, ?> items)
  {
    propSupport.addAll(items);
  }
}
