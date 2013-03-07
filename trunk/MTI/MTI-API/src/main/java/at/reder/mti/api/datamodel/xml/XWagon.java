/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Wagon;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
@XmlRootElement(name = "wagon", namespace = "mti")
public final class XWagon extends AbstractXVehicle
{

  public static final class Adapter extends XmlAdapter<XWagon, Wagon>
  {

    @Override
    public Wagon unmarshal(XWagon v)
    {
      if (v != null) {
        return v.toWagon();
      }
      return null;
    }

    @Override
    public XWagon marshal(Wagon v)
    {
      if (v != null) {
        return new XWagon(v);
      }
      return null;
    }

  }
  private String number;
  private int wheelCount;
  private String kind;
  private String clazz;
  private String company;
  private String country;

  public XWagon()
  {
  }

  public XWagon(Wagon l)
  {
    super(l);
    number = l.getWagonNumber();
    wheelCount = l.getWheelCount();
    kind = l.getKind();
    clazz = l.getWagonClass();
    company = l.getCompany();
    country = l.getCountry();
  }

  public Wagon toWagon()
  {
    Wagon.Builder<? extends Wagon> builder = Lookup.getDefault().lookup(Wagon.BuilderFactory.class).createBuilder();
    initBuilder(builder);
    builder.company(company);
    builder.country(country);
    builder.kind(kind);
    builder.wagonClass(clazz);
    builder.wagonNumber(number);
    builder.wheelCount(wheelCount);
    return builder.build();
  }

  @XmlElement(name = "wagonNumber", namespace = "mti")
  public String getNumber()
  {
    return number;
  }

  public void setNumber(String number)
  {
    this.number = number;
  }

  @XmlAttribute(name = "wheelCount", namespace = "mti")
  public int getWheelCount()
  {
    return wheelCount;
  }

  public void setWheelCount(int wheelCount)
  {
    this.wheelCount = wheelCount;
  }

  @XmlElement(name = "kind", namespace = "mti")
  public String getKind()
  {
    return kind;
  }

  public void setKind(String kind)
  {
    this.kind = kind;
  }

  @XmlElement(name = "clazz", namespace = "mti")
  public String getClazz()
  {
    return clazz;
  }

  public void setClazz(String clazz)
  {
    this.clazz = clazz;
  }

  @XmlElement(name = "company", namespace = "mti")
  public String getCompany()
  {
    return company;
  }

  public void setCompany(String company)
  {
    this.company = company;
  }

  @XmlElement(name = "country", namespace = "mti")
  public String getCountry()
  {
    return country;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

}
