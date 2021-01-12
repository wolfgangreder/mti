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
import at.or.reder.mti.model.EntityKind;
import at.or.reder.mti.model.MTIConfig;
import at.or.reder.mti.model.api.EntityStore;
import at.or.reder.mti.model.api.Factories;
import at.or.reder.mti.model.api.StoreException;
import at.or.reder.mti.model.utils.MTIUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author Wolfgang Reder
 */
public final class FBEntityStore extends AbstractStore implements EntityStore, FBStore
{

  private final FBStores stores;
  private final LocalizableStore locStore;

  public FBEntityStore(FBStores stores,
                       LocalizableStore locStore)
  {
    this.stores = stores;
    this.locStore = locStore;
  }

  private void startupCreate(Connection conn) throws SQLException
  {
    int stepping = getStepping(conn);
    if (stepping < 1) {
      DatabaseMetaData meta = conn.getMetaData();
      try (Statement stmt = conn.createStatement()) {
        checkTable(meta,
                   stmt,
                   "entity",
                   "create table entity (\n"
                   + "id char(36) not null,\n"
                   + "kind varchar(" + MTIUtils.MAX_ENUM_LENGTH + ") not null,\n"
                   + "mime varchar(255) not null,\n"
                   + "filename varchar(255),\n"
                   + "size bigint default -1 not null,\n"
                   + "origin blob sub_type text,\n"
                   + "description blob sub_type text,\n"
                   + "constraint pk_entity primary key(id))");
        IndexInfo index = getIndexInfo(meta,
                                       "entity",
                                       "ndx_entity_kind");
        if (index == null) {
          stmt.execute("create index ndx_entity_kind on entity(kind)");
        }
        index = getIndexInfo(meta,
                             "entity",
                             "ndx_entity_mime");
        if (index == null) {
          stmt.execute("create index ndx_entity_mime on entity(mime)");
        }
      }
    }
  }

  private void startupLink(Connection conn) throws SQLException
  {
    int stepping = getStepping(conn);
    if (stepping < 1) {
      try (Statement stmt = conn.createStatement()) {
        String source = getTriggerSource(conn,
                                         "entity",
                                         "trg_entity_loc_u");
        if (source == null) {
          stmt.execute("create trigger trg_entity_loc_u\n"
                       + "after update on entity\n"
                       + "as\n"
                       + "begin\n"
                       + "  update localisations set id=new.id where id=old.id;\n"
                       + "end");
        }
        source = getTriggerSource(conn,
                                  "entity",
                                  "trg_entity_loc_d");
        if (source == null) {
          stmt.execute("create trigger trg_entity_loc_d\n"
                       + "after update on entity\n"
                       + "as\n"
                       + "begin\n"
                       + "  delete from localisations where id=old.id;\n"
                       + "end");
        }
      }
    }
  }

  private void startupActivate(Connection conn) throws SQLException
  {
    setStepping(conn,
                1);
  }

  @Override
  public void startup(Connection conn,
                      StartupPhase phase) throws StoreException, SQLException
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

  private static final String FIELDLIST = "id,kind,mime,filename,size,origin,description";

  private Path buildPath(Path mediaRoot,
                         UUID id,
                         String fileName,
                         EntityKind kind) throws IOException
  {
    String strId = id.toString();
    Path result = Path.of(mediaRoot.toString(),
                          kind.name().toLowerCase(),
                          strId.substring(0,
                                          2));
    Files.createDirectories(result);
    return Path.of(result.toString(),
                   URLEncoder.encode(fileName,
                                     StandardCharsets.UTF_8));
  }

  private Entity.Builder constructEntity(Entity.Builder builder,
                                         Path mediaRoot,
                                         ResultSet rs) throws SQLException, IOException
  {
    UUID id = getUUID(rs,
                      "id");
    builder.id(id);
    String fileName = rs.getString("filename");
    builder.fileName(fileName);
    EntityKind ek = getEnum(rs,
                            "kind",
                            EntityKind.OTHER);
    builder.kind(ek);
    Path resultPath = buildPath(mediaRoot,
                                id,
                                fileName,
                                ek);
    if (Files.isReadable(resultPath)) {
      builder.data(resultPath.toUri().toURL());
    } else {
      builder.data(null);
    }
    builder.description("",
                        rs.getString("description"));
    builder.mimeType(rs.getString("mime"));
    URL origin = null;
    try {
      origin = new URL(rs.getString("origin"));
    } catch (Throwable th) {
    }
    builder.origin(origin);
    builder.size(rs.getLong("size"));
    return builder;
  }

  private Path getMediaRoot()
  {
    return Lookup.getDefault().lookup(MTIConfig.class).getMediaRoot();
  }

