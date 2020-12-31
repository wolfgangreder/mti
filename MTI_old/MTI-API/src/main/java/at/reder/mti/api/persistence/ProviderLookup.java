/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.persistence;

import org.netbeans.api.progress.ProgressHandle;

/**
 *
 * @author wolfi
 */
public interface ProviderLookup
{

  public <P> P lookupProvider(Class<? extends P> providerClass);

  /**
   *
   * @param progess
   */
  public void startProvider(ProgressHandle progess);

}
