/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.locomotive.impl;

import at.motriv.datamodel.Decoder;
import at.motriv.datamodel.External;
import at.motriv.datamodel.ExternalKind;
import at.motriv.datamodel.ModelCondition;
import at.motriv.datamodel.ServiceEntry;
import at.motriv.datamodel.entities.contact.Manufacturer;
import at.motriv.datamodel.entities.contact.Retailer;
import at.motriv.datamodel.entities.era.Era;
import at.motriv.datamodel.entities.locomotive.Locomotive;
import at.motriv.datamodel.entities.locomotive.MutableLocomotive;
import at.motriv.datamodel.entities.scale.Scale;
import at.mountainsd.util.Money;
import at.mountainsd.util.Utils;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public class DefaultMutableLocomotive implements MutableLocomotive
{

  private UUID id;
  private String name;
  private String locoClass;
  private String wheelArrangement;
  private String kind;
  private Era era;
  private String company;
  private String country;
  private Scale scale;
  private double weight;
  private double height;
  private double width;
  private double length;
  private Manufacturer manufacturer;
  private String productNumber;
  private Retailer retailer;
  private Date dateOfPurchase;
  private Money price;
  private ModelCondition condition;
  private String description;
  private External masterImage;
  private final Map<UUID, External> externals;
  private Decoder decoder;

  public DefaultMutableLocomotive()
  {
    id = UUID.randomUUID();
    this.externals = new HashMap<UUID, External>();
  }

  public DefaultMutableLocomotive(Locomotive loc)
  {
    this.id = loc.getId();
    this.name = loc.getName();
    this.locoClass = loc.getLocomotiveClass();
    this.wheelArrangement = loc.getWheelArragement();
    this.kind = loc.getKind();
    this.era = loc.getEra();
    this.company = loc.getCompany();
    this.country = loc.getCountry();
    this.scale = loc.getScale();
    this.weight = loc.getWeight();
    this.height = loc.getHeight();
    this.width = loc.getWidth();
    this.length = loc.getLength();
    this.manufacturer = loc.getManufacturer();
    this.productNumber = loc.getProductNumber();
    this.retailer = loc.getRetailer();
    this.dateOfPurchase = Utils.copyDate(loc.getDateOfPurchase());
    this.price = loc.getPrice();
    this.condition = loc.getCondition();
    this.description = loc.getDescription();
    this.externals = new HashMap<UUID, External>();
    for (External e : loc.getExternals()) {
      externals.put(e.getId(), e);
    }
    masterImage = loc.getMasterImage();
    if (masterImage != null) {
      externals.put(masterImage.getId(), masterImage);
    }
    this.decoder = loc.getDecoder();
  }

  @Override
  public void setId(UUID id)
  {
    this.id = id != null ? id : UUID.randomUUID();
  }

  @Override
  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }

  @Override
  public void setPrice(Money price)
  {
    this.price = price;
  }

  @Override
  public void setDateOfPurchase(Date dop)
  {
    this.dateOfPurchase = Utils.copyDate(dop);
  }

  @Override
  public void setProductNumber(String productNumber)
  {
    this.productNumber = productNumber;
  }

  @Override
  public void setManufacturer(Manufacturer manufacturer)
  {
    this.manufacturer = manufacturer;
  }

  @Override
  public void setRetailer(Retailer retailer)
  {
    this.retailer = retailer;
  }

  @Override
  public void setCondition(ModelCondition modelCondition)
  {
    this.condition = modelCondition;
  }

  @Override
  public void addExternal(External external)
  {
    if (external != null) {
      externals.put(external.getId(), external);
    }
  }

  @Override
  public void removeExternal(UUID id)
  {
    if (id != null) {
      if (masterImage != null && masterImage.getId().equals(id)) {
        masterImage = null;
      }
      externals.remove(id);
    }
  }

  @Override
  public void setMasterImage(External external)
  {
    if (external != null && external.getKind() != ExternalKind.IMAGE) {
      throw new IllegalArgumentException("external not an image");
    }
    masterImage = external;
    if (external != null) {
      externals.put(external.getId(), external);
    }
  }

  @Override
  public void setEra(Era era)
  {
    this.era = era;
  }

  @Override
  public void setLength(double length)
  {
    this.length = length;
  }

  @Override
  public void setWidth(double width)
  {
    this.width = width;
  }

  @Override
  public void setHeight(double height)
  {
    this.height = height;
  }

  @Override
  public void setWeight(double weight)
  {
    this.weight = weight;
  }

  @Override
  public void setWheelArragement(String whellArrangement)
  {
    this.wheelArrangement = whellArrangement;
  }

  @Override
  public void setKind(String kind)
  {
    this.kind = kind;
  }

  @Override
  public void setLocomotiveClass(String clazz)
  {
    this.locoClass = clazz;
  }

  @Override
  public void setCompany(String company)
  {
    this.company = company;
  }

  @Override
  public void setCountry(String country)
  {
    this.country = country;
  }

  @Override
  public void addServiceEntry(ServiceEntry entry)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void removeServiceEntry(UUID id)
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setScale(Scale scale)
  {
    this.scale = scale;
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
  }

  @Override
  public External getMasterImage()
  {
    return masterImage;
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

  @Override
  public void setDecoder(Decoder decoder)
  {
    this.decoder = decoder;
  }

  @Override
  public Decoder getDecoder()
  {
    return decoder;
  }

  @Override
  public Locomotive build()
  {
    return new DefaultLocomotive(id, name, locoClass, wheelArrangement, kind, era, company, country, scale, weight, height, width,
            length, manufacturer, productNumber, retailer, Utils.copyDate(dateOfPurchase), price, condition, description,
            masterImage, externals.values(), decoder);
  }
}
