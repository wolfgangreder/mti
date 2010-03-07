/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.firebirdsql;

import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactItemProvider;
import at.motriv.datamodel.entities.contact.GenericContactBuilder;
import at.motriv.datamodel.entities.contact.Manufacturer;
import at.motriv.datamodel.entities.contact.ManufacturerBuilder;
import at.motriv.datamodel.entities.contact.Retailer;
import at.motriv.datamodel.entities.contact.RetailerBuilder;
import at.motriv.datamodel.entities.contact.impl.ContactBuilder;
import at.motriv.datamodel.entities.contact.impl.ContactCache;
import at.motriv.datamodel.entities.contact.impl.GenericContact;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.jdbc.JDBCUtilities;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public class FBContactItemProvider extends AbstractMotrivFBItemProvider<UUID, Contact> implements ContactItemProvider
{

  private static class MyInitializer
  {

    private static FBContactItemProvider instance = new FBContactItemProvider();
  }

  private static abstract class Getter<I extends Contact> implements ContactCache.ContactGetter
  {

    private final PreparedStatement stmt;
    private final UUID id;

    public Getter(PreparedStatement stmt, UUID id)
    {
      this.stmt = stmt;
      this.id = id;
    }

    protected abstract ContactBuilder<? extends Contact> getBuilder();

    @Override
    public Contact call() throws Exception
    {
      ResultSet rs = null;
      try {
        stmt.setString(1, id.toString());
        rs = stmt.executeQuery();
        if (rs.next()) {
          ContactBuilder<?> builder = getBuilder();
          builder.address1(rs.getString("address1"));
          builder.address2(rs.getString("address2"));
          builder.city(rs.getString("city"));
          builder.country(rs.getString("country"));
          builder.email(rs.getString("email"));
          builder.id(id);
          builder.memo(rs.getString("description"));
          builder.name(rs.getString("name"));
          builder.www(rs.getString("www"));
          builder.zip(rs.getString("zip"));
          return builder.build();
        }
      } finally {
        JDBCUtilities.close(rs);
      }
      return null;
    }
  }

  private class GenericGetter extends Getter<GenericContact>
  {

    public GenericGetter(PreparedStatement stmt, UUID id)
    {
      super(stmt, id);
    }

    @Override
    protected ContactBuilder<?> getBuilder()
    {
      return new GenericContactBuilder();
    }

    @Override
    public Class<? extends Contact> getInstanceClass()
    {
      return GenericContact.class;
    }
  }

  private class RetailerGetter extends Getter<Retailer>
  {

    public RetailerGetter(PreparedStatement stmt, UUID id)
    {
      super(stmt, id);
    }

    @Override
    protected ContactBuilder<? extends Contact> getBuilder()
    {
      return new RetailerBuilder();
    }

    @Override
    public Class<? extends Contact> getInstanceClass()
    {
      return Retailer.class;
    }
  }

  private class ManufacturerGetter extends Getter<Manufacturer>
  {

    public ManufacturerGetter(PreparedStatement stmt, UUID id)
    {
      super(stmt, id);
    }

    @Override
    protected ContactBuilder<? extends Contact> getBuilder()
    {
      return new ManufacturerBuilder();
    }

    @Override
    public Class<? extends Contact> getInstanceClass()
    {
      return Manufacturer.class;
    }
  }

  public static FBContactItemProvider getInstance()
  {
    return MyInitializer.instance;
  }

  private FBContactItemProvider()
  {
  }

  @Override
  public Contact get(UUID pKey) throws DataProviderException
  {
    if (pKey != null) {
      Connection conn = null;
      try {
        conn = getConnection();
        List<? extends Contact> tmp = get(conn, Collections.singleton(pKey), GenericGetter.class);
        return tmp.isEmpty() ? null : tmp.get(0);
      } catch (SQLException e) {
        throw new DataProviderException(e);
      } finally {
        JDBCUtilities.close(conn);
      }
    }
    return null;
  }

  <VI extends Contact> List<? extends VI> get(final Connection conn,
          Collection<? extends UUID> ids,
          Class<? extends Getter<? extends VI>> getterClass)
          throws SQLException, DataProviderException
  {
    List<VI> result = new ArrayList<VI>(ids.size());
    if (!ids.isEmpty()) {
      PreparedStatement stmt = null;
      ResultSet rs = null;
      try {
        stmt = conn.prepareStatement("select name,address1,address2,zip,city,country,email,www,description from contact where id=?");
        Constructor<? extends Getter<? extends VI>> ctr = getterClass.getConstructor(PreparedStatement.class, UUID.class);
        for (UUID id : ids) {
          ContactCache.getInstance().get(id, ctr.newInstance(stmt, id));
        }
      } catch (Exception e) {
        if (e instanceof SQLException) {
          throw (SQLException) e;
        } else if (e instanceof DataProviderException) {
          throw (DataProviderException) e;
        } else {
          throw new DataProviderException(e);
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
      stmt = conn.prepareStatement("delete from contact where id=?");
      stmt.setString(1, pKey.toString());
      stmt.executeUpdate();
      ContactCache.getInstance().remove(pKey);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(stmt, conn);
    }
  }

  @Override
  public Contact store(Contact pItem) throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Contact result = null;
    try {
      conn = getConnection();
      if (keyExists(conn, pItem.getId())) {
        stmt = conn.prepareStatement(
                "update contact set name=?,address1=?,address2=?,city=?,zip=?,country=?,email=?,www=?,description=? where id=?");
      } else {
        stmt = conn.prepareStatement(
                "insert into contact (name,address1,address2,city,zip,country,email,www,description,id) values (?,?,?,?,?,?,?,?,?,?)");
      }
      stmt.setString(1, pItem.getName());
      stmt.setString(2, pItem.getAddress1());
      stmt.setString(3, pItem.getAddress2());
      stmt.setString(4, pItem.getCity());
      stmt.setString(5, pItem.getZip());
      stmt.setString(6, pItem.getCountry());
      stmt.setString(7, pItem.getEmail());
      stmt.setString(8, pItem.getWWW());
      stmt.setString(9, pItem.getMemo());
      stmt.setString(10, pItem.getId().toString());
      stmt.executeUpdate();
      result = pItem;
      ContactCache.getInstance().store(result);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(stmt, conn);
      if (result == null && pItem != null) {
        ContactCache.getInstance().remove(pItem.getId());
      }
    }
    return result;
  }

  boolean keyExists(final Connection conn, UUID id) throws SQLException
  {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.prepareStatement("select id from contact where id=?");
      stmt.setString(1, id.toString());
      rs = stmt.executeQuery();
      return rs.next();
    } finally {
      JDBCUtilities.close(rs, stmt);
    }
  }

  boolean retailerExists(final Connection conn, UUID id) throws SQLException
  {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.prepareStatement("select id from retailer where id=?");
      stmt.setString(1, id.toString());
      rs = stmt.executeQuery();
      return rs.next();
    } finally {
      JDBCUtilities.close(rs, stmt);
    }
  }

  boolean manufacturerExists(final Connection conn, UUID id) throws SQLException
  {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.prepareStatement("select id from manufacturer where id=?");
      stmt.setString(1, id.toString());
      rs = stmt.executeQuery();
      return rs.next();
    } finally {
      JDBCUtilities.close(rs, stmt);
    }
  }

  @Override
  public List<? extends Retailer> getAllRetailers() throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    HashSet<UUID> ids = new HashSet<UUID>();
    try {
      conn = getConnection();
      stmt = conn.prepareStatement("select r.id from retailer r,contact c where r.id=c.id");
      rs = stmt.executeQuery();
      while (rs.next()) {
        ids.add(JDBCUtilities.getUUID(rs, "id"));
      }
      return get(conn, ids, RetailerGetter.class);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(rs, stmt, conn);
    }
  }

  @Override
  public List<? extends Manufacturer> getAllManufacturer() throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    HashSet<UUID> ids = new HashSet<UUID>();
    try {
      conn = getConnection();
      stmt = conn.prepareStatement("select r.id from manufacturer r,contact c where r.id=c.id");
      rs = stmt.executeQuery();
      while (rs.next()) {
        ids.add(JDBCUtilities.getUUID(rs, "id"));
      }
      return get(conn, ids, ManufacturerGetter.class);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(rs, stmt, conn);
    }
  }

  @Override
  public Manufacturer makeManufacturer(Contact contact) throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getConnection();
      if (!manufacturerExists(conn, contact.getId())) {
        stmt = conn.prepareStatement("insert into manufacturer (id) values (?)");
        stmt.setString(1, contact.getId().toString());
        stmt.executeUpdate();
      }
      List<? extends Manufacturer> tmp = get(conn, Collections.singleton(contact.getId()), ManufacturerGetter.class);
      return tmp.isEmpty() ? null : tmp.get(0);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(stmt, conn);
    }
  }

  @Override
  public Retailer makeRetailer(Contact contact) throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getConnection();
      if (!retailerExists(conn, contact.getId())) {
        stmt = conn.prepareStatement("insert into retailer (id) values (?)");
        stmt.setString(1, contact.getId().toString());
        stmt.executeUpdate();
      }
      List<? extends Retailer> tmp = get(conn, Collections.singleton(contact.getId()), RetailerGetter.class);
      return tmp.isEmpty() ? null : tmp.get(0);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(stmt, conn);
    }
  }

  @Override
  public void removeFromManufacturer(Manufacturer contact) throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getConnection();
      stmt = conn.prepareStatement("delete from manufacturer where id=?");
      stmt.setString(1, contact.getId().toString());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(stmt, conn);
    }
  }

  @Override
  public void removeFromRetailer(Retailer contact) throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = getConnection();
      stmt = conn.prepareStatement("delete from retailer where id=?");
      stmt.setString(1, contact.getId().toString());
      stmt.executeUpdate();
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(stmt, conn);
    }
  }
}
