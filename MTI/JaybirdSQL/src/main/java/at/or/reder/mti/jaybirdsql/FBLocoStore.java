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

import at.or.reder.mti.model.Entity;
import at.or.reder.mti.model.Locomotive;
import at.or.reder.mti.model.QuickInfo;
import at.or.reder.mti.model.api.DefaultQuickInfo;
import at.or.reder.mti.model.api.LocoStore;
import at.or.reder.mti.model.api.StoreException;
import at.or.reder.mti.model.utils.MTIUtils;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 *
 * @author Wolfgang Reder
 */
public class FBLocoStore extends AbstractStore implements LocoStore, FBStore
{

  private final FBStores stores;

  public FBLocoStore(FBStores stores)
  {
    this.stores = stores;
  }

  @Override
  public List<Locomotive> getAll() throws StoreException
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public int getAllQuickInfos(Consumer<? super QuickInfo> consumer) throws StoreException
  {
    int result = 0;
    try (Connection conn = stores.getConnection();
            PreparedStatement stmt = conn.prepareStatement("select id,name,description from loco");
            ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        UUID id = getUUID(rs,
                          "id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        consumer.accept(new DefaultQuickInfo(id,
                                             null,
                                             name,
                                             description));
        ++result;
      }
      conn.commit();
    } catch (SQLException ex) {
      throw new StoreException(ex);
    }
    return result;
  }

  @Override
  public Locomotive get(UUID id) throws StoreException
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void store(Locomotive loco) throws StoreException
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void delete(Locomotive loco) throws StoreException
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void addEntity(Locomotive loco,
                        Entity entity,
                        Consumer<Locomotive> modifiedLoco) throws StoreException
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void removeEntities(Locomotive loco,
                             Collection<? extends Entity> entities,
                             Consumer<Locomotive> modifiedLoco) throws StoreException
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  private void startupCreate(Connection conn) throws SQLException, StoreException
  {
    int stepping = getStepping(conn);
    if (stepping < 1) {
      DatabaseMetaData meta = conn.getMetaData();
      try (Statement stmt = conn.createStatement()) {
        checkTable(meta,
                   stmt,
                   "loco",
                   "create table loco (\n"
                   + "id char(36) not null,\n"
                   + "name varchar(" + MTIUtils.MAX_NAME_LENGTH + ") not null,\n"
                   + "description blob sub_type text,\n"
                   + "price decimal(18,2),\n"
                   + "dop date,\n"
                   + "productNumber varchar(100),\n"
                   + "manufacturer char(36),\n"
                   + "retailer char(36),\n"
                   + "condition varchar(" + MTIUtils.MAX_ENUM_LENGTH + ") not null,\n"
                   + "lastModified timestamp not null,\n"
                   + "number varchar(255) not null,\n"
                   + "wheels varchar(50) not null,\n"
                   + "clazz varchar(255) not null,\n"
                   + "company varchar(255) not null,\n"
                   + "country varchar(255) not null,\n"
                   + "traction varchar(" + MTIUtils.MAX_ENUM_LENGTH + "),\n"
                   + "epoch char(36) not null,\n"
                   + "length double precision,\n"
                   + "width double precision,\n"
                   + "height double precision,\n"
                   + "weight double precision,\n"
                   + "address int,\n"
                   + "consistAddress int,\n"
                   + "decoder varchar(" + MTIUtils.MAX_NAME_LENGTH + "),\n"
                   + "gauge char(36) not null,\n"
                   + "defaultImage char(36),\n"
                   + "constraint pk_loco primary key(id))");
        checkTable(meta,
                   stmt,
                   "loco_entity",
                   "create table loco_entity (\n"
                   + "loco char(36) not null,\n"
                   + "entity char(36) not null,\n"
                   + "constraint pk_loco_entity primary key(loco,entity))");
      }
    }
  }

  private void startupLink(Connection conn) throws SQLException, StoreException
  {
    int stepping = getStepping(conn);
    if (stepping < 1) {
      DatabaseMetaData meta = conn.getMetaData();
      try (Statement stmt = conn.createStatement()) {
        checkForeignKey(meta,
                        stmt,
                        ForeignKey.builder().addFieldNames(0,
                                                           "epoch",
                                                           "id").
                                constraintName("fk_loco_epoch").
                                deleteRule(ForeignKey.CascadeRule.RESTRICT).
                                updateRule(ForeignKey.CascadeRule.CASCADE).
                                pkTableName("epoch").
                                tableName("loco").build());
        checkForeignKey(meta,
                        stmt,
                        ForeignKey.builder().addFieldNames(0,
                                                           "gauge",
                                                           "id").
                                constraintName("fk_loco_gauge").
                                deleteRule(ForeignKey.CascadeRule.RESTRICT).
                                updateRule(ForeignKey.CascadeRule.CASCADE).
                                pkTableName("gauge").
                                tableName("loco").build());
        checkForeignKey(meta,
                        stmt,
                        ForeignKey.builder().addFieldNames(0,
                                                           "defaultImage",
                                                           "id").
                                constraintName("fk_loco_defaultImage").
                                deleteRule(ForeignKey.CascadeRule.RESTRICT).
                                updateRule(ForeignKey.CascadeRule.CASCADE).
                                pkTableName("entity").
                                tableName("loco").build());
        checkForeignKey(meta,
                        stmt,
                        ForeignKey.builder().addFieldNames(0,
                                                           "loco",
                                                           "id").
                                constraintName("fk_loco_entity_loco").
                                rules(ForeignKey.CascadeRule.CASCADE).
                                pkTableName("loco").
                                tableName("loco_entity").build());
        checkForeignKey(meta,
                        stmt,
                        ForeignKey.builder().addFieldNames(0,
                                                           "entity",
                                                           "id").
                                constraintName("fk_loco_entity_entity").
                                rules(ForeignKey.CascadeRule.CASCADE).
                                pkTableName("entity").
                                tableName("loco_entity").build());
        if (false) {
          checkForeignKey(meta,
                          stmt,
                          ForeignKey.builder().addFieldNames(0,
                                                             "manufacturer",
                                                             "id").
                                  constraintName("fk_loco_manufacturer").
                                  deleteRule(ForeignKey.CascadeRule.RESTRICT).
                                  updateRule(ForeignKey.CascadeRule.CASCADE).
                                  pkTableName("contact").
                                  tableName("loco").build());
          checkForeignKey(meta,
                          stmt,
                          ForeignKey.builder().addFieldNames(0,
                                                             "retailer",
                                                             "id").
                                  constraintName("fk_loco_retailer").
                                  deleteRule(ForeignKey.CascadeRule.RESTRICT).
                                  updateRule(ForeignKey.CascadeRule.CASCADE).
                                  pkTableName("contact").
                                  tableName("loco").build());
        }
      }
    }
  }

  private void startupActivate(Connection conn) throws SQLException, StoreException
  {

  }

  @Override
  public void startup(Connection conn,
                      StartupPhase phase) throws SQLException, StoreException
  {
    switch (phase) {
      case CREATE:
        startupCreate(conn);
        break;
      case LINK:
        startupLink(conn);
        break;
      case ACTIVATE:
        startupActivate(conn);
        break;
    }
  }

}
