/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact;

import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.ItemProvider;
import at.mountainsd.dataprovider.api.LabelKeyPair;
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

  public List<? extends Contact> getAllRetailers() throws DataProviderException;

  public Contact getRetailer(UUID retailer) throws DataProviderException;

  public List<? extends Contact> getAllManufacturer() throws DataProviderException;

  public Contact getManufacturer(UUID manufacturer) throws DataProviderException;

  public Contact removeFromRetailer(Contact contact) throws DataProviderException;

  public Contact removeFromManufacturer(Contact contact) throws DataProviderException;

  public Contact makeRetailer(Contact contact) throws DataProviderException;

  public Contact makeManufacturer(Contact contact) throws DataProviderException;

  public List<String> getLookupCountries() throws DataProviderException;

  public List<LabelKeyPair<UUID>> getAllLabels(ContactType type) throws DataProviderException;
}
