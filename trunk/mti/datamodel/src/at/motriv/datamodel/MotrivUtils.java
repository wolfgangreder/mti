/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import at.mountainsd.dataprovider.api.DataProviderContext;
import java.io.IOException;
import javax.swing.Action;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
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

  public static Action getActionFromFileObject(String file)
  {
    return getActionFromFileObject(FileUtil.getConfigFile(file));
  }
  
  public static Action getActionFromFileObject(FileObject fo)
  {
    if (fo != null) {
      try {
        DataObject dob = DataObject.find(fo);
        if (dob != null) {
          InstanceCookie cookie = dob.getLookup().lookup(InstanceCookie.class);
          if (cookie != null) {
            Object tmp = cookie.instanceCreate();
            if (tmp instanceof Action) {
              return (Action) tmp;
            }
          }
        }
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      } catch (ClassNotFoundException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return null;
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
