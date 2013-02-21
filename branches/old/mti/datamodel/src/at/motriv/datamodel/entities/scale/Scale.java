/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.scale;

import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface Scale
{

  public UUID getId();

  public String getName();

  public double getScale();

  public double getTrackWidth();

  public UUID getFamily();
}
