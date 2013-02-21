/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact.wizard;

import at.motriv.datamodel.entities.contact.Contact;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 *
 * @author wolfi
 */
public class NewContactWizardData
{

  public static final Set<CreationMode> ALL_MODES = Collections.unmodifiableSet(new HashSet<CreationMode>(Arrays.asList(CreationMode.
          values())));
  public static final Set<CreationMode> RETAILER_MODES = Collections.unmodifiableSet(new HashSet<CreationMode>(Arrays.asList(
          CreationMode.NEW_RETAILER, CreationMode.NEW_RETAILER_FROM_MANUFACTURER)));
  public static final Set<CreationMode> MANUFACTURER_MODES = Collections.unmodifiableSet(new HashSet<CreationMode>(Arrays.asList(
          CreationMode.NEW_MANUFACTURER, CreationMode.NEW_MANUFACTURER_FROM_RETAILER)));
  private CreationMode creationMode;
  private Callable<? extends Contact> finisher;
  private final Set<CreationMode> allowedModes;

  public NewContactWizardData(Set<CreationMode> allowedModes)
  {
    this.allowedModes = Collections.unmodifiableSet(new HashSet<CreationMode>(allowedModes));
  }

  public Set<CreationMode> getAllowedModes()
  {
    return allowedModes;
  }

  public CreationMode getCreationMode()
  {
    return creationMode;
  }

  public void setCreationMode(CreationMode mode)
  {
    creationMode = mode;
  }

  public Callable<? extends Contact> getFinisher()
  {
    return finisher;
  }

  public void setFinisher(Callable<? extends Contact> finisher)
  {
    this.finisher = finisher;
  }
}
