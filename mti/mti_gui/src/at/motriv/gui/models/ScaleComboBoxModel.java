/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.models;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.config.MotrivConfig;
import at.motriv.datamodel.entities.scale.Scale;
import at.motriv.datamodel.entities.scale.ScaleItemProvider;
import at.mountainsd.dataprovider.api.DataProviderException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.openide.util.Exceptions;

/**
 *
 * @author wolfi
 */
public class ScaleComboBoxModel extends DefaultValuesComboBoxModel<Scale>
{

  @Override
  protected Scale getDefaultSelection()
  {
    UUID defaultId = MotrivConfig.getConfigValue(MotrivConfig.KEY_DEFAULT_SCALE);
    if (defaultId != null) {
      ScaleItemProvider provider = MotrivItemProviderLookup.lookup(ScaleItemProvider.class);
      if (provider != null) {
        try {
          return provider.get(defaultId);
        } catch (DataProviderException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
    }
    return null;
  }

  private final static class ScaleComparator implements Comparator<Scale>
  {

    @Override
    public int compare(Scale o1, Scale o2)
    {
      int result = Double.compare(o1.getScale(), o2.getScale());
      if (result == 0) {
        result = -Double.compare(o1.getTrackWidth(), o2.getTrackWidth());
      }
      return result;
    }
  }
  private final Comparator<Scale> comparator = new ScaleComparator();

  @Override
  protected void filterItems(List<? extends Scale> items)
  {
    Set<UUID> scaleFilter = MotrivConfig.getConfigValue(MotrivConfig.KEY_SCALE_FILTER);
    if (scaleFilter != null) {
      Iterator<? extends Scale> iter = items.iterator();
      while (iter.hasNext()) {
        Scale current = iter.next();
        if (!scaleFilter.contains(current.getId())) {
          iter.remove();
        }
      }
    }
  }

  @Override
  protected void sortItems(List<? extends Scale> items)
  {
    Collections.sort(items, comparator);
  }

  @Override
  protected List<? extends Scale> getCurrentItems()
  {
    try {
      ScaleItemProvider provider = MotrivItemProviderLookup.lookup(ScaleItemProvider.class);
      return provider.getAll();
    } catch (DataProviderException ex) {
      Exceptions.printStackTrace(ex);
    }
    return Collections.emptyList();
  }

  @Override
  protected List<? extends Scale> getDefaultItems()
  {
    return Collections.emptyList();
  }
}
