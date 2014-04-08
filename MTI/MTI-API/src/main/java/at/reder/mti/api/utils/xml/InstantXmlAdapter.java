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
import java.time.Instant;
import java.time.format.DateTimeParseException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Wolfgang Reder
 */
public class InstantXmlAdapter extends XmlAdapter<String, Instant>
{

  @Override
  public Instant unmarshal(String v) throws DateTimeParseException
  {
    if (v != null) {
      return Instant.parse(v);
    }
    return null;
  }

  @Override
  public String marshal(Instant v) throws DateTimeException
  {
    if (v != null) {
      return v.toString();
    }
    return null;
  }

}
