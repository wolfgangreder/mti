/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact;

import at.motriv.datamodel.MotrivUtils;
import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactType;
import at.motriv.gui.MotrivGUIConstants;
import at.mountainsd.dataprovider.api.LabelKeyPair;
import java.io.IOException;
import java.util.UUID;
import javax.swing.Action;
import org.openide.cookies.OpenCookie;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataLoaderPool;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author wolfi
 */
public class ContactNode extends AbstractNode
{

  private final InstanceContent content;
  private final ContactDataObject dob;
  private final Action defaultAction;
  private final Action[] actions;

  public ContactNode(Contact contact)
  {
    this(contact, new InstanceContent());
  }

  public ContactNode(ContactType type, LabelKeyPair<UUID> pair)
  {
    this(type, pair, new InstanceContent());
  }

  private ContactNode(Contact contact, InstanceContent content)
  {
    super(Children.LEAF, new AbstractLookup(content));
    setIconBaseWithExtension("at/motriv/gui/contact/vcard.png");
    this.content = content;
    if (contact != null) {
      setDisplayName(contact.toString());
      content.add(contact);
      content.add(contact.getId());
    }
    dob = getDataObject();
    actions = initActions();
    defaultAction = getDefaultActionFromActions(actions);
    content.add(createOpenCookie());
  }

  private ContactNode(ContactType type, LabelKeyPair<UUID> pair, InstanceContent content)
  {
    super(Children.LEAF, new AbstractLookup(content));
    setIconBaseWithExtension("at/motriv/gui/contact/vcard.png");
    setDisplayName(pair.getLabel());
    this.content = content;
    content.add(pair.getKey(), new ContactConvertor());
    content.add(pair.getKey());
    content.add(createOpenCookie());
    content.add(type);
    dob = getDataObject();
    actions = initActions();
    defaultAction = getDefaultActionFromActions(actions);
  }

  private OpenCookie createOpenCookie()
  {
    return new OpenCookie()
    {

      @Override
      public void open()
      {
        ContactTopComponent.execute(ContactNode.this);
      }
    };
  }

  private void refresh()
  {
    UUID myKey = getLookup().lookup(UUID.class);
    if (myKey != null) {
      this.content.add(myKey, new ContactConvertor());
    } else {
      this.content.remove(myKey, new ContactConvertor());
    }
  }

  public void setDirty(SaveCookie cookie)
  {
    dob.setSaveCookie(cookie);
    synchronized (this) {
      SaveCookie old = getLookup().lookup(SaveCookie.class);
      if (old != null) {
        content.remove(old);
      }
      if (cookie != null) {
        content.add(cookie);
      } else if (old != null) {
        refresh();
      }
    }
  }

  private Action[] initActions()
  {
    return new Action[]{getOpenAction()};
  }

  private Action getDefaultActionFromActions(Action[] actions)
  {
    return actions[0];
  }

  private ContactDataObject getDataObject()
  {
    FileObject fo = new ContactFileObject(this);
    try {
      DataLoaderPool.setPreferredLoader(fo, ContactDataLoader.getInstance());
      return (ContactDataObject) DataObject.find(fo);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  private Action getOpenAction()
  {
    return MotrivUtils.getActionFromFileObject(MotrivGUIConstants.ACTION_OPEN);
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
}
