/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.impl;

import at.motriv.datamodel.Era;
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

  public DefaultEra(UUID id, String name, int yearFrom, Integer yearTo)
  {
    if (yearTo!=null && yearTo<=yearFrom) {
      throw new IllegalArgumentException("yearFrom>yearTo");
    }
    this.id = id;
    this.name = name;
    this.yearFrom = yearFrom;
    this.yearTo = yearTo;
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
}
