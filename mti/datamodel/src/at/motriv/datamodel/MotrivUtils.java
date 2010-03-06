/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import at.mountainsd.dataprovider.api.DataProviderContext;
import javax.sql.DataSource;

/**
 *
 * @author wolfi
 */
public final class MotrivUtils
{

  public static final DataProviderContext MOTRIV_CONTEXT = DataProviderContext.instanceOf(null, "at.motriv.motriv");

  public static void checkDataBase()
  {
  }

  private MotrivUtils()
  {
  }
}
