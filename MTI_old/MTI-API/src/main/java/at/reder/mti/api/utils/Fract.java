/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.api.utils;

import at.reder.mti.api.utils.xml.XFract;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.MessageFormat;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(value = XFract.Adapter.class)
public final class Fract extends Number implements Comparable<Fract>
{

  private static final long serialVersionUID = 1L;
  private final long numerator;
  private final long denominator;
  public static final Fract ZERO = new Fract(0, 1, false);
  public static final Fract ONE = new Fract(1, 1, false);
  public static final Fract HO = new Fract(1, 87, false);

  public static Fract valueOf(long numerator, long demnominator) throws IllegalStateException
  {
    return valueOf(numerator, demnominator, false);
  }

  public static Fract valueOf(long numerator, long demnominator, boolean cancelDown) throws IllegalStateException
  {
    if (demnominator == 0) {
      throw new IllegalArgumentException("denominator==0");
    }
    if (numerator == 0) {
      return ZERO;
    }
    if (cancelDown && numerator == demnominator) {
      return ONE;
    }
    return new Fract(numerator, demnominator, cancelDown);
  }

  private static long gcd(long a, long b)
  {
    long h;
    while (b != 0) {
      h = a % b;
      a = b;
      b = h;
    }
    return a;
  }

  private Fract(long numerator, long denominator, boolean cancelDown)
  {
    if (cancelDown) {
      long gcd = gcd(numerator, denominator);
      this.numerator = numerator / gcd;
      this.denominator = denominator / gcd;
    } else {
      this.numerator = numerator;
      this.denominator = denominator;
    }
  }

  @Override
  public int intValue()
  {
    return (int) (numerator / denominator);
  }

  @Override
  public long longValue()
  {
    return numerator / denominator;
  }

  @Override
  public float floatValue()
  {
    return (float) ((double) numerator / (double) denominator);
  }

  @Override
  public double doubleValue()
  {
    return ((double) numerator / (double) denominator);
  }

  public long getNumerator()
  {
    return numerator;
  }

  public long getDenominator()
  {
    return denominator;
  }

  public Fract cancelDown()
  {
    long gcd = gcd(numerator, denominator);
    if (gcd != 1) {
      return new Fract(numerator / gcd, denominator / gcd, false);
    } else {
      return this;
    }
  }

  public Fract inverse() throws IllegalStateException
  {
    return inverse(false);
  }

  public Fract inverse(boolean cancelDown) throws IllegalStateException
  {
    if (numerator == 0) {
      throw new IllegalStateException("numerator==0");
    }
    return valueOf(denominator, numerator, cancelDown);
  }

  public Fract multiply(Fract f)
  {
    return multiply(f, false);
  }

  public Fract multiply(Fract f, boolean cancelDown)
  {
    return valueOf(numerator * f.numerator, denominator * f.denominator, cancelDown);
  }

  public Fract multiply(long l)
  {
    return multiply(l, false);
  }

  public Fract multiply(long l, boolean cancelDown)
  {
    return valueOf(numerator * l, denominator, cancelDown);
  }

  public static double multiply(Fract f, double d)
  {
    if (f.isZero() || d == 0d) {
      return 0d;
    }
    return (f.numerator * d) / (double) f.denominator;
  }

  public static BigDecimal multiply(Fract f, BigDecimal d)
  {
    if (f.isZero() || BigDecimal.ZERO.compareTo(d) == 0) {
      return BigDecimal.ZERO;
    }
    BigDecimal tmp = BigDecimal.valueOf(f.numerator);
    tmp = tmp.multiply(d);
    return tmp.divide(BigDecimal.valueOf(f.denominator), MathContext.DECIMAL128);
  }

  public Fract divide(Fract f) throws IllegalArgumentException
  {
    return divide(f, false);
  }

  public Fract divide(Fract f, boolean cancelDown) throws IllegalArgumentException
  {
    if (f.isZero()) {
      throw new IllegalArgumentException("division by zero");
    }
    return valueOf(numerator * f.denominator, denominator * f.numerator, cancelDown);
  }

  public double normalizedDenominator() throws IllegalStateException
  {
    if (numerator == 0) {
      throw new IllegalStateException("numerator==0");
    }
    return denominator / numerator;
  }

  public boolean isZero()
  {
    return numerator == 0;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (obj == this) {
      return true;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Fract other = (Fract) obj;
    if (this.numerator != other.numerator) {
      return false;
    }
    return this.denominator == other.denominator;
  }

  @Override
  public int compareTo(Fract o) throws NullPointerException
  {
    if (o == null) {
      throw new NullPointerException("o==null");
    }
    if (equals(o)) {
      return 0;
    }
    return Double.compare(doubleValue(), o.doubleValue());
  }

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 47 * hash + (int) (this.numerator ^ (this.numerator >>> 32));
    hash = 47 * hash + (int) (this.denominator ^ (this.denominator >>> 32));
    return hash;
  }

  @Override
  public String toString()
  {
    return MessageFormat.format("Fract:{0,number,#}/{1,number,#}", numerator, denominator);
  }

}
