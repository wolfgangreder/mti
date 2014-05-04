/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.nb.helper;

import at.reder.mti.api.datamodel.IdItem;
import at.reder.mti.api.persistence.ProviderException;
import java.io.IOException;
import java.util.UUID;
import javax.swing.Action;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataLoader;
import org.openide.loaders.DataLoaderPool;
import org.openide.loaders.DataObject;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Exceptions;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author Wolfgang Reder
 * @param <E>
 * @param <DL>
 */
public abstract class AbstractMTINode<E extends IdItem, DL extends DataLoader> extends AbstractNode
{

  protected final InstanceContent content;
  private E current;
  private final SaveCookieDataObject dob;
  private SaveCookie sc;
  private boolean floating;

  protected AbstractMTINode(E e, DL loader, boolean floating)
  {
    this(e, new InstanceContent(), loader, null, floating);
  }

  protected AbstractMTINode(E e, SaveCookieDataObject dob, boolean floating)
  {
    this(e, new InstanceContent(), null, dob, floating);
  }

  private AbstractMTINode(E e, InstanceContent content, DL loader, SaveCookieDataObject dob, boolean floating)
  {
    super(Children.LEAF, new AbstractLookup(content));
    this.content = content;
    this.floating = floating;
    DataObject tmp = null;
    if (loader != null) {
      try {
        FileObject fo = createFileObject(e, loader, dob);
        DataLoaderPool.setPreferredLoader(fo, loader);
        tmp = DataObject.find(fo);
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    if (tmp instanceof SaveCookieDataObject) {
      this.dob = (SaveCookieDataObject) tmp;
    } else {
      this.dob = dob;
    }
    current = e;
  }

  protected abstract FileObject createFileObject(E element, DL loader, SaveCookieDataObject dob);

  public boolean isModified()
  {
    return getLookup().lookup(SaveCookie.class) != null;
  }

  public final E getCurrent()
  {
    return current;
  }

  protected void setCurrent(E newCurrent)
  {
    this.current = newCurrent;
    setSaveCookie(null);
  }

  public UUID getId()
  {
    E c = getCurrent();
    return c != null ? c.getId() : null;
  }

  public final boolean setSaveCookie(SaveCookie sc)
  {
    boolean wasModified = isModified();
    boolean fire = this.sc != sc;
    if (this.sc != sc) {
      if (this.sc != null) {
        content.remove(this.sc);
      }
      this.sc = sc;
      if (this.sc != null) {
        content.add(sc);
      }
      if (dob != null) {
        dob.setSaveCookie(sc);
      }
    }
    if (fire) {
      firePropertyChange("modified", wasModified, !wasModified);
    }
    return isModified();
  }

  public abstract E save(E newCurrent) throws ProviderException;

  @Override
  public Action getPreferredAction()
  {
    Action[] actions = getActions(true);
    if (actions != null && actions.length > 0) {
      return actions[0];
    }
    return null;
  }

  /**
   *
   * @return {@code true} wenn das object nur im flüchtigen speicher liegt,
   */
  public boolean isFloating()
  {
    synchronized (this) {
      return floating;
    }
  }

  public void resetFloating()
  {
    synchronized (this) {
      floating = false;
    }
  }

}
