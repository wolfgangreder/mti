/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.models;

import at.mountainsd.util.CollatorStringComparator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author wolfi
 */
public abstract class DefaultValuesStringComboBoxModel extends DefaultValuesComboBoxModel<String>
{

  private final Comparator<String> comparator;

  protected DefaultValuesStringComboBoxModel()
  {
    comparator = new CollatorStringComparator();
  }

  protected DefaultValuesStringComboBoxModel(Comparator<String> comp)
  {
    if (comp == null) {
      throw new NullPointerException("comparator==null");
    }
    comparator = comp;
  }

  @Override
  protected void sortItems(List<? extends String> items)
  {
    Collections.sort(items, comparator);
  }

  @Override
  protected List<? extends String> getDefaultItems()
  {
    return Collections.emptyList();
  }
}
