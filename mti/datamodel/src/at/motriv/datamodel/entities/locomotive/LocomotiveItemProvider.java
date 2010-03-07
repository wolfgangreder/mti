/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.locomotive;

import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.ItemProvider;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface LocomotiveItemProvider extends ItemProvider<UUID, Locomotive>
{

  public List<String> getLookupKinds() throws DataProviderException;

  public List<String> getLookupCompany() throws DataProviderException;

  public List<String> getLookupCountry() throws DataProviderException;

  public List<String> getLookupWheelArrangement() throws DataProviderException;
}
