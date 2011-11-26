/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.models;

import at.mountainsd.dataprovider.api.DataProviderEvent;
import at.mountainsd.dataprovider.api.DataProviderEventListener;
import at.mountainsd.dataprovider.api.ItemProvider;
import java.util.concurrent.atomic.AtomicReference;
import org.openide.util.WeakListeners;

/**
 *
 * @author wolfi
 */
public abstract class RefreshableStringCoboBoxModel extends DefaultValuesStringComboBoxModel
{

  private final AtomicReference<DataProviderEventListener> weakListener = new AtomicReference<DataProviderEventListener>();
  private final DataProviderEventListener strongListener = new DataProviderEventListener()
  {

    @Override
    public void handleDataProviderEvent(DataProviderEvent event)
    {
      refresh();
    }
  };

  protected void registerAtProvider(ItemProvider<?, ?> provider)
  {
    weakListener.compareAndSet(null, WeakListeners.create(DataProviderEventListener.class, strongListener, provider));
  }

  protected void unregisterAtProvider(ItemProvider<?, ?> provider)
  {
    DataProviderEventListener old = weakListener.get();
    while (weakListener.compareAndSet(old, null)) {
      provider.removeDataProviderEventListener(old);
      old = weakListener.get();
    }
  }

  public boolean isRegistered()
  {
    return weakListener.get() != null;
  }
}
