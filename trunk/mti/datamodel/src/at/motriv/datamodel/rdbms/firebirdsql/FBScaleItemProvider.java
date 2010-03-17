/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.firebirdsql;

import at.motriv.datamodel.entities.scale.Scale;
import at.motriv.datamodel.entities.scale.ScaleBuilder;
import at.motriv.datamodel.entities.scale.ScaleItemProvider;
import at.motriv.datamodel.entities.scale.impl.ScaleCache;
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
public class FBScaleItemProvider extends AbstractMotrivFBItemProvider<UUID, Scale> implements ScaleItemProvider
{

  private static class MyInitializer
  {

    private static final FBScaleItemProvider instance = new FBScaleItemProvider();
  }

  public static FBScaleItemProvider getInstance()
  {
    return MyInitializer.instance;
  }

  private FBScaleItemProvider()
  {
  }

  @Override
  public Scale get(UUID pKey) throws DataProviderException
  {
    if (pKey != null) {
      Connection conn = null;
      try {
        conn = getConnection();
        List<? extends Scale> tmp = get(conn, Collections.singleton(pKey));
        return tmp.isEmpty() ? null : tmp.get(0);
      } catch (SQLException e) {
        throw new DataProviderException(e);
      } finally {
        JDBCUtilities.close(conn);
      }
    }
    return null;
  }

  private static class Getter implements Callable<Scale>
  {

    private final PreparedStatement stmt;
    private UUID id;

    public Getter(PreparedStatement stmt)
    {
      this.stmt = stmt;
    }

    @Override
    public Scale call() throws Exception
    {
      ResultSet rs = null;
      try {
        stmt.setString(1, id.toString());
        rs = stmt.executeQuery();
        if (rs.next()) {
          ScaleBuilder builder = new ScaleBuilder();
          builder.id(id);
          builder.name(rs.getString("name"));
          builder.scale(rs.getDouble("scalefactor"));
          builder.trackWidth(rs.getDouble("trackwidth"));
          builder.family(JDBCUtilities.getUUID(rs, "family"));
          return builder.build();
        }
        rs.close();
      } finally {
        JDBCUtilities.close(rs);
      }
      return null;
    }
  }

  List<? extends Scale> get(final Connection conn, Collection<? extends UUID> ids) throws SQLException, DataProviderException
  {
    PreparedStatement stmt = null;
    List<Scale> result = new ArrayList<Scale>(ids.size());
    try {
      stmt = conn.prepareStatement("select name,scalefactor,trackwidth,family from modelscale where id=?");
      Getter getter = new Getter(stmt);
      for (UUID id : ids) {
        getter.id = id;
        Scale tmp = ScaleCache.getInstance().get(id, getter);
        if (tmp != null) {
          result.add(tmp);
        }
      }
    } catch (SQLException e) {
      throw e;
    } catch (DataProviderException e) {
      throw e;
    } catch (Exception e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(stmt);
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
      stmt = conn.prepareStatement("delete from modelscale where id=?");
      stmt.setString(1, pKey.toString());
      stmt.executeUpdate();
      ScaleCache.getInstance().remove(pKey);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(stmt, conn);
    }
  }

  @Override
  public Scale store(Scale pItem) throws DataProviderException
  {
    Connection conn = null;
    try {
      conn = getConnection();
      store(conn, pItem, false);
      return get(conn, Collections.singleton(pItem.getId())).get(0);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(conn);
    }
  }

  Scale store(final Connection conn, Scale pItem, boolean noUpdate) throws SQLException
  {
    PreparedStatement stmt = null;
    try {
      if (keyExists(conn, pItem.getId())) {
        if (noUpdate) {
          stmt = null;
        } else {
          stmt = conn.prepareStatement("update modelscale set name=?,scalefactor=?,trackwidth=?,family=? where id=?");
        }
      } else {
        stmt = conn.prepareStatement("insert into modelscale (name,scalefactor,trackwidth,family,id) values (?,?,?,?,?)");
      }
      if (stmt != null) {
        stmt.setString(1, pItem.getName());
        stmt.setDouble(2, pItem.getScale());
        stmt.setDouble(3, pItem.getTrackWidth());
        stmt.setString(4, pItem.getFamily().toString());
        stmt.setString(5, pItem.getId().toString());
        stmt.executeUpdate();
        ScaleCache.getInstance().remove(pItem.getId());
        return pItem;
      }
    } finally {
      JDBCUtilities.close(stmt);
    }
    return null;
  }

  boolean keyExists(final Connection conn, UUID key) throws SQLException
  {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.prepareStatement("select id from modelscale where id=?");
      stmt.setString(1, key.toString());
      rs = stmt.executeQuery();
      return rs.next();
    } finally {
      JDBCUtilities.close(rs, stmt);
    }
  }

  @Override
  public List<? extends Scale> getAll() throws DataProviderException
  {
    Set<UUID> tmp = Collections.emptySet();
    return getAll(tmp);
  }

  @Override
  public List<? extends Scale> getAll(Set<? extends UUID> scales2Ignore) throws DataProviderException
  {
    HashSet<UUID> ids = new HashSet<UUID>();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = getConnection();
      stmt = conn.prepareStatement("select id from modelscale");
      rs = stmt.executeQuery();
      while (rs.next()) {
        UUID id = JDBCUtilities.getUUID(rs, "id");
        if (!scales2Ignore.contains(id)) {
          ids.add(id);
        }
      }
      return get(conn, ids);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(rs, stmt, conn);
    }
  }

  @Override
  public List<? extends Scale> getByFamily(UUID family) throws DataProviderException
  {
    HashSet<UUID> ids = new HashSet<UUID>();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = getConnection();
      stmt = conn.prepareStatement("select id from modelscale where family=?");
      stmt.setString(1, family.toString());
      rs = stmt.executeQuery();
      while (rs.next()) {
        UUID id = JDBCUtilities.getUUID(rs, "id");
        ids.add(id);
      }
      return get(conn, ids);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(rs, stmt, conn);
    }
  }

  void checkScales(Connection conn, Collection<? extends Scale> defaultScales) throws SQLException
  {
    for (Scale s : defaultScales) {
      store(conn, s, true);
    }
  }
}
