/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact;

import at.motriv.datamodel.entities.contact.Contact;
import at.mountainsd.dataprovider.api.LabelKeyPair;
import java.io.IOException;
import java.util.UUID;
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

  public ContactNode(Contact contact)
  {
    this(contact, new InstanceContent());
  }

  public ContactNode(Class<? extends Contact> clazz, LabelKeyPair<UUID> pair)
  {
    this(clazz, pair, new InstanceContent());
  }

  private ContactNode(Contact contact, InstanceContent content)
  {
    super(Children.LEAF, new AbstractLookup(content));
    this.content = content;
    if (contact != null) {
      content.add(contact);
    }
    dob = getDataObject();
  }

  private ContactNode(Class<? extends Contact> clazz, LabelKeyPair<UUID> pair, InstanceContent content)
  {
    super(Children.LEAF, new AbstractLookup(content));
    this.content = content;
    content.add(new ContactLookupKey(pair.getKey(), clazz), new ContactConvertor());
    dob = getDataObject();
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
      }
    }
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
}
