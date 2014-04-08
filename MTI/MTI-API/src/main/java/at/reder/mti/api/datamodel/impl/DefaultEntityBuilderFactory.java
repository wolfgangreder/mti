/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.EntityKind;
import at.reder.mti.api.datamodel.xml.XEntity;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author wolfi
 */
@ServiceProvider(service = Entity.BuilderFactory.class)
public final class DefaultEntityBuilderFactory implements Entity.BuilderFactory
{

  @XmlJavaTypeAdapter(value = XEntity.Adapter.class)
  public static final class DefaultEntity implements Entity
  {

    private final EntityKind kind;
    private final URI uri;
    private final String description;
    private final String mimeType;
    private final String fileName;
    private final int size;
    private final Path tmpFile;
    private final Lookup lookup;

    private DefaultEntity(EntityKind kind,
                          URI uri,
                          String description,
                          String mimeType,
                          String fileName,
                          int size,
                          Path tmpFile)
    {
      this.kind = kind;
      this.uri = uri;
      this.description = description;
      this.mimeType = mimeType;
      this.fileName = fileName;
      this.size = size;
      this.tmpFile = tmpFile;
      if (tmpFile != null) {
        lookup = Lookups.singleton((Entity.Data) () -> DefaultEntity.this.tmpFile);
      } else {
        lookup = Lookup.EMPTY;
      }
    }

    @Override
    public Lookup getLookup()
    {
      return lookup;
    }

    @Override
    public URI getId()
    {
      return uri;
    }

    @Override
    public EntityKind getKind()
    {
      return kind;
    }

    @Override
    public URI getURI()
    {
      return uri;
    }

    @Override
    public String getDescription()
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
    public int getSize()
    {
      return size;
    }

    @Override
    public InputStream getData() throws IOException
    {
      if (tmpFile != null) {
        return Files.newInputStream(tmpFile, StandardOpenOption.READ);
      } else {
        return uri.toURL().openStream();
      }
    }

    @Override
    public int hashCode()
    {
      int hash = 7;
      hash = 79 * hash + Objects.hashCode(this.uri);
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
      final DefaultEntity other = (DefaultEntity) obj;
      return Objects.equals(this.uri, other.uri);
    }

  }

  public static final class DefaultEntityBuilder implements Entity.Builder
  {

    private static final Collection<? extends Class<? extends Entity>> implementingClasses = Collections.singleton(
            DefaultEntity.class);
    private EntityKind kind;
    private URI uri;
    private String description = "";
    private String mimeType;
    private String fileName;
    private int size = -1;
    private Path tmpFile;

    @Override
    public Entity.Builder copy(Entity e) throws NullPointerException
    {
      if (e == null) {
        throw new NullPointerException("entity==null");
      }
      this.kind = e.getKind();
      this.uri = e.getURI();
      this.description = e.getDescription();
      this.mimeType = e.getMimeType();
      this.fileName = e.getFileName();
      this.size = e.getSize();
      return this;
    }

    @Override
    public Entity.Builder data(Path tmpFile)
    {
      this.tmpFile = tmpFile;
      return this;
    }

    @Override
    public Entity.Builder description(String descr)
    {
      if (descr == null) {
        this.description = "";
      } else {
        this.description = descr;
      }
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
      if (kind == null) {
        throw new NullPointerException("kind==null");
      }
      this.kind = kind;
      return this;
    }

    @Override
    public Entity.Builder mimeType(String mimeType) throws NullPointerException, IllegalArgumentException
    {
      if (mimeType == null) {
        throw new NullPointerException("mimeType==null");
      }
      if (mimeType.trim().isEmpty()) {
        throw new IllegalArgumentException("mimeType is empty");
      }
      this.mimeType = mimeType;
      return this;
    }

    @Override
    public Entity.Builder size(int size) throws IllegalArgumentException
    {
      if (size < -1) {
        throw new IllegalArgumentException("size<-1");
      }
      this.size = size;
      return this;
    }

    @Override
    public Entity.Builder uri(URI uri) throws NullPointerException
    {
      if (uri == null) {
        throw new NullPointerException("uri==null");
      }
      this.uri = uri;
      return this;
    }

    @Override
    public DefaultEntity build() throws IllegalStateException
    {
      if (uri == null) {
        throw new IllegalStateException("uri==null");
      }
      if (mimeType == null) {
        throw new IllegalStateException("mimeType==null");
      }
      if (kind == null) {
        throw new IllegalStateException("kind==null");
      }
      return new DefaultEntity(kind, uri, description, mimeType, fileName, size, tmpFile);
    }

    @Override
    public Collection<? extends Class<? extends Entity>> getImplementingClasses()
    {
      return implementingClasses;
    }

    @Override
    public Class<?> getXmlClass()
    {
      return XEntity.class;
    }

  }

  @Override
  public Entity.Builder createBuilder()
  {
    return new DefaultEntityBuilder();
  }

}
