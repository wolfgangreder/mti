/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.era.impl;

import at.motriv.datamodel.entities.era.Era;
import java.util.UUID;
import net.jcip.annotations.Immutable;

/**
 *
 * @author wolfi
 */
@Immutable
public class DefaultEra extends AbstractEra implements Era
{

  private final UUID id;
  private final String name;
  private final int yearFrom;
  private final Integer yearTo;
  private final String country;
  private final String comment;

  public DefaultEra(UUID id, String name, int yearFrom, Integer yearTo, String country, String comment)
  {
    if (yearTo != null && yearTo <= yearFrom) {
      throw new IllegalArgumentException("yearFrom>yearTo");
    }
    this.id = id;
    this.name = name;
    this.yearFrom = yearFrom;
    this.yearTo = yearTo;
    this.country = country;
    this.comment = comment;
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
  public int getYearFrom()
  {
    return yearFrom;
  }

  @Override
  public Integer getYearTo()
  {
    return yearTo;
  }

  @Override
  public String getCountry()
  {
    return country;
  }

  @Override
  public String getComment()
  {
    return comment;
  }
}
