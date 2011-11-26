/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.firebirdsql;

import at.motriv.filesystem.db.DBFileObject;
import at.motriv.filesystem.db.DBFileObjectItemProvider;
import at.motriv.filesystem.db.DatabaseFileSystem;
import at.motriv.filesystem.db.DefaultDBFileObject;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.DataSourceChecker;
import at.mountainsd.dataprovider.api.LabelKeyPair;
import at.mountainsd.dataprovider.api.jdbc.JDBCUtilities;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author wolfi
 */
public class FBDBFileObjectItemProvider extends AbstractMotrivFBItemProvider<UUID, DBFileObject> implements DBFileObjectItemProvider
{

  private static final class MyInitializer
  {

    private static final FBDBFileObjectItemProvider instance = new FBDBFileObjectItemProvider();
  }

  public static FBDBFileObjectItemProvider getInstance()
  {
    return MyInitializer.instance;
  }

  private FBDBFileObjectItemProvider()
  {
  }

  @Override
  protected Lookup createLookup()
  {
    return Lookups.singleton(new DataSourceChecker()
    {

      @Override
      public boolean checkDatabase(Lookup context) throws DataProviderException
      {
        return FBDBFileObjectItemProvider.this.checkDatabase(context);
      }
    });
  }

  @Override
  public DBFileObject getRoot() throws DataProviderException
  {
    return get(ID_ROOT);
  }

  @Override
  public DBFileObject get(UUID pKey) throws DataProviderException
  {
    Connection conn = null;
    try {
      conn = getConnection();
      return get(conn, pKey);
    } catch (SQLException ex) {
      throw new DataProviderException(ex);
    } finally {
      JDBCUtilities.close(conn);
    }
  }

