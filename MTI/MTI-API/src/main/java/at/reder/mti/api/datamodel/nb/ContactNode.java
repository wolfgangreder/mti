/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.nb;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.nb.helper.AbstractMTINode;
import at.reder.mti.api.datamodel.nb.helper.NullFileObject;
import at.reder.mti.api.datamodel.nb.helper.SaveCookieDataObject;
import at.reder.mti.api.persistence.ContactProvider;
import at.reder.mti.api.persistence.ProviderException;
import at.reder.mti.api.persistence.ProviderLookup;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author wolfi
 */
@Messages({"# {0} - classNames",
           "MTINode_no_provider=Es kann kein Entityprovider für die Klasse {0} gefunden werden."})
public final class ContactNode extends AbstractMTINode<Contact, ContactDataLoader>
{

  public ContactNode(Contact c)
  {
    super(c, ContactDataLoader.getInstance());
  }

  @Override
  protected FileObject createFileObject(Contact element, ContactDataLoader loader, SaveCookieDataObject dob)
  {
    return new NullFileObject(this);
  }

  @Override
  protected void saveCurrent() throws ProviderException
  {
    ProviderLookup providerLookup = Lookup.getDefault().lookup(ProviderLookup.class);
    ContactProvider cp = providerLookup != null ? providerLookup.lookupProvider(ContactProvider.class) : null;
    if (cp != null) {
      cp.store(getCurrent());
    } else {
      throw new ProviderException(Bundle.MTINode_no_provider(Contact.class.getName()));
    }
  }

  @Override
  protected Contact createSaved()
  {
    Contact.Builder builder = Lookup.getDefault().lookup(Contact.BuilderFactory.class).createBuilder();
    return builder.copy(getCurrent()).build();
  }

}
