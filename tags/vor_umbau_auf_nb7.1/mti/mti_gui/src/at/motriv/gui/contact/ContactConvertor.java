/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactItemProvider;
import at.mountainsd.dataprovider.api.DataProviderException;
import java.util.UUID;
import org.openide.util.Exceptions;
import org.openide.util.lookup.InstanceContent.Convertor;

/**
 *
 * @author wolfi
 */
public class ContactConvertor implements Convertor<UUID, Contact>
{

  @Override
  public Contact convert(UUID t)
  {
    ContactItemProvider provider = MotrivItemProviderLookup.lookup(ContactItemProvider.class);
    try {
      return provider.get(t);
    } catch (DataProviderException ex) {
      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  @Override
  public Class<? extends Contact> type(UUID t)
  {
    return Contact.class;
  }

  @Override
  public String id(UUID t)
  {
    return "contact_" + t.toString();
  }

  @Override
  public String displayName(UUID t)
  {
    return id(t);
  }
}
