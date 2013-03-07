/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Era;
import at.reder.mti.api.datamodel.xml.XEra;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import org.openide.util.lookup.ServiceProvider;

/**
 * Standardimplementation von {@code Era}
 *
 * @author wolfi
 * @see Era
 */
@ServiceProvider(service = Era.BuilderFactory.class)
public final class DefaultEraBuilderFactory implements Era.BuilderFactory
{

  public static final class Impl implements Era
  {

    private final UUID id;
    private final String name;
    private final int yearFrom;
    private final Integer yearTo;
    private final Set<String> countries;
    private final String comment;

    private Impl(UUID id,
                 String name,
                 int yearFrom,
                 Integer yearTo,
                 Collection<String> countries,
                 String comment)
    {
      this.id = id;
      this.name = name;
      this.yearFrom = yearFrom;
      this.yearTo = yearTo;
      this.countries = Collections.unmodifiableSet(new HashSet<>(countries));
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
    public Set<String> getCountries()
    {
      return countries;
    }

    @Override
    public String getComment()
    {
      return comment;
    }

    @Override
    public int hashCode()
    {
      int hash = 7;
      hash = 83 * hash + Objects.hashCode(this.id);
      return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (obj == null) {
        return false;
      }
      if (obj == this) {
        return true;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final Impl other = (Impl) obj;
      if (!Objects.equals(this.id, other.id)) {
        return false;
      }
      return true;
    }

    @Override
    public String toString()
    {
      return "Era{" + "id=" + id + ", name=" + name + '}';
    }

  }

  public static final class EraBuilder implements Era.Builder<Era>
  {

    private static final Pattern patternCountry = Pattern.compile("([A-Z]{2})|([0-9]{3})");
    private static final Set<? extends Class<? extends Era>> implementingClasses = Collections.singleton(Impl.class);
    private UUID id;
    private String name;
    private int yearFrom;
    private Integer yearTo;
    private final Set<String> countries = new HashSet<>();
    private String comment = "";
    private Impl toCopy;

    @Override
    public Era.Builder<? extends Era> copy(Era toCopy) throws NullPointerException
    {
      if (toCopy instanceof Impl) {
        this.toCopy = (Impl) toCopy;
      } else if (toCopy != null) {
        this.toCopy = null;
        id(toCopy.getId());
        name(toCopy.getName());
        yearFrom(toCopy.getYearFrom());
        yearTo(toCopy.getYearTo());
        clearCountries();
        countries.addAll(toCopy.getCountries());
        comment(toCopy.getComment());
      } else {
        throw new NullPointerException("toCopy==null");
      }
      return this;
    }

    @Override
    public Era.Builder<? extends Era> id(UUID id) throws NullPointerException
    {
      if (id == null) {
        throw new NullPointerException("id==null");
      }
      toCopy = null;
      this.id = id;
      return this;
    }

    @Override
    public Era.Builder<? extends Era> name(String name) throws IllegalArgumentException, NullPointerException
    {
      if (name == null) {
        throw new NullPointerException("name==null");
      }
      if (name.trim().isEmpty()) {
        throw new IllegalArgumentException("name is empty");
      }
      toCopy = null;
      this.name = name;
      return this;
    }

    @Override
    public Era.Builder<? extends Era> yearFrom(int yearFrom)
    {
      toCopy = null;
      this.yearFrom = yearFrom;
      return this;
    }

    @Override
    public Era.Builder<? extends Era> yearTo(Integer yearTo)
    {
      toCopy = null;
      this.yearTo = yearTo;
      return this;
    }

    private void checkCountry(String country) throws IllegalArgumentException
    {
      if (country == null) {
        throw new NullPointerException("country==null");
      }
      synchronized (patternCountry) {
        if (!patternCountry.matcher(country).matches()) {
          throw new IllegalArgumentException("country " + country + " is not a valid country");
        }
      }
    }

    @Override
    public Era.Builder<? extends Era> addCountry(String country) throws IllegalArgumentException, NullPointerException
    {
      checkCountry(country);
      toCopy = null;
      countries.add(country);
      return this;
    }

    @Override
    public Era.Builder<? extends Era> removeCountry(String country)
    {
      if (country != null) {
        toCopy = null;
        countries.remove(country);
      }
      return this;
    }

    @Override
    public Era.Builder<? extends Era> clearCountries()
    {
      toCopy = null;
      countries.clear();
      return this;
    }

    @Override
    public Era.Builder<? extends Era> addCountries(Collection<String> countries) throws IllegalArgumentException,
                                                                                        NullPointerException
    {
      if (countries == null) {
        throw new NullPointerException("countries==null");
      }
      for (String country : countries) {
        addCountry(country);
      }
      toCopy = null;
      return this;
    }

    @Override
    public Era.Builder<? extends Era> comment(String comment)
    {
      if (comment == null) {
        this.comment = "";
      } else {
        this.comment = comment;
      }
      toCopy = null;
      return this;
    }

    @Override
    public Impl build() throws IllegalStateException
    {
      if (toCopy != null) {
        return toCopy;
      }
      if (yearFrom < 1700) {
        throw new IllegalStateException("yearFrom<1700");
      }
      if (yearTo != null && yearTo < yearFrom) {
        throw new IllegalStateException("yearTo<yearFrom");
      }
      if (name == null) {
        throw new IllegalStateException("name==null");
      }
      if (id == null) {
        throw new IllegalStateException("id==null");
      }
      if (comment == null) {
        throw new IllegalStateException("comment==null");
      }
      return new Impl(id, name.trim(), yearFrom, yearTo, countries, comment.trim());
    }

    @Override
    public Collection<? extends Class<? extends Era>> getImplementingClasses()
    {
      return implementingClasses;
    }

    @Override
    public Class<?> getXmlClass()
    {
      return XEra.class;
    }

  }

  @Override
  public Era.Builder<? extends Era> createBuilder()
  {
    return new EraBuilder();
  }

}
