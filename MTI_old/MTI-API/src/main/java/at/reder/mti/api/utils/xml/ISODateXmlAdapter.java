/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.utils.xml;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Wolfgang Reder
 */
public class ISODateXmlAdapter extends XmlAdapter<String, LocalDate>
{

  @Override
  public LocalDate unmarshal(String v) throws DateTimeParseException
  {
    if (v != null) {
      return LocalDate.parse(v);
    }
    return null;
  }

  @Override
  public String marshal(LocalDate v) throws DateTimeException
  {
    if (v != null) {
      return v.toString();
    }
    return null;
  }

}
