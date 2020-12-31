/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.nb.helper;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataLoader;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.util.HelpCtx;

/**
 *
 * @author Wolfgang Reder
 */
public class NullDataObject extends DataObject
{

  public NullDataObject(FileObject fo, DataLoader lo) throws DataObjectExistsException
  {
    super(fo, lo);
  }

  @Override
  public boolean isDeleteAllowed()
  {
    return false;
  }

  @Override
  public boolean isCopyAllowed()
  {
    return false;
  }

  @Override
  public boolean isMoveAllowed()
  {
    return false;
  }

  @Override
  public boolean isRenameAllowed()
  {
    return false;
  }

  @Override
  public HelpCtx getHelpCtx()
  {
    return HelpCtx.DEFAULT_HELP;
  }

  @Override
  protected DataObject handleCopy(DataFolder f) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  protected void handleDelete() throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  protected FileObject handleRename(String name) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  protected FileObject handleMove(DataFolder df) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  protected DataObject handleCreateFromTemplate(DataFolder df, String name) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
