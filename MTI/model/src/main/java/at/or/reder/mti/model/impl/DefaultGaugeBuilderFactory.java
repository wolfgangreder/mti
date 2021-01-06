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

import at.or.reder.mti.model.Gauge;
import at.or.reder.mti.model.api.Factories;
import at.or.reder.mti.model.api.GaugeContainer;
import at.or.reder.mti.model.api.StreamFormat;
import at.or.reder.mti.model.api.Streamer;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wolfgang Reder
 */
@ServiceProvider(service = Gauge.BuilderFactory.class)
public class DefaultGaugeBuilderFactory implements Gauge.BuilderFactory
{

  @Override
  public Gauge.Builder createGaugeBuilder()
  {
    return new BuilderImpl();
  }

  @Override
  public List<Gauge> getDefaultGauges() throws IOException
  {
    Streamer<GaugeContainer> streamer = Factories.getStreamer(StreamFormat.XML,
                                                              GaugeContainer.class);
    InputStream is = getClass().getResourceAsStream("/at/or/reder/mti/model/gauge.default.xml");
    return streamer.unmarshal(is);
  }

  private static final class BuilderImpl implements Gauge.Builder
  {

    private UUID id;
    private String name;
    private double scale;
    private double trackWidth;

    @Override
    public Gauge.Builder copy(Gauge gauge) throws NullPointerException
    {
      this.id = Objects.requireNonNull(gauge,
                                       "gauge is null").getId();
      this.name = gauge.getName();
      this.scale = gauge.getScale();
      this.trackWidth = gauge.getTrackWidth();
      return this;
    }

    @Override
    public Gauge.Builder id(UUID id) throws NullPointerException
    {
      this.id = Objects.requireNonNull(id,
                                       "id is null");
      return this;
    }

    @Override
    public Gauge.Builder name(String name) throws NullPointerException
    {
      Objects.requireNonNull(name,
                             "name is null");
      if (name.isBlank()) {
        throw new IllegalArgumentException("name is blank");
      }
      this.name = name;
      return this;
    }

    @Override
    public Gauge.Builder scale(double scale) throws IllegalArgumentException
    {
      if (scale <= 0 || !Double.isFinite(scale)) {
        throw new IllegalArgumentException("scale is zero or less than zero");
      }
      this.scale = scale;
      return this;
    }

    @Override
    public Gauge.Builder trackWidth(double trackWidth) throws IllegalArgumentException
    {
      if (trackWidth <= 0 || !Double.isFinite(trackWidth)) {
        throw new IllegalArgumentException("trackWidth is zero or less than zero");
      }
      this.trackWidth = trackWidth;
      return this;
    }

    @Override
    public Gauge build()
    {
      if (id == null) {
        id = UUID.randomUUID();
      }
      if (Objects.requireNonNull(name,
                                 "name is null").isBlank()) {
        throw new IllegalStateException("no name set");
      }
      if (Objects.requireNonNull(scale,
                                 "scale is null").doubleValue() <= 0) {
        throw new IllegalStateException("scale is zero or less than zero");
      }
      if (trackWidth <= 0 || !Double.isFinite(trackWidth)) {
        throw new IllegalStateException("trackWidth is zero or less than zero");
      }
      return new Impl(id,
                      name,
                      scale,
                      trackWidth);
    }

    private static final class Impl implements Gauge
    {

      private final UUID id;
      private final String name;
      private final double scale;
      private final double trackWidth;

      public Impl(UUID id,
                  String name,
                  double scale,
                  double trackWidth)
      {
        this.id = id;
        this.name = name;
        this.scale = scale;
        this.trackWidth = trackWidth;
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
      public double getScale()
      {
        return scale;
      }

      @Override
      public double getTrackWidth()
      {
        return trackWidth;
      }

      @Override
      public int hashCode()
      {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + (int) (Double.doubleToLongBits(this.scale) ^ (Double.doubleToLongBits(this.scale) >>> 32));
        hash = 23 * hash + (int) (Double.doubleToLongBits(this.trackWidth) ^ (Double.doubleToLongBits(this.trackWidth) >>> 32));
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
        final Impl other = (Impl) obj;
        if (Double.doubleToLongBits(this.trackWidth) != Double.doubleToLongBits(other.trackWidth)) {
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
        return Double.doubleToLongBits(this.scale) == Double.doubleToLongBits(other.scale);
      }

      @Override
      public String toString()
      {
        return name + " ( 1:" + Double.toString(scale) + ")";
      }

    }
  }
}
