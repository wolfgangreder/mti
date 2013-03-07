/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Defect;
import at.reder.mti.api.datamodel.ServiceEntry;
import at.reder.mti.api.datamodel.UsedSparePart;
import at.reder.mti.api.utils.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
@XmlRootElement(name = "serviceentry", namespace = "mti")
public final class XServiceEntry
{

  public static final class Adapter extends XmlAdapter<XServiceEntry, ServiceEntry>
  {

    @Override
    public ServiceEntry unmarshal(XServiceEntry v)
    {
      if (v != null) {
        return v.toServiceEntry();
      }
      return null;
    }

    @Override
    public XServiceEntry marshal(ServiceEntry v)
    {
      if (v != null) {
        return new XServiceEntry(v);
      }
      return null;
    }

  }
  private UUID id;
  private Timestamp date;
  private final List<Defect> defects = new LinkedList<>();
  private final List<UsedSparePart> parts = new LinkedList<>();
  private String description;

  public XServiceEntry()
  {
  }

  private XServiceEntry(ServiceEntry se)
  {
    this.id = se.getId();
    this.date = se.getDate();
    defects.addAll(se.getDefectsResolved());
    parts.addAll(se.getPartsUsed());
    description = se.getDescription();
  }

  public ServiceEntry toServiceEntry()
  {
    return Lookup.getDefault().lookup(ServiceEntry.BuilderFactory.class).createBuilder().addDefects(defects).addSpareParts(parts).
            date(date).description(description).id(id).build();
  }

  @XmlTransient
  public UUID getId()
  {
    return id;
  }

  @XmlAttribute(name = "id")
  @XmlID
  public String getStringId()
  {
    return id.toString();
  }

  @XmlAttribute(name = "date", namespace = "mti")
  @XmlJavaTypeAdapter(value = Timestamp.DateAdapter.class)
  public Timestamp getDate()
  {
    return date;
  }

  @XmlElement(name = "defecta", namespace = "mti")
  @XmlList
  @XmlIDREF
  public List<Defect> getDefects()
  {
    return defects;
  }

  @XmlElement(name = "usedpart", namespace = "mti")
  @XmlElementWrapper(name = "usedparts", namespace = "mti")
  @XmlJavaTypeAdapter(value = XUsedSparePart.Adapter.class)
  public List<UsedSparePart> getParts()
  {
    return parts;
  }

  @XmlElement(name = "description", namespace = "mti")
  public String getDescription()
  {
    return description;
  }

  public void setId(UUID id)
  {
    this.id = id;
  }

  public void setDate(Timestamp date)
  {
    this.date = date;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

}
