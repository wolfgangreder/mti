/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Definiert Konstanten und Hilfsfunktionen.
 *
 * @author wolfi
 */
public final class MTIUtils
{

  /**
   * Maximale Länge von Enum Bezeichnern.
   */
  public static final int MAX_ENUM_LENGTH = 50;

  /**
   * Erzeugt eine kopie von {@code date}
   *
   * @param date Datum das kopiert werden soll.
   * @return {@code null} wenn {@code date==null} oder eine Kopie von {@code date}.
   */
  public static Date copyDate(Date date)
  {
    return date != null ? new Date(date.getTime()) : null;
  }

  public static Timestamp getDayPart(Timestamp pDate)
  {
    if (pDate == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(pDate);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return new Timestamp(cal.getTime());
  }

  public static Timestamp getTimePart(Timestamp pDate)
  {
    if (pDate == null) {
      return null;
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(pDate);
    cal.set(Calendar.YEAR, 1970);
    cal.set(Calendar.DAY_OF_YEAR, 1);
    return new Timestamp(cal.getTime());
  }

  public static Timestamp composeDateTime(Timestamp pDate, Timestamp pTime)
  {
    if (pDate == null) {
      return pTime;
    }
    if (pTime == null) {
      return pDate;
    }
    Calendar cal = Calendar.getInstance();
    Calendar delta = Calendar.getInstance();
    cal.setTime(getDayPart(pDate));
    delta.setTime(getTimePart(pTime));
    cal.set(Calendar.HOUR_OF_DAY, delta.get(Calendar.HOUR_OF_DAY));
    cal.set(Calendar.MINUTE, delta.get(Calendar.MINUTE));
    cal.set(Calendar.SECOND, delta.get(Calendar.SECOND));
    cal.set(Calendar.MILLISECOND, delta.get(Calendar.MILLISECOND));
    return new Timestamp(cal.getTime());
  }

  private MTIUtils()
  {
  }

}
