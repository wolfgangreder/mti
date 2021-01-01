/*
 * Copyright 2021 Wolfgang Reder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.or.reder.mti.model.impl;

import at.or.reder.dcc.util.Utils;
import at.or.reder.mti.model.Epoch;
import at.or.reder.mti.model.api.EpochContainer;
import at.or.reder.mti.model.api.Factories;
import at.or.reder.mti.model.api.StreamFormat;
import at.or.reder.mti.model.api.Streamer;
import at.or.reder.mti.model.utils.Localizable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wolfgang Reder
 */
@ServiceProvider(service = Epoch.BuilderFactory.class)
public final class DefaultEpochBuilderFactory implements Epoch.BuilderFactory
{

  @Override
  public Epoch.Builder createEpochBuilder()
  {
    return new Builder();
  }

  @Override
  public List<Epoch> getDefaultValues() throws IOException
  {
    InputStream is = getClass().getResourceAsStream("/at/or/reder/mti/model/epoch.default.json");
    if (is != null) {

      Streamer<EpochContainer> streamer = Factories.getStreamer(StreamFormat.JSON,
                                                                EpochContainer.class);
      if (streamer != null) {
        return streamer.unmarshal(is);
      }
    }
    return Collections.emptyList();
  }

  private static final class Builder implements Epoch.Builder
  {

    private UUID id;
    private final Localizable name = new Localizable(true);
    private int yearFrom;
    private Integer yearTo;
    private final Set<String> countries = new HashSet<>();
    private final Localizable comment = new Localizable(true);

    @Override
    public Epoch.Builder copy(Epoch toCopy) throws NullPointerException
    {
      this.id = Objects.requireNonNull(toCopy).getId();
      this.name.addValues(toCopy.getName());
      this.yearFrom = toCopy.getYearFrom();
      this.yearTo = toCopy.getYearTo();
      this.countries.clear();
      this.countries.addAll(toCopy.getCountries());
      this.comment.addValues(toCopy.getComment());
      return this;
    }

    @Override
    public Epoch.Builder id(UUID id) throws NullPointerException
    {
      this.id = Objects.requireNonNull(id,
                                       "id is null");
      return this;
    }

    @Override
    public Epoch.Builder name(String lang,
                              String name) throws IllegalArgumentException, NullPointerException
    {
      this.name.addValue(lang,
                         name);
      return this;
    }

    @Override
    public Epoch.Builder name(Localizable names) throws NullPointerException
    {
      this.name.getValues().clear();
      for (Map.Entry<String, String> e : names.getValues().entrySet()) {
        name(e.getKey(),
             e.getValue());
      }
      return this;
    }

    @Override
    public Epoch.Builder comment(Localizable comments)
    {
      this.comment.getValues().clear();
      for (Map.Entry<String, String> e : comments.getValues().entrySet()) {
        comment(e.getKey(),
                e.getValue());
      }
      return this;
    }

    @Override
    public Epoch.Builder yearFrom(int yearFrom)
    {
      this.yearFrom = yearFrom;
      return this;
    }

    @Override
    public Epoch.Builder yearTo(Integer yearTo)
    {
      this.yearTo = yearTo;
      return this;
    }

    @Override
    public Epoch.Builder addCountry(String country) throws IllegalArgumentException, NullPointerException
    {
      Objects.requireNonNull(country,
                             "country is null");
      if (country.isBlank()) {
        throw new IllegalArgumentException("country is blank");
      }
      this.countries.add(country);
      return this;
    }

    @Override
    public Epoch.Builder removeCountry(String country)
    {
      this.countries.remove(country);
      return this;
    }

    @Override
    public Epoch.Builder clearCountries()
    {
      this.countries.clear();
      return this;
    }

    @Override
    public Epoch.Builder addCountries(Collection<String> countries) throws IllegalArgumentException, NullPointerException
    {
      Objects.requireNonNull(countries,
                             "countries is null");
      countries.stream().filter((c) -> c != null && !c.isBlank()).forEach(this::addCountry);
      return this;
    }

    @Override
    public Epoch.Builder comment(String lang,
                                 String comment)
    {
      this.comment.addValue(lang,
                            comment);

      return this;
    }

    @Override
    public Epoch build()
    {
      Objects.requireNonNull(id,
                             "id is null");
      if (!name.normalize()) {
        throw new IllegalStateException("No default Name given");
      }
      comment.normalize();
      if (yearTo != null) {
        yearFrom = Math.min(yearFrom,
                            yearTo);
        yearTo = Math.max(yearFrom,
                          yearTo);
      }
      return new DefaultEpoch(id,
                              name,
                              yearFrom,
                              yearTo,
                              countries,
                              comment);
    }

  }

  private static final class DefaultEpoch implements Epoch, Serializable
  {

    private final UUID id;
    private final Localizable name;
    private final int yearFrom;
    private final Integer yearTo;
    private final Set<String> countries;
    private final Localizable comment;

    public DefaultEpoch(UUID id,
                        Localizable name,
                        int yearFrom,
                        Integer yearTo,
                        Set<String> countries,
                        Localizable comment)
    {
      this.id = id;
      this.name = name.toImutable();
      this.yearFrom = yearFrom;
      this.yearTo = yearTo;
      this.countries = Utils.copyToUnmodifiableSet(countries,
                                                   null);
      this.comment = comment.toImutable();
    }

    @Override
    public UUID getId()
    {
      return id;
    }

    @Override
    public Localizable getName()
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
    public Localizable getComment()
    {
      return comment;
    }

    @Override
    public int hashCode()
    {
      int hash = 3;
      hash = 47 * hash + Objects.hashCode(this.id);
      hash = 47 * hash + Objects.hashCode(this.name);
      hash = 47 * hash + this.yearFrom;
      hash = 47 * hash + Objects.hashCode(this.yearTo);
      hash = 47 * hash + Objects.hashCode(this.countries);
      hash = 47 * hash + Objects.hashCode(this.comment);
      return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final DefaultEpoch other = (DefaultEpoch) obj;
      if (this.yearFrom != other.yearFrom) {
        return false;
      }
      if (!Objects.equals(this.id,
                          other.id)) {
        return false;
      }
      if (!Objects.equals(this.name,
                          other.name)) {
        return false;
      }
      if (!Objects.equals(this.yearTo,
                          other.yearTo)) {
        return false;
      }
      if (!Objects.equals(this.countries,
                          other.countries)) {
        return false;
      }
      return Objects.equals(this.comment,
                            other.comment);
    }

    @Override
    public String toString()
    {
      return "Epoch :" + name.toString();
    }

  }
}
