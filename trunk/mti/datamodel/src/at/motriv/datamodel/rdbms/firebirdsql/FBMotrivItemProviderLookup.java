/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.firebirdsql;

import at.motriv.datamodel.spi.AbstractMotrivItemProviderLookup;
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
}
