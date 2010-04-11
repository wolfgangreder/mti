/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import at.mountainsd.dataprovider.api.DataProviderContext;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author wolfi
 */
public final class MotrivUtils
{

  public static final DataProviderContext MOTRIV_CONTEXT = DataProviderContext.instanceOf(null, "at.motriv.motriv");
  public static final FileSystem AIR_FS = FileUtil.createMemoryFileSystem();

  public static void checkDataBase()
  {
  }

  private MotrivUtils()
  {
    try {
      FileObject fo = AIR_FS.getRoot().createFolder("entities");
      fo.createFolder("locomotives");
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }
}
