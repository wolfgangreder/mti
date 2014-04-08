/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Scale;
import at.reder.mti.api.datamodel.xml.XScale;
import at.reder.mti.api.utils.Fract;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;

/**
 * Standardimplementation des Interfaces {@link Scale}
 *
 * @author wolfi
 */
@ServiceProvider(service = Scale.BuilderFactory.class)
public final class DefaultScaleBuilderFactory implements Scale.BuilderFactory
{

  public static final class Impl implements Scale
  {

    private final UUID id;
    private final String name;
    private final Fract scale;
    private final double trackWidth;

    private Impl(UUID id, String name, Fract scale, double trackWidth)
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
    public Fract getScale()
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
      int hash = 7;
      hash = 89 * hash + Objects.hashCode(this.id);
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
      return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString()
    {
      return "Impl{" + "id=" + id + ", name=" + name + '}';
    }

  }

  public static final class ScaleBuilder implements Scale.Builder<Impl>
  {

    private static final Collection<? extends Class<? extends Impl>> implementingClasses = Collections.singleton(Impl.class);
    private UUID id;
    private String name;
    private Fract scale;
    private double trackWidth;

    @Override
    public Scale.Builder<? extends Scale> copy(Scale scale) throws NullPointerException
    {
      if (scale == null) {
        throw new NullPointerException("scale==null");
      }
      this.id = scale.getId();
      this.name = scale.getName();
      this.scale = scale.getScale();
      this.trackWidth = scale.getTrackWidth();
      return this;
    }

    @Override
    public Scale.Builder<? extends Scale> id(UUID id) throws NullPointerException
    {
      if (id == null) {
        throw new NullPointerException("id==null");
      }
      this.id = id;
      return this;
    }

    @Override
    public Scale.Builder<? extends Scale> name(String name) throws NullPointerException, IllegalArgumentException
    {
      if (name == null) {
        throw new NullPointerException("name==null");
      }
      if (name.trim().isEmpty()) {
        throw new IllegalArgumentException("name is empty");
      }
      this.name = name.trim();
      return this;
    }

    @Override
    public Scale.Builder<? extends Scale> scale(Fract scale) throws NullPointerException, IllegalArgumentException
    {
      if (scale == null) {
        throw new NullPointerException("scale==null");
      }
      if (scale.doubleValue() <= 0) {
        throw new IllegalArgumentException("scale<=0");
      }
      this.scale = scale;
      return this;
    }

    @Override
    public Scale.Builder<? extends Scale> trackWidth(double trackWidth) throws IllegalArgumentException
    {
      if (trackWidth <= 0) {
        throw new IllegalArgumentException("trackWidth<=0");
      }
      this.trackWidth = trackWidth;
      return this;
    }

    @Override
    public Impl build() throws IllegalStateException
    {
      if (id == null) {
        throw new IllegalStateException("id==null");
      }
      if (name == null) {
        throw new IllegalStateException("name==null");
      }
      if (name.trim().isEmpty()) {
        throw new IllegalStateException("name is empty");
      }
      if (scale == null) {
        throw new IllegalStateException("scale==null");
      }
      if (scale.doubleValue() <= 0) {
        throw new IllegalStateException("scale<=0");
      }
      if (trackWidth <= 0) {
        throw new IllegalStateException("trackWidth<=0");
      }
      return new Impl(id, name, scale, trackWidth);
    }

    @Override
    public Collection<? extends Class<? extends Scale>> getImplementingClasses()
    {
      return implementingClasses;
    }

    @Override
    public Class<?> getXmlClass()
    {
      return XScale.class;
    }

  }

  @Override
  public Scale.Builder<? extends Scale> createBuilder()
  {
    return new ScaleBuilder();
  }

}
