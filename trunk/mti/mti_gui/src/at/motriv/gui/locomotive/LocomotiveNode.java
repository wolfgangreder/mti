/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.locomotive;

import at.motriv.datamodel.entities.locomotive.Locomotive;
import at.motriv.datamodel.entities.locomotive.LocomotiveLookupConvertor;
import at.mountainsd.dataprovider.api.LabelKeyPair;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.UUID;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataLoaderPool;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author wolfi
 */
public class LocomotiveNode extends AbstractNode
{

  private final InstanceContent content;
  private final LocomotiveDataObject dob;
  private final Action defaultAction;

  public LocomotiveNode(Locomotive loc)
  {
    this(loc, new InstanceContent());
  }

  public LocomotiveNode(LabelKeyPair<UUID> key)
  {
    this(key, new InstanceContent());
  }

  private LocomotiveNode(Locomotive loc, InstanceContent content)
  {
    super(Children.LEAF, new AbstractLookup(content));
    this.content = content;
    if (loc != null) {
      content.add(loc);
      content.add(loc.getId());
      setDisplayName(loc.getName());
    }
    dob = getDataObject();
    defaultAction = initDefaultActions();
  }

  private LocomotiveNode(LabelKeyPair<UUID> key, InstanceContent content)
  {
    super(Children.LEAF, new AbstractLookup(content));
    this.content = content;
    this.content.add(key.getKey());
    this.content.add(key.getKey(), new LocomotiveLookupConvertor());
    setDisplayName(key.getLabel());
    dob = getDataObject();
    defaultAction = initDefaultActions();
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

  private LocomotiveDataObject getDataObject()
  {
    FileObject fo = new LocomotiveFileObject(this);
    try {
      DataLoaderPool.setPreferredLoader(fo, LocomotiveDataLoader.getInstance());
      return (LocomotiveDataObject) DataObject.find(fo);
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  private Action initDefaultActions()
  {
    Action result = getOpenAction();
    content.add(result);
    content.add(new LocomotiveOpenCookie(dob));
    return result;
  }

  @Override
  public Action getPreferredAction()
  {
    return defaultAction;
  }

  @Override
  public Action[] getActions(boolean context)
  {
    return new Action[]{defaultAction};
  }

  private Action getOpenAction()
  {
    return new OpenAction();
  }

  private class OpenAction extends AbstractAction
  {

    private static final long serialVersionUID = 1L;

    public OpenAction()
    {
      Locomotive loc = getLookup().lookup(Locomotive.class);
      String name = "";
      if (loc != null) {
        name = loc.getName();
      }
      name = NbBundle.getMessage(LocomotiveNode.class, "LocomotiveNode.OPEN.name", name);
      this.putValue(AbstractAction.NAME, name);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
      getLookup().lookup(LocomotiveOpenCookie.class).open();
    }
  }
}
