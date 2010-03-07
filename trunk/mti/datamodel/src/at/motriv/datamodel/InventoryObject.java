/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import at.motriv.datamodel.entities.contact.Manufacturer;
import at.motriv.datamodel.entities.contact.Retailer;
import at.mountainsd.util.Money;
import java.awt.Image;
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

  public Manufacturer getManufacturer();

  public Retailer getRetailer();

  public ModelCondition getCondition();

  public Collection<? extends External> getExternals();

  public Image getMasterImage();
}
