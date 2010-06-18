/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.locomotive;

import at.motriv.datamodel.Decoder;
import at.motriv.datamodel.externals.External;
import at.motriv.datamodel.ModelCondition;
import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.ServiceEntry;
import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.era.Era;
import at.motriv.datamodel.entities.locomotive.Locomotive;
import at.motriv.datamodel.entities.locomotive.LocomotiveItemProvider;
import at.motriv.datamodel.entities.locomotive.MutableLocomotive;
import at.motriv.datamodel.entities.locomotive.impl.DefaultMutableLocomotive;
import at.motriv.datamodel.entities.scale.Scale;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.util.Money;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public class DirtyMutableLocomotive implements MutableLocomotive
{

  private Locomotive old;
  private DefaultMutableLocomotive current;
  private boolean isDirty;

  public DirtyMutableLocomotive(Locomotive loc)
  {
    if (loc != null) {
      old = new DefaultMutableLocomotive(loc).build();
      current = new DefaultMutableLocomotive(loc);
    } else {
      old = null;
      current = new DefaultMutableLocomotive();
    }
    isDirty = old == null;
  }

  public void save() throws DataProviderException
  {
    LocomotiveItemProvider provider = MotrivItemProviderLookup.lookup(LocomotiveItemProvider.class);
    if (provider != null) {
      old = provider.store(current);
      current = new DefaultMutableLocomotive(old);
      checkDirty();
    }
  }

  private <V> boolean testDirty(V v1, V v2)
  {
    if (v1 == v2) {
      return false;
    }
    if (v1 != null) {
      return !v1.equals(v2);
    }
    if (v2 != null) {
      return !v2.equals(v1);
    }
    return true;
  }

  public boolean checkDirty()
  {
    if (old == null) {
      return isDirty = true;
    }
    if (testDirty(old.getCompany(), current.getCompany())) {
      return isDirty = true;
    }
    if (old.getCondition() != current.getCondition()) {
      return isDirty = true;
    }
    if (testDirty(old.getCountry(), current.getCountry())) {
      return isDirty = true;
    }
    if (testDirty(old.getDateOfPurchase(), current.getDateOfPurchase())) {
      return isDirty = true;
    }
    if (testDirty(old.getDecoder(), current.getDecoder())) {
      return isDirty = true;
    }
    if (testDirty(old.getDescription(), current.getDescription())) {
      return isDirty = true;
    }
    if (testDirty(old.getEra(), current.getEra())) {
      return isDirty = true;
    }
    if (testDirty(old.getHeight(), current.getHeight())) {
      return isDirty = true;
    }
    if (testDirty(old.getKind(), current.getKind())) {
      return isDirty = true;
    }
    if (testDirty(old.getLength(), current.getLength())) {
      return isDirty = true;
    }
    if (testDirty(old.getLocomotiveClass(), current.getLocomotiveClass())) {
      return isDirty = true;
    }
    if (testDirty(old.getManufacturer(), current.getManufacturer())) {
      return isDirty = true;
    }
    if (testDirty(old.getMasterImage(), current.getMasterImage())) {
      return isDirty = true;
    }
    if (testDirty(old.getName(), current.getName())) {
      return isDirty = true;
    }
    if (testDirty(old.getPrice(), current.getPrice())) {
      return isDirty = true;
    }
    if (testDirty(old.getProductNumber(), current.getProductNumber())) {
      return isDirty = true;
    }
    if (testDirty(old.getRetailer(), current.getRetailer())) {
      return isDirty = true;
    }
    if (testDirty(old.getScale(), current.getScale())) {
      return isDirty = true;
    }
    if (testDirty(old.getWeight(), current.getWeight())) {
      return isDirty = true;
    }
    if (testDirty(old.getWheelArragement(), current.getWheelArragement())) {
      return isDirty = true;
    }
    if (testDirty(old.getWidth(), current.getWidth())) {
      return isDirty = true;
    }
    if (testDirty(old.getLocomotiveNumber(),current.getLocomotiveNumber())) {
      return isDirty = true;
    }
    return isDirty = false;
  }

  public boolean isDirty()
  {
    return isDirty;
  }

  @Override
  public String toString()
  {
    return current.toString();
  }

  @Override
  public int hashCode()
  {
    return current.hashCode();
  }

  @Override
  @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
  public boolean equals(Object obj)
  {
    return current.equals(obj);
  }

  @Override
  public void setWidth(double width)
  {
    current.setWidth(width);
    checkDirty();
  }

  @Override
  public void setWheelArragement(String whellArrangement)
  {
    current.setWheelArragement(whellArrangement);
    checkDirty();
  }

  @Override
  public void setWeight(double weight)
  {
    current.setWeight(weight);
    checkDirty();
  }

  @Override
  public void setScale(Scale scale)
  {
    current.setScale(scale);
    checkDirty();
  }

  @Override
  public void setRetailer(Contact retailer)
  {
    current.setRetailer(retailer);
    checkDirty();
  }

  @Override
  public void setProductNumber(String productNumber)
  {
    current.setProductNumber(productNumber);
    checkDirty();
  }

  @Override
  public void setPrice(Money price)
  {
    current.setPrice(price);
    checkDirty();
  }

  @Override
  public void setName(String name)
  {
    current.setName(name);
    checkDirty();
  }

  @Override
  public void setMasterImage(External external)
  {
    current.setMasterImage(external);
    checkDirty();
  }

  @Override
  public void setManufacturer(Contact manufacturer)
  {
    current.setManufacturer(manufacturer);
    checkDirty();
  }

  @Override
  public void setLocomotiveClass(String clazz)
  {
    current.setLocomotiveClass(clazz);
    checkDirty();
  }

  @Override
  public void setLength(double length)
  {
    current.setLength(length);
    checkDirty();
  }

  @Override
  public void setKind(String kind)
  {
    current.setKind(kind);
    checkDirty();
  }

  @Override
  public void setId(UUID id)
  {
    current.setId(id);
    checkDirty();
  }

  @Override
  public void setHeight(double height)
  {
    current.setHeight(height);
    checkDirty();
  }

  @Override
  public void setEra(Era era)
  {
    current.setEra(era);
    checkDirty();
  }

  @Override
  public void setDescription(String description)
  {
    current.setDescription(description);
    checkDirty();
  }

  @Override
  public void setDecoder(Decoder decoder)
  {
    current.setDecoder(decoder);
    checkDirty();
  }

  @Override
  public void setDateOfPurchase(Date dop)
  {
    current.setDateOfPurchase(dop);
    checkDirty();
  }

  @Override
  public void setCountry(String country)
  {
    current.setCountry(country);
    checkDirty();
  }

  @Override
  public void setCondition(ModelCondition modelCondition)
  {
    current.setCondition(modelCondition);
    checkDirty();
  }

  @Override
  public void setCompany(String company)
  {
    current.setCompany(company);
    checkDirty();
  }

  @Override
  public void removeServiceEntry(UUID id)
  {
    current.removeServiceEntry(id);
    checkDirty();
  }

  @Override
  public void removeExternal(UUID id)
  {
    current.removeExternal(id);
    checkDirty();
  }

  @Override
  public double getWidth()
  {
    return current.getWidth();
  }

  @Override
  public String getWheelArragement()
  {
    return current.getWheelArragement();
  }

  @Override
  public double getWeight()
  {
    return current.getWeight();
  }

  @Override
  public List<? extends ServiceEntry> getServiceEntries()
  {
    return current.getServiceEntries();
  }

  @Override
  public Scale getScale()
  {
    return current.getScale();
  }

  @Override
  public Contact getRetailer()
  {
    return current.getRetailer();
  }

  @Override
  public String getProductNumber()
  {
    return current.getProductNumber();
  }

  @Override
  public Money getPrice()
  {
    return current.getPrice();
  }

  @Override
  public String getName()
  {
    return current.getName();
  }

  @Override
  public MutableLocomotive getMutator()
  {
    return current.getMutator();
  }

  @Override
  public External getMasterImage()
  {
    return current.getMasterImage();
  }

  @Override
  public Contact getManufacturer()
  {
    return current.getManufacturer();
  }

  @Override
  public Lookup getLookup()
  {
    return current.getLookup();
  }

  @Override
  public String getLocomotiveClass()
  {
    return current.getLocomotiveClass();
  }

  @Override
  public double getLength()
  {
    return current.getLength();
  }

  @Override
  public String getKind()
  {
    return current.getKind();
  }

  @Override
  public UUID getId()
  {
    return current.getId();
  }

  @Override
  public double getHeight()
  {
    return current.getHeight();
  }

  @Override
  public Collection<? extends External> getExternals()
  {
    return current.getExternals();
  }

  @Override
  public Era getEra()
  {
    return current.getEra();
  }

  @Override
  public String getDescription()
  {
    return current.getDescription();
  }

  @Override
  public Decoder getDecoder()
  {
    return current.getDecoder();
  }

  @Override
  public Date getDateOfPurchase()
  {
    return current.getDateOfPurchase();
  }

  @Override
  public String getCountry()
  {
    return current.getCountry();
  }

  @Override
  public ModelCondition getCondition()
  {
    return current.getCondition();
  }

  @Override
  public String getCompany()
  {
    return current.getCompany();
  }

  @Override
  public Locomotive build()
  {
    return current.build();
  }

  @Override
  public void addServiceEntry(ServiceEntry entry)
  {
    current.addServiceEntry(entry);
    checkDirty();
  }

  @Override
  public void addExternal(External external)
  {
    current.addExternal(external);
    checkDirty();
  }

  @Override
  public void setLastModified(Date lastModified)
  {
  }

  @Override
  public Date getLastModified()
  {
    return current.getLastModified();
  }

  @Override
  public void setLocomotiveNumber(String locNumber)
  {
    current.setLocomotiveNumber(locNumber);
  }

  @Override
  public String getLocomotiveNumber()
  {
    return current.getLocomotiveNumber();
  }
}
