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

import at.or.reder.mti.model.utils.Money;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.netbeans.api.annotations.common.NonNull;

/**
 *
 * @author Wolfgang Reder
 */
abstract class AbstractStore
{

  public static void setMoney(PreparedStatement stmt,
                              int parameterIndex,
                              Money m) throws SQLException
  {
    if (m != null) {
      stmt.setBigDecimal(parameterIndex,
                         m.toBigDecimal());
    } else {
      stmt.setNull(parameterIndex,
                   Types.DECIMAL);
    }
  }

  public static Money getMoney(ResultSet rs,
                               int iParam) throws SQLException
  {
    return Money.valueOf(rs.getBigDecimal(iParam));
  }

  public static Money getMoney(ResultSet rs,
                               String iParam) throws SQLException
  {
    return Money.valueOf(rs.getBigDecimal(iParam));
  }

  public static UUID getUUID(ResultSet rs,
                             int iParam) throws SQLException
  {
    String tmp = rs.getString(iParam);
    if (tmp != null) {
      return UUID.fromString(tmp);
    }
    return null;
  }

  public static UUID getUUID(ResultSet rs,
                             String paramName) throws SQLException
  {
    String tmp = rs.getString(paramName);
    if (tmp != null) {
      return UUID.fromString(tmp);
    }
    return null;
  }

  public static Instant toInstant(java.sql.Date sqlDate)
  {
    if (sqlDate != null) {
      return Instant.ofEpochMilli(sqlDate.getTime());
    }
    return null;
  }

  public static Instant toInstant(java.sql.Timestamp ts)
  {
    if (ts != null) {
      return Instant.ofEpochMilli(ts.getTime());
    }
    return null;
  }

  public static LocalDate getLocalDate(ResultSet rs,
                                       String paramName) throws SQLException
  {
    Instant i = toInstant(rs.getDate(paramName));
    if (i != null) {
      return i.atZone(ZoneId.systemDefault()).toLocalDate();
    }
    return null;
  }

