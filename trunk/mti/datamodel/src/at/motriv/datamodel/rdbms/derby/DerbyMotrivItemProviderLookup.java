/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.derby;

import at.motriv.datamodel.entities.era.Era;
import at.motriv.datamodel.entities.scale.Scale;
import at.motriv.datamodel.spi.AbstractMotrivItemProviderLookup;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import net.jcip.annotations.ThreadSafe;

/**
 *
 * @author wolfi
 */
@ThreadSafe
public final class DerbyMotrivItemProviderLookup extends AbstractMotrivItemProviderLookup
{

  @Override
  protected void internalCheckEra(Connection conn,
          Collection<? extends Era> defaultEras) throws SQLException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  protected void internalCheckScales(Connection conn,
          Collection<? extends Scale> defaultScales) throws SQLException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private static final class InstanceHolder
  {

    private static final DerbyMotrivItemProviderLookup instance = new DerbyMotrivItemProviderLookup();
  }

  public static DerbyMotrivItemProviderLookup getInstance()
  {
    return InstanceHolder.instance;
  }

  public DerbyMotrivItemProviderLookup()
  {
    super("DataProvider/at.motriv.motriv/ItemProviders/database_provider/jdbc_derby_/providers");
  }
}
