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
package at.or.reder.mti.jaybirdsql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.netbeans.api.annotations.common.NonNull;

/**
 *
 * @author Wolfgang Reder
 */
public final class DefaultIndexInfo implements IndexInfo
{

  public static final class Builder
  {

    private String name;
    private boolean unique;
    private boolean primaryKey;
    private final List<String> columnNames = new ArrayList<>();
    private SortOrder sortOrder = SortOrder.NONE;

    public Builder name(@NonNull String name)
    {
      this.name = Objects.requireNonNull(name,
                                         "name is null");
      return this;
    }

    public String getName()
    {
      return name;
    }

    public Builder unique(boolean unique)
    {
      this.unique = unique;
      return this;
    }

    public Builder primaryKey(boolean primaryKey)
    {
      this.primaryKey = primaryKey;
      return this;
    }

    public Builder columnName(int index,
                              @NonNull String columnName)
    {
      Objects.requireNonNull(columnName,
                             "columnName is null");
      while (index > columnNames.size()) {
        columnNames.add(null);
      }
      if (index < columnNames.size()) {
        columnNames.set(index,
                        columnName);
      } else {
        columnNames.add(index,
                        columnName);
      }
      return this;
    }

    public Builder sortOrder(SortOrder sortOrder)
    {
      this.sortOrder = sortOrder;
      return this;
    }

    public IndexInfo build()
    {
      return new DefaultIndexInfo(name,
                                  unique,
                                  primaryKey,
                                  columnNames,
                                  sortOrder);
    }

  }
  private final String name;
  private final boolean unique;
  private final boolean primaryKey;
  private final List<String> columnNames;
  private final SortOrder sortOrder;

  private DefaultIndexInfo(String name,
                           boolean unique,
                           boolean primaryKey,
                           List<String> columnNames,
                           SortOrder sortOrder)
  {
    this.name = Objects.requireNonNull(name,
                                       "name is null");
    this.unique = unique;
    this.primaryKey = primaryKey;
    this.columnNames = Collections.unmodifiableList(new ArrayList<>(columnNames));
    this.sortOrder = sortOrder != null ? sortOrder : SortOrder.NONE;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public boolean isUnique()
  {
    return unique;
  }

  @Override
  public boolean isPrimaryKey()
  {
    return primaryKey;
  }

  @Override
  public List<String> getColumnNames()
  {
    return columnNames;
  }

  @Override
  public SortOrder getSortOrder()
  {
    return sortOrder;
  }

  @Override
  public int hashCode()
  {
    int hash = 3;
    hash = 53 * hash + Objects.hashCode(this.name);
    return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final DefaultIndexInfo other = (DefaultIndexInfo) obj;
    return Objects.equals(this.name,
                          other.name);
  }

  @Override
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    if (primaryKey) {
      builder.append("primary key ");
    } else {
      if (unique) {
        builder.append("unique ");
      }
      builder.append("index ");
    }
    builder.append(name);
    builder.append("(");
    for (String colName : columnNames) {
      builder.append(colName);
      builder.append(",");
    }
    if (columnNames.size() > 0) {
      builder.setLength(builder.length() - 1);
    }
    builder.append(")");
    if (sortOrder != SortOrder.NONE) {
      builder.append(" ");
      builder.append(sortOrder.name());
    }
    return builder.toString();
  }

}
