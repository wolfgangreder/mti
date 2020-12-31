/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.persistence.impl.fb;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.ContactType;
import at.reder.mti.api.persistence.ContactProvider;
import at.reder.mti.api.persistence.EntityProvider;
import at.reder.mti.api.persistence.ProviderException;
import at.reder.mti.api.persistence.ProviderStartup;
import at.reder.mti.api.utils.MTIUtils;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.netbeans.api.progress.ProgressHandle;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author Wolfgang Reder
 */
@ServiceProviders({
  @ServiceProvider(service = ContactProvider.class, path = "firebirdsql"),
  @ServiceProvider(service = EntityProvider.class, path = "firebirdsql")})
public final class FBContactProvider extends FBAbstractProvider<UUID, Contact> implements ContactProvider
{

  private final int MAX_STEPPING = 1;
  private final UUID ID = UUID.fromString("6bdcbba0-70c1-476b-861e-f60ed8eb26e1");
  private final ProviderStartup startup = this::doTestDatabase;
  private final Lookup myLookup = Lookups.singleton(startup);
  private static final Map<UUID, WeakReference<Contact>> floating = new HashMap<>();

  private void doTestDatabase(ProgressHandle handle)
  {
    try (Connection conn = getConnection()) {
      int currentStepping = getStepping(conn, ID);
      if (currentStepping < MAX_STEPPING) {
        DatabaseMetaData meta = conn.getMetaData();
        if (currentStepping == 0) {
          try (ResultSet rs = meta.getTables(null, null, "contact", new String[]{"TABLE"})) {
            if (!rs.next()) {
              try (Statement stmt = conn.createStatement()) {
                stmt.execute("create table contact (\n"
                                     + "id uuid not null,\n"
                                     + "lastname varchar(128) not null,\n"
                                     + "firstname varchar(128) not null,\n"
                                     + "address1 varchar(128) default '' not null,\n"
                                     + "address2 varchar(128) default '' not null,\n"
                                     + "zip varchar(10) default '' not null,\n"
                                     + "city varchar(128) default '' not null,\n"
                                     + "country varchar(128) default '' not null,\n"
                                     + "emailshop varchar(128),\n"
                                     + "emailservice varchar(128),\n"
                                     + "memo blob sub_type text,\n"
                                     + "phone1 varchar(128),\n"
                                     + "phone2 varchar(128),\n"
                                     + "fax varchar(128),\n"
                                     + "www_shop varchar(128),\n"
                                     + "www varchar(128),\n"
                                     + "visible integer default 1 not null,\n"
                                     + "last_modified timestamp default 'now' not null,"
                                     + "constraint pk_contact primary key(id))");
                stmt.execute("create index ndx$contact_lm on contact(last_modified)");
                stmt.execute("create index ndx$contact_name on contact(lastname,firstname)");
                stmt.execute("create table contact2type (\n"
                                     + "contact uuid not null,\n"
                                     + "contacttype varchar(50) not null,"
                                     + "constraint pk_contact2type primary key(contact,contacttype),"
                                     + "constraint fk_contact2type_contact foreign key(contact) references contact(id) on update cascade on delete cascade)");
              }
            }
          }
          ++currentStepping;
        }
        setStepping(conn, ID, currentStepping);
      }
    } catch (SQLException | ProviderException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  private URI createURI(String str) throws URISyntaxException
  {
    if (str != null && !str.trim().isEmpty()) {
      return new URI(str);
    }
    return null;
  }

  Contact get(final Connection conn, UUID id) throws SQLException, ProviderException
  {
    List<Contact> result = get(conn, Collections.singleton(id));
    if (result.isEmpty()) {
      return null;
    } else {
      return result.get(0);
    }
  }

  List<Contact> get(final Connection conn, Collection<? extends UUID> keys) throws SQLException, ProviderException
  {
    List<Contact> result = new ArrayList<>(keys.size());
    Contact.BuilderFactory builderFactory = Lookup.getDefault().lookup(Contact.BuilderFactory.class);
    if (builderFactory == null) {
      throw new ProviderException(Bundle.FBAbstractProvider_factory_null(Contact.class.getName()));
    }
    try (PreparedStatement stmt = conn.prepareStatement("select lastname,firstname,address1,address2,zip,city,\n"
                                                                + "country,emailshop,emailservice,memo,\n"
                                                                + "phone1,phone2,fax,last_modified,\n"
                                                                + "www_shop,www\n"
                                                                + "from contact where id=char_to_uuid(?)");
            PreparedStatement stmtType = conn.prepareStatement(
                    "select contacttype from contact2type where contact=char_to_uuid(?)")) {
      for (UUID id : keys) {
        if (id != null) {
          stmt.setString(1, id.toString());
          stmtType.setString(1, id.toString());
          try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
              synchronized (floating) {
                floating.remove(id);
              }
              Contact.Builder builder = builderFactory.createBuilder();
              builder.address1(rs.getString("address1"));
              builder.address2(rs.getString("address2"));
              builder.city(rs.getString("city"));
              builder.country(rs.getString("country"));
              builder.emailShop(createURI(rs.getString("emailshop")));
              builder.emailService(createURI(rs.getString("emailservice")));
              builder.fax(rs.getString("fax"));
              builder.id(id);
              builder.lastModified(sqlTimestamp2Timestamp(rs.getTimestamp("last_modified")));
              builder.memo(rs.getString("memo"));
              builder.lastName(rs.getString("lastname"));
              builder.firstName(rs.getString("firstname"));
              builder.phone1(rs.getString("phone1"));
              builder.phone2(rs.getString("phone2"));
              builder.shopAddress(createURI(rs.getString("www_shop")));
              builder.www(createURI(rs.getString("www")));
              builder.zip(rs.getString("zip"));
              try (ResultSet rs1 = stmtType.executeQuery()) {
                while (rs1.next()) {
                  builder.addType(ContactType.valueOf(rs1.getString("contacttype")));
                }
              }
              result.add(builder.build());
            } else {
              synchronized (floating) {
                WeakReference<Contact> wr = floating.get(id);
                if (wr != null) {
                  Contact c = wr.get();
                  if (c != null) {
                    result.add(c);
                  } else {
                    floating.remove(id);
                  }
                }
              }
            }
          }
        }
      }
      return result;
    } catch (NullPointerException | URISyntaxException | IllegalArgumentException ex) {
      throw new ProviderException(ex);
    }
  }

