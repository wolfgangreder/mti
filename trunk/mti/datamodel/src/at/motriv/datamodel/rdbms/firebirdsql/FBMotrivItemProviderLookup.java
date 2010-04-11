/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.firebirdsql;

import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.era.Era;
import at.motriv.datamodel.entities.scale.Scale;
import at.motriv.datamodel.spi.AbstractMotrivItemProviderLookup;
import at.mountainsd.dataprovider.api.DataProviderException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import net.jcip.annotations.ThreadSafe;

/**
 *
 * @author wolfi
 */
@ThreadSafe
public final class FBMotrivItemProviderLookup extends AbstractMotrivItemProviderLookup
{

  private static final class InstanceHolder
  {

    private static final FBMotrivItemProviderLookup instance = new FBMotrivItemProviderLookup();
  }

  public static FBMotrivItemProviderLookup getInstance()
  {
    return InstanceHolder.instance;
  }

  public FBMotrivItemProviderLookup()
  {
    super("DataProvider/at.motriv.motriv/ItemProviders/database_provider/jdbc_firebirdsql_/providers");
  }

  @Override
  protected void internalCheckEra(Connection conn,
          Collection<? extends Era> defaultEras) throws DataProviderException, SQLException
  {
    FBEraItemProvider.getInstance().checkEra(conn, defaultEras);
  }

  @Override
  protected void internalCheckScales(Connection conn,
          Collection<? extends Scale> defaultScales) throws DataProviderException, SQLException
  {
    FBScaleItemProvider.getInstance().checkScales(conn, defaultScales);
  }

  @Override
  protected void internalCheckContacts(Connection conn,
          Collection<? extends Contact> defaultContacts) throws DataProviderException, SQLException
  {
    FBContactItemProvider.getInstance().checkContacts(conn, defaultContacts);
  }
}
