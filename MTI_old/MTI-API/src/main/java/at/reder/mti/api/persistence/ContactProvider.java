/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.persistence;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.ContactType;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface ContactProvider extends EntityProvider<UUID, Contact>
{

  public List<Contact> getAll() throws ProviderException;

  public List<Contact> getByType(ContactType type) throws ProviderException;

  public List<UUID> getAllIds() throws ProviderException;

  public List<UUID> getAllIdsByType(ContactType type) throws ProviderException;

  public Contact createContact(Contact.Builder builder);

}