  @Override
  public List<Contact> get(Collection<? extends UUID> keys) throws ProviderException
  {
    if (keys == null) {
      return Collections.emptyList();
    }
    try (Connection conn = getConnection()) {
      return get(conn, keys);
    } catch (SQLException ex) {
      throw new ProviderException(ex);
    }
  }

  private static String uri2String(URI u)
  {
    if (u != null) {
      return u.toString();
    }
    return null;
  }

  @Override
  public List<Contact> store(Collection<? extends Contact> entities) throws ProviderException
  {
    if (entities == null) {
      return Collections.emptyList();
    }
    try (Connection conn = getConnection();
            PreparedStatement stmtExists = conn.prepareStatement("select id from contact where id=char_to_uuid(?)");
            PreparedStatement stmtDelete = conn.prepareStatement("delete from contact2type where contact=char_to_uuid(?)");
            PreparedStatement stmtInsert = conn.prepareStatement("insert into contact (lastname,firstname,address1,address2,\n"
                                                                         + "zip,city,country,emailshop,emailservice,\n"
                                                                         + "memo,phone1,phone2,fax,last_modified,\n"
                                                                         + "www_shop,www,id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,char_to_uuid(?))");
            PreparedStatement stmtUpdate = conn.prepareStatement(
                    "update contact set lastname=?,firstname=?,address1=?,address2=?,\n"
                            + "zip=?,city=?,country=?,emailshop=?,emailservice=?,\n"
                            + "memo=?,phone1=?,phone2=?,fax=?,last_modified=?,\n"
                            + "www_shop=?,www=? where id=char_to_uuid(?)");
            PreparedStatement stmtInsertType = conn.prepareStatement("insert into contact2type (contact,contacttype)\n"
                                                                             + "values (char_to_uuid(?),?)")) {
      List<Contact> result = new ArrayList<>(entities.size());
      for (Contact c : entities) {
        if (c != null) {
          stmtExists.setString(1, c.getId().toString());
          boolean exists;
          try (ResultSet rs = stmtExists.executeQuery()) {
            exists = rs.next();
          }
          PreparedStatement stmt;
          if (exists) {
            stmt = stmtUpdate;
            stmtDelete.setString(1, c.getId().toString());
            stmtDelete.executeUpdate();
          } else {
            stmt = stmtInsert;
          }
          stmt.setString(1, MTIUtils.limitString2Len(c.getLastName(), MTIUtils.MAX_NAME_LENGTH));
          stmt.setString(2, MTIUtils.limitString2Len(c.getFirstName(), MTIUtils.MAX_NAME_LENGTH));
          stmt.setString(3, MTIUtils.limitString2Len(c.getAddress1(), MTIUtils.MAX_NAME_LENGTH));
          stmt.setString(4, MTIUtils.limitString2Len(c.getAddress2(), MTIUtils.MAX_NAME_LENGTH));
          stmt.setString(5, MTIUtils.limitString2Len(c.getZip(), 10));
          stmt.setString(6, MTIUtils.limitString2Len(c.getCity(), MTIUtils.MAX_NAME_LENGTH));
          stmt.setString(7, MTIUtils.limitString2Len(c.getCountry(), MTIUtils.MAX_NAME_LENGTH));
          stmt.setString(8, uri2String(c.getEmailShop()));
          stmt.setString(9, uri2String(c.getEmailService()));
          stmt.setString(10, c.getMemo());
          stmt.setString(11, MTIUtils.limitString2Len(c.getPhone1(), MTIUtils.MAX_NAME_LENGTH));
          stmt.setString(12, MTIUtils.limitString2Len(c.getPhone2(), MTIUtils.MAX_NAME_LENGTH));
          stmt.setString(13, MTIUtils.limitString2Len(c.getFax(), MTIUtils.MAX_NAME_LENGTH));
          stmt.setTimestamp(14, date2SQLTimestamp(c.getLastModified()));
          stmt.setString(15, uri2String(c.getShopAddress()));
          stmt.setString(16, uri2String(c.getWWW()));
          stmt.setString(17, c.getId().toString());
          stmt.executeUpdate();
          stmtInsertType.setString(1, c.getId().toString());
          for (ContactType ct : c.getTypes()) {
            stmtInsertType.setString(2, ct.name());
            stmtInsertType.executeUpdate();
          }
          result.add(get(conn, c.getId()));
          synchronized (floating) {
            floating.remove(c.getId());
          }
        }
      }
      return result;
    } catch (SQLException ex) {
      throw new ProviderException(ex);
    }
  }

