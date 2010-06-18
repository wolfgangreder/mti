/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.locomotive.impl;

import at.motriv.datamodel.Decoder;
import at.motriv.datamodel.externals.External;
import at.motriv.datamodel.externals.ExternalKind;
import at.motriv.datamodel.ModelCondition;
import at.motriv.datamodel.entities.locomotive.MutableLocomotive;
import at.motriv.datamodel.entities.scale.Scale;
import at.motriv.datamodel.ServiceEntry;
import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactType;
import at.motriv.datamodel.entities.era.Era;
import at.mountainsd.util.Money;
import at.mountainsd.util.Utils;
import java.text.MessageFormat;
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
public class DefaultLocomotive extends AbstractLocomotive
{

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
  private final Contact manufacturer;
  private final String productNumber;
  private final Contact retailer;
  private final Date dateOfPurchase;
  private final Money price;
  private final ModelCondition condition;
  private final String description;
  private final External masterImage;
  private final Map<UUID, External> externals;
  private final Decoder decoder;
  private final Date lastModified;
  private final String locNumber;

  public DefaultLocomotive(UUID id, String name, String locoClass, String wheelArrangement, String kind, Era era, String company,
                           String country, Scale scale,
                           double weight, double height, double width, double length, Contact manufacturer, String productNumber,
                           Contact retailer,
                           Date dateOfPurchase, Money price, ModelCondition condition, String descritpion, External masterImage,
                           Collection<? extends External> externals, Decoder decoder, Date lastModified, String locNumber)
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
    if (manufacturer.getTypes().contains(ContactType.MANUFACTURER)) {
      this.manufacturer = manufacturer;
    } else {
      throw new IllegalArgumentException(MessageFormat.format("contact {0} is no manufacturer", manufacturer.getId()));
    }
    this.productNumber = productNumber;
    if (retailer.getTypes().contains(ContactType.RETAILER)) {
      this.retailer = retailer;
    } else {
      throw new IllegalArgumentException(MessageFormat.format("contact {0} is no retailer", retailer.getId()));
    }
    this.dateOfPurchase = Utils.copyDate(dateOfPurchase);
    this.price = price;
    this.condition = condition;
    this.description = descritpion;
    this.locNumber = locNumber;
    Map<UUID, External> tmp = new HashMap<UUID, External>();
    for (External ex : externals) {
      tmp.put(ex.getId(), ex);
    }
    if (masterImage != null && masterImage.getKind() != ExternalKind.IMAGE) {
      throw new IllegalArgumentException("masterimage not an image");
    }
    this.masterImage = masterImage;
    if (masterImage != null) {
      tmp.put(masterImage.getId(), this.masterImage);
    }
    this.externals = Collections.unmodifiableMap(tmp);
    this.decoder = decoder;
    this.lastModified = lastModified != null ? Utils.copyDate(lastModified) : new Date();
  }

  @Override
  public String getLocomotiveNumber()
  {
    return locNumber;
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
  public Contact getManufacturer()
  {
    return manufacturer;
  }

  @Override
  public Contact getRetailer()
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
    return externals.values();
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
    //TODO implementieren
  }

  @Override
  public Scale getScale()
  {
    return scale;
  }

  @Override
  public Decoder getDecoder()
  {
    return decoder;
  }

  @Override
  public MutableLocomotive getMutator()
  {
    return new DefaultMutableLocomotive(this);
  }

  @Override
  public Date getLastModified()
  {
    return Utils.copyDate(lastModified);
  }
}
