/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.EntityKind;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
@XmlRootElement(name = "entity", namespace = "mti")
public class XEntity
{

  public static final class WithoutDataAdapter extends XmlAdapter<XEntity, Entity>
  {

    @Override
    public Entity unmarshal(XEntity v)
    {
      if (v != null) {
        return v.toEntity();
      }
      return null;
    }

    @Override
    public XEntity marshal(Entity v)
    {
      if (v != null) {
        return new XEntity(v, false);
      }
      return null;
    }

  }

  public static final class Adapter extends XmlAdapter<XEntity, Entity>
  {

    @Override
    public Entity unmarshal(XEntity v)
    {
      if (v != null) {
        return v.toEntity();
      }
      return null;
    }

    @Override
    public XEntity marshal(Entity v)
    {
      if (v != null) {
        return new XEntity(v, true);
      }
      return null;
    }

  }
  private EntityKind kind;
  private URI uri;
  private String description;
  private String mimeType;
  private String fileName;
  private int size;
  private boolean withData;
  private Path tmpFile;

  public XEntity()
  {
  }

  private XEntity(Entity e, boolean withData)
  {
    this.kind = e.getKind();
    this.uri = e.getURI();
    this.description = e.getDescription();
    this.mimeType = e.getMimeType();
    this.fileName = e.getFileName();
    this.size = e.getSize();
    this.withData = withData;
    Entity.Data ed = e.getLookup().lookup(Entity.Data.class);
    if (ed != null) {
      tmpFile = ed.getTmpFile();
    }
  }

  public Entity toEntity()
  {
    Entity.Builder<? extends Entity> builder = Lookup.getDefault().lookup(Entity.BuilderFactory.class).createBuilder();
    builder.description(description);
    builder.fileName(fileName);
    builder.kind(kind);
    builder.mimeType(mimeType);
    builder.size(size);
    builder.uri(uri);
    if (tmpFile != null) {
      builder.data(tmpFile);
    }
    return builder.build();
  }

  @XmlAttribute(name = "kind", namespace = "mti")
  public EntityKind getKind()
  {
    return kind;
  }

  @XmlElement(name = "id")
  @XmlID
  public String getId()
  {
    return uri.toString();
  }

  @XmlTransient
  public URI getUri()
  {
    return uri;
  }

  @XmlElement(name = "description", namespace = "mti")
  public String getDescription()
  {
    return description;
  }

  @XmlAttribute(name = "mimetype", namespace = "mti")
  public String getMimeType()
  {
    return mimeType;
  }

  @XmlAttribute(name = "filename", namespace = "mti")
  public String getFileName()
  {
    return fileName;
  }

  @XmlAttribute(name = "size", namespace = "mti")
  public int getSize()
  {
    return size;
  }

  @XmlElement(name = "data", namespace = "mti", required = false, nillable = true)
  public InputStream getData() throws IOException
  {
    if (tmpFile != null) {
      return Files.newInputStream(tmpFile, StandardOpenOption.READ);
    } else {
      return uri.toURL().openStream();
    }
  }

  public void setData(InputStream is) throws IOException
  {
    tmpFile = Files.createTempFile("ent", ".tmp");
    tmpFile.toFile().deleteOnExit();
    size = 0;
    try (OutputStream os = Files.newOutputStream(tmpFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
      byte[] buffer = new byte[4096];
      int read;
      while ((read = is.read(buffer)) > 0) {
        size += read;
        os.write(buffer, 0, read);
      }
    }
  }

  public void setKind(EntityKind kind)
  {
    this.kind = kind;
  }

  public void setUri(URI uri)
  {
    this.uri = uri;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public void setMimeType(String mimeType)
  {
    this.mimeType = mimeType;
  }

  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }

  public void setSize(int size)
  {
    this.size = size;
  }

}
