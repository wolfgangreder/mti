/*
 * Copyright 2021 Wolfgang Reder.
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
package at.or.reder.mti.model.api;

import at.or.reder.mti.model.QuickInfo;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.swing.Icon;

/**
 *
 * @author Wolfgang Reder
 */
public final class DefaultQuickInfo implements QuickInfo, Serializable
{

  private final UUID id;
  private final Icon icon;
  private final String name;
  private final String description;

  public DefaultQuickInfo(UUID id,
                          Icon icon,
                          String name,
                          String description)
  {
    this.id = Objects.requireNonNull(id,
                                     "id is null");
    this.icon = icon;
    this.name = Objects.requireNonNull(name,
                                       "name is null");
    if (name.isBlank()) {
      throw new IllegalArgumentException("name is blank");
    }
    this.description = description;
  }

  @Override
  public UUID getId()
  {
    return id;
  }

  @Override
  public Icon getIcon()
  {
    return icon;
  }

  @Override
  public String getLabel()
  {
    return name;
  }

  @Override
  public String getDescription()
  {
    return description;
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 59 * hash + Objects.hashCode(this.id);
    return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof QuickInfo)) {
      return false;
    }
    final QuickInfo other = (QuickInfo) obj;
    return Objects.equals(this.id,
                          other.getId());
  }

  @Override
  public String toString()
  {
    return "DefaultQuickInfo{" + "id=" + id + ", name=" + name + '}';
  }

}
