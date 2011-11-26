/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.locomotive;

import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.ItemProvider;
import at.mountainsd.dataprovider.api.LabelKeyPair;
import at.mountainsd.dataprovider.api.UniversalSearchRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface LocomotiveItemProvider extends ItemProvider<UUID, Locomotive>
{

  public List<? extends LabelKeyPair<UUID>> findLabels(Collection<? extends UniversalSearchRequest> requests) throws
          DataProviderException;

  public List<? extends Locomotive> find(Collection<? extends UniversalSearchRequest> request) throws DataProviderException;

  public Map<UUID, ? extends Locomotive> get(Collection<UUID> ids) throws DataProviderException;

  public List<String> getLookupKinds() throws DataProviderException;

  public List<String> getLookupCompany() throws DataProviderException;

  public List<String> getLookupCountry() throws DataProviderException;

  public List<String> getLookupWheelArrangement() throws DataProviderException;
}
