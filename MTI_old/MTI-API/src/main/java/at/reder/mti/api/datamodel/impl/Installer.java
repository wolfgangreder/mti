/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.persistence.ProviderLookup;
import at.reder.mti.api.utils.MTIUtils;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@Messages({"Installer_progressLabel=Datenbankstruktur aktualisieren"})
public class Installer extends ModuleInstall
{

  @Override
  public void restored()
  {
    MTIUtils.getRequestProcessor().execute(this::startProvider);
  }

  private void startProvider()
  {
    ProviderLookup pl = Lookup.getDefault().lookup(ProviderLookup.class);
    if (pl != null) {
      ProgressHandle ph = ProgressHandleFactory.createHandle(Bundle.Installer_progressLabel());
      try {
        ph.start();
        pl.startProvider(ph);
      } finally {
        ph.finish();
      }
    }
  }

}
