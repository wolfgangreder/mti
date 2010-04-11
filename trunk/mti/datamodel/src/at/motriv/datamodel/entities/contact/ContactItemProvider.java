/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact;

import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.ItemProvider;
import at.mountainsd.dataprovider.api.UniversalSearchRequest;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface ContactItemProvider extends ItemProvider<UUID, Contact>
{

  public List<? extends Contact> getContacts(Collection<? extends UniversalSearchRequest> requests,
          ContactType classFilter) throws DataProviderException;

  public List<? extends Retailer> getAllRetailers() throws DataProviderException;

  public Retailer getRetailer(UUID retailer) throws DataProviderException;

  public List<? extends Manufacturer> getAllManufacturer() throws DataProviderException;

  public Manufacturer getManufacturer(UUID manufacturer) throws DataProviderException;

  public void removeFromRetailer(Retailer contact) throws DataProviderException;

  public void removeFromManufacturer(Manufacturer contact) throws DataProviderException;

  public Retailer makeRetailer(Contact contact) throws DataProviderException;

  public Manufacturer makeManufacturer(Contact contact) throws DataProviderException;

  public List<String> getLookupCountries() throws DataProviderException;
}
