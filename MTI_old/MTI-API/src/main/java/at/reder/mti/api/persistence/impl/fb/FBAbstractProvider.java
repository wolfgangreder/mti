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
import at.reder.mti.api.persistence.ProviderException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Wolfgang Reder
 */
@Messages({"FBAbstractProvider_pool_null=Es kann kein Verbindungspool gefunden werden.",
           "FBAbstractProvider_conn_null=Es kann keine Verbindung aufgebaut werden.",
           "# {0} - entityClassName",
           "FBAbstractProvider_factory_null=Es kann keiner Builderfactory für die Klasse {0} gefunden werden."})
public abstract class FBAbstractProvider<K, E> implements EntityProvider<K, E>
{

  private ConnectionPool pool;

  protected Connection getConnection() throws ProviderException
  {
    Connection result = null;
    ConnectionPool myPool;
    synchronized (this) {
      if (pool == null) {
        pool = Lookups.forPath("mti").lookup(ConnectionPool.class);
      }
      myPool = pool;
    }
    try {
      result = myPool.getConnection();
      if (result == null) {
        throw new ProviderException(Bundle.FBAbstractProvider_conn_null());
      }
    } catch (SQLException ex) {
      throw new ProviderException(ex);
    } catch (NullPointerException ex) {
      throw new ProviderException(Bundle.FBAbstractProvider_pool_null());
    }
    return result;
  }

  @Override
  public E get(K key) throws ProviderException
  {
    if (key != null) {
      List<E> tmp = get(Collections.singleton(key));
      if (tmp.isEmpty()) {
        return null;
      } else {
        return tmp.get(0);
      }
    }
    return null;
  }

  @Override
  public E store(E entity) throws ProviderException
  {
    if (entity != null) {
      List<E> tmp = store(Collections.singleton(entity));
      if (!tmp.isEmpty()) {
        return tmp.get(0);
      }
    }
    return null;
  }

  @Override
  public void delete(K key) throws ProviderException
  {
    if (key != null) {
      delete(Collections.singleton(key));
    }
  }

  @Override
  public Lookup getLookup()
  {
    return Lookup.EMPTY;
  }

  protected int getStepping(final Connection conn, UUID provider) throws SQLException
  {
    try (PreparedStatement stmt = conn.prepareStatement("select stepping from mti$stepping where provider=char_to_uuid(?)")) {
      stmt.setString(1, provider.toString());
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          return rs.getInt(1);
        }
      }
    }
    return 0;
  }

  protected void setStepping(final Connection conn, UUID provider, int stepping) throws SQLException
  {
    boolean insert;
    try (PreparedStatement stmt = conn.prepareStatement("select stepping from mti$stepping where provider=char_to_uuid(?)")) {
      stmt.setString(1, provider.toString());
      try (ResultSet rs = stmt.executeQuery()) {
        insert = !rs.next();
      }
    }
    if (insert) {
      try (PreparedStatement stmt = conn.prepareStatement(
              "insert into mti$stepping (provider,stepping) values (char_to_uuid(?),?)")) {
        stmt.setString(1, provider.toString());
        stmt.setInt(2, stepping);
        stmt.executeUpdate();
      }
    } else {
      try (PreparedStatement stmt = conn.prepareStatement("update mti$stepping set stepping=? where provider=char_to_uuid(?)")) {
        stmt.setInt(1, stepping);
        stmt.setString(2, provider.toString());
        stmt.executeUpdate();
      }
    }
  }

  public static java.sql.Timestamp date2SQLTimestamp(Instant d) throws DateTimeException,
                                                                       UnsupportedTemporalTypeException
  {
    if (d != null) {
      return new Timestamp(d.getLong(ChronoField.INSTANT_SECONDS));
    }
    return null;
  }

  public static Instant sqlTimestamp2Timestamp(java.sql.Timestamp ts)
  {
    if (ts != null) {
      return Instant.ofEpochSecond(ts.getTime());
    }
    return null;
  }

}
