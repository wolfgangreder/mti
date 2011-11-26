/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.derby;

import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactItemProvider;
import at.motriv.datamodel.entities.contact.ContactType;
import at.motriv.datamodel.entities.contact.MutableContact;
import at.motriv.datamodel.entities.contact.impl.ContactCache;
import at.motriv.datamodel.entities.contact.impl.DefaultMutableContact;
import at.mountainsd.dataprovider.api.DataProviderEvent;
import at.mountainsd.dataprovider.api.DataProviderEvent.Action;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.LabelKeyPair;
import at.mountainsd.dataprovider.api.UniversalSearchRequest;
import at.mountainsd.dataprovider.api.jdbc.JDBCUtilities;
import at.mountainsd.util.Utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 *
 * @author wolfi
 */
public class DerbyContactItemProvider extends AbstractMotrivDerbyItemProvider<UUID, Contact> implements ContactItemProvider
{

  private static class MyInitializer
  {

    private static DerbyContactItemProvider instance = new DerbyContactItemProvider();
  }

  private static class Getter implements Callable<Contact>
  {

    private final PreparedStatement stmt;
    private final UUID id;
    private final Set<ContactType> types;

    public Getter(PreparedStatement stmt, UUID id, Set<ContactType> types)
    {
      this.stmt = stmt;
      this.id = id;
      this.types = types;
    }

    @Override
    public Contact call() throws Exception
    {
      ResultSet rs = null;
      try {
        stmt.setString(1, id.toString());
        rs = stmt.executeQuery();
        if (rs.next()) {
          MutableContact builder = new DefaultMutableContact(types);
          builder.setAddress1(rs.getString("address1"));
          builder.setAddress2(rs.getString("address2"));
          builder.setCity(rs.getString("city"));
          builder.setCountry(rs.getString("country"));
          builder.setEmail(rs.getString("email"));
          builder.setId(id);
          builder.setMemo(rs.getString("description"));
          builder.setName(rs.getString("name"));
          builder.setWWW(rs.getString("www"));
          builder.setZip(rs.getString("zip"));
          builder.setPhone1(rs.getString("phone1"));
          builder.setPhone2(rs.getString("phone2"));
          builder.setFax(rs.getString("fax"));
          builder.setShopAddress(rs.getString("shopaddress"));
          builder.setTypes(types);
          return builder.build();
        }
      } finally {
        JDBCUtilities.close(rs);
      }
      return null;
    }
  }

  public static DerbyContactItemProvider getInstance()
  {
    return MyInitializer.instance;
  }

  private DerbyContactItemProvider()
  {
  }

  @Override
  public Contact get(UUID pKey) throws DataProviderException
  {
    if (pKey != null) {
      Connection conn = null;
      try {
        conn = getConnection();
        List<? extends Contact> tmp = get(conn, Collections.singleton(pKey), null);
        return tmp.isEmpty() ? null : tmp.get(0);
      } catch (SQLException e) {
        throw new DataProviderException(e);
      } finally {
        JDBCUtilities.close(conn);
      }
    }
    return null;
  }

