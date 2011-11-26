/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.era;

import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface Era
{

  public UUID getId();

  public String getName();

  public int getYearFrom();

  public Integer getYearTo();

  public String getCountry();

  public String getComment();
}