  private Map<String, Object> readAttributes(final Connection conn, UUID pKey) throws SQLException
  {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    Map<String, Object> result = new HashMap<String, Object>();
    try {
      stmt = conn.prepareStatement("select name,content from fileattr where fileobject=?");
      stmt.setString(1, pKey.toString());
      rs = stmt.executeQuery();
      while (rs.next()) {
        try {
          String name = rs.getString("name");
          if (ATTR_LAST_MODIFIED.equals(name)) {
            String tmp = getCharacterBlob(rs, "content");
            result.put(name, new Date(Long.parseLong(tmp)));
          } else if (ATTR_READ_ONLY.equals(name)) {
            String tmp = getCharacterBlob(rs, "content");
            result.put(name, Boolean.parseBoolean(tmp));
          } else {
            result.put(name, getObject(rs, "content"));
          }
        } catch (IOException ex) {
          Exceptions.printStackTrace(ex);
        } catch (ClassNotFoundException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
    } finally {
      JDBCUtilities.close(rs, stmt);
    }
    return result;
  }

  DBFileObject get(final Connection conn, UUID pKey) throws SQLException
  {
    if (pKey != null) {
      PreparedStatement stmt = null;
      ResultSet rs = null;
      try {
        stmt = conn.prepareStatement("select parent,name,folder from fileobjects where id=?");
        stmt.setString(1, pKey.toString());
        rs = stmt.executeQuery();
        if (rs.next()) {
          return new DefaultDBFileObject(pKey,
                                         rs.getString("name"),
                                         JDBCUtilities.getUUID(rs, "parent"),
                                         JDBCUtilities.getBoolean(rs, "folder"),
                                         readAttributes(conn, pKey));
        }
      } finally {
        JDBCUtilities.close(rs, stmt);
      }
    }
    return null;

  }

  @Override
  public DBFileObject get(Iterator<String> path) throws DataProviderException
  {
    Connection conn = null;
    try {
      conn = getConnection();
      UUID id = findFileObject(conn, path);
      if (id != null) {
        return get(conn, id);
      }
    } catch (SQLException ex) {
      throw new DataProviderException(ex);
    } finally {
      JDBCUtilities.close(conn);
    }
    return null;
  }

  private boolean hasChildren(final Connection conn, UUID pKey) throws SQLException
  {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.prepareStatement("select id from fileobjects where parent=?");
      stmt.setString(1, pKey.toString());
      rs = stmt.executeQuery();
      return rs.next();
    } finally {
      JDBCUtilities.close(rs, stmt);
    }
  }

  @Override
  public void delete(UUID pKey) throws DataProviderException
  {
    if (pKey != null && !ID_ROOT.equals(pKey)) {
      Connection conn = null;
      PreparedStatement stmt;
      try {
        conn = getConnection();
        if (!hasChildren(conn, pKey)) {
          stmt = conn.prepareStatement("delete from fileattr where fileobject=?");
          stmt.setString(1, pKey.toString());
          stmt.executeUpdate();
          stmt.close();
          stmt = conn.prepareStatement("delete from fileobject_content where fileobject=?");
          stmt.setString(1, pKey.toString());
          stmt.executeUpdate();
          stmt.close();
          stmt = conn.prepareStatement("delete from fileobjects where id=?");
          stmt.setString(1, pKey.toString());
          stmt.executeUpdate();
          stmt.close();
          stmt = conn.prepareStatement(
                  "delete from filecontent fc where not exists (select * from fileobject_content foc where fc.id=foc.filecontent");
          stmt.executeUpdate();
        } else {
          throw new DataProviderException("Cannont delete nonempty folder");
        }
      } catch (SQLException ex) {
        throw new DataProviderException(ex);
      } finally {
        JDBCUtilities.close(conn);
      }
    }
  }

  boolean keyExists(final Connection conn, UUID id) throws SQLException
  {
    if (id != null) {
      PreparedStatement stmt = null;
      ResultSet rs = null;
      try {
        stmt = conn.prepareStatement("select id from fileobjects where id=?");
        stmt.setString(1, id.toString());
        rs = stmt.executeQuery();
        return rs.next();
      } finally {
        JDBCUtilities.close(rs, stmt);
      }
    }
    return false;
  }

  private void storeAttributes(final Connection conn, DBFileObject pItem) throws SQLException, IOException
  {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("delete from fileattr where fileobject=?");
      stmt.setString(1, pItem.getId().toString());
      stmt.executeUpdate();
      stmt.close();
      stmt = conn.prepareStatement("insert into fileattr(fileobject,name,content,contentsize,mime) values (?,?,?,-1,?)");
      stmt.setString(1, pItem.getId().toString());
      stmt.setString(2, ATTR_LAST_MODIFIED);
      String str = MessageFormat.format("{0,number,#}", new Date().getTime());
      setCharacterBlob(stmt, 3, str);
      stmt.setString(4, "text/plain");
      stmt.executeUpdate();
      for (Map.Entry<String, ?> e : pItem.getClientProperites().entrySet()) {
        if (!ATTR_LAST_MODIFIED.equals(e.getKey())) {
          stmt.setString(2, e.getKey());
          if (ATTR_READ_ONLY.equals(e.getKey()) && e.getValue() != null) {
            setCharacterBlob(stmt, 3, Boolean.toString(Boolean.parseBoolean(e.getValue().toString())));
            stmt.setString(4, "text/plain");
          } else {
            setObject(stmt, 3, e.getValue());
            stmt.setString(4, "application/octed-stream");
          }
          stmt.executeUpdate();
        }
      }

    } finally {
      JDBCUtilities.close(stmt);
    }
  }

  @Override
  public DBFileObject store(DBFileObject pItem) throws DataProviderException
  {
    if (pItem != null && pItem.getId() != null && pItem.getParent() != null && !pItem.getId().equals(pItem.getParent())) {
      Connection conn = null;
      PreparedStatement stmt = null;
      try {
        conn = getConnection();
        if (keyExists(conn, pItem.getId())) {
          stmt = conn.prepareStatement("udpate fileobjects set name=?,parent=? where id=?");
          stmt.setString(1, pItem.getName());
          stmt.setString(2, pItem.getParent().toString());
          stmt.setString(3, pItem.getId().toString());
        } else {
          stmt = conn.prepareStatement("insert into fileobjects (id,name,parent,folder) values (?,?,?,?)");
          stmt.setString(1, pItem.getId().toString());
          stmt.setString(2, pItem.getName());
          stmt.setString(3, pItem.getParent().toString());
          stmt.setInt(4, pItem.isFolder() ? 1 : 0);
        }
        stmt.executeUpdate();
        storeAttributes(conn, pItem);
        return get(conn, pItem.getId());
      } catch (IOException ex) {
        throw new DataProviderException(ex);
      } catch (SQLException ex) {
        throw new DataProviderException(ex);
      } finally {
        JDBCUtilities.close(stmt, conn);
      }
    }
    return null;
  }

