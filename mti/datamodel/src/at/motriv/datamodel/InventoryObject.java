/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import at.motriv.datamodel.externals.External;
import at.motriv.datamodel.entities.contact.Contact;
import at.mountainsd.util.Money;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public interface InventoryObject extends Lookup.Provider
{

  public UUID getId();

  public String getName();

  public String getDescription();

  public Money getPrice();

  public Date getDateOfPurchase();

  public String getProductNumber();

  public Contact getManufacturer();

  public Contact getRetailer();

  public ModelCondition getCondition();

  public Collection<? extends External> getExternals();

  public External getMasterImage();

  public Date getLastModified();
}
