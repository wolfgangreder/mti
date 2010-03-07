/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.firebirdsql;

import at.motriv.datamodel.entities.locomotive.Locomotive;
import at.motriv.datamodel.entities.locomotive.LocomotiveItemProvider;
import at.mountainsd.dataprovider.api.DataProviderException;
import java.util.List;
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
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void delete(UUID pKey) throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Locomotive store(Locomotive pItem) throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<String> getLookupKinds() throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<String> getLookupCompany() throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<String> getLookupCountry() throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<String> getLookupWheelArrangement() throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
