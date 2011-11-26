/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.externals.model;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.externals.ExternalKind;
import at.motriv.datamodel.externals.ExternalRepository;
import at.motriv.datamodel.externals.ExternalRepositoryItemProvider;
import at.motriv.datamodel.externals.MutableExternalRepository;
import at.motriv.datamodel.externals.impl.DefaultMutableExternalRepository;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.util.Utils;
import java.net.URI;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 *
 * @author wolfi
 */
public class RepositoryListModel implements ComboBoxModel
{

  private final List<MutableExternalRepository> items = new ArrayList<MutableExternalRepository>();
  private final Set<ListDataListener> listeners = new CopyOnWriteArraySet<ListDataListener>();
  private MutableExternalRepository selected;

  private static class Wrapper implements MutableExternalRepository
  {

    private final MutableExternalRepository wrapped;

    private Wrapper(MutableExternalRepository wrapped)
    {
      this.wrapped = wrapped;
    }

    @Override
    public int hashCode()
    {
      return wrapped.hashCode();
    }

    @Override
    public Lookup getLookup()
    {
      return wrapped.getLookup();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj)
    {
      return wrapped.equals(obj);
    }

    @Override
    public MutableExternalRepository getMutator()
    {
      return wrapped.getMutator();
    }

    @Override
    public boolean isReadOnly()
    {
      return wrapped.isReadOnly();
    }

    @Override
    public boolean isImmutable()
    {
      return wrapped.isImmutable();
    }

    @Override
    public URI getURI()
    {
      return wrapped.getURI();
    }

    @Override
    public String getName()
    {
      return wrapped.getName();
    }

    @Override
    public String getDescription(Locale loc)
    {
      return wrapped.getDescription(loc);
    }

    @Override
    public Map<? extends String, ? extends String> getDescriptions()
    {
      return wrapped.getDescriptions();
    }

    @Override
    public Set<ExternalKind> getKinds()
    {
      return wrapped.getKinds();
    }

    @Override
    public UUID getId()
    {
      return wrapped.getId();
    }

    @Override
    public String toString()
    {
      return wrapped.getName();
    }

    @Override
    public void addDescription(String language, String description)
    {
      wrapped.addDescription(language, description);
    }

    @Override
    public void addKind(ExternalKind kind)
    {
      wrapped.addKind(kind);
    }

    @Override
    public void removeDescription(String language)
    {
      wrapped.removeDescription(language);
    }

    @Override
    public void removeKind(ExternalKind kind)
    {
      wrapped.removeKind(kind);
    }

    @Override
    public void setDescriptions(Map<? extends String, ? extends String> values)
    {
      wrapped.setDescriptions(values);
    }

    @Override
    public void setId(UUID id)
    {
      wrapped.setId(id);
    }

    @Override
    public void setImmutable(boolean immutable)
    {
      wrapped.setImmutable(immutable);
    }

    @Override
    public void setKinds(Collection<ExternalKind> kinds)
    {
      wrapped.setKinds(kinds);
    }

    @Override
    public void setName(String name)
    {
      wrapped.setName(name);
    }

    @Override
    public void setReadOnly(boolean readOnly)
    {
      wrapped.setReadOnly(readOnly);
    }

    @Override
    public void setURI(URI uri)
    {
      wrapped.setURI(uri);
    }

    @Override
    public ExternalRepository build()
    {
      return wrapped.build();
    }
  }

  @Override
  public void setSelectedItem(Object anItem)
  {
    if (anItem == null || anItem instanceof ExternalRepository) {
      ExternalRepository old = selected;
      if (anItem == null || anItem instanceof MutableExternalRepository) {
        selected = (MutableExternalRepository) anItem;
      } else {
        selected = ((ExternalRepository) anItem).getMutator();
      }
      if (!Utils.equals(old, selected)) {
        fireSelectedChanged();
      }
    }
  }

  @Override
  public MutableExternalRepository getSelectedItem()
  {
    return selected;
  }

  public void refresh()
  {
    items.clear();
    try {
      ExternalRepositoryItemProvider provider = MotrivItemProviderLookup.lookup(ExternalRepositoryItemProvider.class);
      List<? extends ExternalRepository> tmp = provider.getAll();
      for (ExternalRepository r : tmp) {
        items.add(new Wrapper(r.getMutator()));
      }
      Collections.sort(items, new Comparator<ExternalRepository>()
      {

        Collator coll = Collator.getInstance();

        @Override
        public int compare(ExternalRepository o1, ExternalRepository o2)
        {
          return coll.compare(o1.getName(), o2.getName());
        }
      });
    } catch (DataProviderException ex) {
      Exceptions.printStackTrace(ex);
    }
    fireDataChanged();
  }

  @Override
  public int getSize()
  {
    return items.size();
  }

  @Override
  public MutableExternalRepository getElementAt(int index)
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

  private void fireSelectedChanged()
  {
    ListDataEvent evt = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, -1, -1);
    for (ListDataListener l : listeners) {
      l.contentsChanged(evt);
    }
  }

  private void fireDataChanged()
  {
    ListDataEvent evt = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, items.size() - 1);
    for (ListDataListener l : listeners) {
      l.contentsChanged(evt);
    }
  }

  public MutableExternalRepository createNew()
  {
    DefaultMutableExternalRepository n = new DefaultMutableExternalRepository();
    n.setName(getNewName());
    MutableExternalRepository w = new Wrapper(n);
    items.add(w);
    setSelectedItem(w);
    return w;
  }

  private String getNewName()
  {
    int index = 0;
    String name;
    do {
      name = NbBundle.getMessage(RepositoryListModel.class, "ExternalRepository.default.name", ++index);
    } while (checkName(name) != -1);
    return name;
  }

  public int checkName(String name)
  {
    for (int i = 0; i < items.size(); ++i) {
      if (items.get(i).getName().equals(name)) {
        return i;
      }
    }
    return -1;
  }

  public void store()
  {
    ExternalRepositoryItemProvider provider = MotrivItemProviderLookup.lookup(ExternalRepositoryItemProvider.class);
    for (ExternalRepository e:items) {
      try {
        provider.store(e);
      } catch (DataProviderException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
  }
}
