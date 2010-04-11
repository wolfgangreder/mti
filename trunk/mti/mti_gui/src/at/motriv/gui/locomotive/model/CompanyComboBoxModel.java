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
public class CompanyComboBoxModel extends RefreshableStringCoboBoxModel
{

  @Override
  protected List<? extends String> getCurrentItems()
  {
    LocomotiveItemProvider provider = MotrivItemProviderLookup.lookup(LocomotiveItemProvider.class);
    if (provider != null) {
      registerAtProvider(provider);
      try {
        return provider.getLookupCompany();
      } catch (DataProviderException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return Collections.emptyList();
  }
}
