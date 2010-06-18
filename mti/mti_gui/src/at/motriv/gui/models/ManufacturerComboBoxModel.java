/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.models;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.config.MotrivConfig;
import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactItemProvider;
import at.mountainsd.dataprovider.api.DataProviderException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.openide.util.Exceptions;

/**
 *
 * @author wolfi
 */
public class ManufacturerComboBoxModel extends AbstractContactComboBoxModel
{

  @Override
  protected List<? extends Contact> getCurrentItems()
  {
    try {
      ContactItemProvider provider = MotrivItemProviderLookup.lookup(ContactItemProvider.class);
      registerAtProvider(provider);
      return provider.getAllManufacturer();
    } catch (DataProviderException ex) {
      Exceptions.printStackTrace(ex);
    }
    return Collections.emptyList();
  }

  @Override
  protected List<? extends Contact> getDefaultItems()
  {
    return Collections.singletonList(DUMMY_CONTACT);
  }

  @Override
  protected Contact getDefaultSelection()
  {
    UUID defaultId = MotrivConfig.getConfigValue(MotrivConfig.KEY_DEFAULT_MANUFACTURER);
    if (defaultId != null) {
      ContactItemProvider provider = MotrivItemProviderLookup.lookup(ContactItemProvider.class);
      if (provider != null) {
        try {
          return provider.getManufacturer(defaultId);
        } catch (DataProviderException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
    }
    return null;
  }
}
