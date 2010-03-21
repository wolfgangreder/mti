/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.models;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.entities.scale.Scale;
import at.motriv.datamodel.entities.scale.ScaleItemProvider;
import at.mountainsd.dataprovider.api.DataProviderException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.openide.util.Exceptions;

/**
 *
 * @author wolfi
 */
public class ScaleComboBoxModel extends DefaultValuesComboBoxModel<Scale>
{

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
