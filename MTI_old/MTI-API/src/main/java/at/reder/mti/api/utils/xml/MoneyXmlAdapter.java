/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.utils.xml;

import at.reder.mti.api.utils.Money;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public final class MoneyXmlAdapter extends XmlAdapter<String, Money>
{

  @Override
  public Money unmarshal(String v)
  {
    return Money.valueOf(v);
  }

  @Override
  public String marshal(Money v)
  {
    return v != null ? v.toString() : null;
  }

}
