/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Scale;
import at.reder.mti.api.utils.Fract;
import at.reder.mti.api.utils.xml.XFract;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
@XmlRootElement(name = "scale", namespace = "mti")
public final class XScale
{

  public static final class Adapter extends XmlAdapter<XScale, Scale>
  {

    @Override
    public Scale unmarshal(XScale v)
    {
      if (v != null) {
        return v.toScale();
      }
      return null;
    }

    @Override
    public XScale marshal(Scale v)
    {
      if (v != null) {
        return new XScale(v);
      }
      return null;
    }

  }
  private String id;
  private String name;
  private Fract scale;
  private double trackWidth;

  public XScale()
  {
  }

  public XScale(Scale scale) throws NullPointerException
  {
    if (scale == null) {
      throw new NullPointerException("scale==null");
    }
    this.id = scale.getId().toString();
    this.name = scale.getName();
    this.scale = scale.getScale();
    this.trackWidth = scale.getTrackWidth();
  }

  public Scale toScale() throws NullPointerException, IllegalArgumentException, IllegalStateException
  {
    return Lookup.getDefault().lookup(Scale.BuilderFactory.class).createBuilder().id(UUID.fromString(id)).name(name).scale(scale).
            trackWidth(
            trackWidth).build();
  }

  @XmlAttribute(name = "id", namespace = "mti", required = true)
  @XmlID
  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  @XmlElement(name = "name", namespace = "mti", required = true)
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @XmlElement(name = "scale", namespace = "mti", required = true)
  @XmlJavaTypeAdapter(value = XFract.Adapter.class)
  public Fract getScale()
  {
    return scale;
  }

  public void setScale(Fract scale)
  {
    this.scale = scale;
  }

  @XmlElement(name = "trackWdith", namespace = "mti", required = true)
  public double getTrackWidth()
  {
    return trackWidth;
  }

  public void setTrackWidth(double trackWidth)
  {
    this.trackWidth = trackWidth;
  }

}
