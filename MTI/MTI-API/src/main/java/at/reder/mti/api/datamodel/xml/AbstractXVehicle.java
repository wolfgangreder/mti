/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Decoder;
import at.reder.mti.api.datamodel.Era;
import at.reder.mti.api.datamodel.Scale;
import at.reder.mti.api.datamodel.ServiceEntry;
import at.reder.mti.api.datamodel.Vehicle;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author wolfi
 */
public abstract class AbstractXVehicle extends AbstractXInventoryObject
{

  protected Era era;
  protected double length;
  protected double width;
  protected double height;
  protected double weight;
  protected final Set<Decoder> decoder = new HashSet<>();
  protected Scale scale;
  protected final Set<ServiceEntry> serviceEntries = new HashSet<>();

  protected AbstractXVehicle()
  {
  }

  protected AbstractXVehicle(Vehicle v)
  {
    super(v);
  }

  @XmlAttribute(name = "era", namespace = "mti")
  @XmlIDREF
  public Era getEra()
  {
    return era;
  }

  public void setEra(Era era)
  {
    this.era = era;
  }

  @XmlAttribute(name = "length", namespace = "mti")
  public double getLength()
  {
    return length;
  }

  public void setLength(double length)
  {
    this.length = length;
  }

  @XmlAttribute(name = "width", namespace = "mti")
  public double getWidth()
  {
    return width;
  }

  public void setWidth(double width)
  {
    this.width = width;
  }

  @XmlAttribute(name = "height", namespace = "mti")
  public double getHeight()
  {
    return height;
  }

  public void setHeight(double height)
  {
    this.height = height;
  }

  @XmlAttribute(name = "weight", namespace = "mti")
  public double getWeight()
  {
    return weight;
  }

  public void setWeight(double weight)
  {
    this.weight = weight;
  }

  @XmlAttribute(name = "scale", namespace = "mti")
  @XmlIDREF
  public Scale getScale()
  {
    return scale;
  }

  public void setScale(Scale scale)
  {
    this.scale = scale;
  }

  @XmlElement(name = "decoder", namespace = "mti")
  @XmlElementWrapper(name = "decoders", namespace = "mti")
  @XmlJavaTypeAdapter(value = XDecoder.Adapter.class)
  public Set<Decoder> getDecoder()
  {
    return decoder;
  }

  @XmlElement(name = "serviceentry", namespace = "mti")
  @XmlElementWrapper(name = "serviceentries", namespace = "mti")
  @XmlJavaTypeAdapter(value = XServiceEntry.Adapter.class)
  public Set<ServiceEntry> getServiceEntries()
  {
    return serviceEntries;
  }

}
