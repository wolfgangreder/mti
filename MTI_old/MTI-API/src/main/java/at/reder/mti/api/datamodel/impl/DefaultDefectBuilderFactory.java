/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Defect;
import at.reder.mti.api.datamodel.xml.XDefect;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = Defect.BuilderFactory.class)
@XmlSeeAlso(value = XDefect.class)
public final class DefaultDefectBuilderFactory implements Defect.BuilderFactory
{

  private static final Collection<? extends Class<? extends Defect>> implementingClasses = Collections.singleton(DefectImpl.class);

  public static final class DefectImpl implements Defect
  {

    private final UUID id;
    private final LocalDate date;
    private final String description;

    private DefectImpl(UUID id,
                       LocalDate date,
                       String description)
    {
      this.id = id;
      this.date = date;
      this.description = description;
    }

    @Override
    public UUID getId()
    {
      return id;
    }

    @Override
    public LocalDate getDate()
    {
      return date;
    }

    @Override
    public String getDescription()
    {
      return description;
    }

    @Override
    public int hashCode()
    {
      int hash = 3;
      hash = 17 * hash + Objects.hashCode(this.id);
      return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final DefectImpl other = (DefectImpl) obj;
      return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString()
    {
      return "DefectImpl{" + "id=" + id + ", date=" + date + '}';
    }

  }

  public static final class DefectBuilder implements Defect.Builder
  {

    private UUID id;
    private LocalDate date;
    private String description;

    @Override
    public Defect.Builder copy(Defect defect) throws NullPointerException
    {
      if (defect == null) {
        throw new NullPointerException("defect==null2");
      }
      this.id = defect.getId();
      this.date = defect.getDate();
      this.description = defect.getDescription();
      return this;
    }

    @Override
    public Defect.Builder id(UUID id) throws NullPointerException
    {
      if (id == null) {
        throw new NullPointerException("id==null");
      }
      this.id = id;
      return this;
    }

    @Override
    public Defect.Builder date(LocalDate date) throws NullPointerException
    {
      if (date == null) {
        throw new NullPointerException("date==null");
      }
      this.date = date;
      return this;
    }

    @Override
    public Defect.Builder description(String descr) throws NullPointerException, IllegalArgumentException
    {
      if (descr == null) {
        throw new NullPointerException("description==null");
      }
      if (descr.trim().isEmpty()) {
        throw new IllegalArgumentException("description is empty");
      }
      this.description = descr;
      return this;
    }

    @Override
    public Defect build() throws IllegalStateException
    {
      if (id == null) {
        throw new IllegalStateException("id==null");
      }
      if (date == null) {
        throw new IllegalStateException("date==null");
      }
      if (description == null) {
        throw new IllegalStateException("description==null");
      }
      if (description.trim().isEmpty()) {
        throw new IllegalStateException("description is empty");
      }
      return new DefectImpl(id, date, description);
    }

    @Override
    public Collection<? extends Class<? extends Defect>> getImplementingClasses()
    {
      return implementingClasses;
    }

    @Override
    public Class<?> getXmlClass()
    {
      return XDefect.class;
    }

  }

  @Override
  public Defect.Builder createBuilder()
  {
    return new DefectBuilder();
  }

}
