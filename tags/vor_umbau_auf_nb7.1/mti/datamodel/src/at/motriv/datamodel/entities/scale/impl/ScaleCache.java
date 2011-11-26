/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.scale.impl;

import at.motriv.datamodel.entities.scale.Scale;
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
public final class ScaleCache
{

  private static final ScaleCache instance = new ScaleCache();
  private final Map<UUID, WeakReference<Scale>> cache = new ConcurrentHashMap<UUID, WeakReference<Scale>>();

  public static ScaleCache getInstance()
  {
    return instance;
  }

  private ScaleCache()
  {
  }

  public Scale get(UUID id, Callable<Scale> getter) throws Exception
  {
    WeakReference<Scale> ref = cache.get(id);
    Scale result = ref != null ? ref.get() : null;
    if (result == null) {
      synchronized (cache) {
        result = getter.call();
        cache.put(id, new WeakReference<Scale>(result));
      }
    }
    return result;
  }

  public void store(Scale era)
  {
    cache.put(era.getId(), new WeakReference<Scale>(era));
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
