/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.scale;

import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.ItemProvider;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface ScaleItemProvider extends ItemProvider<UUID, Scale>
{

  public List<? extends Scale> getAll() throws DataProviderException;

  public List<? extends Scale> getAll(Set<? extends UUID> scales2Ignore) throws DataProviderException;

  public List<? extends Scale> getByFamily(UUID family) throws DataProviderException;
}
