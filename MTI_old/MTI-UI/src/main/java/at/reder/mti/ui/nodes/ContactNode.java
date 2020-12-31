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
import at.reder.mti.api.datamodel.api.DeleteCookie;
import at.reder.mti.api.datamodel.nb.ContactDataLoader;
import at.reder.mti.api.datamodel.nb.helper.AbstractMTINode;
import at.reder.mti.api.datamodel.nb.helper.NullFileObject;
import at.reder.mti.api.datamodel.nb.helper.SaveCookieDataObject;
import at.reder.mti.api.persistence.ContactProvider;
import at.reder.mti.api.persistence.ProviderException;
import at.reder.mti.api.persistence.ProviderLookup;
import at.reder.mti.api.utils.MTIUtils;
import at.reder.mti.ui.contact.EditContactTopComponent;
import java.awt.Image;
import java.io.IOException;
import java.util.List;
import javax.swing.Action;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@Messages({"# {0} - classNames",
           "MTINode_no_provider=Es kann kein Entityprovider für die Klasse {0} gefunden werden.",
           "# {0} - firstName",
           "# {1} - lastName",
           "MTINode_name_pattern={0} {1}",
           "# {0} - firstName",
           "# {1} - lastName",
           "MTINode_name_htmlpattern={0} <b>{1}</b>"})
public final class ContactNode extends AbstractMTINode<Contact, ContactDataLoader>
{

  private Action[] actions;

  ContactNode(Contact c, boolean floating)
  {
    super(c, ContactDataLoader.getInstance(), floating);
    updateDisplayName();
    content.add((OpenCookie) this::openNode);
    content.add((DeleteCookie) this::deleteMe);
  }

  private void openNode()
  {
    EditContactTopComponent tc = EditContactTopComponent.getInstance(this);
    tc.open();
    tc.requestActive();
  }

  @Override
  protected FileObject createFileObject(Contact element, ContactDataLoader loader, SaveCookieDataObject dob)
  {
    return new NullFileObject(this);
  }

  @Override
  public Contact save(Contact c) throws ProviderException
  {
    try {
      ProviderLookup providerLookup = Lookup.getDefault().lookup(ProviderLookup.class);
      ContactProvider cp = providerLookup != null ? providerLookup.lookupProvider(ContactProvider.class) : null;
      if (cp != null) {
        setCurrent(cp.store(c));
        resetFloating();
      } else {
        throw new ProviderException(Bundle.MTINode_no_provider(Contact.class.getName()));
      }
      return getCurrent();
    } finally {

    }
  }

  @Override
  public String getHtmlDisplayName()
  {
    Contact c = getCurrent();
    if (c != null) {
      return Bundle.MTINode_name_htmlpattern(c.getFirstName(), c.getLastName());
    } else {
      return null;
    }
  }

  private void updateDisplayName()
  {
    Contact c = getCurrent();
    if (c != null) {
      setDisplayName(Bundle.MTINode_name_pattern(c.getLastName(), c.getFirstName()));
    } else {
      setDisplayName("???");
    }
  }

  @Override
  protected void setCurrent(Contact newCurrent)
  {
    super.setCurrent(newCurrent);
    updateDisplayName();
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
    List<Action> actionList = MTIUtils.actionsForPath("Nodes/mti/ContactNode");
    actions = new Action[actionList.size()];
    if (!actionList.isEmpty()) {
      actions = actionList.toArray(actions);
    }
  }

  private void deleteMe()
  {
    try {
      destroy();
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  @Override
  public Image getIcon(int type)
  {
    return ImageUtilities.loadImage("at/reder/mti/ui/contact/vcard.png");
  }

}
