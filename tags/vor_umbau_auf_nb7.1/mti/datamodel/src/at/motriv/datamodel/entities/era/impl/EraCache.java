/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.era.impl;

import at.motriv.datamodel.entities.era.Era;
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
public final class EraCache
{

  private static final EraCache instance = new EraCache();
  private final Map<UUID, WeakReference<Era>> cache = new ConcurrentHashMap<UUID, WeakReference<Era>>();

  public static EraCache getInstance()
  {
    return instance;
  }

  private EraCache()
  {
  }

  public Era get(UUID id, Callable<Era> getter) throws Exception
  {
    WeakReference<Era> ref = cache.get(id);
    Era result = ref != null ? ref.get() : null;
    if (result == null) {
      synchronized (cache) {
        result = getter.call();
        cache.put(id, new WeakReference<Era>(result));
      }
    }
    return result;
  }

  public void store(Era era)
  {
    cache.put(era.getId(), new WeakReference<Era>(era));
  }

  public void remove(UUID id)
  {
    cache.remove(id);
  }

  public void clear()
  {
    cache.clear();
  }
}
