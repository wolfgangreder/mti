/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.nb.helper;

import at.reder.mti.api.persistence.ProviderException;
import java.io.IOException;
import java.util.Objects;
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
 */
public abstract class AbstractMTINode<E, DL extends DataLoader> extends AbstractNode
{

  protected final InstanceContent content;
  private E current;
  protected E saved;
  private final SaveCookie saveCookie = () -> {
    try {
      saveCurrent();
    } catch (ProviderException ex) {
      if (ex.getCause() instanceof IOException) {
        throw ((IOException) ex.getCause());
      } else {
        throw new IOException(ex);
      }
    }
  };
  private final SaveCookieDataObject dob;

  protected AbstractMTINode(E e, DL loader)
  {
    this(e, new InstanceContent(), loader, null);
  }

  protected AbstractMTINode(E e, SaveCookieDataObject dob)
  {
    this(e, new InstanceContent(), null, dob);
  }

  private AbstractMTINode(E e, InstanceContent content, DL loader, SaveCookieDataObject dob)
  {
    super(Children.LEAF, new AbstractLookup(content));
    this.content = content;
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
    setCurrent(e);
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

  public final boolean setCurrent(E e)
  {
    current = e;
    return checkModified();
  }

  public void reset()
  {
    current = saved;
    saved = createSaved();
    checkModified();
  }

  protected abstract void saveCurrent() throws ProviderException;

  protected abstract E createSaved();

  protected boolean testModified(E current, E saved)
  {
    return !Objects.equals(current, saved);
  }

  protected final boolean checkModified()
  {
    boolean wasModified = isModified();
    boolean fire = false;
    if (testModified(current, saved)) {
      if (!wasModified) {
        content.add(saveCookie);
        if (dob != null) {
          dob.setSaveCookie(saveCookie);
        }
        fire = true;
      }
    } else {
      content.remove(saveCookie);
      if (dob != null) {
        dob.setSaveCookie(null);
      }
      fire = true;
    }
    if (fire) {
      firePropertyChange("modified", wasModified, !wasModified);
    }
    return isModified();
  }

}
