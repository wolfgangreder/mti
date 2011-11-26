/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.locomotive.model;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.entities.locomotive.LocomotiveItemProvider;
import at.motriv.gui.models.RefreshableStringCoboBoxModel;
import at.mountainsd.dataprovider.api.DataProviderException;
import java.util.Collections;
import java.util.List;
import org.openide.util.Exceptions;

/**
 *
 * @author wolfi
 */
public class WheelArrangementComboBoxModel extends RefreshableStringCoboBoxModel
{

  @Override
  protected List<String> getCurrentItems()
  {
    try {
      LocomotiveItemProvider provider = MotrivItemProviderLookup.lookup(LocomotiveItemProvider.class);
      registerAtProvider(provider);
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
