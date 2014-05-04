/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.persistence.impl.fb;

import at.reder.mti.api.persistence.ConnectionPool;
import at.reder.mti.api.persistence.EntityProvider;
import at.reder.mti.api.persistence.ProviderLookup;
import at.reder.mti.api.persistence.ProviderStartup;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wolfgang Reder
 */
@ServiceProvider(service = ProviderLookup.class)
@Messages({"# {0} - tableName",
           "FBProviderLookup_startup=Erzeuge Tabelle {0}"})
public class FBProviderLookup implements ProviderLookup
{

  private final Lookup providerLookup;
  private boolean started;

  public FBProviderLookup()
  {
    providerLookup = Lookups.forPath("firebirdsql");
  }

  @Override
  public <P> P lookupProvider(Class<? extends P> providerClass)
  {
    startProvider(null);
    return providerLookup.lookup(providerClass);
  }

  private void innerStartProvider()
  {
    startProvider(null);
  }

  @Override
  public void startProvider(ProgressHandle handle)
  {
    synchronized (this) {
      if (!started) {
        testDatabase(handle);
        providerLookup.lookupAll(EntityProvider.class).stream().
                map((ep) -> ep.getLookup().lookup(ProviderStartup.class)).
                filter((startup) -> (startup != null)).
                forEach((startup) -> {
                  startup.testDatabase(handle);
                });
        started = true;
      }
    }
  }

  private void testDatabase(ProgressHandle handle)
  {
    if (handle != null) {
      handle.progress(Bundle.FBProviderLookup_startup("mti$stepping"));
    }
    ConnectionPool pool = Lookups.forPath("mti").lookup(ConnectionPool.class);
    try (Connection conn = pool.getConnection()) {
      DatabaseMetaData meta = conn.getMetaData();
      try (ResultSet rs = meta.getTables(null, null, "mti$stepping", new String[]{"TABLE"})) {
        if (!rs.next()) {
          try (Statement stmt = conn.createStatement()) {
            stmt.execute("create domain uuid char(16) character set OCTETS");
            stmt.execute("create domain boolean integer default 0 not null");
            stmt.execute("create table mti$stepping (provider uuid not null,\n"
                                 + "stepping integer default 0 not null,\n"
                                 + "constraint pk_mti$stepping primary key(provider))");
          }
        }
      }
    } catch (SQLException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

}
