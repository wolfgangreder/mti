/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact.impl;

import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.Manufacturer;
import at.motriv.datamodel.entities.contact.Retailer;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import net.jcip.annotations.ThreadSafe;

/**
 *
 * @author wolfi
 */
@ThreadSafe
public final class ContactCache
{

  public interface ContactGetter extends Callable<Contact>
  {

    public Class<? extends Contact> getInstanceClass();
  }

  private static final class ContactKey
  {

    private final UUID id;
    private final Class<? extends Contact> itemClass;

    public ContactKey(Contact contact)
    {
      if (contact instanceof Retailer) {
        itemClass = Retailer.class;
      } else if (contact instanceof Manufacturer) {
        itemClass = Manufacturer.class;
      } else {
        itemClass = GenericContact.class;
      }
      this.id = contact.getId();
    }

    public ContactKey(UUID id, Class<? extends Contact> itemClass)
    {
      this.id = id;
      this.itemClass = itemClass;
      if (itemClass != Retailer.class && itemClass != Manufacturer.class && itemClass != GenericContact.class) {
        throw new IllegalArgumentException("invalid itemclass");
      }
    }

    @Override
    public boolean equals(Object obj)
    {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final ContactKey other = (ContactKey) obj;
      if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
        return false;
      }
      if (this.itemClass != other.itemClass && (this.itemClass == null || !this.itemClass.equals(other.itemClass))) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode()
    {
      int hash = 3;
      hash = 37 * hash + (this.id != null ? this.id.hashCode() : 0);
      hash = 37 * hash + (this.itemClass != null ? this.itemClass.hashCode() : 0);
      return hash;
    }
  }
  private static final ContactCache instance = new ContactCache();
  private final Map<ContactKey, WeakReference<Contact>> cache = new ConcurrentHashMap<ContactKey, WeakReference<Contact>>();

  public static ContactCache getInstance()
  {
    return instance;
  }

  private ContactCache()
  {
  }

  public Contact get(UUID id, ContactGetter getter) throws Exception
  {
    ContactKey key = new ContactKey(id, getter.getInstanceClass());
    WeakReference<Contact> ref = cache.get(key);
    Contact result = ref != null ? ref.get() : null;
    if (result == null) {
      synchronized (cache) {
        result = getter.call();
        cache.put(key, new WeakReference<Contact>(result));
      }
    }
    return result;
  }

  public void store(Contact contact)
  {
    ContactKey key = new ContactKey(contact);
    cache.put(key, new WeakReference<Contact>(contact));
  }

  public void remove(UUID id)
  {
    synchronized (cache) {
      cache.remove(new ContactKey(id, GenericContact.class));
      cache.remove(new ContactKey(id, Manufacturer.class));
      cache.remove(new ContactKey(id, Retailer.class));
    }
  }

  public void clear()
  {
    cache.clear();
  }
}
