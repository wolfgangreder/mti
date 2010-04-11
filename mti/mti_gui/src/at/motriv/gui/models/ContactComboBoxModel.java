/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.models;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.entities.contact.ContactItemProvider;
import at.motriv.datamodel.entities.contact.Contact;
import at.mountainsd.dataprovider.api.DataProviderEventListener;
import at.mountainsd.dataprovider.api.DataProviderEventListener;
import at.mountainsd.dataprovider.api.DataProviderException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.openide.util.Exceptions;
import org.openide.util.WeakListeners;

/**
 *
 * @author wolfi
 */
public class ContactComboBoxModel extends AbstractContactComboBoxModel<Contact>
{

  @Override
  protected List<? extends Contact> getCurrentItems()
  {
    try {
      ContactItemProvider provider = MotrivItemProviderLookup.lookup(ContactItemProvider.class);
      registerAtProvider(provider);
      List<Contact> tmp = new LinkedList<Contact>();
      tmp.addAll(provider.getAllManufacturer());
      tmp.addAll(provider.getAllRetailers());
      return tmp;
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

}
