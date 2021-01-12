/*
 * Copyright 2020 Wolfgang Reder.
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

import at.or.reder.mti.model.Epoch;
import at.or.reder.mti.model.api.EpochStore;
import at.or.reder.mti.model.api.Factories;
import at.or.reder.mti.model.api.StoreException;
import at.or.reder.mti.model.utils.MTIUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.netbeans.api.annotations.common.NonNull;

public final class FBEpochStore extends AbstractStore implements EpochStore, FBStore
{

  private final FBStores store;
  private final LocalizableStore localizations;
  private final Map<UUID, Epoch> epochCache = new ConcurrentHashMap<>();

  public FBEpochStore(FBStores store,
                      LocalizableStore loc)
  {
    this.store = store;
    this.localizations = loc;
  }

  private void startupCreate(Connection conn) throws SQLException, StoreException
  {
    int stepping = getStepping(conn);
    if (stepping < 1) {
      DatabaseMetaData meta = conn.getMetaData();
      try (Statement stmt = conn.createStatement()) {
        checkTable(meta,
                   stmt,
                   "epoch",
                   "create table epoch(\n"
                   + "id char(36) not null,\n"
                   + "name varchar(" + MTIUtils.MAX_NAME_LENGTH + ") not null,\n"
                   + "yearfrom int not null,\n"
                   + "yearto int ,\n"
                   + "comment blob sub_type text,\n"
                   + "constraint pk_epoch primary key(id))");
        checkTable(meta,
                   stmt,
                   "epoch_country",
                   "create table epoch_country(\n"
                   + "epoch char(36) not null,\n"
                   + "country char(3) not null,\n"
                   + "dirty smallint default 0 not null,\n"
                   + "constraint pk_epoch_country primary key(epoch,country))");
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
                                constraintName("fk_epoch_country_epoch").
                                rules(ForeignKey.CascadeRule.CASCADE).
                                pkTableName("epoch").
                                tableName("epoch_country").build());
        String source = getTriggerSource(conn,
                                         "epoch",
                                         "trg_epoch_loc_u");
        if (source == null) {
          stmt.execute("create trigger trg_epoch_loc_u\n"
                       + "after update on epoch\n"
                       + "as\n"
                       + "begin\n"
                       + "  update localisations set id=new.id where id=old.id;\n"
                       + "end");
        }
        source = getTriggerSource(conn,
                                  "epoch",
                                  "trg_epoch_loc_d");
        if (source == null) {
          stmt.execute("create trigger trg_epoch_loc_d\n"
                       + "after update on epoch\n"
                       + "as\n"
                       + "begin\n"
                       + "  delete from localisations where id=old.id;\n"
                       + "end");
        }
      }
    }
  }

  private void startupFill(Connection conn) throws SQLException, StoreException
  {
    int stepping = getStepping(conn);
    if (stepping < 1) {
      try {
        createDefaultValues(conn);
      } catch (IOException ex) {
        throw new StoreException(ex);
      }
    }
  }

  private void startupActivate(Connection conn) throws SQLException, StoreException
  {
    setStepping(conn,
                1);
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
      case FILL:
        startupFill(conn);
        break;
      case ACTIVATE:
        startupActivate(conn);
        break;
    }
  }

  private void createDefaultValues(Connection conn) throws SQLException, StoreException, IOException
  {
    List<Epoch> defaultValues = Factories.getEpochBuilderFactory().getDefaultValues();
    for (Epoch e : defaultValues) {
      store(conn,
            e);
    }
  }

  private int get(@NonNull Connection conn,
                  Collection<UUID> ids,
                  Consumer<Epoch> consumer) throws SQLException, StoreException
  {
    if (ids == null || ids.isEmpty()) {
      return 0;
    }
    Map<UUID, Epoch.Builder> builderMap = new HashMap<>();
    Epoch.BuilderFactory builderFactory = Factories.getEpochBuilderFactory();
    if (builderFactory == null) {
      throw new IllegalStateException("Cannot find Epoch.BuilderFactory");
    }
    int result = 0;
    try (PreparedStatement stmt = conn.prepareStatement("select name,yearfrom,yearto,comment from epoch where id=?")) {
      for (UUID id : ids) {
        if (id != null) {
          Epoch tmpEpoch = epochCache.get(ids);
          if (tmpEpoch != null) {
            if (consumer != null) {
              consumer.accept(tmpEpoch);
            }
            ++result;
          }
          stmt.setString(1,
                         id.toString());
          try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
              Epoch.Builder builder = builderFactory.createEpochBuilder();
              builder.id(id);
              builder.comment("",
                              rs.getString("comment"));
              builder.name("",
                           rs.getString("name"));
              builder.yearFrom(rs.getInt("yearfrom"));
              int tmp = rs.getInt("yearto");
              if (!rs.wasNull()) {
                builder.yearTo(tmp);
              } else {
                builder.yearTo(null);
              }
              builderMap.put(id,
                             builder);
            }
          }
        }
      }
    }
    if (!builderMap.isEmpty()) {
      try (PreparedStatement stmt = conn.prepareStatement("select country from epoch_country where epoch=?")) {
        for (Map.Entry<UUID, Epoch.Builder> e : builderMap.entrySet()) {
          stmt.setString(1,
                         e.getKey().toString());
          Epoch.Builder builder = e.getValue();
          try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
              String country = rs.getString("country");
              if (country != null && !country.isBlank()) {
                builder.addCountry(country);
              }
            }
          }
        }
      }
      for (Map.Entry<UUID, Epoch.Builder> e : builderMap.entrySet()) {
        Epoch.Builder builder = e.getValue();
        localizations.loadLocalizable(conn,
                                      "name",
                                      e.getKey(),
                                      e.getValue()::name);
        localizations.loadLocalizable(conn,
                                      "comment",
                                      e.getKey(),
                                      e.getValue()::name);
        Epoch epoch = builder.build();
        ++result;
        epochCache.put(e.getKey(),
                       epoch);
        if (consumer != null) {
          consumer.accept(epoch);
        }
      }
    }
    return result;
  }

  @Override
  public Epoch getEpoch(UUID id) throws StoreException
  {
    if (id == null) {
      return null;
    }
    AtomicReference<Epoch> result = new AtomicReference<>();
    try (Connection conn = store.getConnection()) {
      get(conn,
          Collections.singleton(id),
          result::set);
    } catch (SQLException ex) {
      throw new StoreException(ex);
    }
    return result.get();
  }

  @Override
  public List<Epoch> getAll() throws StoreException
  {
    List<Epoch> result = new ArrayList<>();
    try (Connection conn = store.getConnection();
            PreparedStatement stmt = conn.prepareStatement("select id from epoch")) {
      Set<UUID> ids = new HashSet<>();
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          ids.add(getUUID(rs,
                          "id"));
        }
      }
      get(conn,
          ids,
          result::add);
    } catch (SQLException ex) {
      throw new StoreException(ex);
    }
    return result;
  }

  @Override
  public void delete(Epoch epoch) throws StoreException
  {
    if (epoch == null) {
      return;
    }
    try (Connection conn = store.getConnection();
            PreparedStatement stmt = conn.prepareStatement("delete from epoch where id=?")) {
      stmt.setString(1,
                     epoch.getId().toString());
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new StoreException(ex);
    } finally {
      epochCache.remove(epoch.getId());
    }
  }

  boolean store(Connection conn,
                Epoch epoch) throws SQLException, StoreException
  {
    int modified = 0;
    try (PreparedStatement stmt = conn.prepareStatement("update or insert into epoch\n"
                                                        + "(id,name,yearfrom,yearto,comment)\n"
                                                        + "values (?,?,?,?,?) matching(id)")) {
      stmt.setString(1,
                     epoch.getId().toString());
      stmt.setString(2,
                     epoch.getName().getValue(""));
      stmt.setInt(3,
                  epoch.getYearFrom());
      if (epoch.getYearTo() != null) {
        stmt.setInt(4,
                    epoch.getYearTo());
      } else {
        stmt.setNull(4,
                     Types.INTEGER);
      }
      stmt.setString(5,
                     epoch.getComment().getValue(""));
      modified = stmt.executeUpdate();
    }
    try (PreparedStatement stmt = conn.prepareStatement("update epoch_country set dirty=1 where epoch=?")) {
      stmt.setString(1,
                     epoch.getId().toString());
      stmt.executeUpdate();
    }
    try (PreparedStatement stmt = conn.prepareStatement("update or insert into epoch_country\n(epoch,country,dirty)\n"
                                                        + "values(?,?,0)matching(epoch,country)")) {
      stmt.setString(1,
                     epoch.getId().toString());
      for (String country : epoch.getCountries()) {
        stmt.setString(2,
                       country);
        stmt.executeUpdate();
      }
    }
    try (PreparedStatement stmt = conn.prepareStatement("delete from epoch_country where epoch=? and dirty!=0")) {
      stmt.setString(1,
                     epoch.getId().toString());
      stmt.executeUpdate();
    }
    localizations.storeLocalizables(conn,
                                    "name",
                                    Collections.singletonMap(epoch.getId(),
                                                             epoch.getName()));
    localizations.storeLocalizables(conn,
                                    "comment",
                                    Collections.singletonMap(epoch.getId(),
                                                             epoch.getComment()));
    epochCache.remove(epoch.getId());
    return modified == 1;
  }

  @Override
  public boolean store(Epoch epoch) throws StoreException
  {
    if (epoch == null) {
      return false;
    }
    try (Connection conn = store.getConnection()) {
      return store(conn,
                   epoch);
    } catch (SQLException ex) {
      throw new StoreException(ex);
    }
  }

}