  @Override
  public void delete(Collection<? extends UUID> keys) throws ProviderException
  {
    if (keys == null) {
      return;
    }
    try (Connection conn = getConnection();
            PreparedStatement stmt = conn.prepareStatement("delete from contact where id=char_to_uuid(?)")) {
      for (UUID key : keys) {
        if (key != null) {
          stmt.setString(1, key.toString());
          stmt.executeUpdate();
        }
      }
    } catch (SQLException ex) {
      throw new ProviderException(ex);
    }
    synchronized (floating) {
      floating.keySet().removeAll(keys);
    }
  }

  @Override
  public List<UUID> getAllIds() throws ProviderException
  {
    try (Connection conn = getConnection()) {
      return findAll(conn);
    } catch (SQLException ex) {
      throw new ProviderException(ex);
    }
  }

  @Override
  public List<UUID> getAllIdsByType(ContactType type) throws ProviderException
  {
    try (Connection conn = getConnection()) {
      return findByType(conn, type);
    } catch (SQLException ex) {
      throw new ProviderException(ex);
    }
  }

  @Override
  public List<Contact> getAll() throws ProviderException
  {
    try (Connection conn = getConnection()) {
      return get(conn, findAll(conn));
    } catch (SQLException ex) {
      throw new ProviderException(ex);
    }
  }

  @Override
  public List<Contact> getByType(ContactType type) throws ProviderException
  {
    try (Connection conn = getConnection()) {
      return get(conn, findByType(conn, type));
    } catch (SQLException ex) {
      throw new ProviderException(ex);
    }
  }

  private List<UUID> findAll(final Connection conn) throws SQLException
  {
    try (PreparedStatement stmt = conn.prepareStatement("select trim(uuid_to_char(id)) as id from contact");
            ResultSet rs = stmt.executeQuery()) {
      Set<UUID> ids = new HashSet<>();
      while (rs.next()) {
        ids.add(UUID.fromString(rs.getString("id")));
      }
      synchronized (floating) {
        ids.addAll(floating.keySet());
      }
      return new ArrayList<>(ids);
    }
  }

  private List<UUID> findByType(final Connection conn, ContactType type) throws SQLException
  {
    if (type == null) {
      return Collections.emptyList();
    }
    try (PreparedStatement stmt = conn.prepareStatement("select trim(uuid_to_char(contact)) as id \n"
                                                                + "from contact2type where contacttype=?")) {
      stmt.setString(1, type.name());
      Set<UUID> ids = new HashSet<>();
      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          ids.add(UUID.fromString(rs.getString("id")));
        }
      }
      synchronized (floating) {
        floating.entrySet().stream().
                forEach((e) -> {
                  Contact c = e.getValue() != null ? e.getValue().get() : null;
                  if (c != null && c.getTypes().contains(type)) {
                    ids.add(e.getKey());
                  }
                });
      }
      return new ArrayList<>(ids);
    }
  }

  @Override
  public Contact createContact(Contact.Builder builder)
  {
    if (builder != null) {
      UUID id = UUID.randomUUID();
      Contact result = builder.id(id).build();
      synchronized (floating) {
        floating.put(id, new WeakReference<>(result));
      }
      return result;
    }
    return null;
  }

  @Override
  public boolean isFloating(UUID key)
  {
    synchronized (floating) {
      return floating.containsKey(key);
    }
  }

  @Override
  public Lookup getLookup()
  {
    return myLookup;
  }

}
