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

import at.or.reder.mti.model.api.StoreException;
import at.or.reder.mti.model.api.StoreProvider;
import at.or.reder.mti.model.api.Stores;
import at.or.reder.osplatform.PlatformFolders;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.firebirdsql.gds.impl.jni.EmbeddedGDSFactoryPlugin;
import org.firebirdsql.management.FBManager;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = StoreProvider.class)
@Messages({"FBEmbStoreProvider_name=FirebirdSQL Embedded Store",
           "FBEmbStoreProvider_err_nodefault_dir=Es kann das Datenverzeichnis nicht automatisch ermittelt werden.",
           "# {0} - dirName",
           "# {1} - errMsg",
           "FBEmbStoreProvider_err_cannot_create_datadir=Es kann das Datenverzeichnis \"{0}\" nicht erzeugt werden:{2}",
           "# {0} - dirName",
           "FBEmbStoreProver_err_cannot_write_datadir=Das datenverzeichis \"{0}\" ist nicht schreibbar."})
public class FBEmbStoreProvider implements StoreProvider
{

  public static final UUID ID = UUID.fromString("1906c1d3-560f-4bb3-b386-b65a230fd7ad");
  private String url;

  @Override
  public UUID getId()
  {
    return ID;
  }

  @Override
  public String getName()
  {
    return Bundle.FBEmbStoreProvider_name();
  }

  @Override
  public Stores openStores(Map<String, String> params) throws StoreException
  {
    try {
      Properties props = new Properties();
      if (params.get("username") != null) {
        props.put("user",
                  params.get("username"));
      }
      if (params.get("password") != null) {
        props.put("password",
                  params.get("password"));
      }
      props.put("charSet",
                "UTF8");
      String dbq = createDatabase(params.get("fbstore.datadir"),
                                  props);
      url = "jdbc:firebirdsql:embedded:" + dbq;
      ConnectionFactory connFactory = new DriverManagerConnectionFactory(url,
                                                                         props);
      PooledObjectFactory factory = new PoolableConnectionFactory(connFactory,
                                                                  ObjectName.getInstance("at.or.reder.mti",
                                                                                         "FBConnectionPool",
                                                                                         dbq));
      GenericObjectPoolConfig cfg = new GenericObjectPoolConfig();
      ObjectPool pool = new GenericObjectPool(factory,
                                              cfg);
      PoolingDataSource ds = new PoolingDataSource(pool);
      FBStores result = new FBStores(ds);
      result.startup();
      return result;
    } catch (MalformedObjectNameException | SQLException ex) {
      throw new StoreException(ex);
    }
  }

  private String createDatabase(String dataDir,
                                Properties props) throws SQLException, StoreException
  {
    Path dataPath = null;
    try {
      if (dataDir == null) {
        if (!PlatformFolders.isPlatformFoldersSupported()) {
          throw new StoreException(Bundle.FBEmbStoreProvider_err_nodefault_dir());
        }
        PlatformFolders folders = PlatformFolders.getInstance();
        dataPath = Paths.get(folders.getDokumentsFolder().toRealPath().toString(),
                             "mtidata");
      } else {
        dataPath = Paths.get(dataDir).toRealPath();
      }
      Files.createDirectories(dataPath);
      if (!Files.isWritable(dataPath)) {
        throw new StoreException(Bundle.FBEmbStoreProver_err_cannot_write_datadir(dataPath));
      }
      dataPath = Paths.get(dataPath.toString(),
                           "database");
      Files.createDirectories(dataPath);
    } catch (IOException ex) {
      throw new StoreException(Bundle.FBEmbStoreProvider_err_cannot_create_datadir(dataPath.toString(),
                                                                                   ex.getLocalizedMessage()),
                               ex);
    }
    try {
      FBManager fbManager = new FBManager(EmbeddedGDSFactoryPlugin.EMBEDDED_TYPE_NAME);
      try {
        Path dbq = Paths.get(dataPath.toString(),
                             "mti.firebird");
        fbManager.start();
        if (!fbManager.isDatabaseExists(dbq.toString(),
                                        props.getProperty("user"),
                                        props.getProperty("password"))) {
          fbManager.setDialect(3);
          fbManager.setDefaultCharacterSet("UTF8");
          fbManager.setPageSize(16 * 1024);
          fbManager.createDatabase(dbq.toString(),
                                   props.getProperty("user"),
                                   props.getProperty("password"));
          try (Connection conn = DriverManager.getConnection("jdbc:firebirdsql:embedded:" + dbq,
                                                             props);
                  Statement stmt = conn.createStatement()) {
            stmt.execute("create table config (\n"
                         + "name varchar(255) not null,\n"
                         + "val varchar(255),\n"
                         + "constraint pk_config primary key(name))");
          }
        }
        return dbq.toString();
      } finally {
        fbManager.stop();
      }
    } catch (Exception ex) {
      throw new StoreException(ex);
    }
  }

  @Override
  public Lookup getLookup()
  {
    return Lookup.EMPTY;
  }

}
