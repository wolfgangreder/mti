/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.utils.xml;

import at.reder.mti.api.utils.Fract;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author wolfi
 */
@XmlRootElement(name = "fract", namespace = "mti")
public final class XFract
{

  public static final class Adapter extends XmlAdapter<XFract, Fract>
  {

    @Override
    public Fract unmarshal(XFract v)
    {
      if (v != null) {
        return v.toFract();
      }
      return null;
    }

    @Override
    public XFract marshal(Fract v)
    {
      if (v != null) {
        return new XFract(v);
      }
      return null;
    }

  }
  private long numerator;
  private long denominator;

  public XFract()
  {
  }

  public XFract(Fract f)
  {
    this.numerator = f.getNumerator();
    this.denominator = f.getDenominator();
  }

  public Fract toFract()
  {
    return Fract.valueOf(numerator, denominator);
  }

  @XmlAttribute(name = "numerator", namespace = "mti")
  public long getNumerator()
  {
    return numerator;
  }

  public void setNumerator(long numerator)
  {
    this.numerator = numerator;
  }

  @XmlAttribute(name = "denominator", namespace = "mti")
  public long getDenominator()
  {
    return denominator;
  }

  public void setDenominator(long denominator)
  {
    this.denominator = denominator;
  }

}
