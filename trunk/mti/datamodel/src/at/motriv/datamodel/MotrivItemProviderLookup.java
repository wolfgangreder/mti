/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import at.mountainsd.dataprovider.api.DataProvider;
import at.mountainsd.dataprovider.api.DataProviderContext;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.ItemProvider;
import at.mountainsd.dataprovider.api.ItemProviderLookup;
import java.io.InputStream;

/**
 *
 * @author wolfi
 */
public abstract class MotrivItemProviderLookup implements ItemProviderLookup
{

  private static MotrivItemProviderLookup getInstance()
  {
    DataProvider provider = DataProvider.getProvider(MotrivUtils.MOTRIV_CONTEXT);
    if (provider != null) {
      return provider.getLookup().lookup(MotrivItemProviderLookup.class);
    }
    return null;
  }

  @Override
  public DataProviderContext getContext()
  {
    return MotrivUtils.MOTRIV_CONTEXT;
  }

  public static <T extends ItemProvider<?, ?>> T lookup(Class<? extends T> clazz)
  {
    MotrivItemProviderLookup instance = getInstance();
    if (instance != null) {
      return instance.getLookup().lookup(clazz);
    }
    return null;
  }

  public static void checkDatasource() throws DataProviderException
  {
    getInstance().checkDatasourceImpl();
  }

  protected InputStream getModelStream()
  {
    return getClass().getClassLoader().getResourceAsStream("at/motriv/datamodel/rdbms/model.xml");
  }

  protected abstract void checkDatasourceImpl() throws DataProviderException;
}
