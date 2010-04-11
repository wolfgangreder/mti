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
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

/**
 *
 * @author wolfi
 */
public class LocomotiveKindComboBoxModel extends RefreshableStringCoboBoxModel
{
  private List<String> defaultValues;

  @Override
  protected List<String> getCurrentItems()
  {
    LocomotiveItemProvider provider = MotrivItemProviderLookup.lookup(LocomotiveItemProvider.class);
    registerAtProvider(provider);
    try {
      return provider.getLookupKinds();
    } catch (DataProviderException ex) {
      Exceptions.printStackTrace(ex);
    }
    return Collections.emptyList();
  }

  @Override
  protected List<String> getDefaultItems()
  {
    if (defaultValues==null) {
      defaultValues = new LinkedList<String>();
      ResourceBundle bundle = NbBundle.getBundle("at.motriv.datamodel.entities.locomotive.DefaultLocomotiveKinds");
      Set<String> keys = bundle.keySet();
      for (String k:keys) {
        defaultValues.add(bundle.getString(k));
      }
    }
    return defaultValues;
  }

}
