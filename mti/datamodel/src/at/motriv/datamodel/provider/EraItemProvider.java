/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.provider;

import at.motriv.datamodel.Era;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.ItemProvider;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface EraItemProvider extends ItemProvider<UUID, Era>
{

  public List<? extends Era> getAll() throws DataProviderException;
}
