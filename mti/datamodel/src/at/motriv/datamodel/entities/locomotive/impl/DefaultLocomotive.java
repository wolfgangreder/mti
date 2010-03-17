/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.locomotive.impl;

import at.motriv.datamodel.External;
import at.motriv.datamodel.ModelCondition;
import at.motriv.datamodel.entities.locomotive.MutableLocomotive;
import at.motriv.datamodel.entities.scale.Scale;
import at.motriv.datamodel.ServiceEntry;
import at.motriv.datamodel.entities.contact.Manufacturer;
import at.motriv.datamodel.entities.contact.Retailer;
import at.motriv.datamodel.entities.era.Era;
import at.mountainsd.util.Money;
import at.mountainsd.util.Utils;
import java.awt.Image;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public class DefaultLocomotive extends AbstractLocomotive
{

  private WeakReference<Image> masterImage;
  private final UUID id;
  private final String name;
  private final String locoClass;
  private final String wheelArrangement;
  private final String kind;
  private final Era era;
  private final String company;
  private final String country;
  private final Scale scale;
  private final double weight;
  private final double height;
  private final double width;
  private final double length;
  private final Manufacturer manufacturer;
  private final String productNumber;
  private final Retailer retailer;
  private final Date dateOfPurchase;
  private final Money price;
  private final ModelCondition condition;
  private final String description;

  public DefaultLocomotive(UUID id, String name, String locoClass, String wheelArrangement, String kind, Era era, String company,
          String country, Scale scale,
          double weight, double height, double width, double length, Manufacturer manufacturer, String productNumber, Retailer retailer,
          Date dateOfPurchase, Money price, ModelCondition condition, String descritpion)
  {
    this.id = id;
    this.name = name;
    this.locoClass = locoClass;
    this.wheelArrangement = wheelArrangement;
    this.kind = kind;
    this.era = era;
    this.company = company;
    this.country = country;
    this.scale = scale;
    this.weight = weight;
    this.height = height;
    this.width = width;
    this.length = length;
    this.manufacturer = manufacturer;
    this.productNumber = productNumber;
    this.retailer = retailer;
    this.dateOfPurchase = Utils.copyDate(dateOfPurchase);
    this.price = price;
    this.condition = condition;
    this.description = descritpion;
  }

  @Override
  public String getWheelArragement()
  {
    return wheelArrangement;
  }

  @Override
  public String getKind()
  {
    return kind;
  }

  @Override
  public String getLocomotiveClass()
  {
    return locoClass;
  }

  @Override
  public String getCompany()
  {
    return company;
  }

  @Override
  public String getCountry()
  {
    return country;
  }

  @Override
  public Era getEra()
  {
    return era;
  }

  @Override
  public double getLength()
  {
    return length;
  }

  @Override
  public double getWidth()
  {
    return width;
  }

  @Override
  public double getHeight()
  {
    return height;
  }

  @Override
  public double getWeight()
  {
    return weight;
  }

  @Override
  public UUID getId()
  {
    return id;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public String getDescription()
  {
    return description;
  }

  @Override
  public Money getPrice()
  {
    return price;
  }

  @Override
  public Date getDateOfPurchase()
  {
    return Utils.copyDate(dateOfPurchase);
  }

  @Override
  public String getProductNumber()
  {
    return productNumber;
  }

  @Override
  public Manufacturer getManufacturer()
  {
    return manufacturer;
  }

  @Override
  public Retailer getRetailer()
  {
    return retailer;
  }

  @Override
  public ModelCondition getCondition()
  {
    return condition;
  }

  @Override
  public Collection<? extends External> getExternals()
  {
    return Collections.emptyList();
    //TODO implementieren
  }

  @Override
  public Image getMasterImage()
  {
    return null;
    //TODO implementieren
  }

  @Override
  public Lookup getLookup()
  {
    return Lookup.EMPTY;
  }

  @Override
  public List<? extends ServiceEntry> getServiceEntries()
  {
    return Collections.emptyList();
    //TODO implementieren
  }

  @Override
  public Scale getScale()
  {
    return scale;
  }

  @Override
  public MutableLocomotive getMutator()
  {
    return new DefaultMutableLocomotive(this);
  }
}
