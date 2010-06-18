/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact.impl;

import at.motriv.datamodel.entities.contact.Contact;
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

  private static final ContactCache instance = new ContactCache();
  private final Map<UUID, WeakReference<Contact>> cache = new ConcurrentHashMap<UUID, WeakReference<Contact>>();

  public static ContactCache getInstance()
  {
    return instance;
  }

  private ContactCache()
  {
  }

  public Contact get(UUID id, Callable<Contact> getter) throws Exception
  {
    WeakReference<Contact> ref = cache.get(id);
    Contact result = ref != null ? ref.get() : null;
    if (result == null) {
      synchronized (cache) {
        result = getter.call();
        cache.put(id, new WeakReference<Contact>(result));
      }
    }
    return result;
  }

  public void store(Contact contact)
  {
    cache.put(contact.getId(), new WeakReference<Contact>(contact));
  }

  public void remove(UUID id)
  {
    synchronized (cache) {
      cache.remove(id);
    }
  }

  public void clear()
  {
    cache.clear();
  }
}