  @Override
  public UUID findByName(UUID parent, String name) throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = getConnection();
      stmt = conn.prepareStatement("select id from fileobjects where parent=? and name=?");
      stmt.setString(1, parent.toString());
      stmt.setString(2, name);
      rs = stmt.executeQuery();
      if (rs.next()) {
        return JDBCUtilities.getUUID(rs, "id");
      }
    } catch (SQLException ex) {
      throw new DataProviderException(ex);
    } finally {
      JDBCUtilities.close(rs, stmt, conn);
    }
    return null;
  }

  @Override
  public List<LabelKeyPair<UUID>> getChildren(UUID parent) throws DataProviderException
  {
    Connection conn = null;
    try {
      conn = getConnection();
      return getChildren(conn, parent);
    } catch (SQLException ex) {
      throw new DataProviderException(ex);
    } finally {
      JDBCUtilities.close(conn);
    }
  }

  List<LabelKeyPair<UUID>> getChildren(final Connection conn, UUID parent) throws SQLException
  {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List<LabelKeyPair<UUID>> result = new LinkedList<LabelKeyPair<UUID>>();
    if (parent != null) {
      try {
        stmt = conn.prepareStatement("select id,name from fileobjects where parent=?");
        stmt.setString(1, parent.toString());
        rs = stmt.executeQuery();
        while (rs.next()) {
          result.add(new LabelKeyPair<UUID>(JDBCUtilities.getUUID(rs, "id"), rs.getString("name")));
        }
      } finally {
        JDBCUtilities.close(rs, stmt);
      }
    }
    return result;
  }

  private UUID findFolder(final Connection conn, Iterator<String> path) throws SQLException
  {
    UUID runner = ID_ROOT;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.prepareStatement("select id from fileobjects where name=? and parent=? and folder<>0");
      while (path.hasNext()) {
        stmt.setString(1, path.next());
        stmt.setString(2, runner.toString());
        rs = stmt.executeQuery();
        if (rs.next()) {
          runner = JDBCUtilities.getUUID(rs, "id");
        } else {
          return null;
        }
      }
      return runner;
    } finally {
      JDBCUtilities.close(rs, stmt);
    }
  }

  private UUID findFileObject(final Connection conn, Iterator<String> path) throws SQLException
  {
    UUID runner = ID_ROOT;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.prepareStatement("select id from fileobjects where name=? and parent=?");
      while (path.hasNext()) {
        stmt.setString(1, path.next());
        stmt.setString(2, runner.toString());
        rs = stmt.executeQuery();
        if (rs.next()) {
          runner = JDBCUtilities.getUUID(rs, "id");
        } else {
          return null;
        }
      }
      return runner;
    } finally {
      JDBCUtilities.close(rs, stmt);
    }
  }

  @Override
  public List<LabelKeyPair<UUID>> getChildren(Iterator<String> path) throws DataProviderException
  {
    Connection conn = null;
    try {
      conn = getConnection();
      return getChildren(conn, findFolder(conn, path));
    } catch (SQLException ex) {
      throw new DataProviderException(ex);
    } finally {
      JDBCUtilities.close(conn);
    }
  }

  private boolean checkDatabase(Lookup context) throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getConnection();
      if (get(conn, ID_ROOT) == null) {
        stmt = conn.prepareStatement("insert into fileobjects (id,parent,name,folder) values (?,?,?,?)");
        stmt.setString(1, ID_ROOT.toString());
        stmt.setString(2, ID_ROOT.toString());
        stmt.setString(3, "motriv.db");
        stmt.setInt(4, 1);
        stmt.executeUpdate();
        stmt.close();
        stmt = conn.prepareStatement("insert into fileattr(fileobject,name,content,contentsize,mime) values (?,?,?,-1,'text/plain')");
        stmt.setString(1, ID_ROOT.toString());
        stmt.setString(2, ATTR_LAST_MODIFIED);
        String str = MessageFormat.format("{0,number,#}", new Date().getTime());
        setCharacterBlob(stmt, 3, str);
        stmt.executeUpdate();
      }
    } catch (IOException ex) {
      throw new DataProviderException(ex);
    } catch (SQLException ex) {
      throw new DataProviderException(ex);
    } finally {
      JDBCUtilities.close(stmt, conn);
    }
    DatabaseFileSystem.start();
    return true;
  }
}
