/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author wolfi
 */
public interface ServiceableObject
{

  public static interface Builder<S extends ServiceableObject>
  {

    public ServiceableObject.Builder<? extends ServiceableObject> addServiceEntry(ServiceEntry e) throws NullPointerException;

    public ServiceableObject.Builder<? extends ServiceableObject> removeServiceEntry(ServiceEntry e);

    public ServiceableObject.Builder<? extends ServiceableObject> addServiceEntries(Collection<? extends ServiceEntry> e) throws
            NullPointerException,
            IllegalArgumentException;

    public ServiceableObject.Builder<? extends ServiceableObject> clearServiceEntries();

  }

  public List<? extends ServiceEntry> getServiceEntries();

}
