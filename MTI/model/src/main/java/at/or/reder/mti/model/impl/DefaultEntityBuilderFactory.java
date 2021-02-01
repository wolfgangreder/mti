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

import at.or.reder.mti.model.Entity;
import at.or.reder.mti.model.EntityKind;
import at.or.reder.dcc.util.Localizable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wolfgang Reder
 */
@ServiceProvider(service = Entity.BuilderFactory.class)
public final class DefaultEntityBuilderFactory implements Entity.BuilderFactory
{

  @Override
  public Entity.Builder createEntityBuilder()
  {
    return new Builder();
  }

  private final class Builder implements Entity.Builder
  {

    private UUID id;
    private final Localizable description = new Localizable(true);
    private String fileName;
    private EntityKind kind;
    private String mimeType;
    private long size;
    private URL url;
    private URL origin;

    @Override
    public Entity.Builder copy(Entity e) throws NullPointerException
    {
      this.id = Objects.requireNonNull(e,
                                       "entity is null").getId();
      this.description.getValues().clear();
      this.description.addValues(e.getDescription().getValues());
      this.fileName = e.getFileName();
      this.kind = e.getKind();
      this.mimeType = e.getMimeType();
      this.size = e.getSize();
      this.url = null;
      this.origin = e.getOrigin();
      return this;
    }

    @Override
    public Entity.Builder id(UUID id)
    {
      this.id = id;
      return this;
    }

    @Override
    public Entity.Builder description(Localizable descr)
    {
      this.description.getValues().clear();
      this.description.addValues(descr.getValues());
      return this;
    }

    @Override
    public Entity.Builder description(String lang,
                                      String desc)
    {
      this.description.addValue(lang,
                                desc);
      return this;
    }

    @Override
    public Entity.Builder fileName(String fileName)
    {
      this.fileName = fileName;
      return this;
    }

    @Override
    public Entity.Builder kind(EntityKind kind) throws NullPointerException
    {
      this.kind = kind;
      return this;
    }

    @Override
    public Entity.Builder mimeType(String mimeType) throws NullPointerException, IllegalArgumentException
    {
      this.mimeType = mimeType;
      return this;
    }

    @Override
    public Entity.Builder size(long size)
    {
      this.size = size;
      return this;
    }

    @Override
    public Entity.Builder origin(URL origin)
    {
      this.origin = origin;
      return this;
    }

    @Override
    public Entity.Builder data(URL tmpFile)
    {
      this.url = tmpFile;
      return this;
    }

    @Override
    public Entity build()
    {
      return new Impl(id,
                      description,
                      fileName,
                      kind,
                      mimeType,
                      size,
                      url,
                      origin);
    }

  }

  private final class Impl implements Entity
  {

    private final UUID id;
    private final Localizable description;
    private final String fileName;
    private final EntityKind kind;
    private final String mimeType;
    private final long size;
    private final URL url;
    private final URL origin;

    public Impl(UUID id,
                Localizable description,
                String fileName,
                EntityKind kind,
                String mimeType,
                long size,
                URL url,
                URL origin)
    {
      this.id = id;
      this.description = description;
      this.fileName = fileName;
      this.kind = kind;
      this.mimeType = mimeType;
      this.size = size;
      this.url = url;
      this.origin = origin;
    }

    @Override
    public EntityKind getKind()
    {
      return kind;
    }

    @Override
    public UUID getId()
    {
      return id;
    }

    @Override
    public Localizable getDescription()
    {
      return description;
    }

    @Override
    public String getMimeType()
    {
      return mimeType;
    }

    @Override
    public String getFileName()
    {
      return fileName;
    }

    @Override
    public long getSize()
    {
      return size;
    }

    @Override
    public InputStream getData() throws IOException
    {
      return url.openStream();
    }

    @Override
    public URL getDataURL()
    {
      return url;
    }

    @Override
    public URL getOrigin()
    {
      return origin;
    }

    @Override
    public Lookup getLookup()
    {
      return Lookup.EMPTY;
    }

  }
}
