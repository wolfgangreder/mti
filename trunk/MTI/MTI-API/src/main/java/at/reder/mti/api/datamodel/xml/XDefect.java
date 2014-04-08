/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Defect;
import at.reder.mti.api.utils.xml.ISODateXmlAdapter;
import java.time.LocalDate;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
@XmlRootElement(name = "defect", namespace = "mti")
public final class XDefect
{

  public static final class Adapter extends XmlAdapter<XDefect, Defect>
  {

    @Override
    public Defect unmarshal(XDefect v)
    {
      if (v != null) {
        return v.toDefect();
      }
      return null;
    }

    @Override
    public XDefect marshal(Defect v)
    {
      if (v != null) {
        return new XDefect(v);
      }
      return null;
    }

  }
  private UUID id;
  private LocalDate date;
  private String description;

  public XDefect()
  {
  }

  public XDefect(Defect defect)
  {
    this.id = defect.getId();
    this.date = defect.getDate();
    this.description = defect.getDescription();
  }

  public Defect toDefect()
  {
    return Lookup.getDefault().lookup(Defect.BuilderFactory.class).createBuilder().id(id).date(date).description(description).
            build();
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

  @XmlAttribute(name = "id")
  @XmlID
  public String getStringId()
  {
    return id != null ? id.toString() : null;
  }

  public void setStringId(String s)
  {
    id = s != null ? UUID.fromString(s) : null;
  }

  @XmlAttribute(name = "date", namespace = "mti")
  @XmlJavaTypeAdapter(value = ISODateXmlAdapter.class)
  public LocalDate getDate()
  {
    return date;
  }

  public void setDate(LocalDate date)
  {
    this.date = date;
  }

  @XmlElement(name = "description", namespace = "mti")
  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

}
