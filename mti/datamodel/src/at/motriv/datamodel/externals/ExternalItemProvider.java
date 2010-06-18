/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.externals;

import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.ItemProvider;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface ExternalItemProvider extends ItemProvider<UUID, External>
{

  public List<? extends External> getAll() throws DataProviderException;

  public int getItemCount() throws DataProviderException;
}
