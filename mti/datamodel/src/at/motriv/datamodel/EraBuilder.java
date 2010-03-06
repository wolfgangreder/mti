/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import at.motriv.datamodel.impl.AbstractEra;
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
  public Era build()
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