  private int get(Connection conn,
                  Supplier<UUID> ids,
                  Consumer<Entity> consumer) throws SQLException, IOException, StoreException
  {
    int result = 0;
    try (PreparedStatement stmt = conn.prepareStatement("select " + FIELDLIST + " from entity where id=?")) {
      Entity.BuilderFactory factory = Factories.getEntityBuilderFactory();
      UUID id;
      Path mediaRoot = getMediaRoot();
      while ((id = ids.get()) != null) {
        stmt.setString(1,
                       id.toString());
        try (ResultSet rs = stmt.executeQuery()) {
          if (rs.next()) { // singleton Query!
            Entity.Builder builder = constructEntity(factory.createEntityBuilder(),
                                                     mediaRoot,
                                                     rs);
            locStore.loadLocalizable(conn,
                                     "description",
                                     id,
                                     builder::description);
            consumer.accept(builder.build());
            ++result;
          }
        }
      }
    }
    return result;
  }

  @Override
  public Entity getEntity(UUID id) throws StoreException
  {
    try (Connection conn = stores.getConnection()) {
      AtomicReference<Entity> result = new AtomicReference<>();
      get(conn,
          wrapSingleton(id),
          result::set);
      return result.get();
    } catch (IOException | SQLException ex) {
      throw new StoreException(ex);
    }
  }

  @Override
  public List<Entity> getEntities() throws StoreException
  {
    try (Connection conn = stores.getConnection();
            PreparedStatement stmt = conn.prepareStatement("select " + FIELDLIST + " from entity")) {
      Map<UUID, Entity.Builder> result = new HashMap<>();
      Map<UUID, BiConsumer<String, String>> descriptionMap = new HashMap<>();
      Path mediaRoot = getMediaRoot();
      Entity.BuilderFactory factory = Factories.getEntityBuilderFactory();
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          UUID id = getUUID(rs,
                            "id");
          Entity.Builder builder = constructEntity(factory.createEntityBuilder(),
                                                   mediaRoot,
                                                   rs);
          result.put(id,
                     builder);
          descriptionMap.put(id,
                             builder::description);
        }
      }
      locStore.loadLocalizable(conn,
                               "description",
                               descriptionMap);
      return result.values().stream().map(Entity.Builder::build).collect(Collectors.toList());
    } catch (IOException | SQLException ex) {
      throw new StoreException(ex);
    }
  }

  @Override
  public void storeEntity(Entity e) throws StoreException
  {
    if (e == null) {
      return;
    }
    Path mediaPath = null;
    try (Connection conn = stores.getConnection()) {
      try (PreparedStatement stmt = conn.prepareStatement("update or insert into entity\n"
                                                          + "(id,kind,mime,filename,size,origin,description)\n"
                                                          + "values (?,?,?,?,?,?,?) matching(?)")) {
        stmt.setString(1,
                       e.getId().toString());
        stmt.setString(2,
                       e.getKind().name());
        stmt.setString(3,
                       e.getMimeType());
        stmt.setString(4,
                       e.getFileName());
        stmt.setLong(5,
                     e.getSize());
        if (e.getOrigin() != null) {
          stmt.setString(6,
                         e.getOrigin().toString());
        } else {
          stmt.setNull(6,
                       Types.VARCHAR);
        }
        stmt.setString(1,
                       e.getDescription().getValue());
        stmt.executeUpdate();
        locStore.storeLocalizables(conn,
                                   "description",
                                   Collections.singletonMap(e.getId(),
                                                            e.getDescription()));
        mediaPath = storeMedia(e);
      } catch (IOException | SQLException ex) {
        if (mediaPath != null) {
          try {
            Files.deleteIfExists(mediaPath);
          } catch (IOException ex1) {
            Exceptions.printStackTrace(ex1);
          }
        }
        conn.rollback();
        throw new StoreException(ex);
      }
    } catch (SQLException ex) {
      throw new StoreException(ex);
    }
  }

  private Path storeMedia(Entity e) throws IOException
  {
    Path mediaPath = buildPath(getMediaRoot(),
                               e.getId(),
                               e.getFileName(),
                               e.getKind());
    try (InputStream is = e.getData();
            OutputStream out = Files.newOutputStream(mediaPath)) {
      is.transferTo(out);
    }
    return mediaPath;
  }

  @Override
  public void deleteEntity(Entity e) throws StoreException
  {
    if (e == null) {
      return;
    }
    try (Connection conn = stores.getConnection();
            PreparedStatement stmt = conn.prepareStatement("delete from entity where id=?")) {
      stmt.setString(1,
                     e.getId().toString());
      stmt.executeUpdate();
    } catch (SQLException ex) {
      throw new StoreException(ex);
    }
  }

}
