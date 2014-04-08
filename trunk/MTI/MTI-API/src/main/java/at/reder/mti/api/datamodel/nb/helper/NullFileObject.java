/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.nb.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.openide.filesystems.FileChangeListener;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileStateInvalidException;
import org.openide.filesystems.FileSystem;
import org.openide.nodes.Node;

/**
 *
 * @author Wolfgang Reder
 */
public class NullFileObject extends FileObject
{

  private final Node node;
  private final Map<String, Object> attr = new ConcurrentHashMap<>();

  public NullFileObject(Node node)
  {
    this.node = node;
  }

  @Override
  public String getName()
  {
    return node.getName();
  }

  @Override
  public String getExt()
  {
    return "";
  }

  @Override
  public void rename(FileLock fl, String string, String string1) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public FileSystem getFileSystem() throws FileStateInvalidException
  {
    return NullFileSystem.getInstance();
  }

  @Override
  public FileObject getParent()
  {
    return null;
  }

  @Override
  public boolean isFolder()
  {
    return false;
  }

  @Override
  public Date lastModified()
  {
    return new Date();
  }

  @Override
  public boolean isRoot()
  {
    return true;
  }

  @Override
  public boolean isData()
  {
    return true;
  }

  @Override
  public boolean isValid()
  {
    return true;
  }

  @Override
  public void delete(FileLock fl) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Object getAttribute(String string)
  {
    return attr.get(string);
  }

  @Override
  public void setAttribute(String string, Object o) throws IOException
  {
    attr.put(string, o);
  }

  @Override
  public Enumeration<String> getAttributes()
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void addFileChangeListener(FileChangeListener fl)
  {
  }

  @Override
  public void removeFileChangeListener(FileChangeListener fl)
  {
  }

  @Override
  public long getSize()
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public InputStream getInputStream() throws FileNotFoundException
  {
    return null;
  }

  @Override
  public OutputStream getOutputStream(FileLock fl) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public FileLock lock() throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  @Deprecated
  public void setImportant(boolean bln)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public FileObject[] getChildren()
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public FileObject getFileObject(String string, String string1)
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public FileObject createFolder(String string) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public FileObject createData(String string, String string1) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  @Deprecated
  public boolean isReadOnly()
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
