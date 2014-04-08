/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.nb;

import at.reder.mti.api.datamodel.nb.helper.SaveCookieDataObject;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;

/**
 *
 * @author Wolfgang Reder
 */
public class ContactDataObject extends SaveCookieDataObject
{

  public ContactDataObject(FileObject fo) throws DataObjectExistsException
  {
    super(fo, ContactDataLoader.getInstance());
  }

}
