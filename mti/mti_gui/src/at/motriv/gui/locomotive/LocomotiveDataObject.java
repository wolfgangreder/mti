/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.locomotive;

import at.mountainsd.dataprovider.api.NullDataLoader;
import at.mountainsd.dataprovider.api.NullDataObject;
import java.io.IOException;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;

/**
 *
 * @author wolfi
 */
public class LocomotiveDataObject extends NullDataObject implements SaveCookie
{

  private static final long serialVersionUID = 1L;
  private SaveCookie saveCookie;

  public LocomotiveDataObject(FileObject fo, NullDataLoader ldr) throws DataObjectExistsException
  {
    super(fo, ldr);
    if (!(fo instanceof LocomotiveFileObject)) {
      throw new IllegalArgumentException("fo not instanceof LocomotiveFileObject");
    }
  }

  public LocomotiveNode getNode()
  {
    return (LocomotiveNode) getLocomotiveFile().getNode();
  }

  public LocomotiveFileObject getLocomotiveFile()
  {
    return (LocomotiveFileObject) getPrimaryFile();
  }

  @Override
  public String getName()
  {
    return this.getPrimaryFile().getName();
  }

  public void setSaveCookie(SaveCookie cookie)
  {
    this.saveCookie = cookie;
    setModified(saveCookie != null);
  }

  public SaveCookie getSaveCookie()
  {
    return saveCookie;
  }

  @Override
  public void save() throws IOException
  {
    if (saveCookie != null) {
      saveCookie.save();
    }
  }
}
