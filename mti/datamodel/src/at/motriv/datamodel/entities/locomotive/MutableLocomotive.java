/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.locomotive;

import at.motriv.datamodel.External;
import at.motriv.datamodel.ModelCondition;
import at.motriv.datamodel.ServiceEntry;
import at.motriv.datamodel.entities.contact.Manufacturer;
import at.motriv.datamodel.entities.contact.Retailer;
import at.motriv.datamodel.entities.era.Era;
import at.motriv.datamodel.entities.scale.Scale;
import at.mountainsd.util.Builder;
import at.mountainsd.util.Money;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface MutableLocomotive extends Locomotive, Builder<Locomotive>
{

  public void setId(UUID id);

  public void setName(String name);

  public void setDescription(String description);

  public void setPrice(Money price);

  public void setDateOfPurchase(Date dop);

  public void setProductNumber(String productNumber);

  public void setManufacturer(Manufacturer manufacturer);

  public void setRetailer(Retailer retailer);

  public void setCondition(ModelCondition modelCondition);

  public void addExternal(External external);

  public void removeExternal(UUID id);

  public void setMasterImage(External external);

  public void setEra(Era era);

  public void setLength(double length);

  public void setWidth(double width);

  public void setHeight(double height);

  public void setWeight(double weight);

  public void setWheelArragement(String whellArrangement);

  public void setKind(String kind);

  public void setLocomotiveClass(String clazz);

  public void setCompany(String company);

  public void setCountry(String country);

  public void addServiceEntry(ServiceEntry entry);

  public void removeServiceEntry(UUID id);

  public void setScale(Scale scale);

}
