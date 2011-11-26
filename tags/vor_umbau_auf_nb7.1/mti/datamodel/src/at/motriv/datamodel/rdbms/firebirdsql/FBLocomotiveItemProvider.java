/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.firebirdsql;

import at.motriv.datamodel.Decoder;
import at.motriv.datamodel.externals.External;
import at.motriv.datamodel.ModelCondition;
import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.era.Era;
import at.motriv.datamodel.entities.locomotive.Locomotive;
import at.motriv.datamodel.entities.locomotive.LocomotiveItemProvider;
import at.motriv.datamodel.entities.locomotive.impl.DefaultMutableLocomotive;
import at.motriv.datamodel.entities.scale.Scale;
import at.mountainsd.dataprovider.api.DataProviderEvent;
import at.mountainsd.dataprovider.api.DataProviderEvent.Action;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.LabelKeyPair;
import at.mountainsd.dataprovider.api.UniversalSearchRequest;
import at.mountainsd.dataprovider.api.jdbc.JDBCUtilities;
import at.mountainsd.util.Money;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public class FBLocomotiveItemProvider extends AbstractMotrivFBItemProvider<UUID, Locomotive> implements LocomotiveItemProvider
{

  private static class MyInitializer
  {

    private static FBLocomotiveItemProvider instance = new FBLocomotiveItemProvider();
  }

  public static FBLocomotiveItemProvider getInstance()
  {
    return MyInitializer.instance;
  }

  private FBLocomotiveItemProvider()
  {
  }

  @Override
  public Locomotive get(UUID pKey) throws DataProviderException
  {
    if (pKey != null) {
      Connection conn = null;
      try {
        conn = getConnection();
        return get(conn, Collections.singleton(pKey)).get(pKey);
      } catch (SQLException e) {
        throw new DataProviderException(e);
      } finally {
        JDBCUtilities.close(conn);
      }
    }
    return null;
  }

  @Override
  public List<LabelKeyPair<UUID>> getAllLabels() throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = getConnection();
      stmt = conn.prepareStatement("select i.id,i.name from inventoryobject i,locomotive l where i.id=l.id");
      rs = stmt.executeQuery();
      List<LabelKeyPair<UUID>> result = new LinkedList<LabelKeyPair<UUID>>();
      while (rs.next()) {
        result.add(new LabelKeyPair<UUID>(JDBCUtilities.getUUID(rs, "id"), rs.getString("name")));
      }
      return result;
    } catch (SQLException ex) {
      throw new DataProviderException(ex);
    } finally {
      JDBCUtilities.close(rs, stmt, conn);
    }
  }

  @Override
  public LabelKeyPair<UUID> getLabel(UUID pKey) throws DataProviderException
  {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = getConnection();
      stmt = conn.prepareStatement("select i.id,i.name from inventoryobject i,locomotive l where i.id=l.id and i.id=?");
      stmt.setString(1, pKey.toString());
      rs = stmt.executeQuery();
      if (rs.next()) {
        return new LabelKeyPair<UUID>(JDBCUtilities.getUUID(rs, "id"), rs.getString("name"));
      }
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(rs, stmt, conn);
    }
    return null;
  }

  @Override
  public List<LabelKeyPair<UUID>> getLabelByName(String pName) throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<? extends LabelKeyPair<UUID>> findLabels(Collection<? extends UniversalSearchRequest> requests) throws
          DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<? extends Locomotive> find(Collection<? extends UniversalSearchRequest> request) throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Map<UUID, ? extends Locomotive> get(Collection<UUID> ids) throws DataProviderException
  {
    if (ids != null && !ids.isEmpty()) {
      Connection conn = null;
      try {
        conn = getConnection();
        return get(conn, ids);
      } catch (SQLException e) {
        throw new DataProviderException(e);
      } finally {
        JDBCUtilities.close(conn);
      }
    }
    return new HashMap<UUID, Locomotive>();
  }

  Map<UUID, ? extends Locomotive> get(Connection conn, Collection<UUID> ids) throws SQLException, DataProviderException
  {
    Map<UUID, Locomotive> result = new HashMap<UUID, Locomotive>();
    if (!ids.isEmpty()) {
      PreparedStatement stmt = null;
      ResultSet rs = null;
      try {
        stmt = conn.prepareStatement("select l.id,l.DECODER,l.ERA,l.modelscale,l.LENGTH_,l.WIDTH,l.HEIGHT,l.WEIGHT,"
                                     + "l.WHEELARRANGEMENT,l.KIND,l.LOCCLASS,l.COMPANY,l.COUNTRY,i.NAME,i.DESCRIPTION,i.PRICE,i.DATEOFPURCHASE,"
                                     + "i.PRODUCTNO,i.MANUFACTURER,i.RETAILER,i.CONDITION,i.MASTERIMAGE,i.lastModified,l.locnumber "
                                     + "from LOCOMOTIVE l,INVENTORYOBJECT i "
                                     + "where l.id=i.id and l.id=?");
        DefaultMutableLocomotive builder = new DefaultMutableLocomotive();
        Map<UUID, Contact> retailers = new HashMap<UUID, Contact>();
        Map<UUID, Contact> manufacturer = new HashMap<UUID, Contact>();
        Map<UUID, Era> eras = new HashMap<UUID, Era>();
        Map<UUID, Scale> scales = new HashMap<UUID, Scale>();
        FBEraItemProvider eraProvider = FBEraItemProvider.getInstance();
        FBContactItemProvider contactProvider = FBContactItemProvider.getInstance();
        FBScaleItemProvider scaleProvider = FBScaleItemProvider.getInstance();
        for (UUID id : ids) {
          stmt.setString(1, id.toString());
          rs = stmt.executeQuery();
          if (rs.next()) {
            builder.setLastModified(JDBCUtilities.dateFromSqlTimestamp(rs.getTimestamp("lastModified")));
            builder.setCompany(rs.getString("company"));
            builder.setCondition(ModelCondition.valueOf(rs.getString("condition")));
            builder.setCountry(rs.getString("country"));
            builder.setDateOfPurchase(JDBCUtilities.dateFromSqlDate(rs.getDate("dateofpurchase")));
            builder.setDescription(rs.getString("description"));
            builder.setLocomotiveNumber(rs.getString("locnumber"));
            UUID tmp = JDBCUtilities.getUUID(rs, "era");
            Era era = eras.get(tmp);
            if (era == null) {
              era = eraProvider.get(tmp);
              eras.put(tmp, era);
            }
            builder.setEra(era);
            builder.setHeight(rs.getDouble("height"));
            builder.setId(JDBCUtilities.getUUID(rs, "id"));
            builder.setKind(rs.getString("kind"));
            builder.setLength(rs.getDouble("length_"));
            builder.setLocomotiveClass(rs.getString("locclass"));
            tmp = JDBCUtilities.getUUID(rs, "manufacturer");
            Contact manu = manufacturer.get(tmp);
            if (manu == null) {
              manu = contactProvider.getManufacturer(tmp);
              manufacturer.put(tmp, manu);
            }
            builder.setManufacturer(manu);
            builder.setMasterImage(null);
            builder.setName(rs.getString("name"));
            BigDecimal price = rs.getBigDecimal("price");
            if (price != null) {
              builder.setPrice(Money.valueOf(rs.getBigDecimal("price")));
            } else {
              builder.setPrice(null);
            }
            builder.setProductNumber(rs.getString("productno"));
            tmp = JDBCUtilities.getUUID(rs, "retailer");
            Contact retailer = retailers.get(tmp);
            if (retailer == null) {
              retailer = contactProvider.getRetailer(tmp);
              retailers.put(tmp, retailer);
            }
            builder.setRetailer(retailer);
            tmp = JDBCUtilities.getUUID(rs, "modelscale");
            Scale scale = scales.get(tmp);
            if (scale == null) {
              scale = scaleProvider.get(tmp);
              scales.put(tmp, scale);
            }
            builder.setScale(scale);
            builder.setWeight(rs.getDouble("weight"));
            builder.setWheelArragement(rs.getString("wheelarrangement"));
            builder.setWidth(rs.getDouble("width"));
            result.put(builder.getId(), builder.build());
          }
          rs.close();
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
    if (pKey != null) {
      Connection conn = null;
      PreparedStatement stmt = null;
      try {
        conn = getConnection();
        if (keyExists(conn, pKey)) {
          stmt = conn.prepareStatement("delete from locomotive where id=?");
          stmt.setString(1, pKey.toString());
          stmt.executeUpdate();
          fireDataProviderEvent(new DataProviderEvent(this, Action.DELETED, null, pKey));
        }
      } catch (SQLException e) {
        throw new DataProviderException(e);
      } finally {
        JDBCUtilities.close(stmt, conn);
      }
    }
  }

  boolean keyExists(Connection conn, UUID pKey) throws SQLException
  {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      stmt = conn.prepareStatement("select id from locomotive where id=?");
      stmt.setString(1, pKey.toString());
      rs = stmt.executeQuery();
      return rs.next();
    } finally {
      JDBCUtilities.close(rs, stmt);
    }
  }

  @Override
  public Locomotive store(Locomotive pItem) throws DataProviderException
  {
    if (pItem != null) {
      Connection conn = null;
      Action action;
      try {
        conn = getConnection();
        if (keyExists(conn, pItem.getId())) {
          update(conn, pItem);
          action = Action.MODIFIED;
        } else {
          insert(conn, pItem);
          action = Action.CREATED;
        }
      } catch (SQLException e) {
        throw new DataProviderException(e);
      } finally {
        JDBCUtilities.close(conn);
      }
      fireDataProviderEvent(new DataProviderEvent(this, action, pItem, pItem.getId()));
    }
    return pItem;
  }

  private void insert(Connection conn, Locomotive pItem) throws SQLException
  {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("insert into inventoryobject (name,description,price,dateofpurchase,productno,manufacturer,"
                                   + "retailer,condition,masterimage,lastModified,id)values(?,?,?,?,?,?,?,?,?,?,?)");
      stmt.setString(1, pItem.getName());
      stmt.setString(2, pItem.getDescription());
      if (pItem.getPrice() != null) {
        stmt.setBigDecimal(3, pItem.getPrice().toBigDecimal());
      } else {
        stmt.setBigDecimal(3, null);
      }
      stmt.setDate(4, JDBCUtilities.sqlDateFromDate(pItem.getDateOfPurchase()));
      stmt.setString(5, pItem.getProductNumber());
      stmt.setString(6, pItem.getManufacturer().getId().toString());
      stmt.setString(7, pItem.getRetailer().getId().toString());
      stmt.setString(8, pItem.getCondition().name());
      External masterImage = pItem.getMasterImage();
      if (masterImage != null) {
        stmt.setString(9, masterImage.getId().toString());
      } else {
        stmt.setString(9, null);
      }
      stmt.setTimestamp(10, JDBCUtilities.sqlTimestampFromDate(new Date()));
      stmt.setString(11, pItem.getId().toString());
      stmt.executeUpdate();
      stmt.close();
      stmt = conn.prepareStatement("insert into locomotive (decoder,era,modelscale,length_,width,height,weight,wheelarrangement,"
                                   + "kind,locclass,company,country,locnumber,id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
      Decoder decoder = pItem.getDecoder();
      if (decoder != null) {
        stmt.setString(1, pItem.getDecoder().getId().toString());
      } else {
        stmt.setString(1, null);
      }
      stmt.setString(2, pItem.getEra().getId().toString());
      stmt.setString(3, pItem.getScale().getId().toString());
      stmt.setDouble(4, pItem.getLength());
      stmt.setDouble(5, pItem.getWidth());
      stmt.setDouble(6, pItem.getHeight());
      stmt.setDouble(7, pItem.getWeight());
      stmt.setString(8, pItem.getWheelArragement());
      stmt.setString(9, pItem.getKind());
      stmt.setString(10, pItem.getLocomotiveClass());
      stmt.setString(11, pItem.getCompany());
      stmt.setString(12, pItem.getCountry());
      stmt.setString(13, pItem.getLocomotiveNumber());
      stmt.setString(14, pItem.getId().toString());
      stmt.executeUpdate();
    } finally {
      JDBCUtilities.close(stmt);
    }
  }

  private void update(Connection conn, Locomotive pItem) throws SQLException
  {
    PreparedStatement stmt = null;
    try {
      stmt = conn.prepareStatement("update inventoryobject set name=?,description=?,price=?,dateofpurchase=?,productno=?,"
                                   + "manufacturer=?,retailer=?,condition=?,masterimage=?,lastModified=? where id=?");
      stmt.setString(1, pItem.getName());
      stmt.setString(2, pItem.getDescription());
      if (pItem.getPrice() != null) {
        stmt.setBigDecimal(3, pItem.getPrice().toBigDecimal());
      } else {
        stmt.setBigDecimal(3, null);
      }
      stmt.setDate(4, JDBCUtilities.sqlDateFromDate(pItem.getDateOfPurchase()));
      stmt.setString(5, pItem.getProductNumber());
      stmt.setString(6, pItem.getManufacturer().getId().toString());
      stmt.setString(7, pItem.getRetailer().getId().toString());
      stmt.setString(8, pItem.getCondition().name());
      External masterImage = pItem.getMasterImage();
      if (masterImage != null) {
        stmt.setString(9, masterImage.getId().toString());
      } else {
        stmt.setString(9, null);
      }
      stmt.setTimestamp(10, JDBCUtilities.sqlTimestampFromDate(new Date()));
      stmt.setString(11, pItem.getId().toString());
      stmt.executeUpdate();
      stmt.close();
      stmt = conn.prepareStatement("update locomotive set decoder=?,era=?,modelscale=?,length_=?,width=?,height=?,weight=?,"
                                   + "wheelarrangement=?,kind=?,locclass=?,company=?,country=?,locnumber=? where id=?");
      Decoder decoder = pItem.getDecoder();
      if (decoder != null) {
        stmt.setString(1, pItem.getDecoder().getId().toString());
      } else {
        stmt.setString(1, null);
      }
      stmt.setString(2, pItem.getEra().getId().toString());
      stmt.setString(3, pItem.getScale().getId().toString());
      stmt.setDouble(4, pItem.getLength());
      stmt.setDouble(5, pItem.getWidth());
      stmt.setDouble(6, pItem.getHeight());
      stmt.setDouble(7, pItem.getWeight());
      stmt.setString(8, pItem.getWheelArragement());
      stmt.setString(9, pItem.getKind());
      stmt.setString(10, pItem.getLocomotiveClass());
      stmt.setString(11, pItem.getCompany());
      stmt.setString(12, pItem.getCountry());
      stmt.setString(13, pItem.getLocomotiveNumber());
      stmt.setString(14, pItem.getId().toString());
      stmt.executeUpdate();
    } finally {
      JDBCUtilities.close(stmt);
    }
  }

  private List<String> getLookupItems(String fieldName) throws DataProviderException
  {
    List<String> result = new LinkedList<String>();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      conn = getConnection();
      stmt = conn.prepareStatement(MessageFormat.format("select distinct {0} from locomotive group by {0}", fieldName));
      rs = stmt.executeQuery();
      while (rs.next()) {
        result.add(rs.getString(fieldName));
      }
    } catch (SQLException e) {
      throw new DataProviderException(e);
    } finally {
      JDBCUtilities.close(rs, stmt, conn);
    }
    return result;
  }

  @Override
  public List<String> getLookupKinds() throws DataProviderException
  {
    return getLookupItems("kind");
  }

  @Override
  public List<String> getLookupCompany() throws DataProviderException
  {
    return getLookupItems("company");
  }

  @Override
  public List<String> getLookupCountry() throws DataProviderException
  {
    return getLookupItems("country");
  }

  @Override
  public List<String> getLookupWheelArrangement() throws DataProviderException
  {
    return getLookupItems("wheelarrangement");
  }
}
