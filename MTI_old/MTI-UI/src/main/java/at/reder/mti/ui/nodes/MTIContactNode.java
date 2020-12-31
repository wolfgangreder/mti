/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.nodes;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.persistence.ContactProvider;
import at.reder.mti.api.persistence.ProviderException;
import at.reder.mti.api.persistence.ProviderLookup;
import at.reder.mti.api.utils.MTIUtils;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@Messages("MTIContactNode_name=Adressen")
public final class MTIContactNode extends AbstractNode
{

  private static final MTIContactNode INSTANCE = new MTIContactNode();

  private static final class MTIContactChildren extends Children.SortedMap<UUID>
  {

    private static class Comp<N extends ContactNode> implements Comparator<N>
    {

      private final Collator coll = Collator.getInstance();

      @Override
      public int compare(ContactNode o1, ContactNode o2)
      {
        Contact c1 = o1.getCurrent();
        Contact c2 = o2.getCurrent();
        int result = coll.compare(c1.getLastName(), c2.getLastName());
        if (result == 0) {
          result = coll.compare(c1.getFirstName(), c2.getFirstName());
          if (result == 0) {
            result = o1.getId().compareTo(o2.getId());
          }
        }
        return result;
      }

    };

    public MTIContactChildren()
    {
      setComparator(new Comp());
    }

    protected void add(final Collection<? extends ContactNode> nodeCollection)
    {
      if (nodeCollection != null && !nodeCollection.isEmpty()) {
        MUTEX.writeAccess(() -> {
          nodeCollection.stream().
                  filter((node) -> (node != null)).
                  forEach((node) -> {
                    nodes.put(node.getId(), node);
                  });
          refresh();
        });

      }
    }

    protected void remove(final Collection<? extends ContactNode> nodesCollection)
    {
      if (nodesCollection != null && !nodesCollection.isEmpty()) {
        ProviderLookup providerLookup = Lookup.getDefault().lookup(ProviderLookup.class);
        final ContactProvider cp = providerLookup != null ? providerLookup.lookupProvider(ContactProvider.class) : null;
        MUTEX.writeAccess(() -> {
          nodesCollection.stream().
                  filter((node) -> (node != null)).
                  forEach((node) -> {
                    try {
                      if (cp != null) {
                        cp.delete(node.getId());
                      }
                      MUTEX.writeAccess(() -> {
                        nodes.remove(node.getId());
                        refresh();
                      });
                    } catch (ProviderException ex) {
                      Exceptions.printStackTrace(ex);
                    }
                  });
          refresh();
        });

      }
    }

    @Override
    protected void addNotify()
    {
      try {
        ProviderLookup providerLookup = Lookup.getDefault().lookup(ProviderLookup.class);
        ContactProvider cp = providerLookup != null ? providerLookup.lookupProvider(ContactProvider.class) : null;
        final List<Contact> ids = cp != null ? cp.getAll() : null;
        if (ids != null && !ids.isEmpty()) {
          List<ContactNode> nodeList = new ArrayList<>(ids.size());
          ids.stream().
                  filter((c) -> (c != null)).
                  forEach((c) -> {
                    nodeList.add(new ContactNode(c, cp != null ? cp.isFloating(c.getId()) : true));
                  });
          add(nodeList);
        }
      } catch (ProviderException ex) {
        Exceptions.printStackTrace(ex);
      }
    }

    protected Contact addContact(Contact.Builder contactBuilder)
    {
      if (contactBuilder != null) {
        try {
          ProviderLookup providerLookup = Lookup.getDefault().lookup(ProviderLookup.class);
          ContactProvider cp = providerLookup != null ? providerLookup.lookupProvider(ContactProvider.class) : null;
          if (cp != null) {
            Set<UUID> ids = new HashSet<>(cp.getAllIds());
            Contact contact = cp.createContact(contactBuilder);
            ids.add(contact.getId());
            add(Collections.singleton(new ContactNode(contact, cp.isFloating(contact.getId()))));
            return contact;
          }
        } catch (ProviderException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
      return null;
    }

    @Override
    protected void removeNotify()
    {
      MUTEX.writeAccess(() -> {
        nodes.clear();
        refresh();
      });
    }

    public ContactNode findNode(final UUID key)
    {
      return MUTEX.readAccess((org.openide.util.Mutex.Action<ContactNode>) () -> (ContactNode) nodes.get(key));
    }

    void removeNode(final ContactNode node)
    {
      try {
        ProviderLookup providerLookup = Lookup.getDefault().lookup(ProviderLookup.class);
        final ContactProvider cp = providerLookup != null ? providerLookup.lookupProvider(ContactProvider.class) : null;
        if (cp != null) {
          cp.delete(node.getId());
        }
        MUTEX.writeAccess(() -> {
          nodes.remove(node.getId());
          refresh();
        });
      } catch (ProviderException ex) {
        Exceptions.printStackTrace(ex);
      }
    }

    @Override
    public boolean remove(Node[] arr)
    {
      if (arr != null && arr.length > 0) {
        List<ContactNode> nodeList = new ArrayList<>(arr.length);
        for (Node n : arr) {
          if (n instanceof ContactNode) {
            nodeList.add((ContactNode) n);
          }
        }
        remove(nodeList);
      }
      return true;

    }

    @Override
    public boolean add(Node[] arr)
    {
      if (arr != null && arr.length > 0) {
        List<ContactNode> nodeList = new ArrayList<>(arr.length);
        for (Node n : arr) {
          if (n instanceof ContactNode) {
            nodeList.add((ContactNode) n);
          }
        }
        add(nodeList);
      }
      return true;
    }

    @Override
    protected void remove(UUID key)
    {
      ContactNode node = findNode(key);
      remove(Collections.singleton(node));
    }

  }

  public static MTIContactNode getInstance()
  {
    return INSTANCE;
  }

  private Action[] actions;
  private final MTIContactChildren children;

  private MTIContactNode()
  {
    this(new MTIContactChildren());
  }

  private MTIContactNode(MTIContactChildren children)
  {
    super(children);
    this.children = children;
  }

  @Override
  public String getName()
  {
    return Bundle.MTIContactNode_name();
  }

  @Override
  public Action[] getActions(boolean context)
  {
    if (actions == null) {
      initActions();
    }
    assert actions != null;
    return actions;
  }

  private void initActions()
  {
    List<Action> actionList = MTIUtils.actionsForPath("Nodes/mti/MTIContactNode");
    actions = new Action[actionList.size()];
    if (!actionList.isEmpty()) {
      actions = actionList.toArray(actions);
    }
  }

  public ContactNode createNode(Contact.Builder c)
  {
    if (c != null) {
      Contact contact = children.addContact(c);
      return children.findNode(contact.getId());
    }
    return null;
  }

  public void removeNode(ContactNode node)
  {
    if (node != null) {
      children.removeNode(node);
    }
  }

}
