/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.nb.helper;

import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.util.actions.SystemAction;

/**
 *
 * @author Wolfgang Reder
 */
public class NullFileSystem extends FileSystem
{

  private static final long serialVersionUID = 1L;
  private static final NullFileSystem instance = new NullFileSystem();

  public static NullFileSystem getInstance()
  {
    return instance;
  }

  private NullFileSystem()
  {
  }

  @Override
  public String getDisplayName()
  {
    return "NullFileSystem";
  }

  @Override
  public boolean isReadOnly()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public FileObject getRoot()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public FileObject findResource(String name)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public SystemAction[] getActions()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
