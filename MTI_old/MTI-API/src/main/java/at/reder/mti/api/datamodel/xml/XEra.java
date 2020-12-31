/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Era;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.openide.util.Lookup;

/**
 * JAXB-fähige {@code Era} implementation.
 *
 * @author wolfi
 */
@XmlRootElement(name = "era", namespace = "mti")
public final class XEra
{

  private UUID id;
  private String name;
  private int yearFrom;
  private Integer yearTo;
  private final List<String> countries = new LinkedList<>();
  private String comment;

  /**
   * XmlAdapter {@link Era}<->{@link XEra}
   *
   * @see Era
   * @see XmlAdapter
   */
  public static final class Adapter extends XmlAdapter<XEra, Era>
  {

    @Override
    public Era unmarshal(XEra v)
    {
      if (v != null) {
        return v.toEra();
      }
      return null;
    }

    @Override
    public XEra marshal(Era v)
    {
      if (v != null) {
        return new XEra(v);
      }
      return null;
    }

  }

  public XEra()
  {
  }

  public XEra(Era era) throws NullPointerException
  {
    if (era == null) {
      throw new NullPointerException("era==null");
    }
    id = era.getId();
    name = era.getName();
    yearFrom = era.getYearFrom();
    yearTo = era.getYearTo();
    comment = era.getComment();
    era.getCountries().stream().
            filter((l) -> (l != null && !l.trim().isEmpty())).
            forEach((l) -> {
              countries.add(l.trim());
            });
  }

  public Era toEra() throws IllegalArgumentException, NullPointerException
  {
    Era.BuilderFactory factory = Lookup.getDefault().lookup(Era.BuilderFactory.class);
    assert (factory != null);
    Era.Builder builder = factory.createBuilder();
    for (String c : countries) {
      if (c != null && !c.trim().isEmpty()) {
        builder.addCountry(c);
      }
    }
    builder.comment(comment);
    builder.name(name);
    builder.id(id);
    builder.yearFrom(yearFrom);
    builder.yearTo(yearTo);
    return builder.build();
  }

  @XmlTransient
  public UUID getId()
  {
    return id;
  }

  public void setId(UUID id)
  {
    this.id = id;
  }

  @XmlAttribute(name = "id", namespace = "mti", required = true)
  @XmlID
  public String getStringId()
  {
    return id != null ? id.toString() : null;
  }

  public void setStringId(String strId)
  {
    try {
      id = UUID.fromString(strId);
    } catch (Throwable th) {
      id = UUID.randomUUID();
    }
  }

  @XmlAttribute(name = "name", namespace = "mti", required = true)
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @XmlAttribute(name = "yearFrom", namespace = "mti", required = true)
  public int getYearFrom()
  {
    return yearFrom;
  }

  public void setYearFrom(int yearFrom)
  {
    this.yearFrom = yearFrom;
  }

  @XmlAttribute(name = "yearTo", namespace = "mti", required = false)
  public Integer getYearTo()
  {
    return yearTo;
  }

  public void setYearTo(Integer yearTo)
  {
    this.yearTo = yearTo;
  }

  @XmlElement(name = "comment", namespace = "mti", required = false, nillable = true)
  public String getComment()
  {
    return comment;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

  @XmlElement(name = "countries", namespace = "mti", nillable = true, required = false)
  @XmlList
  public List<String> getCountries()
  {
    return countries;
  }

}
