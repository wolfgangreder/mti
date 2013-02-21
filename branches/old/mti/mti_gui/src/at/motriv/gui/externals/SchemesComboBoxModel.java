/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.externals;

import at.mountainsd.util.Utils;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author wolfi
 */
public class SchemesComboBoxModel implements ComboBoxModel
{

  private static final List<String> items = Collections.unmodifiableList(Arrays.asList("file", "http","nbfs"));
  private final Set<ListDataListener> listeners = new CopyOnWriteArraySet<ListDataListener>();
  private String selected;

  @Override
  public void setSelectedItem(Object anItem)
  {
    String old = selected;
    selected = anItem != null ? anItem.toString() : null;
    if (!Utils.equals(old, selected)) {
      fireSelectionChanged();
    }
  }

  @Override
  public String getSelectedItem()
  {
    return selected;
  }

  @Override
  public int getSize()
  {
    return items.size();
  }

  @Override
  public String getElementAt(int index)
  {
    return items.get(index);
  }

  @Override
  public void addListDataListener(ListDataListener l)
  {
    if (l != null) {
      listeners.add(l);
    }
  }

  @Override
  public void removeListDataListener(ListDataListener l)
  {
    listeners.remove(l);
  }

  private void fireSelectionChanged()
  {
    ListDataEvent evt = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, -1, -1);
    for (ListDataListener l : listeners) {
      l.contentsChanged(evt);
    }
  }
}
