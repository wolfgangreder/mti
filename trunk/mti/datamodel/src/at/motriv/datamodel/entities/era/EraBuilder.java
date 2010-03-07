/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.era;

import at.motriv.datamodel.entities.era.impl.AbstractEra;
import at.motriv.datamodel.entities.era.impl.DefaultEra;
import at.mountainsd.util.Builder;
import java.util.UUID;
import net.jcip.annotations.NotThreadSafe;

/**
 *
 * @author wolfi
 */
@NotThreadSafe
public class EraBuilder extends AbstractEra implements Era, Builder<Era>
{

  private UUID id;
  private String name;
  private int yearFrom;
  private Integer yearTo;
  private String country;
  private String comment;

  @Override
  public UUID getId()
  {
    return id;
  }

  public EraBuilder id(UUID id)
  {
    if (id == null) {
      throw new IllegalArgumentException("id==null");
    }
    this.id = id;
    return this;
  }

  public void setId(UUID id)
  {
    id(id);
  }

  @Override
  public String getName()
  {
    return name;
  }

  public EraBuilder name(String name)
  {
    if (name == null) {
      throw new IllegalArgumentException("name==null");
    }
    this.name = name;
    return this;
  }

  public void setName(String name)
  {
    name(name);
  }

  @Override
  public int getYearFrom()
  {
    return yearFrom;
  }

  public EraBuilder yearFrom(int yearFrom)
  {
    if (yearTo != null && yearTo <= yearFrom) {
      throw new IllegalArgumentException("yearFrom>yearTo");
    }
    this.yearFrom = yearFrom;
    return this;
  }

  public void setYearFrom(int yearFrom)
  {
    yearFrom(yearFrom);
  }

  @Override
  public Integer getYearTo()
  {
    return yearTo;
  }

  public EraBuilder yearTo(Integer yearTo)
  {
    if (yearTo != null && yearTo <= yearFrom) {
      throw new IllegalArgumentException("yearTo<=yearFrom");
    }
    this.yearTo = yearTo;
    return this;
  }

  public void setYearTo(Integer yearTo)
  {
    yearTo(yearTo);
  }

  @Override
  public String getCountry()
  {
    return country;
  }

  public EraBuilder country(String country)
  {
    this.country = country;
    return this;
  }

  public void setCountry(String country)
  {
    this.country = country;
  }

  @Override
  public String getComment()
  {
    return comment;
  }

  public EraBuilder comment(String comment)
  {
    this.comment = comment;
    return this;
  }

  public void setComment(String comment)
  {
    this.comment = comment;
  }

  @Override
  public Era build()
  {
    return new DefaultEra(id, name, yearFrom, yearTo, country, comment);
  }
}
