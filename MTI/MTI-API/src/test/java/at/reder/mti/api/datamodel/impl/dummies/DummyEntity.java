/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.impl.dummies;

import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.EntityKind;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public class DummyEntity implements Entity
{

  private URI id;
  private String mimeType = "text/plain";
  private boolean realData = false;

  public DummyEntity()
  {
    this("http://www.mountain-sd.at");
  }

  public DummyEntity(String uri)
  {
    try {
      id = new URI(uri);
    } catch (URISyntaxException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  public DummyEntity(URI uri, String mimeType)
  {
    this.id = uri;
    this.mimeType = mimeType;
    realData = true;
  }

  @Override
  public EntityKind getKind()
  {
    return EntityKind.DECODER_DESCRIPTION;
  }

  @Override
  public URI getId()
  {
    return id;
  }

  @Override
  public URI getURI()
  {
    return id;
  }

  @Override
  public String getDescription()
  {
    return "description";
  }

  @Override
  public String getMimeType()
  {
    return mimeType;
  }

  @Override
  public String getFileName()
  {
    return "filename";
  }

  @Override
  public int getSize()
  {
    return -1;
  }

  @Override
  public InputStream getData() throws IOException
  {
    if (realData) {
      return id.toURL().openStream();
    }
    return null;
  }

  @Override
  public Lookup getLookup()
  {
    return Lookup.EMPTY;
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 53 * hash + Objects.hashCode(this.id);
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
    final DummyEntity other = (DummyEntity) obj;
    if (!Objects.equals(this.id, other.id)) {
      return false;
    }
    return true;
  }

}
