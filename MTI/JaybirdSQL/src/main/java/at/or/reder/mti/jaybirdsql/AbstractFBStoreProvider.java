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
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
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
import org.firebirdsql.gds.impl.GDSType;
import org.firebirdsql.management.FBManager;
import org.openide.util.Lookup;

public abstract class AbstractFBStoreProvider implements StoreProvider
{

  private String url;
  private final GDSType gdsType;

  protected AbstractFBStoreProvider(GDSType gdsType)
  {
    this.gdsType = gdsType;
  }

  @Override
  public final synchronized Stores openStores(Map<String, String> params) throws StoreException
  {
    try {
      Properties props = new Properties();
      if (params.get("user") != null) {
        props.put("user",
                  params.get("user"));
      }
      if (params.get("password") != null) {
        props.put("password",
                  params.get("password"));
      }
      props.put("charSet",
                "UTF8");
      String dbq = createDatabase(params,
                                  props);
      url = getURLPrefix() + dbq;
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

  protected final String getURLPrefix()
  {
    switch (gdsType.toString()) {
      case "PURE_JAVA":
        return "jdbc:firebirdsql:";
      case "NATIVE":
        return "jdbc:firebirdsql:native";
      case "EMBEDDED":
        return "jdbc:firebirdsql:embedded:";
    }
    throw new IllegalStateException("Unexpected GDSType " + gdsType.toString());
  }

  protected abstract String prepareCreateDatabase(Map<String, String> config,
                                                  Properties props) throws SQLException, StoreException, IOException;

  protected abstract String getHost(Map<String, String> config);

  protected abstract int getPort(Map<String, String> config);

  private final String createDatabase(Map<String, String> config,
                                      Properties props) throws SQLException, StoreException
  {
    try {
      String dbq = prepareCreateDatabase(config,
                                         props);
      String host = getHost(config);
      FBManager fbManager = new FBManager(gdsType);
      try {
        StringBuilder urlBuilder = new StringBuilder(getURLPrefix());
        if (host != null) {
          urlBuilder.append("//");
          urlBuilder.append(host);
          fbManager.setServer(host);
        }
        int port = getPort(config);
        if (port > 0) {
          urlBuilder.append(':');
          urlBuilder.append(Integer.toString(port));
          fbManager.setPort(port);
        }
        if (!dbq.startsWith("/")) {
          urlBuilder.append('/');
        }
        url = urlBuilder.append(dbq).toString();
        fbManager.setPassword(props.getProperty("password"));
        fbManager.setUserName(props.getProperty("user"));
        fbManager.start();
        if (!fbManager.isDatabaseExists(dbq.toString(),
                                        fbManager.getUserName(),
                                        fbManager.getPassword())) {
          fbManager.setDialect(3);
          fbManager.setDefaultCharacterSet("UTF8");
          fbManager.setPageSize(16 * 1024);
          fbManager.createDatabase(dbq.toString(),
                                   fbManager.getUserName(),
                                   fbManager.getPassword());

          try (Connection conn = DriverManager.getConnection(url,
                                                             props);
                  Statement stmt = conn.createStatement()) {
            stmt.execute("create table config (\n"
                         + "name varchar(255) not null,\n"
                         + "val varchar(255),\n"
                         + "constraint pk_config primary key(name))");
          }
        }
        return dbq;
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
