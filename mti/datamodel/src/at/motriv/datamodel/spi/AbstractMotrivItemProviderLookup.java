/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.spi;

import at.motriv.datamodel.ExternalKind;
import at.motriv.datamodel.ModelCondition;
import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactXMLSupport;
import at.motriv.datamodel.entities.era.Era;
import at.motriv.datamodel.entities.era.EraXMLSupport;
import at.motriv.datamodel.entities.scale.Scale;
import at.motriv.datamodel.entities.scale.ScaleXMLSupport;
import at.mountainsd.dataprovider.api.DataProvider;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.ItemProvider;
import at.mountainsd.dataprovider.api.jdbc.JDBCDriverConnection;
import at.mountainsd.dataprovider.api.jdbc.JDBCDriverConnectionProvider;
import at.mountainsd.dataprovider.api.jdbc.JDBCUtilities;
import at.mountainsd.util.XMLException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import javax.sql.DataSource;
import net.jcip.annotations.ThreadSafe;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.xml.sax.InputSource;

/**
 *
 * @author wolfi
 */
@ThreadSafe
public abstract class AbstractMotrivItemProviderLookup extends MotrivItemProviderLookup
{

  private final Lookup myLookup;
  private final InstanceContent content;
  private final FileObject myRoot;

  protected AbstractMotrivItemProviderLookup(String rootPath)
  {
    content = new InstanceContent();
    myLookup = new AbstractLookup(content);
    FileObject root = null;
    try {
      FileSystem sys = FileUtil.getConfigRoot().getFileSystem();
      root = sys.findResource(rootPath);
    } catch (FileStateInvalidException ex) {
      Exceptions.printStackTrace(ex);
    }
    myRoot = root;
    if (myRoot != null) {
      myRoot.addRecursiveListener(new FileChangeListener()
      {

        @Override
        public void fileDataCreated(FileEvent fe)
        {
          myFileDataCreated(fe);
        }

        @Override
        public void fileChanged(FileEvent fe)
        {
          myFileChanged(fe);
        }

        @Override
        public void fileDeleted(FileEvent fe)
        {
          myFileDeleted(fe);
        }

        @Override
        public void fileRenamed(FileRenameEvent fe)
        {
          myFileRenamed(fe);
        }

        @Override
        public void fileAttributeChanged(FileAttributeEvent fe)
        {
          myFileAttributeChanged(fe);
        }

        @Override
        public void fileFolderCreated(FileEvent fe)
        {
        }
      });
      init();
    }
  }

  @Override
  public Lookup getLookup()
  {
    return myLookup;
  }

