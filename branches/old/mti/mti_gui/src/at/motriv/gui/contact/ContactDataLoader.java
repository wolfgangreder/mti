/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact;

import at.mountainsd.dataprovider.api.NullDataLoader;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataLoader.RecognizedFiles;
import org.openide.loaders.DataObject;

/**
 *
 * @author wolfi
 */
public class ContactDataLoader extends NullDataLoader
{

  private static final long serialVersionUID = 1L;
  private static final ContactDataLoader instance = new ContactDataLoader();

  public static ContactDataLoader getInstance()
  {
    return instance;
  }

  private ContactDataLoader()
  {
    super(ContactDataObject.class.getName());
  }

  @Override
  protected DataObject handleFindDataObject(FileObject fo, RecognizedFiles recognized) throws IOException
  {
    return new ContactDataObject(fo, this);
  }
}
