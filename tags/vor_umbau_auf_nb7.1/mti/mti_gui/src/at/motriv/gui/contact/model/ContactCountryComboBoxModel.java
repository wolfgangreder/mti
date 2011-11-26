/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact.model;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.entities.contact.ContactItemProvider;
import at.motriv.gui.models.DefaultValuesStringComboBoxModel;
import at.mountainsd.dataprovider.api.DataProviderEvent;
import at.mountainsd.dataprovider.api.DataProviderEventListener;
import at.mountainsd.dataprovider.api.DataProviderException;
import java.util.Collections;
import java.util.List;
import org.openide.util.Exceptions;
import org.openide.util.WeakListeners;

/**
 *
 * @author wolfi
 */
public class ContactCountryComboBoxModel extends DefaultValuesStringComboBoxModel implements DataProviderEventListener
{

  private DataProviderEventListener weakListener;

  @Override
  protected List<? extends String> getCurrentItems()
  {
    ContactItemProvider provider = MotrivItemProviderLookup.lookup(ContactItemProvider.class);
    if (provider != null) {
      synchronized (this) {
        if (weakListener == null) {
          weakListener = WeakListeners.create(DataProviderEventListener.class, this, provider);
          provider.addDataProviderEventListener(weakListener);
        }
      }
      try {
        return provider.getLookupCountries();
      } catch (DataProviderException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return Collections.emptyList();
  }

  @Override
  public void handleDataProviderEvent(DataProviderEvent event)
  {
    refresh();
  }
}
