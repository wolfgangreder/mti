/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact.model;

import at.mountainsd.dataprovider.api.UniversalSearchRequest;
import at.mountainsd.util.Money;
import java.util.Date;

/**
 *
 * @author wolfi
 */
public class ContactUniversalSerachRequest implements UniversalSearchRequest
{

  private static final long serialVersionUID = 1L;
  private final String filter;

  public ContactUniversalSerachRequest(String filter)
  {
    this.filter = filter;
  }

  @Override
  public String getAsString()
  {
    return filter;
  }

  @Override
  public Integer getAsInteger()
  {
    try {
      return Integer.parseInt(filter);
    } catch (Throwable th) {
    }
    return null;
  }

  @Override
  public Long getAsLong()
  {
    try {
      return Long.parseLong(filter);
    } catch (Throwable th) {
    }
    return null;
  }

  @Override
  public Double getAsDouble()
  {
    try {
      return Double.parseDouble(filter);
    } catch (Throwable th) {
    }
    return null;
  }

  @Override
  public Date getAsDate()
  {
    return null;
  }

  @Override
  public Money getAsMoney()
  {
    return null;
  }
}
