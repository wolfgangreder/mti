/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactItemProvider;
import at.motriv.datamodel.entities.contact.ContactType;
import at.motriv.datamodel.entities.contact.MutableContact;
import at.motriv.datamodel.entities.contact.impl.DefaultMutableContact;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.util.Utils;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Set;
import java.util.UUID;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public class DirtyMutableContact implements MutableContact
{

  private Contact old;
  private MutableContact current;
  private boolean isDirty;
  private boolean isValid;
  private PropertyChangeSupport propSupport = new PropertyChangeSupport(this);

  public DirtyMutableContact(Contact c)
  {
    if (c != null) {
      old = new DefaultMutableContact(c).build();
      current = old.getMutator();
    } else {
      old = null;
      current = new DefaultMutableContact();
    }
    isDirty = old == null;
  }

  public void save() throws DataProviderException
  {
    ContactItemProvider provider = MotrivItemProviderLookup.lookup(ContactItemProvider.class);
    if (provider != null) {
      old = provider.store(current);
      current = old.getMutator();
      checkDirty();
    }
  }

  public void addPropertyChangeListener(PropertyChangeListener l)
  {
    propSupport.addPropertyChangeListener(l);
  }

  public void removePropertyChangeListener(PropertyChangeListener l)
  {
    propSupport.removePropertyChangeListener(l);
  }

  public void addPropertyChangeListener(String propName, PropertyChangeListener l)
  {
    propSupport.addPropertyChangeListener(propName, l);
  }

  public void removePropertyChangeListener(String propName, PropertyChangeListener l)
  {
    propSupport.removePropertyChangeListener(propName, l);
  }

  private void checkValid()
  {
    boolean wasValid = isValid;
    isValid = current.getName() != null && current.getName().trim().length() > 0;
    if (wasValid != isValid) {
      propSupport.firePropertyChange("valid", wasValid, isValid);
    }
  }

  public boolean isValid()
  {
    return isValid;
  }

  private boolean checkDirty()
  {
    boolean wasDirty = isDirty;
    if (innerCheckDirty() != wasDirty) {
      propSupport.firePropertyChange("dirty", wasDirty, !wasDirty);
    }
    return isDirty;
  }

  private boolean innerCheckDirty()
  {
    checkValid();
    if (old == null) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getId(), current.getId())) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getAddress1(), current.getAddress1())) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getAddress2(), current.getAddress2())) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getCity(), current.getCity())) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getCountry(), current.getCountry())) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getEmail(), current.getEmail())) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getFax(), current.getFax())) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getMemo(), current.getMemo())) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getName(), current.getName())) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getPhone1(), current.getPhone1())) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getPhone2(), current.getPhone2())) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getShopAddress(), current.getShopAddress())) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getTypes(), current.getTypes())) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getWWW(), current.getWWW())) {
      return isDirty = true;
    }
    if (!Utils.equals(old.getZip(), current.getZip())) {
      return isDirty = true;
    }
    return isDirty = false;
  }

  public boolean isDirty()
  {
    return isDirty;
  }

  @Override
  @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
  public boolean equals(Object obj)
  {
    return current.equals(obj);
  }

  @Override
  public int hashCode()
  {
    return current.hashCode();
  }

  @Override
  public String toString()
  {
    return current.toString();
  }

  @Override
  public String getAddress1()
  {
    return current.getAddress1();
  }

  @Override
  public String getAddress2()
  {
    return current.getAddress2();
  }

  @Override
  public String getCity()
  {
    return current.getCity();
  }

  @Override
  public String getCountry()
  {
    return current.getCountry();
  }

  @Override
  public String getEmail()
  {
    return current.getEmail();
  }

  @Override
  public String getFax()
  {
    return current.getFax();
  }

  @Override
  public UUID getId()
  {
    return current.getId();
  }

  @Override
  public String getMemo()
  {
    return current.getMemo();
  }

  @Override
  public String getName()
  {
    return current.getName();
  }

  @Override
  public String getPhone1()
  {
    return current.getPhone1();
  }

  @Override
  public String getPhone2()
  {
    return current.getPhone2();
  }

  @Override
  public String getShopAddress()
  {
    return current.getShopAddress();
  }

  @Override
  public Set<ContactType> getTypes()
  {
    return current.getTypes();
  }

  @Override
  public String getWWW()
  {
    return current.getWWW();
  }

  @Override
  public String getZip()
  {
    return current.getZip();
  }

  @Override
  public Lookup getLookup()
  {
    return current.getLookup();
  }

  @Override
  public MutableContact getMutator()
  {
    return current.getMutator();
  }

  @Override
  public Contact build()
  {
    return current.build();
  }

  @Override
  public void setAddress1(String address1)
  {
    current.setAddress1(address1);
    checkDirty();
  }

  @Override
  public void setAddress2(String address2)
  {
    current.setAddress2(address2);
    checkDirty();
  }

  @Override
  public void setCity(String city)
  {
    current.setCity(city);
    checkDirty();
  }

  @Override
  public void setCountry(String country)
  {
    current.setCountry(country);
    checkDirty();
  }

  @Override
  public void setEmail(String email)
  {
    current.setEmail(email);
    checkDirty();
  }

  @Override
  public void setFax(String fax)
  {
    current.setFax(fax);
    checkDirty();
  }

  @Override
  public void setId(UUID id)
  {
    current.setId(id);
    checkDirty();
  }

  @Override
  public void setMemo(String memo)
  {
    current.setMemo(memo);
    checkDirty();
  }

  @Override
  public void setName(String name)
  {
    current.setName(name);
    checkDirty();
  }

  @Override
  public void setPhone1(String phone1)
  {
    current.setPhone1(phone1);
    checkDirty();
  }

  @Override
  public void setPhone2(String phone2)
  {
    current.setPhone1(phone2);
    checkDirty();
  }

  @Override
  public void setShopAddress(String shopAddress)
  {
    current.setShopAddress(shopAddress);
    checkDirty();
  }

  @Override
  public void setTypes(Set<ContactType> types)
  {
    current.setTypes(types);
    checkDirty();
  }

  @Override
  public void setWWW(String www)
  {
    current.setWWW(www);
    checkDirty();
  }

  @Override
  public void setZip(String zip)
  {
    current.setZip(zip);
    checkDirty();
  }
}
