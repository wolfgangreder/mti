/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.persistence.impl;

import at.reder.mti.api.persistence.ConnectionPool;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wolfgang Reder
 */
@ServiceProvider(service = ConnectionPool.class, path = "mti")
public class DefaultConnectionPool implements ConnectionPool
{

  private final DataSource ds;

  public DefaultConnectionPool()
  {
    ComboPooledDataSource cpds = new ComboPooledDataSource();
    try {
      cpds.setDriverClass("org.firebirdsql.jdbc.FBDriver");
    } catch (PropertyVetoException ex) {
      Exceptions.printStackTrace(ex);
      ds = null;
      return;
    }
    cpds.setJdbcUrl("jdbc:firebirdsql://localhost/mti?lc_ctype=utf-8");
    cpds.setUser("sysdba");
    cpds.setPassword("masterkey");
    cpds.setMinPoolSize(1);
    cpds.setMaxPoolSize(50);
    cpds.setMaxIdleTime(300);
    cpds.setAutoCommitOnClose(true);
    cpds.setMaxStatements(500);
    ds = cpds;
  }

  @Override
  public Connection getConnection() throws SQLException
  {
    return ds != null ? ds.getConnection() : null;
  }

}
