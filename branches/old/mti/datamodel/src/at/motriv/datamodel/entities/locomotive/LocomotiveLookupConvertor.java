/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.locomotive;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.mountainsd.dataprovider.api.DataProviderException;
import java.util.UUID;
import org.openide.util.Exceptions;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author wolfi
 */
public class LocomotiveLookupConvertor implements InstanceContent.Convertor<UUID, Locomotive>
{

  @Override
  public Locomotive convert(UUID t)
  {
    LocomotiveItemProvider provider = MotrivItemProviderLookup.lookup(LocomotiveItemProvider.class);
    if (provider != null) {
      try {
        return provider.get(t);
      } catch (DataProviderException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return null;
  }

  @Override
  public Class<? extends Locomotive> type(UUID t)
  {
    return Locomotive.class;
  }

  @Override
  public String id(UUID t)
  {
    return "locomotive_" + t.toString();
  }

  @Override
  public String displayName(UUID t)
  {
    return id(t);
  }
}