  List<? extends Contact> get(final Connection conn, Collection<? extends UUID> ids, ContactType filter) throws SQLException,
                                                                                                                DataProviderException
  {
    List<Contact> result = new ArrayList<Contact>(ids.size());
    if (!ids.isEmpty()) {
      PreparedStatement stmt = null;
      ResultSet rs = null;
      Map<UUID, Set<ContactType>> tmp = new HashMap<UUID, Set<ContactType>>();
      try {
        if (filter != null) {
          stmt = conn.prepareStatement(MessageFormat.format("select name from contacttypes where id=? and name='{0}'", filter.name()));
        } else {
          stmt = conn.prepareStatement("select name from contacttypes where id=?");
        }
        for (UUID id : ids) {
          stmt.setString(1, id.toString());
          rs = stmt.executeQuery();
          HashSet<ContactType> set = new HashSet<ContactType>();
          while (rs.next()) {
            set.add(ContactType.valueOf(rs.getString("name")));
          }
          rs.close();
          if (!set.isEmpty()) {
            tmp.put(id, set);
          }
        }
        stmt.close();
        stmt = conn.prepareStatement("select name,address1,address2,zip,city,country,email,www,description,phone1,phone2,fax,shopaddress "
                                     + "from contact where id=?");
        for (Map.Entry<UUID, Set<ContactType>> id : tmp.entrySet()) {
          result.add(ContactCache.getInstance().get(id.getKey(), new Getter(stmt, id.getKey(), id.getValue())));
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
    fireDataProviderEvent(new DataProviderEvent(this, Action.DELETED, null, pKey));
  }

  @Override
  public Contact store(Contact pItem) throws DataProviderException
  {
    Connection conn = null;
    StoreResult tmp = null;
    try {
      conn = getConnection();
      tmp = store(conn, pItem);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(conn);
    }
    if (tmp != null && tmp.action != null) {
      fireDataProviderEvent(new DataProviderEvent(this, tmp.action, tmp.contact, tmp.contact.getId()));
    }
    return tmp != null ? tmp.contact : null;
  }

  static class StoreResult
  {

    public Contact contact;
    public DataProviderEvent.Action action;
  }

  StoreResult store(final Connection conn, Contact pItem) throws SQLException
  {
    PreparedStatement stmt = null;
    StoreResult result = new StoreResult();
    try {
      if (keyExists(conn, pItem.getId())) {
        stmt = conn.prepareStatement(
                "update contact set name=?,address1=?,address2=?,city=?,zip=?,country=?,email=?,www=?,description=?,phone1=?,phone2=?"
                + ",fax=?,shopaddress=? "
                + " where id=?");
        result.action = DataProviderEvent.Action.MODIFIED;
      } else {
        stmt = conn.prepareStatement(
                "insert into contact (name,address1,address2,city,zip,country,email,www,description,phone1,phone2,fax,shopaddress,id) "
                + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        result.action = DataProviderEvent.Action.CREATED;
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
      stmt.setString(10, pItem.getPhone1());
      stmt.setString(11, pItem.getPhone2());
      stmt.setString(12, pItem.getFax());
      stmt.setString(13, pItem.getShopAddress());
      stmt.setString(14, pItem.getId().toString());
      stmt.executeUpdate();
      stmt.close();
      stmt = conn.prepareStatement("delete from contacttypes where id=?");
      stmt.setString(1, pItem.getId().toString());
      stmt.executeUpdate();
      stmt.close();
      stmt = conn.prepareStatement("insert into contacttypes (id,name) values (?,?)");
      stmt.setString(1, pItem.getId().toString());
      for (ContactType t : pItem.getTypes()) {
        stmt.setString(2, t.name());
        stmt.executeUpdate();
      }
      result.contact = pItem;
      ContactCache.getInstance().store(result.contact);
    } finally {
      JDBCUtilities.close(stmt);
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

  boolean contactTypeExists(final Connection conn, UUID id, ContactType type) throws SQLException
  {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.prepareStatement("select id from contacttypes where id=? and name=?");
      stmt.setString(1, id.toString());
      stmt.setString(2, type.name());
      rs = stmt.executeQuery();
      return rs.next();
    } finally {
      JDBCUtilities.close(rs, stmt);
    }
  }

  List<? extends Contact> getAllByContactType(final Connection conn, ContactType type) throws SQLException, DataProviderException
  {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    HashSet<UUID> ids = new HashSet<UUID>();
    try {
      stmt = conn.prepareStatement("select c.id from contacttypes t,contact c where t.id=c.id and t.name=?");
      stmt.setString(1, type.name());
      rs = stmt.executeQuery();
      while (rs.next()) {
        ids.add(JDBCUtilities.getUUID(rs, "id"));
      }
      return get(conn, ids, type);
    } finally {
      JDBCUtilities.close(rs, stmt);
    }
  }

  @Override
  public List<? extends Contact> getAllRetailers() throws DataProviderException
  {
    Connection conn = null;
    try {
      conn = getConnection();
      return getAllByContactType(conn, ContactType.RETAILER);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(conn);
    }
  }

  @Override
  public Contact getRetailer(UUID retailer) throws DataProviderException
  {
    if (retailer != null) {
      Connection conn = null;
      try {
        conn = getConnection();
        List<? extends Contact> tmp = get(conn, Collections.singleton(retailer), ContactType.RETAILER);
        return tmp.isEmpty() ? null : tmp.get(0);
      } catch (SQLException e) {
        throw new DataProviderException(e);
      } finally {
        JDBCUtilities.close(conn);
      }
    }
    return null;
  }

  @Override
  public List<? extends Contact> getAllManufacturer() throws DataProviderException
  {
    Connection conn = null;
    try {
      conn = getConnection();
      return getAllByContactType(conn, ContactType.MANUFACTURER);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(conn);
    }
  }

  @Override
  public Contact getManufacturer(UUID manufacturer) throws DataProviderException
  {
    if (manufacturer != null) {
      Connection conn = null;
      try {
        conn = getConnection();
        List<? extends Contact> tmp = get(conn, Collections.singleton(manufacturer), ContactType.MANUFACTURER);
        return tmp.isEmpty() ? null : tmp.get(0);
      } catch (SQLException e) {
        throw new DataProviderException(e);
      } finally {
        JDBCUtilities.close(conn);
      }

    }
    return null;
  }

  @Override
  public Contact makeManufacturer(Contact contact) throws DataProviderException
  {
    Connection conn = null;
    Contact result;
    try {
      conn = getConnection();
      result = makeContactType(conn, contact, ContactType.MANUFACTURER);
    } catch (SQLException ex) {
      throw new DataProviderException(ex);
    } finally {
      JDBCUtilities.close(conn);
    }
    fireDataProviderEvent(new DataProviderEvent(this, Action.MODIFIED, result, result.getId()));
    return result;
  }

  Contact makeContactType(final Connection conn, Contact contact, ContactType type) throws SQLException, DataProviderException
  {
    PreparedStatement stmt = null;
    try {
      if (!contactTypeExists(conn, contact.getId(), type)) {
        stmt = conn.prepareStatement("insert into contacttypes (id,name) values (?,?)");
        stmt.setString(1, contact.getId().toString());
        stmt.setString(2, type.name());
        stmt.executeUpdate();
      }
      List<? extends Contact> tmp = get(conn, Collections.singleton(contact.getId()), type);
      return tmp.isEmpty() ? null : tmp.get(0);
    } finally {
      JDBCUtilities.close(stmt);
    }
  }

  @Override
  public Contact makeRetailer(Contact contact) throws DataProviderException
  {
    Connection conn = null;
    Contact result;
    try {
      conn = getConnection();
      result = makeContactType(conn, contact, ContactType.RETAILER);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(conn);
    }
    fireDataProviderEvent(new DataProviderEvent(this, Action.MODIFIED, result, result.getId()));
    return result;
  }

  Contact removeFromContactType(Contact contact, ContactType type) throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    Contact result;
    try {
      conn = getConnection();
      stmt = conn.prepareStatement("delete from contacttypes where id=? and name=?");
      stmt.setString(1, contact.getId().toString());
      stmt.setString(2, type.name());
      stmt.executeUpdate();
      List<? extends Contact> tmp = get(conn, Collections.singleton(contact.getId()), null);
      result = tmp.isEmpty() ? null : tmp.get(0);
    } catch (SQLException ex) {
      throw new DataProviderException(ex);
    } finally {
      JDBCUtilities.close(stmt);
    }
    fireDataProviderEvent(new DataProviderEvent(this, Action.MODIFIED, result, result.getId()));
    return result;
  }

  @Override
  public Contact removeFromManufacturer(Contact contact) throws DataProviderException
  {
    return removeFromContactType(contact, ContactType.MANUFACTURER);
  }

  @Override
  public Contact removeFromRetailer(Contact contact) throws DataProviderException
  {
    return removeFromContactType(contact, ContactType.RETAILER);
  }

  void checkContacts(Connection conn, Collection<? extends Contact> contacts) throws SQLException, DataProviderException
  {
    for (Contact c : contacts) {
      if (!keyExists(conn, c.getId())) {
        store(conn, c);
      }
    }
  }

  @Override
  public List<String> getLookupCountries() throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    List<String> result = new LinkedList<String>();
    try {
      conn = getConnection();
      stmt = conn.prepareStatement("select distinct country from contact group by country");
      rs = stmt.executeQuery();
      while (rs.next()) {
        result.add(rs.getString("country"));
      }
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(rs, stmt, conn);
    }
    return result;
  }

  @Override
  public List<? extends Contact> getContacts(Collection<? extends UniversalSearchRequest> requests,
                                             ContactType classFilter) throws DataProviderException
  {
    Set<UUID> ids = null;
    Connection conn = null;
    try {
      conn = getConnection();
      for (UniversalSearchRequest request : requests) {
        Set<UUID> tmp = new HashSet<UUID>();
        tmp.addAll(findByString(conn, request.getAsString(), classFilter));
        ids = new HashSet<UUID>(Utils.nullUniverseIntersection(ids, tmp));
        if (ids.isEmpty()) {
          return Collections.emptyList();
        }
      }
      return get(conn, ids, classFilter);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(conn);
    }
  }

  private Set<UUID> findByString(final Connection conn, String filter, ContactType typeFilter) throws SQLException
  {
    Set<UUID> result = new HashSet<UUID>();
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String query = "select c.id from contact c {0} where (lower(c.name) like ? or lower(c.city) like ?) {1}";
    try {
      switch (typeFilter) {
        case MANUFACTURER:
          stmt = conn.prepareStatement(MessageFormat.format(query, ",manufacturer m", " and m.id=c.id"));
          break;
        case RETAILER:
          stmt = conn.prepareStatement(MessageFormat.format(query, ",retailer r", " and r.id=c.id"));
          break;
        default:
          stmt = conn.prepareStatement(MessageFormat.format(query, "", ""));
      }
      String f = filter.toLowerCase() + "%";
      stmt.setString(1, f);
      stmt.setString(2, f);
      rs = stmt.executeQuery();
      while (rs.next()) {
        result.add(JDBCUtilities.getUUID(rs, "id"));
      }
    } finally {
      JDBCUtilities.close(rs, stmt);
    }
    return result;
  }

  @Override
  public List<LabelKeyPair<UUID>> getAllLabels(ContactType type) throws DataProviderException
  {
    List<LabelKeyPair<UUID>> result = new LinkedList<LabelKeyPair<UUID>>();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = getConnection();
      switch (type) {
        case MANUFACTURER:
          stmt = conn.prepareStatement("select c.id,c.name from contact c,manufacturer m where c.id=m.id order by c.name");
          break;
        case RETAILER:
          stmt = conn.prepareStatement("select c.id,c.name from contact c,retailer m where c.id=m.id order by c.name");
          break;
      }
      rs = stmt.executeQuery();
      while (rs.next()) {
        result.add(new LabelKeyPair<UUID>(JDBCUtilities.getUUID(rs, "id"), rs.getString("name")));
      }
    } catch (SQLException ex) {
      throw new DataProviderException(ex);
    } finally {
      JDBCUtilities.close(rs, stmt, conn);
    }
    return result;
  }
}
