/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.spi;

import at.mountainsd.dataprovider.api.ItemProvider;
import java.io.IOException;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author wolfi
 */
public class ItemProviderConvertor<R extends ItemProvider<?,?>> implements InstanceContent.Convertor<FileObject,R>
{

  private final Class<? extends R> myClass;

  @SuppressWarnings("unchecked")
  public ItemProviderConvertor(FileObject fo) throws ClassNotFoundException
  {
    String className = fo.getName().replace('-', '.');
    myClass = (Class<? extends R>) Class.forName(className);
  }

  @Override
  @SuppressWarnings("unchecked")
  public R convert(FileObject t)
  {
    try {
      DataObject dob = DataObject.find(t);
      if (dob != null) {
        InstanceCookie cookie = dob.getLookup().lookup(InstanceCookie.class);
        if (cookie!=null) {
          Class<?> foClass = cookie.instanceClass();
          if (foClass.isAssignableFrom(myClass)) {
            return (R)cookie.instanceCreate();
          }
        }
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    } catch (ClassNotFoundException ex) {
      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  @Override
  public Class<? extends R> type(FileObject t)
  {
    return myClass;
  }

  @Override
  public String id(FileObject t)
  {
    return t.getPath();
  }

  @Override
  public String displayName(FileObject t)
  {
    return t.getNameExt();
  }
}
