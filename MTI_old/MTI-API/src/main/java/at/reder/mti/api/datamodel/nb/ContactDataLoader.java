/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.nb;

import at.reder.mti.api.datamodel.nb.helper.MTIDataLoader;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

/**
 *
 * @author Wolfgang Reder
 */
public class ContactDataLoader extends MTIDataLoader
{

  private static final ContactDataLoader instance = new ContactDataLoader();

  public static ContactDataLoader getInstance()
  {
    return instance;
  }

  private ContactDataLoader()
  {
    super(ContactDataObject.class);
  }

  @Override
  protected DataObject handleFindDataObject(FileObject fo, RecognizedFiles recognized) throws IOException
  {
    return new ContactDataObject(fo);
  }

}
