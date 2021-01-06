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

import at.or.reder.mti.model.Gauge;
import at.or.reder.mti.model.api.Factories;
import at.or.reder.mti.model.api.GaugeStore;
import at.or.reder.mti.model.api.StoreException;
import at.or.reder.mti.model.utils.MTIUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Wolfgang Reder
 */
final class FBGaugeStore extends AbstractStore implements GaugeStore
{

  private final FBStores stores;

  FBGaugeStore(FBStores stores)
  {
    this.stores = stores;
  }

  private void startupCreate(Connection conn) throws SQLException
  {
    int stepping = getStepping(conn);
    if (stepping < 1) {
      DatabaseMetaData meta = conn.getMetaData();
      try (Statement stmt = conn.createStatement()) {
        checkTable(meta,
                   stmt,
                   "gauge",
                   "create table gauge(\n"
                   + "id char(36) not null,\n"
                   + "name varchar(" + MTIUtils.MAX_NAME_LENGTH + ") not null,\n"
                   + "scale double precision not null,\n"
                   + "track double precision not null,\n"
                   + "constraint pk_gauge primary key(id),\n"
                   + "constraint ck_gauge_scale check(scale>0))");
      }
    }
  }

  private void startupFill(Connection conn) throws SQLException, StoreException
  {
    Gauge.BuilderFactory factory = Factories.getGaugeBuilderFactory();
    try {
      store(conn,
            factory.getDefaultGauges());
    } catch (IOException ex) {
      throw new StoreException(ex);
    }
  }

  void startup(Connection conn,
               StartupPhase phase) throws SQLException, StoreException
  {
    switch (phase) {
      case CREATE:
        startupCreate(conn);
        break;
      case FILL:
        startupFill(conn);
        break;
    }
  }

  @Override
  public Gauge get(UUID id) throws StoreException
  {
    if (id == null) {
      return null;
    }
    try (Connection conn = stores.getConnection();
            PreparedStatement stmt = conn.prepareStatement("select name,scale,track from gauge where id=?")) {
      Gauge.BuilderFactory factory = Factories.getGaugeBuilderFactory();
      stmt.setString(1,
                     id.toString());
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          Gauge.Builder builder = factory.createGaugeBuilder();
          builder.id(id);
          builder.name(rs.getString("name"));
          builder.trackWidth(rs.getDouble("track"));
          builder.scale(rs.getDouble("scale"));
          return builder.build();
        }
      }
    } catch (SQLException ex) {
      throw new StoreException(ex);
    }
    return null;
  }

  @Override
  public List<Gauge> getAll() throws StoreException
  {
    List<Gauge> result = new ArrayList<>();
    try (Connection conn = stores.getConnection();
            PreparedStatement stmt = conn.prepareStatement("select id,name,scale,track from gauge where id=?")) {
      Gauge.BuilderFactory factory = Factories.getGaugeBuilderFactory();
      try (ResultSet rs = stmt.executeQuery()) {
        Gauge.Builder builder = factory.createGaugeBuilder();
        builder.id(getUUID(rs,
                           "id"));
        builder.name(rs.getString("name"));
        builder.trackWidth(rs.getDouble("track"));
        builder.scale(rs.getDouble("scale"));
        result.add(builder.build());
      }
    } catch (SQLException ex) {
      throw new StoreException(ex);
    }
    return result;
  }

  void store(Connection conn,
             Collection<? extends Gauge> gauge) throws SQLException, StoreException
  {
    try (PreparedStatement stmt = conn.prepareStatement("update or insert into gauge\n"
                                                        + "(id,name,scale,track)\n"
                                                        + "values (?,?,?,?) matching (id)")) {
      for (Gauge g : gauge) {
        if (g != null) {
          stmt.setString(1,
                         g.getId().toString());
          stmt.setString(2,
                         g.getName());
          stmt.setDouble(3,
                         g.getScale());
          stmt.setDouble(4,
                         g.getTrackWidth());
          stmt.addBatch();
        }
      }
      stmt.executeBatch();
    }
  }

  @Override
  public void store(Gauge gauge) throws StoreException
  {
    try (Connection conn = stores.getConnection()) {
      store(conn,
            Collections.singleton(gauge));
    } catch (SQLException ex) {
      throw new StoreException(ex);
    }
  }

  @Override
  public void delete(Gauge gauge) throws StoreException
  {
    if (gauge == null) {
      return;
    }
    try (Connection conn = stores.getConnection();
            PreparedStatement stmt = conn.prepareStatement("delete from gauge where id=?")) {
      stmt.setString(1,
                     gauge.getId().toString());
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new StoreException(ex);
    }
  }

}
