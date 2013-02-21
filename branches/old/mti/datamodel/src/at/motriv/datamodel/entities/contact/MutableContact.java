/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact;

import at.mountainsd.util.Builder;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface MutableContact extends Contact, Builder<Contact>
{

  public void setId(UUID id);

  public void setName(String name);

  public void setAddress1(String address1);

  public void setAddress2(String address2);

  public void setZip(String zip);

  public void setCity(String city);

  public void setCountry(String country);

  public void setEmail(String email);

  public void setWWW(String www);

  public void setShopAddress(String shopAddress);

  public void setMemo(String memo);

  public void setPhone1(String phone1);

  public void setPhone2(String phone2);

  public void setFax(String fax);

  public void setTypes(Set<ContactType> types);
}
