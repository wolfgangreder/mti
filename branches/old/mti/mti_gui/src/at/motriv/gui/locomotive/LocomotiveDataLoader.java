/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.locomotive;

import at.mountainsd.dataprovider.api.NullDataLoader;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

/**
 *
 * @author wolfi
 */
public class LocomotiveDataLoader extends NullDataLoader
{

  private static final long serialVersionUID = 1L;
  private static final LocomotiveDataLoader instance = new LocomotiveDataLoader();

  public static LocomotiveDataLoader getInstance()
  {
    return instance;
  }

  private LocomotiveDataLoader()
  {
    super(LocomotiveDataObject.class.getName());
  }

  @Override
  protected DataObject handleFindDataObject(FileObject fo, RecognizedFiles recognized) throws IOException
  {
    return new LocomotiveDataObject(fo, this);
  }
}
