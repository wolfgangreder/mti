/*
 * Copyright 2017-2021 Wolfgang Reder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.or.reder.mti.ui;

import java.awt.Color;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.function.Supplier;

public final class Utils
{

  private Utils()
  {
  }

  /**
   * Converts all occurrences of {@link java.time.LocalDate},{@link java.time.LocalTime},{@link java.time.LocalDateTime} and
   * {@link java.time.ZonedDateTime} to {@link java.util.Date}.
   * <p>
   * This is a support method to {@link java.text.MessageFormat}.</p>
   * <p>
   * If necessary, a new array is created and the old array stays unmodified.</p>
   *
   * @param params array of params
   * @return a array without Date / Time classes of package {@code java.util.time}.
   */
  public static Object[] convertArrayToOldDate(Object... params)
  {
    if (params == null) {
      return null;
    }
    if (params.length == 0) {
      return params;
    }
    boolean newArrayCreated = false;
    Object[] result = params;
    for (int i = 0; i < params.length; ++i) {
      Object o = params[i];
      if (o instanceof Temporal) {
        if (o instanceof LocalDateTime) {
          if (!newArrayCreated) {
            result = Arrays.copyOf(params,
                                   params.length);
            newArrayCreated = true;
          }
          result[i] = Utils.localDateTimeToDate((LocalDateTime) o);
        } else if (o instanceof LocalDate) {
          if (!newArrayCreated) {
            result = Arrays.copyOf(params,
                                   params.length);
            newArrayCreated = true;
          }
          result[i] = Utils.localDateToDate((LocalDate) o);
        } else if (o instanceof LocalTime) {
          if (!newArrayCreated) {
            result = Arrays.copyOf(params,
                                   params.length);
            newArrayCreated = true;
          }
          result[i] = Utils.localTimeToDate((LocalTime) o);
        } else if (o instanceof ZonedDateTime) {
          if (!newArrayCreated) {
            result = Arrays.copyOf(params,
                                   params.length);
            newArrayCreated = true;
          }
          result[i] = Utils.zonedDateTimeToDate((ZonedDateTime) o);
        }
      }
    }
    return result;
  }

  /**
   * Converts a {@code LocalDate} to {@code Date} at the System default Timezone
   *
   * @param date date
   * @return a Date with time 00:00 or {@code null} if {@code date} is null.
   * @see Utils#localDateToDate(java.time.LocalDate, java.time.ZoneId)
   */
  public static Date localDateToDate(LocalDate date)
  {
    return localDateToDate(date,
                           ZoneId.systemDefault());
  }

  /**
   * Converts a {@code LocalDate} to {@code Date} at the given Timezone.
   *
   * @param date date
   * @param zone not null
   * @exception NullPointerException if {@code zone==null}
   * @return a Date with time 00:00 or {@code null} if {@code date} is null.
   */
  public static Date localDateToDate(LocalDate date,
                                     ZoneId zone)
  {
    Objects.requireNonNull(zone,
                           "zone==null");
    if (date != null) {
      return Date.from(date.atStartOfDay().atZone(zone).toInstant());
    }
    return null;
  }

  /**
   * Converts a {@code LocalTime} to {@code Date}.
   *
   * @param time time
   * @return a Date with day 1970-01-01 or {@code null} if {@code time} is null.
   * @see Utils#localTimeToDate(java.time.LocalTime, java.time.ZoneId)
   */
  public static Date localTimeToDate(LocalTime time)
  {
    return localTimeToDate(time,
                           ZoneId.systemDefault());
  }

  /**
   * Converts a {@code LocalTime} to {@code Date}.
   *
   * @param time time
   * @param zone not null
   * @exception NullPointerException if {@code zone==null}
   * @return a Date with day 1970-01-01 or {@code null} if {@code time} is null.
   */
  public static Date localTimeToDate(LocalTime time,
                                     ZoneId zone)
  {
    Objects.requireNonNull(zone,
                           "zone==null");
    if (time != null) {
      return Date.from(time.atDate(LocalDate.ofEpochDay(0)).atZone(zone).toInstant());
    }
    return null;
  }

  public static Date localDateAndLocalTimeToDate(LocalDate dt,
                                                 LocalTime lt)
  {
    if (dt == null) {
      return localTimeToDate(lt);
    }
    if (lt == null) {
      return localDateToDate(dt);
    }
    return localDateTimeToDate(LocalDateTime.of(dt,
                                                lt));
  }

  /**
   * Converts a {@code LocalDateTime} to {@code Date} at the system default timezone.
   *
   * @param dt timestamp
   * @return date
   * @see Utils#localDateTimeToDate(java.time.LocalDateTime, java.time.ZoneId)
   */
  public static Date localDateTimeToDate(LocalDateTime dt)
  {
    if (dt != null) {
      return Date.from(dt.atZone(ZoneId.systemDefault()).toInstant());
    }
    return null;
  }

  public static Date zonedDateTimeToDate(ZonedDateTime dt)
  {
    if (dt != null) {
      return Date.from(dt.toInstant());
    }
    return null;
  }

  /**
   * Converts a {@code LocalDateTime} to {@code Date} at the Timezone {@code zone}.
   *
   * @param dt timestamp
   * @param zone not null
   * @exception NullPointerException if {@code zone==null}.
   * @return date
   */
  public static Date localDateTimeToDate(LocalDateTime dt,
                                         ZoneId zone)
  {
    Objects.requireNonNull(zone,
                           "zone==null");
    if (dt != null) {
      return Date.from(dt.atZone(zone).toInstant());
    }
    return null;
  }

  /**
   * Converts a supplied {@code Date} to {@code LocalDateTime}.
   *
   * @param supplier not null
   * @return localDateTime
   * @throws NullPointerException if {@code supplier==null}.
   * @see Utils#dateToLocalDateTime(java.util.function.Supplier, java.time.ZoneId)
   */
  public static LocalDateTime dateToLocalDateTime(Supplier<Date> supplier) throws NullPointerException
  {
    Objects.requireNonNull(supplier,
                           "supplier==null");
    return dateToLocalDateTime(supplier,
                               ZoneId.systemDefault());
  }

  /**
   * Converts a supplied {@code Date} to {@code LocalDatetime} at the Timezone {@code zone}.
   *
   * @param supplier not null
   * @param zone not null
   * @return localDateTime
   * @throws NullPointerException if {@code supplier==null} or {@code zone==null}.
   * @see Utils#dateToLocalDateTime(java.util.Date, java.time.ZoneId)
   */
  public static LocalDateTime dateToLocalDateTime(Supplier<Date> supplier,
                                                  ZoneId zone) throws NullPointerException
  {
    Objects.requireNonNull(supplier,
                           "supplier==null");
    return dateToLocalDateTime(supplier.get(),
                               zone);
  }

  /**
   * Converts a {@code Date} to {@code LocalDateTime}. Equal to {@code dateToLocalDateTime(dt,ZoneId.systemDefault()}
   *
   * @param dt date
   * @return a {@code LocalDateTime} or {@code null} if {@code dt==null}.
   * @see Utils#dateToLocalDateTime(java.util.Date, java.time.ZoneId)
   */
  public static LocalDateTime dateToLocalDateTime(Date dt)
  {
    return dateToLocalDateTime(dt,
                               ZoneId.systemDefault());
  }

  public static ZonedDateTime dateToZonedDateTime(Date dt)
  {
    if (dt != null) {
      return dt.toInstant().atZone(ZoneId.systemDefault());
    }
    return null;
  }

  /**
   * Converts a {@code Date} to {@code LocalDateTime}.
   *
   * @param dt date
   * @param zone - not null
   * @return a {@code LocalDateTime} or {@code null} if {@code dt==null}.
   * @throws NullPointerException if {@code zone==null}.
   */
  public static LocalDateTime dateToLocalDateTime(Date dt,
                                                  ZoneId zone) throws NullPointerException
  {
    Objects.requireNonNull(zone,
                           "zone==null");
    if (dt == null) {
      return null;
    }
    return dt.toInstant().atZone(zone).toLocalDateTime();
  }

  /**
   * Converts a supplied {@code Date} to {@code LocalTime} at the system default Timezone.
   *
   * @param supplier not null
   * @return localTime
   * @throws NullPointerException if {@code supplier==null}.
   * @see Utils#dateToLocalTime(java.util.Date, java.time.ZoneId)
   */
  public static LocalTime dateToLocalTime(Supplier<Date> supplier) throws NullPointerException
  {
    Objects.requireNonNull(supplier,
                           "supplier==null");
    return dateToLocalTime(supplier.get(),
                           ZoneId.systemDefault());
  }

  /**
   * Converts a supplied {@code Date} to {@code LocalTime} at the Timezone {@code zone}.
   *
   * @param supplier not null
   * @param zone not null
   * @return localTime
   * @throws NullPointerException if {@code supplier==null} or {@code zone==null}.
   * @see Utils#dateToLocalTime(java.util.Date, java.time.ZoneId)
   */
  public static LocalTime dateToLocalTime(Supplier<Date> supplier,
                                          ZoneId zone) throws NullPointerException
  {
    Objects.requireNonNull(supplier,
                           "supplier==null");
    return dateToLocalTime(supplier.get(),
                           zone);
  }

  /**
   * Converts a {@code Date} to {@code LocalTime}. Equal to {@code dateToLocalTime(time,Zone.systemDefault());}
   *
   * @param time time
   * @return a {@code LocalTime} or {@code null} if {@code time==null}.
   * @see Utils#dateToLocalTime(java.util.Date, java.time.ZoneId)
   */
  public static LocalTime dateToLocalTime(Date time)
  {
    return dateToLocalTime(time,
                           ZoneId.systemDefault());
  }

  /**
   * Converts a {@code Date} to {@code LocalTime}.
   *
   * @param time time
   * @param zone - not null
   * @return a {@code LocalTime} or {@code null} if {@code time==null}.
   * @throws NullPointerException if {@code zone==null}.
   */
  public static LocalTime dateToLocalTime(Date time,
                                          ZoneId zone) throws NullPointerException
  {
    Objects.requireNonNull(zone,
                           "zone==null");
    if (time == null) {
      return null;
    }
    return time.toInstant().atZone(zone).toLocalTime();
  }

  /**
   * Converts a supplied {@code Date} to {@code LocalDate} at the system default Timezone.
   *
   * @param supplier not null
   * @return localDate
   * @throws NullPointerException if {@code supplier==nul}
   * @see Utils#dateToLocalDate(java.util.function.Supplier, java.time.ZoneId)
   */
  public static LocalDate dateToLocalDate(Supplier<Date> supplier) throws NullPointerException
  {
    Objects.requireNonNull(supplier,
                           "supplier==null");
    return dateToLocalDate(supplier.get(),
                           ZoneId.systemDefault());
  }

  /**
   * Converts a supplier {@code Date} to {@code LocalDate} at the Timezone {@code zoneId}.
   *
   * @param supplier not null
   * @param zoneId not null
   * @return localDate
   * @throws NullPointerException if {@code supplier==null} or {@code zoneId==null}.
   * @see Utils#dateToLocalDate(java.util.Date, java.time.ZoneId)
   */
  public static LocalDate dateToLocalDate(Supplier<Date> supplier,
                                          ZoneId zoneId) throws NullPointerException
  {
    Objects.requireNonNull(supplier,
                           "supplier==null");
    return dateToLocalDate(supplier.get(),
                           zoneId);
  }

  /**
   * Converts a {@code date} to {@code LocaleDate}. Equal to {@code dateToLocalDate(date,ZoneId.systemDefault());}
   *
   * @param date date
   * @return a {@code LocalDate} or {@code null} if {@code date==null}.
   * @see Utils#localDateTimeToDate(java.time.LocalDateTime, java.time.ZoneId)
   */
  public static LocalDate dateToLocalDate(Date date)
  {
    return dateToLocalDate(date,
                           ZoneId.systemDefault());
  }

  /**
   * Converts a {@code Date} to {@code LocalDate}.
   *
   * @param date date
   * @param zone - not null
   * @return a {@code LocalDate} or {@code null} if {@code date==null}.
   * @throws NullPointerException if {@code zone==null}.
   */
  public static LocalDate dateToLocalDate(Date date,
                                          ZoneId zone) throws NullPointerException
  {
    Objects.requireNonNull(zone,
                           "zone==null");
    if (date == null) {
      return null;
    }
    return date.toInstant().atZone(zone).toLocalDate();
  }

  /**
   * Build a {@code LocalDateTime} object from two {@code Date}'s
   *
   * @param date date
   * @param time time
   * @return a LocalDateTime instance with the datepart from {@code date} and the timepart from {@code time} if time is not
   * {@code}, else the time part is {@code 00:00}, or {@code null} if {@code date==null && time==null}.
   * @throws NullPointerException if {@code date==null && time!=null}
   */
  public static LocalDateTime composeDateToLocalDateTime(Date date,
                                                         Date time) throws NullPointerException
  {
    if (date == null && time == null) {
      return null;
    }
    if (date == null) {
      throw new NullPointerException("date==null && time!=null");
    }
    if (time != null) {
      return LocalDateTime.of(LocalDateTime.ofInstant(date.toInstant(),
                                                      ZoneId.systemDefault()).toLocalDate(),
                              LocalDateTime.ofInstant(time.toInstant(),
                                                      ZoneId.systemDefault()).toLocalTime());
    } else {
      return LocalDateTime.of(LocalDateTime.ofInstant(date.toInstant(),
                                                      ZoneId.systemDefault()).toLocalDate(),
                              LocalTime.MIN);
    }
  }

  private static void byteHexString(int pValue,
                                    StringBuilder b)
  {
    String tmp = Integer.toHexString(pValue & 0xff);
    if (tmp.length() == 1) {
      b.append('0');
    }
    b.append(tmp);
  }

  public static String color2String(Color pColor)
  {
    if (pColor == null) {
      return null;
    }
    StringBuilder result = new StringBuilder();
    byteHexString(pColor.getRed(),
                  result);
    byteHexString(pColor.getGreen(),
                  result);
    byteHexString(pColor.getBlue(),
                  result);
    return result.toString();
  }

  public static Color string2Color(String pStr)
  {
    if (pStr == null) {
      return null;
    }
    if (pStr.length() != 6 && pStr.length() != 7) {
      throw new IllegalArgumentException();
    }
    int offset = 0;
    if (pStr.length() == 7) {
      offset = 1;
    }
    int red = Integer.parseInt(pStr.substring(offset,
                                              2 + offset),
                               16);
    int green = Integer.parseInt(pStr.substring(2 + offset,
                                                4 + offset),
                                 16);
    int blue = Integer.parseInt(pStr.substring(4 + offset,
                                               6 + offset),
                                16);
    return new Color(red,
                     green,
                     blue);
  }

  public static String colorAlpha2String(Color pColor)
  {
    if (pColor == null) {
      return null;
    }
    StringBuilder result = new StringBuilder();
    byteHexString(pColor.getAlpha(),
                  result);
    byteHexString(pColor.getRed(),
                  result);
    byteHexString(pColor.getGreen(),
                  result);
    byteHexString(pColor.getBlue(),
                  result);
    return result.toString();
  }

  public static Color string2ColorAlpha(String pStr)
  {
    if (pStr == null) {
      return null;
    }
    if (pStr.length() != 8 && pStr.length() != 9) {
      throw new IllegalArgumentException();
    }
    int offset = 0;
    if (pStr.length() == 9) {
      offset = 1;
    }
    int alpha = Integer.parseInt(pStr.substring(offset,
                                                2 + offset),
                                 16);
    int red = Integer.parseInt(pStr.substring(2 + offset,
                                              4 + offset),
                               16);
    int green = Integer.parseInt(pStr.substring(4 + offset,
                                                6 + offset),
                                 16);
    int blue = Integer.parseInt(pStr.substring(6 + offset,
                                               8 + offset),
                                16);
    return new Color(red,
                     green,
                     blue,
                     alpha);
  }

  public static boolean isHTTP(String www)
  {
    try {
      return testIsHTTP(new URI(www)) == null;
    } catch (URISyntaxException ex) {

    }
    return false;
  }

  public static String testIsHTTP(URI www)
  {
    if (www != null) {
      if (!"http".equals(www.getScheme()) && !"https".equals(www.getScheme())) {
        return "illegal scheme";
      }
      if (www.getHost() == null || www.getHost().trim().isEmpty()) {
        return "no host";
      }
    }
    return null;
  }

  public static boolean isEmail(String str)
  {
    try {
      return testIsEmail(new URI(str)) == null;
    } catch (URISyntaxException ex) {
    }
    return false;
  }

  public static String testIsEmail(URI u)
  {
    if (u != null) {
      if (!"email".equals(u.getScheme())) {
        return "illegal scheme";
      }
    }
    return null;
  }

}
