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

import java.sql.DatabaseMetaData;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author Wolfgang Reder
 */
public class ForeignKey
{

  public static final class Builder
  {

    private String tableName;
    private String pkTableName;
    private final SortedMap<Integer, String> fieldNames = new TreeMap<>();
    private final SortedMap<Integer, String> pkFieldNames = new TreeMap<>();
    private String constraintName;
    private CascadeRule updateRule;
    private CascadeRule deleteRule;

    public Builder copy(ForeignKey key)
    {
      tableName = key.getTableName();
      pkTableName = key.getPkTableName();
      fieldNames.clear();
      pkFieldNames.clear();
      List<String> tmp = key.getFieldName();
      for (int i = 0; i < tmp.size(); ++i) {
        fieldNames.put(i,
                       tmp.get(i));
      }
      tmp = key.getPkFieldName();
      for (int i = 0; i < tmp.size(); ++i) {
        pkFieldNames.put(i,
                         tmp.get(i));
      }
      constraintName = key.getConstraintName();
      updateRule = key.getUpdateRule();
      deleteRule = key.getDeleteRule();
      return this;
    }

    public Builder tableName(String tableName)
    {
      this.tableName = tableName;
      return this;
    }

    public Builder pkTableName(String pkTableName)
    {
      this.pkTableName = pkTableName;
      return this;
    }

    public Builder addFieldNames(int index,
                                 String fieldName,
                                 String pkFieldName)
    {
      fieldNames.put(index,
                     fieldName);
      pkFieldNames.put(index,
                       pkFieldName);
      return this;
    }

    public Builder constraintName(String constraintName)
    {
      this.constraintName = constraintName;
      return this;
    }

    public Builder updateRule(CascadeRule updateRule)
    {
      this.updateRule = updateRule;
      return this;
    }

    public Builder deleteRule(CascadeRule deleteRule)
    {
      this.deleteRule = deleteRule;
      return this;
    }

    public Builder rules(CascadeRule rule)
    {
      updateRule(rule);
      deleteRule(rule);
      return this;
    }

    public ForeignKey build()
    {
      return new ForeignKey(tableName,
                            fieldNames.values().stream().filter((f) -> f != null).collect(Collectors.toList()),
                            pkTableName,
                            pkFieldNames.values().stream().filter((f) -> f != null).collect(Collectors.toList()),
                            updateRule,
                            deleteRule,
                            constraintName);
    }

  }

  public static enum CascadeRule
  {
    CASCADE("cascade"),
    DELETE("delete"),
    SET_DEFAULT("set default"),
    SET_NULL("set null"),
    RESTRICT("no action");
    private final String sql;

    private CascadeRule(String sql)
    {
      this.sql = sql;
    }

    public String getSQL()
    {
      return sql;
    }

    public static CascadeRule valueOfDatabaseMetaData(short rule)
    {
      switch (rule) {
        case DatabaseMetaData.importedKeyCascade:
          return CASCADE;
        case DatabaseMetaData.importedKeySetDefault:
          return SET_DEFAULT;
        case DatabaseMetaData.importedKeySetNull:
          return SET_NULL;
        case DatabaseMetaData.importedKeyNoAction:
        case DatabaseMetaData.importedKeyRestrict:
        default:
          return RESTRICT;
      }
    }

  }
  private final String tableName;
  private final List<String> fieldName;
  private final String pkTableName;
  private final List<String> pkFieldName;
  private final String constraintName;
  private String createSQL;
  private String dropSQL;
  private final CascadeRule updateRule;
  private final CascadeRule deleteRule;

