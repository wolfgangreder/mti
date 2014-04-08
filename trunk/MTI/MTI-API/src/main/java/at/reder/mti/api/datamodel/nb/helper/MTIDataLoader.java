/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.nb.helper;

import org.openide.loaders.DataLoader;
import org.openide.loaders.DataObject;

/**
 *
 * @author Wolfgang Reder
 * @param <NODE>
 */
public abstract class MTIDataLoader extends DataLoader
{

  protected MTIDataLoader(Class<? extends DataObject> dobClass)
  {
    super(dobClass.getName());
  }

}
