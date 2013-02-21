/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.scale;

import at.motriv.datamodel.entities.scale.impl.AbstractScale;
import at.motriv.datamodel.entities.scale.impl.DefaultScale;
import at.mountainsd.util.Builder;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public class ScaleBuilder extends AbstractScale implements Builder<Scale>
{

  private UUID id;
  private String name;
  private double scale;
  private double trackWidth;
  private UUID family;

  @Override
  public UUID getId()
  {
    return id;
  }

  public void setId(UUID id)
  {
    this.id = id != null ? id : UUID.randomUUID();
  }

  public ScaleBuilder id(UUID id)
  {
    setId(id);
    return this;
  }

  @Override
  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public ScaleBuilder name(String name)
  {
    setName(name);
    return this;
  }

  @Override
  public double getScale()
  {
    return scale;
  }

  public void setScale(double scale)
  {
    if (scale < 1) {
      throw new IllegalArgumentException("scale<1");
    }
    this.scale = scale;
  }

  public ScaleBuilder scale(double scale)
  {
    setScale(scale);
    return this;
  }

  @Override
  public double getTrackWidth()
  {
    return trackWidth;
  }

  public void setTrackWidth(double tw)
  {
    if (tw < 0) {
      throw new IllegalArgumentException("trackwidth<0");
    }
    this.trackWidth = tw;
  }

  public ScaleBuilder trackWidth(double tw)
  {
    setTrackWidth(tw);
    return this;
  }

  @Override
  public UUID getFamily()
  {
    return family;
  }

  public void setFamily(UUID familiy)
  {
    this.family = familiy;
  }

  public ScaleBuilder family(UUID family)
  {
    setFamily(family);
    return this;
  }

  @Override
  public Scale build()
  {
    return new DefaultScale(id, name, scale, trackWidth, family);
  }
}