  public ForeignKey(String tableName,
                    Collection<String> fieldName,
                    String pkTableName,
                    Collection<String> pkFieldName,
                    CascadeRule updateRule,
                    CascadeRule deleteRule,
                    String constraintName)
  {
    if (tableName == null || tableName.isBlank()) {
      throw new NullPointerException("table must not be null or blank");
    }
    if (pkTableName == null || pkTableName.isBlank()) {
      throw new NullPointerException("pkTableName must not be null or blank");
    }
    if (constraintName == null || constraintName.isBlank()) {
      throw new NullPointerException("constraintName must not be null or blank");
    }
    this.updateRule = updateRule != null ? updateRule : CascadeRule.RESTRICT;
    this.deleteRule = deleteRule != null ? deleteRule : CascadeRule.RESTRICT;
    this.tableName = tableName;
    this.fieldName = fieldName.stream().filter((f) -> f != null).collect(Collectors.toUnmodifiableList());
    this.pkTableName = pkTableName;
    this.pkFieldName = pkFieldName.stream().filter((f) -> f != null).collect(Collectors.toUnmodifiableList());
    if (this.fieldName.size() != this.pkFieldName.size()) {
      throw new IllegalArgumentException("fieldName and pkFieldName must have the same size");
    }
    this.constraintName = constraintName;
  }

  public static ForeignKey.Builder builder()
  {
    return new ForeignKey.Builder();
  }

  public String getTableName()
  {
    return tableName;
  }

  public List<String> getFieldName()
  {
    return fieldName;
  }

  public String getPkTableName()
  {
    return pkTableName;
  }

  public List<String> getPkFieldName()
  {
    return pkFieldName;
  }

  public String getConstraintName()
  {
    return constraintName;
  }

  public CascadeRule getUpdateRule()
  {
    return updateRule;
  }

  public CascadeRule getDeleteRule()
  {
    return deleteRule;
  }

  private String buildCreateSQL()
  {
    StringBuilder builder = new StringBuilder("alter table ");
    builder.append(tableName);
    builder.append(" add constraint ");
    builder.append(constraintName);
    builder.append(" foreign key");
    builder.append(fieldName.stream().collect(Collectors.joining(",",
                                                                 "(",
                                                                 ")")));
    builder.append(" references ");
    builder.append(pkTableName);
    builder.append(pkFieldName.stream().collect(Collectors.joining(",",
                                                                   "(",
                                                                   ")")));
    builder.append(" on delete ");
    builder.append(deleteRule.getSQL());
    builder.append(" on update ");
    builder.append(updateRule.getSQL());
    builder.append(";");
    return builder.toString();
  }

  private String buildDropSQL()
  {
    StringBuilder builder = new StringBuilder("alter table ");
    builder.append(tableName);
    builder.append(" drop constraint ");
    builder.append(constraintName);
    builder.append(";");
    return builder.toString();
  }

  public synchronized String getCreateSQL()
  {
    if (createSQL == null) {
      createSQL = buildCreateSQL();
    }
    return createSQL;
  }

  public synchronized String getDropSQL()
  {
    if (dropSQL == null) {
      dropSQL = buildDropSQL();
    }
    return dropSQL;
  }

  @Override
  public int hashCode()
  {
    int hash = 3;
    hash = 67 * hash + Objects.hashCode(this.tableName);
    hash = 67 * hash + Objects.hashCode(this.fieldName);
    hash = 67 * hash + Objects.hashCode(this.pkTableName);
    hash = 67 * hash + Objects.hashCode(this.pkFieldName);
    hash = 67 * hash + Objects.hashCode(this.constraintName);
    hash = 67 * hash + Objects.hashCode(this.updateRule);
    hash = 67 * hash + Objects.hashCode(this.deleteRule);
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
    final ForeignKey other = (ForeignKey) obj;
    if (!Objects.equals(this.tableName,
                        other.tableName)) {
      return false;
    }
    if (!Objects.equals(this.pkTableName,
                        other.pkTableName)) {
      return false;
    }
    if (!Objects.equals(this.constraintName,
                        other.constraintName)) {
      return false;
    }
    if (!Objects.equals(this.fieldName,
                        other.fieldName)) {
      return false;
    }
    if (!Objects.equals(this.pkFieldName,
                        other.pkFieldName)) {
      return false;
    }
    if (this.updateRule != other.updateRule) {
      return false;
    }
    if (this.deleteRule != other.deleteRule) {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return getCreateSQL();
  }

}
