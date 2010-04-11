/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.ComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 *
 * @author wolfi
 */
public abstract class DefaultValuesComboBoxModel<V> implements ComboBoxModel
{

  private final Set<ListDataListener> listener = new CopyOnWriteArraySet<ListDataListener>();
  private final List<V> items = new ArrayList<V>();
  private Object selection;

  public void setDefaultValue()
  {
    setSelectedItem(getDefaultSelection());
  }

  public void weakSetDefaultValue()
  {
    if (getSelectedItem() == null) {
      setDefaultValue();
    }
  }

  public void refresh()
  {
    Set<V> current = new HashSet<V>(getCurrentItems());
    current.addAll(getDefaultItems());
    final List<V> tmp = new ArrayList<V>(current);
    filterItems(tmp);
    sortItems(tmp);
    if (SwingUtilities.isEventDispatchThread()) {
      setCurrentItems(tmp);
    } else {
      SwingUtilities.invokeLater(new Runnable()
      {

        @Override
        public void run()
        {
          setCurrentItems(tmp);
        }
      });
    }
  }

  private void setCurrentItems(List<? extends V> newItems)
  {
    items.clear();
    items.addAll(newItems);
    fireContentsChanged();
  }

  protected void filterItems(List<? extends V> items)
  {
  }

  protected abstract void sortItems(List<? extends V> items);

  protected abstract List<? extends V> getCurrentItems();

  protected abstract List<? extends V> getDefaultItems();

  protected abstract V getDefaultSelection();

  @Override
  public void setSelectedItem(Object anItem)
  {
    Object old = selection;
    selection = anItem;
    if ((old != null && !old.equals(selection)) || (selection != null && !selection.equals(old))) {
      fireSelectionChanged();
    }
  }

  @Override
  public Object getSelectedItem()
  {
    return selection;
  }

  @Override
  public int getSize()
  {
    return items.size();
  }

  @Override
  public Object getElementAt(int index)
  {
    return items.get(index);
  }

  @Override
  public void addListDataListener(ListDataListener l)
  {
    if (l != null) {
      listener.add(l);
    }
  }

  @Override
  public void removeListDataListener(ListDataListener l)
  {
    listener.remove(l);
  }

  private void fireSelectionChanged()
  {
    ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, -1, -1);
    for (ListDataListener l : listener) {
      l.contentsChanged(event);
    }
  }

  private void fireContentsChanged()
  {
    ListDataEvent event = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, items.size() - 1);
    for (ListDataListener l : listener) {
      l.contentsChanged(event);
    }
  }
}
