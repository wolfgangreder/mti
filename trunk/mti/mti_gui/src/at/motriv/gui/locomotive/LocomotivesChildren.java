/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.locomotive;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.entities.locomotive.LocomotiveItemProvider;
import at.mountainsd.dataprovider.api.DataProviderEvent;
import at.mountainsd.dataprovider.api.DataProviderEventListener;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.LabelKeyPair;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.swing.SwingUtilities;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.WeakListeners;

/**
 *
 * @author wolfi
 */
public class LocomotivesChildren extends Children.Keys<LabelKeyPair<UUID>> implements DataProviderEventListener
{

  private DataProviderEventListener weakListener;

  @Override
  protected Node[] createNodes(LabelKeyPair<UUID> key)
  {
    return new Node[]{new LocomotiveNode(key)};
  }

  public void refreshItems()
  {
    addNotify();
  }

  @Override
  protected void addNotify()
  {
    LocomotiveItemProvider provider = MotrivItemProviderLookup.lookup(LocomotiveItemProvider.class);
    if (provider != null) {
      if (weakListener == null) {
        weakListener = WeakListeners.create(DataProviderEventListener.class, this, provider);
        provider.addDataProviderEventListener(weakListener);
      }
      try {
        List<LabelKeyPair<UUID>> tmp = provider.getAllLabels();
        setKeys(tmp);
      } catch (DataProviderException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  }

  @Override
  protected void removeNotify()
  {
    Set<LabelKeyPair<UUID>> tmp = Collections.emptySet();
    setKeys(tmp);
    if (weakListener != null) {
      LocomotiveItemProvider provider = MotrivItemProviderLookup.lookup(LocomotiveItemProvider.class);
      provider.removeDataProviderEventListener(weakListener);
      weakListener = null;
    }
  }

  @Override
  public void handleDataProviderEvent(DataProviderEvent event)
  {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(new Runnable()
      {

        @Override
        public void run()
        {
          addNotify();
        }
      });
    } else {
      addNotify();
    }
  }
}
