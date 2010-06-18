/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.filesystem.db;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.LabelKeyPair;
import at.mountainsd.util.EmptyEnum;
import at.mountainsd.util.PathIterator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.openide.filesystems.AbstractFileSystem;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author wolfi
 */
@ServiceProvider(service = FileSystem.class)
public class DatabaseFileSystem extends AbstractFileSystem
{

  private static final long serialVersionUID = 1L;
  public final AtomicBoolean started = new AtomicBoolean(false);
  public static DatabaseFileSystem instance;

  public static void start()
  {
    instance.started.set(true);
    FileUtil.refreshAll();
  }

  @SuppressWarnings("serial")
  private final class MyList implements AbstractFileSystem.List
  {

    @Override
    public String[] children(String arg0)
    {
      return DatabaseFileSystem.this.children(arg0);
    }
  }

  @SuppressWarnings("serial")
  private final class MyInfo implements AbstractFileSystem.Info
  {

    @Override
    public Date lastModified(String arg0)
    {
      return DatabaseFileSystem.this.lastModified(arg0);
    }

    @Override
    public boolean folder(String arg0)
    {
      return DatabaseFileSystem.this.folder(arg0);
    }

    @Override
    public boolean readOnly(String arg0)
    {
      return DatabaseFileSystem.this.readOnly(arg0);
    }

    @Override
    public String mimeType(String arg0)
    {
      return DatabaseFileSystem.this.mimeType(arg0);
    }

    @Override
    public long size(String arg0)
    {
      return DatabaseFileSystem.this.size(arg0);
    }

    @Override
    public InputStream inputStream(String arg0) throws FileNotFoundException
    {
      return DatabaseFileSystem.this.inputStream(arg0);
    }

    @Override
    public OutputStream outputStream(String arg0) throws IOException
    {
      return DatabaseFileSystem.this.outputStream(arg0);
    }

    @Override
    public void lock(String arg0) throws IOException
    {
      DatabaseFileSystem.this.lock(arg0);
    }

    @Override
    public void unlock(String arg0)
    {
      DatabaseFileSystem.this.unlock(arg0);
    }

    @Override
    public void markUnimportant(String arg0)
    {
      DatabaseFileSystem.this.markUnimportant(arg0);
    }
  }

  @SuppressWarnings("serial")
  private final class MyChange implements AbstractFileSystem.Change
  {

    @Override
    public void createFolder(String arg0) throws IOException
    {
      DatabaseFileSystem.this.createFolder(arg0);
    }

    @Override
    public void createData(String arg0) throws IOException
    {
      DatabaseFileSystem.this.createData(arg0);
    }

    @Override
    public void rename(String arg0, String arg1) throws IOException
    {
      DatabaseFileSystem.this.rename(arg0, arg1);
    }

    @Override
    public void delete(String arg0) throws IOException
    {
      DatabaseFileSystem.this.delete(arg0);
    }
  }

  @SuppressWarnings("serial")
  private final class MyAttr implements AbstractFileSystem.Attr
  {

    @Override
    public Object readAttribute(String arg0, String arg1)
    {
      return DatabaseFileSystem.this.readAttribute(arg0, arg1);
    }

    @Override
    public void writeAttribute(String arg0, String arg1, Object arg2) throws IOException
    {
      DatabaseFileSystem.this.writeAttribute(arg0, arg1, arg2);
    }

    @Override
    public Enumeration<String> attributes(String arg0)
    {
      return DatabaseFileSystem.this.attributes(arg0);
    }

    @Override
    public void renameAttributes(String arg0, String arg1)
    {
      DatabaseFileSystem.this.renameAttributes(arg0, arg1);
    }

    @Override
    public void deleteAttributes(String arg0)
    {
      DatabaseFileSystem.this.deleteAttributes(arg0);
    }
  }

  @SuppressWarnings("serial")
  private final class MyTransfer implements AbstractFileSystem.Transfer
  {

    @Override
    public boolean move(String arg0, Transfer arg1, String arg2) throws IOException
    {
      return DatabaseFileSystem.this.move(arg0, arg1, arg2);
    }

    @Override
    public boolean copy(String arg0, Transfer arg1, String arg2) throws IOException
    {
      return DatabaseFileSystem.this.copy(arg0, arg1, arg2);
    }
  }

  public DatabaseFileSystem()
  {
    instance = this;
    this.list = new MyList();
    this.info = new MyInfo();
    this.change = new MyChange();
    this.attr = new MyAttr();
    this.transfer = new MyTransfer();
  }

  @Override
  public String getDisplayName()
  {
    return NbBundle.getMessage(DatabaseFileSystem.class, "DatabaseFileSystem.displayName");
  }

  @Override
  public boolean isReadOnly()
  {
    return false;
  }

  private DBFileObject getFileObject(String path) throws DataProviderException
  {
    DBFileObjectItemProvider provider = MotrivItemProviderLookup.lookup(DBFileObjectItemProvider.class);
    return provider.get(new PathIterator(path));
  }

  private String[] children(String arg0)
  {
    if (started.get()) {
      DBFileObjectItemProvider provider = MotrivItemProviderLookup.lookup(DBFileObjectItemProvider.class);
      try {
        if (arg0 == null || "".equals(arg0)) {
          DBFileObject fo = provider.getRoot();
          return new String[]{fo.getName()};
        } else {
          java.util.List<LabelKeyPair<UUID>> children = provider.getChildren(new PathIterator(arg0));
          String[] result = new String[children.size()];
          int i = 0;
          for (LabelKeyPair<UUID> p : children) {
            result[i++] = p.getLabel();
          }
          return result;
        }
      } catch (DataProviderException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return null;
  }

  private Date lastModified(String arg0)
  {
    return null;
  }

  private boolean folder(String arg0)
  {
    try {
      DBFileObject tmp = getFileObject(arg0);
      return tmp != null ? tmp.isFolder() : false;
    } catch (DataProviderException ex) {
      Exceptions.printStackTrace(ex);
    }
    return false;
  }

  private boolean readOnly(String arg0)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private String mimeType(String arg0)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private long size(String arg0)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private InputStream inputStream(String arg0) throws FileNotFoundException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private OutputStream outputStream(String arg0) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private void lock(String arg0) throws IOException
  {
  }

  private void unlock(String arg0)
  {
  }

  private void markUnimportant(String arg0)
  {
  }

  private void createFolder(String arg0) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private void createData(String arg0) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private void rename(String arg0, String arg1) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private void delete(String arg0) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private Object readAttribute(String arg0, String arg1)
  {
    return null;
  }

  private void writeAttribute(String arg0, String arg1, Object arg2) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private Enumeration<String> attributes(String arg0)
  {
    return new EmptyEnum<String>();
  }

  private void renameAttributes(String arg0, String arg1)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private void deleteAttributes(String arg0)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private boolean move(String arg0, Transfer arg1, String arg2) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  private boolean copy(String arg0, Transfer arg1, String arg2) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
