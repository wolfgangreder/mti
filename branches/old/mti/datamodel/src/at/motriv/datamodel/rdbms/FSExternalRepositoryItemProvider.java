/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms;

import at.motriv.datamodel.MotrivUtils;
import at.motriv.datamodel.externals.ExternalRepository;
import at.motriv.datamodel.externals.ExternalRepositoryItemProvider;
import at.motriv.datamodel.externals.ExternalRepositoryXMLSupport;
import at.mountainsd.dataprovider.api.AbstractItemProvider;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.LabelKeyPair;
import at.mountainsd.util.XMLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.openide.filesystems.FileAttributeEvent;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileEvent;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileRenameEvent;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public class FSExternalRepositoryItemProvider extends AbstractItemProvider<UUID, ExternalRepository> implements
        ExternalRepositoryItemProvider
{

  private static class MyInitializer
  {

    private static final FSExternalRepositoryItemProvider instance = new FSExternalRepositoryItemProvider();
  }

  public static final FSExternalRepositoryItemProvider getInstance()
  {
    return MyInitializer.instance;
  }
  private final FileObject folder;
  private final FileChangeListener myChangeListener = new FileChangeListener()
  {

    @Override
    public void fileFolderCreated(FileEvent fe)
    {
    }

    @Override
    public void fileDataCreated(FileEvent fe)
    {
      FSExternalRepositoryItemProvider.this.fileDataCreated(fe);
    }

    @Override
    public void fileChanged(FileEvent fe)
    {
      FSExternalRepositoryItemProvider.this.fileChanged(fe);
    }

    @Override
    public void fileDeleted(FileEvent fe)
    {
      FSExternalRepositoryItemProvider.this.fileDeleted(fe);
    }

    @Override
    public void fileRenamed(FileRenameEvent fe)
    {
    }

    @Override
    public void fileAttributeChanged(FileAttributeEvent fe)
    {
    }
  };
  private final Map<UUID, ExternalRepository> cache = new ConcurrentHashMap<UUID, ExternalRepository>();

  private FSExternalRepositoryItemProvider()
  {
    super(MotrivUtils.MOTRIV_CONTEXT);
    folder = FileUtil.getConfigFile("Repositories");
    initCache();
    folder.addRecursiveListener(myChangeListener);
  }

  private void initCache()
  {
    Enumeration<? extends FileObject> fos = folder.getData(true);
    while (fos.hasMoreElements()) {
      readFromFileObject(fos.nextElement());
    }
  }

  private void removeFromFileObject(FileObject fo)
  {
    try {
      DataObject dob = DataObject.find(fo);
      ExternalRepositoryXMLSupport xmlSupport = dob.getLookup().lookup(ExternalRepositoryXMLSupport.class);
      if (xmlSupport != null) {
        try {
          Collection<? extends ExternalRepository> reps = xmlSupport.loadEntitiesFromStream(null);
          for (ExternalRepository r : reps) {
            cache.remove(r.getId());
          }
        } catch (IOException ex) {
          Exceptions.printStackTrace(ex);
        } catch (XMLException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
    } catch (DataObjectNotFoundException ex) {
      Exceptions.printStackTrace(ex);
    }

  }

  private void readFromFileObject(FileObject fo)
  {
    try {
      DataObject dob = DataObject.find(fo);
      ExternalRepositoryXMLSupport xmlSupport = dob.getLookup().lookup(ExternalRepositoryXMLSupport.class);
      if (xmlSupport != null) {
        Collection<? extends ExternalRepository> reps = xmlSupport.loadEntitiesFromStream(null);
        for (ExternalRepository r : reps) {
          cache.put(r.getId(), r);
        }
      }
    } catch (Throwable ex) {
      Exceptions.printStackTrace(ex);
    }

  }

  @Override
  protected Lookup createLookup()
  {
    return null;
  }

  @Override
  public ExternalRepository get(UUID pKey) throws DataProviderException
  {
    return cache.get(pKey);
  }

  @Override
  public LabelKeyPair<UUID> getLabel(UUID pKey) throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<LabelKeyPair<UUID>> getAllLabels() throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<LabelKeyPair<UUID>> getLabelByName(String pName) throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void delete(UUID pKey) throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public ExternalRepository store(ExternalRepository pItem) throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean isConnectionActive() throws DataProviderException
  {
    return true;
  }

  @Override
  public List<? extends ExternalRepository> getAll() throws DataProviderException
  {
    return new ArrayList<ExternalRepository>(cache.values());
  }

  private void fileDataCreated(FileEvent fe)
  {
    readFromFileObject(fe.getFile());
  }

  private void fileChanged(FileEvent fe)
  {
    readFromFileObject(fe.getFile());
  }

  private void fileDeleted(FileEvent fe)
  {
    removeFromFileObject(fe.getFile());
  }
}
