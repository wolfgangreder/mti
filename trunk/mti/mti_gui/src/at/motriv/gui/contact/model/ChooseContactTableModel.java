/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact.model;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactItemProvider;
import at.motriv.datamodel.entities.contact.ContactType;
import at.mountainsd.dataprovider.api.DataProviderException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;
import org.openide.util.RequestProcessor.Task;

/**
 *
 * @author wolfi
 */
public class ChooseContactTableModel implements TableModel
{

  public static final String PROP_SEARCHSTATE = "searchstate";
  private final List<Contact> items = new ArrayList<Contact>();
  private final Set<TableModelListener> listeners = new CopyOnWriteArraySet<TableModelListener>();
  private ContactType contactFilter;
  private final PropertyChangeSupport propSupport = new PropertyChangeSupport(this);
  private volatile SearchState searchState;
  private volatile String filter;
  private Task update = RequestProcessor.getDefault().create(new Runnable()
  {

    @Override
    public void run()
    {
      innerRefresh();
    }
  });

  public void refresh()
  {
    update.schedule(0);
  }

  private void triggerFilter()
  {
    update.schedule(100);
  }

  public void cancelRefresh()
  {
    update.cancel();
  }

  private void innerRefresh()
  {
    setSearchState(SearchState.SEARCH_RUNNING);
    try {
      ContactItemProvider provider = MotrivItemProviderLookup.lookup(ContactItemProvider.class);
      final List<? extends Contact> result = provider.getContacts(Collections.singleton(new ContactUniversalSerachRequest(filter)),
              contactFilter);
      Collections.sort(result, new Comparator<Contact>()
      {

        Collator coll = Collator.getInstance();

        @Override
        public int compare(Contact o1, Contact o2)
        {
          return coll.compare(o1.getName(), o2.getName());
        }
      });
      SwingUtilities.invokeLater(new Runnable()
      {

        @Override
        public void run()
        {
          items.clear();
          items.addAll(result);
          fireContentsChanged();
          if (items.isEmpty()) {
            setSearchState(SearchState.SEARCH_NOT_FOUND);
          } else {
            setSearchState(SearchState.SEARCH_FINISHED);
          }
        }
      });
    } catch (DataProviderException ex) {
      setSearchState(SearchState.SEARCH_ERROR);
      Exceptions.printStackTrace(ex);
    }
  }

  private boolean equals(Object o1, Object o2)
  {
    return o1 == o2 || o1 != null && o1.equals(o2) || o2 != null && o2.equals(o1);
  }

  public void setFilter(String newFilter)
  {
    if (!equals(filter, newFilter)) {
      filter = newFilter;
      triggerFilter();
    }
  }

  public String getFilter()
  {
    return filter;
  }

  public SearchState getSearchState()
  {
    return searchState;
  }

  private void setSearchState(final SearchState newState)
  {
    final SearchState old = searchState;
    searchState = newState;
    if (old != searchState) {
      SwingUtilities.invokeLater(new Runnable()
      {

        @Override
        public void run()
        {
          propSupport.firePropertyChange(PROP_SEARCHSTATE, old, newState);
        }
      });
    }
  }

  @Override
  public int getRowCount()
  {
    return items.size();
  }

  @Override
  public int getColumnCount()
  {
    return 1;
  }

  @Override
  public String getColumnName(int columnIndex)
  {
    return "";
  }

  @Override
  public Class<?> getColumnClass(int columnIndex)
  {
    return String.class;
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex)
  {
    return false;
  }

  @Override
  public Object getValueAt(int rowIndex, int columnIndex)
  {
    return items.get(rowIndex).toString();
  }

  @Override
  public void setValueAt(Object aValue, int rowIndex, int columnIndex)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void addTableModelListener(TableModelListener l)
  {
    if (l != null) {
      listeners.add(l);
    }
  }

  @Override
  public void removeTableModelListener(TableModelListener l)
  {
    listeners.remove(l);
  }

  private void fireContentsChanged()
  {
    TableModelEvent evt = new TableModelEvent(this);
    for (TableModelListener l:listeners) {
      l.tableChanged(evt);
    }
  }

  public void addPropertyChangeListener(PropertyChangeListener l)
  {
    if (l != null) {
      propSupport.addPropertyChangeListener(l);
    }
  }

  public void addPropertyChangeListener(String propName, PropertyChangeListener l)
  {
    if (l != null) {
      propSupport.addPropertyChangeListener(propName, l);
    }
  }

  public void removePropertyChangeListener(PropertyChangeListener l)
  {
    propSupport.removePropertyChangeListener(l);
  }

  public void removePropertyChangeListener(String propName, PropertyChangeListener l)
  {
    propSupport.removePropertyChangeListener(propName, l);
  }
}
