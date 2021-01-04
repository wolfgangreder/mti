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

import at.or.reder.mti.model.api.StoreException;
import at.or.reder.mti.model.utils.Localizable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 *
 * @author Wolfgang Reder
 */
public final class LocalizableStore extends AbstractStore
{

  private final FBStores store;

  public LocalizableStore(FBStores store)
  {
    this.store = store;
  }

  void startup(Connection conn,
               StartupPhase phase) throws SQLException, StoreException
  {
    switch (phase) {
      case CREATE:
        startupCreate(conn);
        break;
    }
  }

  void startupCreate(Connection conn) throws SQLException, StoreException
  {
    int stepping = getStepping(conn);
    if (stepping < 1) {
      try (Statement stmt = conn.createStatement()) {
        DatabaseMetaData meta = conn.getMetaData();
        checkTable(meta,
                   stmt,
                   "localisations",
                   "create table localisations (\n"
                   + "id char(36) not null,\n"
                   + "key varchar(255) not null,\n"
                   + "lang char(3) not null,\n"
                   + "val blob sub_type text,\n"
                   + "constraint pk_localisations primary key(id,key,lang))");
        IndexInfo ndx = getIndexInfo(meta,
                                     "localisations",
                                     "ndx_loc_id_key");
        if (ndx == null) {
          stmt.executeUpdate("create index ndx_loc_id_key on localisations(id,key)");
        }
      }
    }
  }

  public void loadLocalizable(Connection conn,
                              String key,
                              UUID id,
                              BiConsumer<String, String> consumer) throws SQLException, StoreException
  {
    loadLocalizable(conn,
                    key,
                    Collections.singletonMap(id,
                                             consumer));
  }

  public void loadLocalizable(Connection conn,
                              String key,
                              Map<UUID, BiConsumer<String, String>> locToLoad) throws SQLException, StoreException
  {
    try (PreparedStatement stmt = conn.prepareStatement("select lang,val from localisations where id=? and key=?")) {
      stmt.setString(2,
                     key);
      for (Map.Entry<UUID, BiConsumer<String, String>> e : locToLoad.entrySet()) {
        stmt.setString(1,
                       e.getKey().toString());
        try (ResultSet rs = stmt.executeQuery()) {
          while (rs.next()) {
            String lang = rs.getString("lang");
            String val = rs.getString("val");
            e.getValue().accept(lang,
                                val);
          }
        }
      }
    }
  }

  public void storeLocalizables(Connection conn,
                                String key,
                                Map<UUID, Localizable> locToStore) throws SQLException, StoreException
  {
    try (PreparedStatement stmt = conn.prepareStatement("update or insert into localisations\n"
                                                        + "(key,id,lang,val)values(?,?,?,?) matching (id,key,lang)")) {
      stmt.setString(1,
                     key);
      for (Map.Entry<UUID, Localizable> e : locToStore.entrySet()) {
        stmt.setString(2,
                       e.getKey().toString());
        for (Map.Entry<String, String> v : e.getValue().getValues().entrySet()) {
          if (v.getKey() != null && !v.getKey().isBlank()) {
            stmt.setString(3,
                           v.getKey());
            stmt.setString(4,
                           v.getValue());
            stmt.executeUpdate();
          }
        }
      }
    }
  }

}
