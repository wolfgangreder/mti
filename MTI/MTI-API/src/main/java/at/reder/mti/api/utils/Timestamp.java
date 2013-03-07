/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Read-Only implementation von Date
 *
 * @author wolfi
 */
public final class Timestamp extends Date
{

  public static abstract class AbstractAdapter extends XmlAdapter<String, Timestamp>
  {

    private final DateFormat fmt;

    public AbstractAdapter(DateFormat fmt)
    {
      this.fmt = fmt;
    }

    @Override
    public final Timestamp unmarshal(String v) throws ParseException
    {
      if (v != null) {
        return new Timestamp(fmt.parse(v));
      }
      return null;
    }

    @Override
    public final String marshal(Timestamp v)
    {
      if (v != null) {
        return fmt.format(new Date(v.getTime()));
      }
      return null;
    }

  }

  public static final class DateAdapter extends AbstractAdapter
  {

    public DateAdapter()
    {
      super(new SimpleDateFormat("yyyy-MM-dd"));
    }

  }

  public static final class TimeAdapter extends AbstractAdapter
  {

    public TimeAdapter()
    {
      super(new SimpleDateFormat("HH:mm:ss"));
    }

  }

  public static final class TimestampAdapter extends AbstractAdapter
  {

    public TimestampAdapter()
    {
      super(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
    }

  }

  public static final class ISO822TimestampAdapter extends AbstractAdapter
  {

    public ISO822TimestampAdapter()
    {
      super(getFormat());
    }

    private static DateFormat getFormat()
    {
      DateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
      fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
      return fmt;
    }

  }

  public Timestamp()
  {
  }

  public Timestamp(Date date)
  {
    super(date.getTime());
  }

  public Date toDate()
  {
    return new Date(getTime());
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 79 * hash + (int) (this.getTime() ^ (this.getTime() >>> 32));
    return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Date)) {
      return false;
    }
    final Date other = (Date) obj;
    if (this.getTime() != other.getTime()) {
      return false;
    }
    return true;
  }

  @Override
  @Deprecated
  public void setYear(int year)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  public void setMonth(int month)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  public void setDate(int date)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  public void setHours(int hours)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  public void setMinutes(int minutes)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  public void setSeconds(int seconds)
  {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  public void setTime(long time)
  {
    throw new UnsupportedOperationException();
  }

}
