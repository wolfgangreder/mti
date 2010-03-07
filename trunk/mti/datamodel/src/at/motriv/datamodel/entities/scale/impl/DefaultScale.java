/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.scale.impl;

import java.util.UUID;

/**
 *
 * @author wolfi
 */
public class DefaultScale extends AbstractScale
{

  private final UUID id;
  private final String name;
  private final double scale;
  private final double trackWidth;
  private final UUID family;

  public DefaultScale(UUID id, String name, double scale, double trackWidth, UUID family)
  {
    this.id = id;
    this.name = name;
    this.scale = scale;
    this.trackWidth = trackWidth;
    this.family = family;
  }

  @Override
  public UUID getId()
  {
    return id;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public double getScale()
  {
    return scale;
  }

  @Override
  public double getTrackWidth()
  {
    return trackWidth;
  }

  @Override
  public UUID getFamily()
  {
    return family;
  }
}
