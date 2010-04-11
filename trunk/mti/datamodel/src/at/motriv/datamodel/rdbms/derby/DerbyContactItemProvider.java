/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.derby;

import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactItemProvider;
import at.motriv.datamodel.entities.contact.ContactType;
import at.motriv.datamodel.entities.contact.GenericContactBuilder;
import at.motriv.datamodel.entities.contact.Manufacturer;
import at.motriv.datamodel.entities.contact.ManufacturerBuilder;
import at.motriv.datamodel.entities.contact.Retailer;
import at.motriv.datamodel.entities.contact.RetailerBuilder;
import at.motriv.datamodel.entities.contact.impl.ContactBuilder;
import at.motriv.datamodel.entities.contact.impl.ContactCache;
import at.motriv.datamodel.entities.contact.impl.GenericContact;
import at.mountainsd.dataprovider.api.DataProviderEvent;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.UniversalSearchRequest;
import at.mountainsd.dataprovider.api.jdbc.JDBCUtilities;
import at.mountainsd.util.Utils;
import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
          builder.phone1(rs.getString("phone1"));
          builder.phone2(rs.getString("phone2"));
          builder.fax(rs.getString("fax"));
          return builder.build();
        }
      } finally {
        JDBCUtilities.close(rs);
      }
      return null;
    }
  }

  private static class GenericGetter extends Getter<GenericContact>
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

  private static class RetailerGetter extends Getter<Retailer>
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

  private static class ManufacturerGetter extends Getter<Manufacturer>
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

  @SuppressWarnings("unchecked")
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
        stmt = conn.prepareStatement("select name,address1,address2,zip,city,country,email,www,description,phone1,phone2,fax "
                + "from contact where id=?");
        Constructor<? extends Getter<? extends VI>> ctr = getterClass.getConstructor(PreparedStatement.class, UUID.class);
        for (UUID id : ids) {
          Contact tmp = ContactCache.getInstance().get(id, ctr.newInstance(stmt, id));
          result.add((VI) tmp);
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
                "update contact set name=?,address1=?,address2=?,city=?,zip=?,country=?,email=?,www=?,description=?,phone1,phone2,fax"
                + " where id=?");
        result.action = DataProviderEvent.Action.MODIFIED;
      } else {
        stmt = conn.prepareStatement(
                "insert into contact (name,address1,address2,city,zip,country,email,www,description,phone1,phone2,fax,id) "
                + "values (?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
      stmt.setString(13, pItem.getId().toString());
      stmt.executeUpdate();
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
  public Retailer getRetailer(UUID retailer) throws DataProviderException
  {
    if (retailer != null) {
      Connection conn = null;
      PreparedStatement stmt = null;
      ResultSet rs = null;
      HashSet<UUID> ids = new HashSet<UUID>();
      try {
        conn = getConnection();
        stmt = conn.prepareStatement("select r.id from retailer r,contact c where r.id=c.id and r.id=?");
        stmt.setString(1, retailer.toString());
        rs = stmt.executeQuery();
        if (rs.next()) {
          List<? extends Retailer> tmp = get(conn, Collections.singleton(retailer), RetailerGetter.class);
          return tmp.isEmpty() ? null : tmp.get(0);
        }
      } catch (SQLException e) {
        throw new DataProviderException(e);
      } finally {
        JDBCUtilities.close(rs, stmt, conn);
      }
    }
    return null;
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
  public Manufacturer getManufacturer(UUID manufacturer) throws DataProviderException
  {
    if (manufacturer != null) {
      Connection conn = null;
      PreparedStatement stmt = null;
      ResultSet rs = null;
      HashSet<UUID> ids = new HashSet<UUID>();
      try {
        conn = getConnection();
        stmt = conn.prepareStatement("select r.id from manufacturer r,contact c where r.id=c.id");
        rs = stmt.executeQuery();
        if (rs.next()) {
          List<? extends Manufacturer> tmp = get(conn, Collections.singleton(manufacturer), ManufacturerGetter.class);
          return tmp.isEmpty() ? null : tmp.get(0);
        }
      } catch (SQLException e) {
        throw new DataProviderException(e);
      } finally {
        JDBCUtilities.close(rs, stmt, conn);
      }

    }
    return null;
  }

  @Override
  public Manufacturer makeManufacturer(Contact contact) throws DataProviderException
  {
    Connection conn = null;
    try {
      conn = getConnection();
      return makeManufacturer(conn, contact);
    } catch (SQLException ex) {
      throw new DataProviderException(ex);
    } finally {
      JDBCUtilities.close(conn);
    }
  }

  Manufacturer makeManufacturer(final Connection conn, Contact contact) throws SQLException, DataProviderException
  {
    PreparedStatement stmt = null;
    try {
      if (!manufacturerExists(conn, contact.getId())) {
        stmt = conn.prepareStatement("insert into manufacturer (id) values (?)");
        stmt.setString(1, contact.getId().toString());
        stmt.executeUpdate();
      }
      List<? extends Manufacturer> tmp = get(conn, Collections.singleton(contact.getId()), ManufacturerGetter.class);
      return tmp.isEmpty() ? null : tmp.get(0);
    } finally {
      JDBCUtilities.close(stmt);
    }
  }

  @Override
  public Retailer makeRetailer(Contact contact) throws DataProviderException
  {
    Connection conn = null;
    try {
      conn = getConnection();
      return makeRetailer(conn, contact);
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(conn);
    }
  }

  Retailer makeRetailer(final Connection conn, Contact contact) throws DataProviderException, SQLException
  {
    PreparedStatement stmt = null;
    try {
      if (!retailerExists(conn, contact.getId())) {
        stmt = conn.prepareStatement("insert into retailer (id) values (?)");
        stmt.setString(1, contact.getId().toString());
        stmt.executeUpdate();
      }
      List<? extends Retailer> tmp = get(conn, Collections.singleton(contact.getId()), RetailerGetter.class);
      return tmp.isEmpty() ? null : tmp.get(0);
    } finally {
      JDBCUtilities.close(stmt);
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

  void checkContacts(Connection conn, Collection<? extends Contact> contacts) throws SQLException, DataProviderException
  {
    for (Contact c : contacts) {
      if (!keyExists(conn, c.getId())) {
        store(conn, c);
      }
      if (c instanceof Manufacturer) {
        makeManufacturer(conn, c);
      }
      if (c instanceof Retailer) {
        makeRetailer(conn, c);
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
      Class<? extends Getter<?>> getter = null;
      switch (classFilter) {
        case MANUFACTURER:
          getter = ManufacturerGetter.class;
          break;
        case RETAILER:
          getter = RetailerGetter.class;
          break;
        default:
          getter = GenericGetter.class;
      }
      return get(conn, ids, getter);
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
}
