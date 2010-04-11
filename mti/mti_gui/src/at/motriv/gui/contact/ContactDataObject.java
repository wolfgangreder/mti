/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact;

import at.mountainsd.dataprovider.api.NullDataObject;
import java.io.IOException;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;

/**
 *
 * @author wolfi
 */
public class ContactDataObject extends NullDataObject implements SaveCookie
{

  private static final long serialVersionUID = 1L;
  private SaveCookie saveCookie;

  public ContactDataObject(FileObject fo, ContactDataLoader ldr) throws DataObjectExistsException
  {
    super(fo, ldr);
    if (!(fo instanceof ContactFileObject)) {
      throw new IllegalArgumentException("fo not instanceof ContactFileObject");
    }
  }

  public ContactNode getNode()
  {
    return (ContactNode) getContactFile().getNode();
  }

  public ContactFileObject getContactFile()
  {
    return (ContactFileObject) getPrimaryFile();
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
