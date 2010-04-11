/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactItemProvider;
import at.motriv.datamodel.entities.contact.Manufacturer;
import at.motriv.datamodel.entities.contact.Retailer;
import at.mountainsd.dataprovider.api.DataProviderException;
import org.openide.util.Exceptions;
import org.openide.util.lookup.InstanceContent.Convertor;

/**
 *
 * @author wolfi
 */
public class ContactConvertor implements Convertor<ContactLookupKey, Contact>
{

  @Override
  public Contact convert(ContactLookupKey t)
  {
    ContactItemProvider provider = MotrivItemProviderLookup.lookup(ContactItemProvider.class);
    try {
      if (Manufacturer.class.isAssignableFrom(t.getClassKey())) {
        return provider.getManufacturer(t.getId());
      } else if (Retailer.class.isAssignableFrom(t.getClassKey())) {
        return provider.getRetailer(t.getId());
      } else {
        return provider.get(t.getId());
      }
    } catch (DataProviderException ex) {
      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  @Override
  public Class<? extends Contact> type(ContactLookupKey t)
  {
    return t.getClassKey();
  }

  @Override
  public String id(ContactLookupKey t)
  {
    return "contact_" + t.toString();
  }

  @Override
  public String displayName(ContactLookupKey t)
  {
    return id(t);
  }
}
