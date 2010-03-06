/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.firebirdsql;

import at.motriv.datamodel.Era;
import at.motriv.datamodel.EraBuilder;
import at.motriv.datamodel.provider.EraItemProvider;
import at.motriv.datamodel.rdbms.EraCache;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.jdbc.JDBCUtilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 *
 * @author wolfi
 */
public class FBEraItemProvider extends AbstractMotrivFBItemProvider<UUID, Era> implements EraItemProvider
{

  private static class MyInitrializer
  {

    private static final FBEraItemProvider instance = new FBEraItemProvider();
  }

  public static FBEraItemProvider getInstance()
  {
    return MyInitrializer.instance;
  }

  private FBEraItemProvider()
  {
  }

  @Override
  public Era get(UUID pKey) throws DataProviderException
  {
    Connection conn = null;
    try {
      conn = getConnection();
      List<? extends Era> tmp = get(conn, Collections.singleton(pKey));
      return tmp.isEmpty() ? null : tmp.get(0);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(conn);
    }
  }

  @Override
  public List<? extends Era> getAll() throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = getConnection();
      stmt = conn.prepareStatement("select id from era");
      rs = stmt.executeQuery();
      Set<UUID> ids = new HashSet<UUID>();
      while (rs.next()) {
        ids.add(JDBCUtilities.getUUID(rs, "id"));
      }
      return get(conn, ids);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(rs, stmt, conn);
    }
  }

  private static class Getter implements Callable<Era>
  {

    private UUID id;
    private PreparedStatement stmt;

    public Getter(PreparedStatement stmt)
    {
      this.stmt = null;
    }

    @Override
    public Era call() throws Exception
    {
      stmt.setString(1, id.toString());
      ResultSet rs = stmt.executeQuery();
      EraBuilder builder = new EraBuilder();
      if (rs.next()) {
        builder.setId(id);
        builder.setName(rs.getString("name"));
        builder.setYearFrom(rs.getInt("yearfrom"));
        int yearTo = rs.getInt("yearto");
        builder.setYearTo(rs.wasNull() ? null : yearTo);
        return builder.build();
      }
      return null;
    }
  }

  private List<? extends Era> get(Connection conn, Collection<? extends UUID> keys) throws SQLException, DataProviderException
  {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List<Era> result = new ArrayList<Era>(keys.size());
    if (!keys.isEmpty()) {
      try {
        stmt = conn.prepareStatement("select name,yearfrom,yearto from era where id=?");
        Getter getter = new Getter(stmt);
        for (UUID key : keys) {
          try {
            getter.id = key;
            Era item = EraCache.getInstance().get(key, getter);
            if (item != null) {
              result.add(item);
            }
          } catch (Exception e) {
            if (e instanceof SQLException) {
              throw (SQLException) e;
            } else {
              throw new DataProviderException(e);
            }
          }
        }
      } finally {
        JDBCUtilities.close(rs, stmt);
      }
    }
    return result;
  }

  @Override
  public void delete(UUID pKey) throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getConnection();
      stmt = conn.prepareStatement("delete from era where id=?");
      stmt.setString(1, pKey.toString());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(stmt, conn);
      EraCache.getInstance().remove(pKey);
    }
  }

  @Override
  public Era store(Era pItem) throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Era result = null;
    try {
      conn = getConnection();
      if (keyExists(conn, pItem.getId())) {
        stmt = conn.prepareStatement("update era set name=?,yearfrom=?,yearto=? where id=?");
      } else {
        stmt = conn.prepareStatement("insert into era (name,yearfrom,yearto,id) values (?,?,?,?)");
      }
      stmt.setString(1, pItem.getName());
      stmt.setInt(2, pItem.getYearFrom());
      Integer yt = pItem.getYearTo();
      if (yt != null) {
        stmt.setInt(3, pItem.getYearTo());
      }
      stmt.setString(4, pItem.getId().toString());
      stmt.executeUpdate();
      EraCache.getInstance().store(pItem);
      result = pItem;
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(stmt, conn);
      if (result!=null) {
        EraCache.getInstance().store(result);
      } else {
        EraCache.getInstance().remove(pItem.getId());
      }
    }
    return result;
  }

  private boolean keyExists(Connection conn, UUID key) throws SQLException
  {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.prepareStatement("select id from era where id=?");
      stmt.setString(1, key.toString());
      rs = stmt.executeQuery();
      return rs.next();
    } finally {
      JDBCUtilities.close(rs, stmt);
    }
  }
}
