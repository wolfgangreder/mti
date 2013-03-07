/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Locomotive;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
@XmlRootElement(name = "locomotive", namespace = "mti")
public final class XLocomotive extends AbstractXVehicle
{

  public static final class Adapter extends XmlAdapter<XLocomotive, Locomotive>
  {

    @Override
    public Locomotive unmarshal(XLocomotive v)
    {
      if (v != null) {
        return v.toLocomotive();
      }
      return null;
    }

    @Override
    public XLocomotive marshal(Locomotive v)
    {
      if (v != null) {
        return new XLocomotive(v);
      }
      return null;
    }

  }
  private String number;
  private String arrangement;
  private String kind;
  private String clazz;
  private String company;
  private String country;

  public XLocomotive()
  {
  }

  public XLocomotive(Locomotive l)
  {
    super(l);
    number = l.getLocomotiveNumber();
    arrangement = l.getWheelArrangement();
    kind = l.getKind();
    clazz = l.getLocomotiveClass();
    company = l.getCompany();
    country = l.getCountry();
  }

  public Locomotive toLocomotive()
  {
    Locomotive.Builder<? extends Locomotive> builder = Lookup.getDefault().lookup(Locomotive.BuilderFactory.class).createBuilder();
    initBuilder(builder);
    builder.company(company);
    builder.country(country);
    builder.kind(kind);
    builder.locomotiveClass(clazz);
    builder.locomotiveNumber(number);
    builder.wheelArrangement(arrangement);
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

  @XmlElement(name = "wheelarrangement", namespace = "mti")
  public String getArrangement()
  {
    return arrangement;
  }

  public void setArrangement(String arrangement)
  {
    this.arrangement = arrangement;
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
