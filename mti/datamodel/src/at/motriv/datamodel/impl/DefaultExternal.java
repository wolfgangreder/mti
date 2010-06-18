/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.impl;

import at.motriv.datamodel.externals.External;
import at.motriv.datamodel.externals.ExternalKind;
import at.motriv.datamodel.externals.ExternalRepository;
import at.mountainsd.util.StreamFactory;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author wolfi
 */
public class DefaultExternal implements External
{

  private final UUID id;
  private final ExternalRepository repository;
  private final ExternalKind kind;
  private final String file;
  private final String description;
  private final String mime;
  private final String fileName;
  private final String originalFileName;
  private final InstanceContent content;
  private final AbstractLookup lookup;

  public DefaultExternal(UUID id, ExternalRepository repository, ExternalKind kind, String file, String description, String mime,
                         String fileName, String originalFileName)
  {
    this.id = id;
    this.repository = repository;
    this.kind = kind;
    this.file = file;
    this.description = description;
    this.mime = mime;
    this.content = new InstanceContent();
    this.lookup = new AbstractLookup(content);
    switch (kind) {
      case IMAGE:
        content.add(kind, new ImageConvertor());
        break;
      case SOUND:
      case DOCUMENT:
      case PARAMETER:
    }
    content.add(file, new StreamFactoryConvertor());
    this.fileName = fileName;
    this.originalFileName = originalFileName;
  }

  @Override
  public UUID getId()
  {
    return id;
  }

  @Override
  public ExternalRepository getRepository()
  {
    return repository;
  }

  @Override
  public ExternalKind getKind()
  {
    return kind;
  }

  @Override
  public String getFile()
  {
    return file;
  }

  @Override
  public String getDescription()
  {
    return description;
  }

  @Override
  public String getMimeType()
  {
    return mime;
  }

  @Override
  public String getFileName()
  {
    return fileName;
  }

  @Override
  public String getOriginalFileName()
  {
    return originalFileName;
  }

  @Override
  public Lookup getLookup()
  {
    return lookup;
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
    final DefaultExternal other = (DefaultExternal) obj;
    if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }

  private class ImageConvertor implements InstanceContent.Convertor<ExternalKind, Image>
  {

    @Override
    public Image convert(ExternalKind t)
    {
      return null;
      //TODO implement
    }

    @Override
    public Class<? extends Image> type(ExternalKind t)
    {
      return Image.class;
    }

    @Override
    public String id(ExternalKind t)
    {
      return "ImageConvertor_" + t.name();
    }

    @Override
    public String displayName(ExternalKind t)
    {
      return id(t);
    }
  }

  private class StreamFactoryConvertor implements InstanceContent.Convertor<String, StreamFactory>, StreamFactory
  {

    @Override
    public StreamFactory convert(String t)
    {
      return this;
    }

    @Override
    public Class<? extends StreamFactory> type(String t)
    {
      return getClass();
    }

    @Override
    public String id(String t)
    {
      return "StreamFactoryConvertor_" + t;
    }

    @Override
    public String displayName(String t)
    {
      return id(t);
    }

    @Override
    public InputStream getInputStream() throws IOException
    {
      return null;
    }

    @Override
    public OutputStream getOutputStream() throws IOException
    {
      return null;
    }
  }
}
