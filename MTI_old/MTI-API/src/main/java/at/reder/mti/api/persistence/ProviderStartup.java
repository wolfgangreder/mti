/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.api.persistence;

import org.netbeans.api.progress.ProgressHandle;

/**
 *
 * @author wolfi
 */
public interface ProviderStartup
{

  public void testDatabase(ProgressHandle handle);

}