  private void addProvider(FileObject fo)
  {
    try {
      content.add(fo, new ItemProviderConvertor<ItemProvider<?, ?>>(fo));
    } catch (ClassNotFoundException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  private void readFolder(FileObject folder)
  {
    Enumeration<? extends FileObject> e = folder.getData(false);
    while (e.hasMoreElements()) {
      addProvider(e.nextElement());
    }
  }

  private void init()
  {
    readFolder(myRoot);
    Enumeration<? extends FileObject> e = myRoot.getFolders(true);
    while (e.hasMoreElements()) {
      readFolder(e.nextElement());
    }
  }

  private void myFileDataCreated(FileEvent fe)
  {
    addProvider(fe.getFile());
  }

  private void myFileChanged(FileEvent fe)
  {
    addProvider(fe.getFile());
  }

  private void myFileRenamed(FileRenameEvent fe)
  {
    // for now do nowthing but
    // TODO implement
  }

  private void myFileDeleted(FileEvent fe)
  {
    content.remove(fe);
  }

  private void myFileAttributeChanged(FileAttributeEvent fe)
  {
    addProvider(fe.getFile());
  }

  private DataSource getDataSource()
  {
    DataProvider provider = DataProvider.getProvider(getContext());
    JDBCDriverConnectionProvider connProvider = provider.getLookup().lookup(
            JDBCDriverConnectionProvider.class);
    if (connProvider != null) {
      JDBCDriverConnection dconn = connProvider.getConnection(provider.getContext());
      if (dconn != null) {
        return dconn.getLookup().lookup(DataSource.class);
      }
    }

    return null;
  }

  protected PreparedStatement getEnumSelectStatement(Connection conn, String enumName) throws SQLException
  {
    return conn.prepareStatement("select name from " + enumName);
  }

  protected PreparedStatement getEnumInsertStatement(Connection conn, String enumName) throws SQLException
  {
    return conn.prepareStatement("insert into " + enumName + " (name) values (?)");
  }

  protected void checkEnum(Connection conn, Class<? extends Enum<?>> clazz)
  {
    try {
      Method method_values = clazz.getMethod("values");
      Object result = method_values.invoke(null);
      if (result instanceof Enum<?>[]) {
        Enum<?>[] values = (Enum<?>[]) result;
        String name = clazz.getSimpleName().toUpperCase();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Set<String> names = new HashSet<String>();
        try {
          stmt = getEnumSelectStatement(conn, name);
          rs = stmt.executeQuery();
          while (rs.next()) {
            names.add(rs.getString(1));
          }
          rs.close();
          stmt.close();
          stmt = getEnumInsertStatement(conn, name);
          for (Enum<?> s : values) {
            if (!names.contains(s.name())) {
              stmt.setString(1, s.name());
              stmt.executeUpdate();
            }
          }
        } finally {
          JDBCUtilities.close(rs, stmt);
        }
      }
    } catch (SQLException ex) {
      Exceptions.printStackTrace(ex);
    } catch (IllegalAccessException ex) {
      Exceptions.printStackTrace(ex);
    } catch (IllegalArgumentException ex) {
      Exceptions.printStackTrace(ex);
    } catch (InvocationTargetException ex) {
      Exceptions.printStackTrace(ex);
    } catch (NoSuchMethodException ex) {
      Exceptions.printStackTrace(ex);
    } catch (SecurityException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  protected void checkEnums() throws DataProviderException
  {
    Connection conn = null;
    try {
      conn = getDataSource().getConnection();
      conn.setAutoCommit(false);
      checkEnum(conn, ExternalKind.class);
      checkEnum(conn, ModelCondition.class);
    } catch (SQLException ex) {
      throw new DataProviderException(ex);
    } finally {
      JDBCUtilities.close(conn);
    }
  }

  @Override
  protected void checkDatasourceImpl() throws DataProviderException
  {
    try {
      DataSource ds = getDataSource();
      InputStream is = getModelStream();
      try {
        InputSource source = new InputSource(is);
        Database db = new DatabaseIO().read(source);
        Platform platform = PlatformFactory.createNewPlatformInstance(ds);
        platform.alterTables(db, true);
      } finally {
        if (is != null) {
          is.close();
        }
      }
      checkEnums();
      checkEra();
      checkScales();
      checkContacts();
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    } catch (DdlUtilsException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  protected abstract void internalCheckEra(Connection conn, Collection<? extends Era> defaultEras) throws DataProviderException,
          SQLException;

  private FileObject locateDefaultEra()
  {
    Locale loc = Locale.getDefault();
    FileObject result = FileUtil.getConfigFile("defaultdata/era/era_" + loc.getCountry() + ".era");
    if (result == null) {
      result = FileUtil.getConfigFile("defaultdata/era/era.era");
    }
    return result;
  }

  private void checkEra()
  {
    try {
      FileObject fo = locateDefaultEra();
      DataObject dob = DataObject.find(fo);
      EraXMLSupport xmlSupport = dob.getLookup().lookup(EraXMLSupport.class);
      Collection<? extends Era> defaultEras = xmlSupport.loadEntitiesFromStream(null);
      Connection conn = null;
      try {
        conn = getDataSource().getConnection();
        conn.setAutoCommit(false);
        internalCheckEra(conn, defaultEras);
      } catch (DataProviderException e) {
        Exceptions.printStackTrace(e);
      } catch (SQLException e) {
        Exceptions.printStackTrace(e);
      } finally {
        JDBCUtilities.close(conn);
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    } catch (XMLException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  protected abstract void internalCheckScales(Connection conn, Collection<? extends Scale> defaultScales) throws DataProviderException,
          SQLException;

  private void checkScales()
  {
    try {
      FileObject fo = FileUtil.getConfigFile("defaultdata/scales/scales.scales");
      DataObject dob = DataObject.find(fo);
      ScaleXMLSupport xmlSupport = dob.getLookup().lookup(ScaleXMLSupport.class);
      Collection<? extends Scale> defaultScales = xmlSupport.loadEntitiesFromStream(null);
      Connection conn = null;
      try {
        conn = getDataSource().getConnection();
        conn.setAutoCommit(false);
        internalCheckScales(conn, defaultScales);
      } catch (DataProviderException e) {
        Exceptions.printStackTrace(e);
      } catch (SQLException e) {
        Exceptions.printStackTrace(e);
      } finally {
        JDBCUtilities.close(conn);
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    } catch (XMLException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  protected abstract void internalCheckContacts(Connection conn, Collection<? extends Contact> defaultContacts) throws
          DataProviderException, SQLException;

  private void checkContacts()
  {
    try {
      FileObject fo = FileUtil.getConfigFile("defaultdata/contacts/defaultcontacts.contacts");
      DataObject dob = DataObject.find(fo);
      ContactXMLSupport xmlSupport = dob.getLookup().lookup(ContactXMLSupport.class);
      Collection<? extends Contact> defaultContacts = xmlSupport.loadEntitiesFromStream(null);
      Connection conn = null;
      try {
        conn = getDataSource().getConnection();
        conn.setAutoCommit(false);
        internalCheckContacts(conn, defaultContacts);
      } catch (DataProviderException e) {
        Exceptions.printStackTrace(e);
      } catch (SQLException e) {
        Exceptions.printStackTrace(e);
      } finally {
        JDBCUtilities.close(conn);
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    } catch (XMLException ex) {
      Exceptions.printStackTrace(ex);
    }
  }
}
