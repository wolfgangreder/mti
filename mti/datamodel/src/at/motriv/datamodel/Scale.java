/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface Scale
{

  public UUID getId();

  public String getName();

  public int getScale();

  public double getTrackWidth();
}
