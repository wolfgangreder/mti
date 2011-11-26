/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.infrastructure;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.mountainsd.dataprovider.api.DataProviderException;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall
{
  private static final long serialVersionUID = 1L;
  @Override
  public void restored()
  {
    try {
      MotrivItemProviderLookup.checkDatasource();
    } catch (DataProviderException ex) {
      Exceptions.printStackTrace(ex);
    }
  }
}
