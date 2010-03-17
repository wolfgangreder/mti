/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.firebirdsql;

import at.motriv.datamodel.ModelCondition;
import at.motriv.datamodel.entities.contact.Manufacturer;
import at.motriv.datamodel.entities.contact.Retailer;
import at.motriv.datamodel.entities.era.Era;
import at.motriv.datamodel.entities.locomotive.Locomotive;
import at.motriv.datamodel.entities.locomotive.LocomotiveItemProvider;
import at.motriv.datamodel.entities.locomotive.impl.DefaultMutableLocomotive;
import at.motriv.datamodel.entities.scale.Scale;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.LabelKeyPair;
import at.mountainsd.dataprovider.api.UniversalSearchRequest;
import at.mountainsd.dataprovider.api.jdbc.JDBCUtilities;
import at.mountainsd.util.Money;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
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
                + "l.WHEELARRAGEMENT,l.KIND,l.LOCCLASS,l.COMPANY,l.COUNTRY,i.NAME,i.DESCRIPTION,i.PRICE,i.DATEOFPURCHASE,"
                + "i.PRODUCTNO,i.MANUFACTURER,i.RETAILER,i.CONDITION,i.MASTERIMAGE "
                + "from LOCOMOTIVE l,INVENTORYOBJECT i "
                + "where l.id=i.id and l.id=?");
        DefaultMutableLocomotive builder = new DefaultMutableLocomotive();
        Map<UUID, Retailer> retailers = new HashMap<UUID, Retailer>();
        Map<UUID, Manufacturer> manufacturer = new HashMap<UUID, Manufacturer>();
        Map<UUID, Era> eras = new HashMap<UUID, Era>();
        Map<UUID, Scale> scales = new HashMap<UUID, Scale>();
        FBEraItemProvider eraProvider = FBEraItemProvider.getInstance();
        FBContactItemProvider contactProvider = FBContactItemProvider.getInstance();
        FBScaleItemProvider scaleProvider = FBScaleItemProvider.getInstance();
        for (UUID id : ids) {
          stmt.setString(1, id.toString());
          rs = stmt.executeQuery();
          if (rs.next()) {
            builder.setCompany(rs.getString("company"));
            builder.setCondition(ModelCondition.valueOf(rs.getString("condition")));
            builder.setCountry(rs.getString("country"));
            builder.setDateOfPurchase(JDBCUtilities.dateFromSqlDate(rs.getDate("dateofpurchase")));
            builder.setDescription(rs.getString("description"));
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
            Manufacturer manu = manufacturer.get(tmp);
            if (manu == null) {
              manu = contactProvider.getManufacturer(tmp);
              manufacturer.put(tmp, manu);
            }
            builder.setManufacturer(manu);
            builder.setMasterImage(null);
            builder.setName(rs.getString("name"));
            builder.setPrice(Money.valueOf(rs.getBigDecimal("price")));
            builder.setProductNumber(rs.getString("productnumber"));
            tmp = JDBCUtilities.getUUID(rs, "retailer");
            Retailer retailer = retailers.get(tmp);
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
          stmt = conn.prepareStatement("delete from inventoryobject where id=?");
          stmt.setString(1, pKey.toString());
          stmt.executeUpdate();
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
      try {
        conn = getConnection();
        if (keyExists(conn, pItem.getId())) {
          update(conn, pItem);
        } else {
          insert(conn, pItem);
        }
      } catch (SQLException e) {
        throw new DataProviderException(e);
      } finally {
        JDBCUtilities.close(conn);
      }
    }
    return pItem;
  }

  private void insert(Connection conn, Locomotive pItem) throws SQLException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private void update(Connection conn, Locomotive pItem) throws SQLException
  {
    throw new UnsupportedOperationException("Not supported yet.");
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
    return getLookupItems("wheelarragement");
  }
}