  public static LocalDateTime getLocalDateTime(ResultSet rs,
                                               String paramName) throws SQLException
  {
    Instant i = toInstant(rs.getTimestamp(paramName));
    if (i != null) {
      return i.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    return null;
  }

  private interface ValueOf
  {

    public <E extends Enum<E>> E valueOf(String name,
                                         E defaultValue);

  }
  @SuppressWarnings("Convert2Lambda")
  private static final ValueOf nullValueOf = new ValueOf()
  {

    @Override
    public <E extends Enum<E>> E valueOf(String name,
                                         E defaultValue)
    {
      return defaultValue;
    }

  };

  private static final class MethodValueOf implements ValueOf
  {

    private final Method valueOf;

    private MethodValueOf(Method vo)
    {
      valueOf = vo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Enum<E>> E valueOf(String name,
                                         E defaultValue)
    {
      if (valueOf != null) {
        try {
          return (E) valueOf.invoke(null,
                                    name);
        } catch (Error | Exception ex) {
          throw new IllegalArgumentException(ex);
        }
      }
      return defaultValue;
    }

  }
  private static final Map<Class<? extends Enum<?>>, ValueOf> valueOfMap = new HashMap<>();

  private static <E extends Enum<E>> ValueOf getValueOf(Class<? extends Enum<?>> clazz)
  {
    synchronized (valueOfMap) {
      return valueOfMap.computeIfAbsent(clazz,
                                        (Class<? extends Enum<?>> cl) -> {
                                          Method method = null;
                                          try {
                                            method = cl.getMethod("valueOf",
                                                                  String.class);
                                          } catch (Error | Exception ex) {
                                            Logger.getGlobal().log(Level.SEVERE,
                                                                   "getValueOf enum",
                                                                   ex);
                                          }
                                          if (method != null) {
                                            return new MethodValueOf(method);
                                          } else {
                                            return nullValueOf;
                                          }
                                        });
    }
  }

  /**
   * <p>
   * getEnumDefaultIfFail.</p>
   *
   * @param rs a {@link java.sql.ResultSet} object.
   * @param colName a {@link java.lang.String} object.
   * @param defaultValue a E object.
   * @param <E> a E object.
   * @return a E object.
   * @throws java.sql.SQLException
   * @throws java.lang.NullPointerException if any.
   */
  public static <E extends Enum<E>> E getEnumDefaultIfFail(ResultSet rs,
                                                           String colName,
                                                           E defaultValue) throws SQLException,
                                                                                  NullPointerException
  {
    if (defaultValue == null) {
      throw new NullPointerException("defaultValue==null");
    }
    return getEnumDefaultIfFail(rs,
                                colName,
                                defaultValue.getDeclaringClass(),
                                defaultValue);
  }

  /**
   * <p>
   * getEnum.</p>
   *
   * @param rs a {@link java.sql.ResultSet} object.
   * @param colName a {@link java.lang.String} object.
   * @param defaultValue a E object.
   * @param <E> a E object.
   * @return a E object.
   * @throws java.sql.SQLException if any.
   * @throws java.lang.IllegalArgumentException if any.
   */
  @SuppressWarnings("unchecked")
  public static <E extends Enum<E>> E getEnum(ResultSet rs,
                                              String colName,
                                              E defaultValue) throws SQLException,
                                                                     IllegalArgumentException
  {
    if (defaultValue == null) {
      throw new NullPointerException("defaultValue==null");
    }
    return getEnum(rs,
                   colName,
                   (Class<? extends Enum<E>>) defaultValue.getClass(),
                   defaultValue);
  }

  /**
   * <p>
   * getEnumDefaultIfFail.</p>
   *
   * @param rs a {@link java.sql.ResultSet} object.
   * @param colName a {@link java.lang.String} object.
   * @param clazz a {@link java.lang.Class} object.
   * @param defaultValue a E object.
   * @param <E> a E object.
   * @return a E object.
   * @throws java.sql.SQLException
   */
  public static <E extends Enum<E>> E getEnumDefaultIfFail(ResultSet rs,
                                                           String colName,
                                                           Class<? extends Enum<E>> clazz,
                                                           E defaultValue) throws SQLException
  {
    try {
      return getEnum(rs,
                     colName,
                     clazz,
                     defaultValue);
    } catch (Error | IllegalArgumentException ex) {

    }
    return defaultValue;
  }

  /**
   * <p>
   * getEnum.</p>
   *
   * @param rs a {@link java.sql.ResultSet} object.
   * @param colName a {@link java.lang.String} object.
   * @param clazz a {@link java.lang.Class} object.
   * @param defaultValue a E object.
   * @param <E> a E object.
   * @return a E object.
   * @throws java.sql.SQLException if any.
   * @throws java.lang.IllegalArgumentException if any.
   */
  public static <E extends Enum<E>> E getEnum(ResultSet rs,
                                              String colName,
                                              Class<? extends Enum<E>> clazz,
                                              E defaultValue) throws SQLException, IllegalArgumentException
  {
    String s = rs.getString(colName);
    if (s == null || s.trim().isEmpty()) {
      return defaultValue;
    }
    return getValueOf(clazz).valueOf(s,
                                     defaultValue);
  }

  public static boolean deleteField(final Connection conn,
                                    final DatabaseMetaData meta,
                                    final String tableName,
                                    final String columnName) throws SQLException

  {
    try (ResultSet rs = meta.getColumns(null,
                                        null,
                                        tableName,
                                        columnName)) {
      if (rs.next()) {
        try (Statement stmt = conn.createStatement()) {
          stmt.execute("alter table " + tableName + " drop " + columnName);
          return true;
        }
      }
    }
    return false;
  }

  public static IndexInfo getPrimaryKey(final @NonNull DatabaseMetaData meta,
                                        final @NonNull String tableName) throws SQLException
  {
    DefaultIndexInfo.Builder indexBuilder = null;
    try (ResultSet rs = meta.getPrimaryKeys(null,
                                            null,
                                            tableName)) {
      if (rs.next()) {
        if (indexBuilder == null) {
          indexBuilder = new DefaultIndexInfo.Builder();
          String ndx = rs.getString("PK_NAME");
          if (ndx == null) {
            ndx = "PK to table " + tableName;
          }
          indexBuilder.name(ndx);
          indexBuilder.primaryKey(true);
          indexBuilder.unique(true);
        }
        int colIndex = rs.getInt("KEY_SEQ");
        String colName = rs.getString("COLUMN_NAME");
        if (colIndex > 0 && colName != null) {
          indexBuilder.columnName(colIndex - 1,
                                  colName);
        }
      }
    }
    if (indexBuilder != null) {
      return indexBuilder.build();
    }
    return null;
  }

  public static IndexInfo getIndexInfo(final @NonNull DatabaseMetaData meta,
                                       final @NonNull String tableName,
                                       final @NonNull String indexName) throws SQLException
  {
    DefaultIndexInfo.Builder indexBuilder = null;
    try (ResultSet rs = meta.getIndexInfo(null,
                                          null,
                                          tableName.toUpperCase(),
                                          false,
                                          true)) {
      while (rs.next()) {
        String ndx = rs.getString("INDEX_NAME");
        short type = rs.getShort("TYPE");
        if (type != DatabaseMetaData.tableIndexStatistic) {
          if (indexName.equalsIgnoreCase(ndx)) {
            if (indexBuilder == null) {
              indexBuilder = new DefaultIndexInfo.Builder();
              indexBuilder.name(ndx);
              String ad = rs.getString("ASC_OR_DESC");
              if (null == ad) {
                indexBuilder.sortOrder(SortOrder.NONE);
              } else {
                switch (ad) {
                  case "A":
                    indexBuilder.sortOrder(SortOrder.ASCENDING);
                    break;
                  case "D":
                    indexBuilder.sortOrder(SortOrder.DESCENDING);
                    break;
                  default:
                    indexBuilder.sortOrder(SortOrder.NONE);
                    break;
                }
              }
              indexBuilder.unique(!rs.getBoolean("NON_UNIQUE"));
            }
            int colIndex = rs.getInt("ORDINAL_POSITION");
            String colName = rs.getString("COLUMN_NAME");
            if (colName != null && colIndex > 0) {
              indexBuilder.columnName(colIndex - 1,
                                      colName);
            }
          }
        }
      }
    }
    if (indexBuilder != null) {
      IndexInfo primaryKey = getPrimaryKey(meta,
                                           tableName);
      indexBuilder.primaryKey(primaryKey != null && primaryKey.getName().equals(indexBuilder.getName()));
      return indexBuilder.build();
    }
    return null;
  }

  public static List<IndexInfo> getIndexInfo(final @NonNull DatabaseMetaData meta,
                                             final @NonNull String tableName) throws SQLException
  {
    Map<String, DefaultIndexInfo.Builder> builderMap = new HashMap<>();
    try (ResultSet rs = meta.getIndexInfo(null,
                                          null,
                                          tableName,
                                          false,
                                          true)) {
      while (rs.next()) {
        String ndx = rs.getString("INDEX_NAME");
        short type = rs.getShort("TYPE");
        if (type != DatabaseMetaData.tableIndexStatistic) {
          DefaultIndexInfo.Builder indexBuilder = builderMap.get(ndx);
          if (indexBuilder == null) {
            indexBuilder = new DefaultIndexInfo.Builder();
            indexBuilder.name(ndx);
            String ad = rs.getString("ASC_OR_DESC");
            if (null == ad) {
              indexBuilder.sortOrder(SortOrder.NONE);
            } else {
              switch (ad) {
                case "A":
                  indexBuilder.sortOrder(SortOrder.ASCENDING);
                  break;
                case "D":
                  indexBuilder.sortOrder(SortOrder.DESCENDING);
                  break;
                default:
                  indexBuilder.sortOrder(SortOrder.NONE);
                  break;
              }
            }
            indexBuilder.unique(!rs.getBoolean("NON_UNIQUE"));
            builderMap.put(ndx,
                           indexBuilder);
          }
          int colIndex = rs.getInt("ORDINAL_POSITION");
          String colName = rs.getString("COLUMN_NAME");
          if (colName != null && colIndex > 0) {
            indexBuilder.columnName(colIndex - 1,
                                    colName);
          }
        }
      }
    }
    if (!builderMap.isEmpty()) {
      final IndexInfo primaryKey = getPrimaryKey(meta,
                                                 tableName);

      return builderMap.values().stream().
              map((b) -> {
                b.primaryKey(primaryKey != null && b.getName().equals(primaryKey.getName()));
                return b.build();
              }).
              sorted(Comparator.comparing(IndexInfo::getName)).
              collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }

  public static boolean deleteIndex(final Connection conn,
                                    String indexName) throws SQLException
  {
    try (PreparedStatement stmt = conn.prepareStatement("select RDB$INDEX_NAME from RDB$INDICES where  RDB$INDEX_NAME=?")) {
      stmt.setString(1,
                     indexName.toUpperCase());
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          try (Statement s = conn.createStatement()) {
            s.execute("drop index " + indexName);
            conn.commit();
            return true;
          }
        }
      }
    }
    return false;
  }

  public static boolean deleteTrigger(final Connection conn,
                                      final String tableName,
                                      final String triggerName) throws SQLException
  {
    try (PreparedStatement stmt = conn.prepareStatement("select rdb$trigger_source from rdb$triggers\n"
                                                        + "where rdb$trigger_name=? and rdb$relation_name=?")) {
      stmt.setString(1,
                     triggerName.toUpperCase());
      stmt.setString(2,
                     tableName.toUpperCase());
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          try (Statement s = conn.createStatement()) {
            s.execute("drop trigger " + triggerName);
            conn.commit();
            return true;
          }
        }
      }
    }
    return false;
  }

  public static String getTriggerSource(final Connection conn,
                                        final String tableName,
                                        final String triggerName) throws SQLException
  {
    try (PreparedStatement stmt = conn.prepareStatement("select rdb$trigger_source from rdb$triggers\n"
                                                        + "where rdb$trigger_name=? and rdb$relation_name=?")) {
      stmt.setString(1,
                     triggerName.toUpperCase());
      stmt.setString(2,
                     tableName.toUpperCase());
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getString("rdb$trigger_source");
        }
      }
    }
    return null;
  }

  public static String getExceptionMessage(final Connection conn,
                                           final String exceptionName) throws SQLException
  {
    try (PreparedStatement stmt = conn.prepareStatement("select rdb$message from rdb$exceptions\n"
                                                        + "where rdb$system_flag=0 and rdb$exception_name=?")) {
      stmt.setString(1,
                     exceptionName.toUpperCase());
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getString(1);
        }
      }
    }
    return null;
  }

  public static String getViewSource(final Connection conn,
                                     final String viewName) throws SQLException
  {
    try (PreparedStatement stmt = conn.prepareStatement("select rdb$view_source\n"
                                                        + "from rdb$relations\n"
                                                        + "where rdb$system_flag=0 and rdb$relation_name=?")) {
      stmt.setString(1,
                     viewName.toUpperCase());
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getString(1);
        }
      }
      return null;
    }
  }

  public static boolean checkGenerator(final Connection conn,
                                       final String generatorName) throws SQLException
  {
    try (Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select rdb$generator_name from rdb$generators \n"
                                             + "where rdb$generator_name='" + generatorName.toUpperCase() + "' and rdb$system_flag=0")) {
      if (!rs.next()) {
        stmt.execute("create generator " + generatorName);
        return true;
      }
    }
    return false;
  }

  public static void checkStringColSize(DatabaseMetaData meta,
                                        String table,
                                        String column,
                                        int minSize) throws SQLException
  {
    checkStringColSize(meta,
                       table,
                       column,
                       minSize,
                       false,
                       false,
                       null);
  }

  public static boolean checkStringColSize(DatabaseMetaData meta,
                                           String table,
                                           String column,
                                           int minSize,
                                           boolean create,
                                           boolean allowNull,
                                           String defaultValue) throws SQLException
  {
    try (ResultSet rs = meta.getColumns(null,
                                        null,
                                        table.toUpperCase(),
                                        column.toUpperCase())) {
      if (rs.next()) {
        int size = rs.getInt("COLUMN_SIZE");
        if (size < minSize) {
          try (Statement stmt = meta.getConnection().createStatement()) {
            stmt.execute("alter table " + table + " alter " + column + " type varchar(" + minSize + ")");
          }
        }
      } else if (create) {
        try (Statement stmt = meta.getConnection().createStatement()) {
          if (allowNull) {
            stmt.execute("alter table " + table + " add " + column + " varchar(" + minSize + ")");
          } else {
            stmt.execute("alter table " + table + " add " + column + " varchar(" + minSize + ") default '"
                         + defaultValue + "' not null");
            meta.getConnection().commit();
            if (defaultValue != null) {
              stmt.execute("update " + table + " set " + column + "='" + defaultValue + "' where " + column + " is null");
            }
          }
          return true;
        }
      }
    }
    return false;
  }

  public static boolean checkUTFStringColSize(DatabaseMetaData meta,
                                              String table,
                                              String column,
                                              int minSize,
                                              boolean create,
                                              boolean allowNull,
                                              String defaultValue) throws SQLException
  {
    try (ResultSet rs = meta.getColumns(null,
                                        null,
                                        table.toUpperCase(),
                                        column.toUpperCase())) {
      if (rs.next()) {
        int size = rs.getInt("COLUMN_SIZE");
        if (size < minSize) {
          try (Statement stmt = meta.getConnection().createStatement()) {
            stmt.execute("alter table " + table + " alter " + column + " type varchar(" + minSize + ")");
          }
        }
      } else if (create) {
        try (Statement stmt = meta.getConnection().createStatement()) {
          if (allowNull) {
            stmt.execute(
                    "alter table " + table + " add " + column + " varchar(" + minSize + ") character set UTF8");
          } else {
            stmt.execute(
                    "alter table " + table + " add " + column + " varchar(" + minSize + ") character set UTF8 default '"
                    + defaultValue + "' not null");
            meta.getConnection().commit();
            if (defaultValue != null) {
              stmt.execute("update " + table + " set " + column + "='" + defaultValue + "' where " + column + " is null");
            }
          }
          return true;
        }
      }
    }
    return false;
  }

  public static String getComputedFieldSource(Connection conn,
                                              String table,
                                              String column) throws SQLException
  {
    try (PreparedStatement stmt = conn.prepareStatement("select f.rdb$computed_source \n"
                                                        + "from rdb$relation_fields r,rdb$fields f \n"
                                                        + "where r.rdb$field_source=f.rdb$field_name and r.rdb$field_name=? and r.rdb$relation_name=?")) {
      stmt.setString(1,
                     column.toUpperCase());
      stmt.setString(2,
                     table.toUpperCase());
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getString(1);
        }
      }
    }
    return null;
  }

  public static String getDomainValidationSource(Connection conn,
                                                 String domain) throws SQLException

  {
    try (PreparedStatement stmt = conn.prepareStatement("select rdb$validation_source from rdb$fields where rdb$field_name=?")) {
      stmt.setString(1,
                     domain.toUpperCase());
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getString(1);
        }
      }
      return null;
    }
  }

  public boolean checkTable(DatabaseMetaData meta,
                            Statement stmt,
                            String tableName,
                            String strStmt) throws SQLException
  {
    try (ResultSet rs = meta.getTables(null,
                                       null,
                                       tableName.toUpperCase(),
                                       new String[]{"TABLE"})) {
      if (!rs.next()) {
        if (strStmt != null) {
          stmt.execute(strStmt);
        }
        return true;
      }
    }
    return false;
  }

  public boolean checkTableField(DatabaseMetaData meta,
                                 Statement stmt,
                                 String tableName,
                                 String fieldName,
                                 String strStmt) throws SQLException
  {
    try (ResultSet rs = meta.getColumns(null,
                                        null,
                                        tableName.toUpperCase(),
                                        fieldName.toUpperCase())) {
      if (!rs.next()) {
        if (strStmt != null) {
          stmt.execute(strStmt);
        }
        return true;
      }
    }
    return false;
  }

  public boolean checkForeignKey(DatabaseMetaData meta,
                                 Statement stmt,
                                 ForeignKey key) throws SQLException
  {
    return checkForeignKey(meta,
                           stmt,
                           key,
                           true);
  }

  public boolean checkForeignKey(DatabaseMetaData meta,
                                 Statement stmt,
                                 ForeignKey key,
                                 boolean create) throws SQLException
  {
    ForeignKey.Builder builder = null;
    try (ResultSet rs = meta.getImportedKeys(null,
                                             null,
                                             key.getTableName().toUpperCase())) {
      while (rs.next()) {
        String pkTableName = rs.getString("PKTABLE_NAME");
        String pkColumnName = rs.getString("PKCOLUMN_NAME");
        String fkColumnName = rs.getString("FKCOLUMN_NAME");
        int keySeq = rs.getInt("KEY_SEQ");
        short updateRule = rs.getShort("UPDATE_RULE");
        short deleteRule = rs.getShort("DELETE_RULE");
        String fkName = rs.getString("FK_NAME");
        if (key.getConstraintName().equalsIgnoreCase(fkName)) {
          if (builder == null) {
            builder = new ForeignKey.Builder();
            builder.constraintName(key.getConstraintName());
            builder.pkTableName(pkTableName);
            builder.tableName(key.getTableName());
            builder.updateRule(ForeignKey.CascadeRule.valueOfDatabaseMetaData(updateRule));
            builder.deleteRule(ForeignKey.CascadeRule.valueOfDatabaseMetaData(deleteRule));
          }
          builder.addFieldNames(keySeq,
                                fkColumnName,
                                pkColumnName);
        }
      }
    }
    boolean drop = builder != null;
    if (builder != null) {
      ForeignKey foundKey = builder.build();
      if (foundKey.equals(key)) {
        return false;
      }
    }
    if (drop) {
      stmt.execute(key.getDropSQL());
    }
    stmt.execute(key.getCreateSQL());
    return true;
  }

  public int getStepping(final Connection conn) throws SQLException
  {
    return getStepping(conn,
                       getClass().getName() + ".stepping");
  }

  @SuppressWarnings("UseSpecificCatch")
  public static int getStepping(final Connection conn,
                                final String key) throws SQLException
  {
    try (PreparedStatement stmt = conn.prepareStatement("select val from config where name=?")) {
      stmt.setString(1,
                     key);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          String tmp = rs.getString("val");
          if (tmp != null) {
            try {
              return Integer.parseInt(tmp);
            } catch (Throwable th) {

            }
          }
        }
      }
    }
    return 0;
  }

  public void setStepping(final Connection conn,
                          int stepping) throws SQLException
  {
    setStepping(conn,
                getClass().getName() + ".stepping",
                stepping);
  }

  public static void setStepping(final Connection conn,
                                 final String key,
                                 final int stepping) throws SQLException
  {
    try (PreparedStatement stmt = conn.prepareStatement(
            "update or insert into config (name,val) values (?,?) matching (name)")) {
      stmt.setString(1,
                     key);
      stmt.setString(2,
                     Integer.toString(stepping));
      stmt.executeUpdate();
    }
  }

}
