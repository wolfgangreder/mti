/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.MotrivUtils;
import at.motriv.datamodel.entities.contact.ContactItemProvider;
import at.motriv.datamodel.entities.contact.ContactType;
import at.motriv.gui.MotrivGUIConstants;
import at.motriv.gui.RefreshCookie;
import at.mountainsd.dataprovider.api.DataProviderEvent;
import at.mountainsd.dataprovider.api.DataProviderEventListener;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.LabelKeyPair;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.swing.Action;
import javax.swing.SwingUtilities;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.WeakListeners;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author wolfi
 */
public class ContactsNode extends AbstractNode
{

  private final InstanceContent content;
  private final Action defaultAction;
  private final Action[] actions;

  public ContactsNode()
  {
    this(new InstanceContent());
  }

  private ContactsNode(InstanceContent content)
  {
    super(new ContactsChildren(), new AbstractLookup(content));
    this.content = content;
    setDisplayName(NbBundle.getMessage(getClass(), "ContactsNode.displayName"));
    setIconBaseWithExtension("at/motriv/gui/contact/vcard.png");
    actions = initActions();
    defaultAction = actions[0];
    content.add(new RefreshCookie()
    {

      @Override
      public void refresh()
      {
        for (Node n : getChildren().getNodes(true)) {
          RefreshCookie cookie = n.getLookup().lookup(RefreshCookie.class);
          if (cookie != null) {
            cookie.refresh();
          }
        }
      }
    });
  }

  private Action[] initActions()
  {
    return new Action[]{MotrivUtils.getActionFromFileObject(MotrivGUIConstants.ACTION_REFRESH)};
  }

  @Override
  public Action getPreferredAction()
  {
    return defaultAction;
  }

  @Override
  public Action[] getActions(boolean context)
  {
    return actions;
  }

  private static class ContactsChildren extends Children.Keys<String>
  {

    @Override
    protected Node[] createNodes(String key)
    {
      return new Node[]{new SubNode(ContactType.MANUFACTURER),
                        new SubNode(ContactType.RETAILER)};
    }

    @Override
    protected void addNotify()
    {
      setKeys(Collections.singletonList(""));
    }
  }

  private static class SubNode extends AbstractNode
  {

    private final InstanceContent content;

    public SubNode(ContactType type)
    {
      this(new InstanceContent(), type);
      String key;
      switch (type) {
        case MANUFACTURER:
          key = "ContactsNode.manufacturer.displayName";
          break;
        case RETAILER:
          key = "ContactsNode.retailer.displayName";
          break;
        default:
          key = "ContactsNode.displayName";
      }
      setDisplayName(NbBundle.getMessage(getClass(), key));
      setIconBaseWithExtension("at/motriv/gui/contact/vcard.png");
    }

    private SubNode(InstanceContent content, ContactType type)
    {
      super(new FilteredContactsChildren(type), new AbstractLookup(content));
      this.content = content;
      this.content.add(new RefreshCookie()
      {

        @Override
        public void refresh()
        {
          ((FilteredContactsChildren) getChildren()).refreshItems();
        }
      });
    }
  }

  private static class FilteredContactsChildren extends Children.Keys<LabelKeyPair<UUID>> implements DataProviderEventListener
  {

    private DataProviderEventListener weakListener;
    private final ContactType type;

    public FilteredContactsChildren(ContactType type)
    {
      this.type = type;
    }

    public void refreshItems()
    {
      addNotify();
    }

    @Override
    protected Node[] createNodes(LabelKeyPair<UUID> key)
    {
      return new Node[]{new ContactNode(type, key)};
    }

    @Override
    protected void addNotify()
    {
      ContactItemProvider provider = MotrivItemProviderLookup.lookup(ContactItemProvider.class);
      if (provider != null) {
        if (weakListener == null) {
          weakListener = WeakListeners.create(DataProviderEventListener.class, this, provider);
          provider.addDataProviderEventListener(weakListener);
        }
        try {
          List<LabelKeyPair<UUID>> tmp = provider.getAllLabels(type);
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
        ContactItemProvider provider = MotrivItemProviderLookup.lookup(ContactItemProvider.class);
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
}
