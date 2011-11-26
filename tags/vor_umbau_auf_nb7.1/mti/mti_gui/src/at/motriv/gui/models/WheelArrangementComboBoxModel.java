/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.models;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.entities.locomotive.LocomotiveItemProvider;
import at.mountainsd.dataprovider.api.DataProviderException;
import java.util.Collections;
import java.util.List;
import org.openide.util.Exceptions;

/**
 *
 * @author wolfi
 */
public class WheelArrangementComboBoxModel extends DefaultValuesStringComboBoxModel
{

  @Override
  protected List<String> getCurrentItems()
  {
    try {
      LocomotiveItemProvider provider = MotrivItemProviderLookup.lookup(LocomotiveItemProvider.class);
      return provider.getLookupWheelArrangement();
    } catch (DataProviderException ex) {
      Exceptions.printStackTrace(ex);
    }
    return Collections.emptyList();
  }

  @Override
  protected List<String> getDefaultItems()
  {
    return Collections.emptyList();
  }
}
