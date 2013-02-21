/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.firebirdsql;

import at.motriv.datamodel.externals.ExternalKind;
import at.motriv.datamodel.externals.ExternalRepository;
import at.motriv.datamodel.externals.ExternalRepositoryItemProvider;
import at.motriv.datamodel.externals.impl.DefaultExternalRepository;
import at.motriv.datamodel.rdbms.FSExternalRepositoryItemProvider;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.jdbc.JDBCUtilities;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.openide.util.Exceptions;

/**
 *
 * @author wolfi
 */
public class FBExternalRepositoryItemProvider extends AbstractMotrivFBItemProvider<UUID, ExternalRepository> implements
        ExternalRepositoryItemProvider
{

  private static final class MyInitializer
  {

    private static final FBExternalRepositoryItemProvider instance = new FBExternalRepositoryItemProvider();
  }

  public static FBExternalRepositoryItemProvider getInstance()
  {
    return MyInitializer.instance;
  }

  private FBExternalRepositoryItemProvider()
  {
  }

  private Set<ExternalKind> buildKinds(String s)
  {
    String[] split = s.split("\\|");
    Set<ExternalKind> result = new HashSet<ExternalKind>();
    for (String sp : split) {
      try {
        ExternalKind k = ExternalKind.valueOf(sp.trim());
        result.add(k);
      } catch (IllegalArgumentException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return result;
  }

  @Override
  public ExternalRepository get(UUID pKey) throws DataProviderException
  {
    if (pKey != null) {
      Connection conn = null;
      PreparedStatement stmt = null;
      ResultSet rs = null;
      try {
        conn = getConnection();
        stmt = conn.prepareStatement("select name,baseurl,kinds,readonly,immutable from externalrepository where id=?");
        stmt.setString(1, pKey.toString());
        rs = stmt.executeQuery();
        if (rs.next()) {
          Set<ExternalKind> kinds = buildKinds(rs.getString("kinds"));
          return new DefaultExternalRepository(pKey,
                                               rs.getString("name"),
                                               new URI(rs.getString("baseurl")),
                                               kinds,
                                               JDBCUtilities.getBoolean(rs, "readonly"),
                                               JDBCUtilities.getBoolean(rs, "immutable"),
                                               Collections.singletonMap((String) null, rs.getString("description")));
        } else {
          return FSExternalRepositoryItemProvider.getInstance().get(pKey);
        }
      } catch (URISyntaxException ex) {
        throw new DataProviderException(ex);
      } catch (SQLException ex) {
        throw new DataProviderException(ex);
      } finally {
        JDBCUtilities.close(rs, stmt, conn);
      }
    }
    return null;
  }

  @Override
  public List<? extends ExternalRepository> getAll() throws DataProviderException
  {
    List<ExternalRepository> result = new LinkedList<ExternalRepository>();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = getConnection();
      stmt = conn.prepareStatement("select id,name,baseurl,kinds,readonly,immutable,description from externalrepository");
      rs = stmt.executeQuery();
      while (rs.next()) {
        Set<ExternalKind> kinds = buildKinds(rs.getString("kinds"));
        result.add(new DefaultExternalRepository(JDBCUtilities.getUUID(rs, "id"),
                                                 rs.getString("name"),
                                                 new URI(rs.getString("baseurl")),
                                                 kinds,
                                                 JDBCUtilities.getBoolean(rs, "readonly"),
                                                 JDBCUtilities.getBoolean(rs, "immutable"),
                                                 Collections.singletonMap((String) null, rs.getString("description"))));
      }
      result.addAll(FSExternalRepositoryItemProvider.getInstance().getAll());
    } catch (URISyntaxException ex) {
      throw new DataProviderException(ex);
    } catch (SQLException ex) {
      throw new DataProviderException(ex);
    } finally {
      JDBCUtilities.close(rs, stmt, conn);
    }
    return result;
  }

  @Override
  public void delete(UUID pKey) throws DataProviderException
  {
    if (pKey != null) {
      Connection conn = null;
      PreparedStatement stmt = null;
      try {
        conn = getConnection();
        stmt = conn.prepareStatement("delete from externalrepository where id=?");
        stmt.setString(1, pKey.toString());
        stmt.executeUpdate();
      } catch (SQLException ex) {
        throw new DataProviderException(ex);
      } finally {
        JDBCUtilities.close(stmt, conn);
      }
    }
  }

  boolean keyExists(final Connection conn, UUID key) throws SQLException
  {
    if (key != null) {
      PreparedStatement stmt = null;
      ResultSet rs = null;
      try {
        stmt = conn.prepareStatement("select id from externalrepository where id=?");
        stmt.setString(1, key.toString());
        rs = stmt.executeQuery();
        return rs.next();
      } finally {
        JDBCUtilities.close(rs, stmt);
      }
    }
    return false;
  }

  private String buildKinds(Collection<ExternalKind> kinds)
  {
    StringBuilder builder = new StringBuilder();
    for (ExternalKind k : kinds) {
      builder.append(k.name());
      builder.append("|");
    }
    if (builder.length() > 0) {
      builder.setLength(builder.length() - 1);
    }
    return builder.toString();
  }

  @Override
  public ExternalRepository store(ExternalRepository pItem) throws DataProviderException
  {
    if (pItem != null && pItem.isImmutable()) {
      Connection conn = null;
      PreparedStatement stmt = null;
      try {
        conn = getConnection();
        if (keyExists(conn, pItem.getId())) {
          stmt = conn.prepareStatement(
                  "update externalrepository set name=?,baseurl=?,kinds=?,readonly=?,immutable=?,description=? where id=?");
        } else {
          stmt = conn.prepareStatement(
                  "insert into externalrepository (name,baseurl,kinds,readonly,immutable,description,id) values (?,?,?,?,?,?,?)");
        }
        stmt.setString(1, pItem.getName());
        stmt.setString(2, pItem.getURI().toString());
        stmt.setString(3, buildKinds(pItem.getKinds()));
        JDBCUtilities.setBoolean(stmt, 4, pItem.isReadOnly());
        JDBCUtilities.setBoolean(stmt, 5, pItem.isImmutable());
        stmt.setString(6, pItem.getDescription(null));
        stmt.setString(7, pItem.getId().toString());
        stmt.executeUpdate();
      } catch (SQLException ex) {
        throw new DataProviderException(ex);
      } finally {
        JDBCUtilities.close(stmt, conn);
      }
    }
    return pItem;
  }
}
