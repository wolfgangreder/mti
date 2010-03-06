/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.derby;

import at.motriv.datamodel.spi.AbstractMotrivItemProviderLookup;
import net.jcip.annotations.ThreadSafe;

/**
 *
 * @author wolfi
 */
@ThreadSafe
public final class DerbyMotrivItemProviderLookup extends AbstractMotrivItemProviderLookup
{

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
