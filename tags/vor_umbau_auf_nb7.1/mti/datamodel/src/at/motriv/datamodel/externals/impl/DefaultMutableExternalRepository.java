/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.externals.impl;

import at.motriv.datamodel.externals.ExternalKind;
import at.motriv.datamodel.externals.ExternalRepository;
import at.motriv.datamodel.externals.MutableExternalRepository;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public class DefaultMutableExternalRepository implements MutableExternalRepository
{

  private UUID id;
  private String name;
  private URI uri;
  private final Set<ExternalKind> kinds = new HashSet<ExternalKind>();
  private boolean readOnly;
  private boolean immutable;
  private final Map<String, String> descriptions = new HashMap<String, String>();

  public DefaultMutableExternalRepository()
  {
    this.id = UUID.randomUUID();
    try {
      this.uri = new URI("file", "localhost", "/");
    } catch (URISyntaxException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  public DefaultMutableExternalRepository(ExternalRepository rep)
  {
    this.id = rep.getId();
    this.name = rep.getName();
    this.uri = rep.getURI();
    this.kinds.addAll(rep.getKinds());
    this.readOnly = rep.isReadOnly();
    this.immutable = rep.isImmutable();
    this.descriptions.putAll(rep.getDescriptions());
  }

  @Override
  public void setId(UUID id)
  {
    this.id = id;
  }

  @Override
  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public void addDescription(String language, String description)
  {
    descriptions.put(language, description);
  }

  @Override
  public Map<? extends String, ? extends String> getDescriptions()
  {
    return descriptions;
  }

  @Override
  public void removeDescription(String language)
  {
    descriptions.remove(language);
  }

  @Override
  public void setDescriptions(Map<? extends String, ? extends String> values)
  {
    descriptions.clear();
    descriptions.putAll(values);
  }

  @Override
  public String getDescription(Locale loc)
  {
    Locale used = loc != null ? loc : Locale.getDefault();
    String tmp = descriptions.get(used.getLanguage());
    if (tmp == null) {
      return descriptions.get(null);
    }
    return tmp;
  }

  @Override
  public void setURI(URI uri)
  {
    this.uri = uri;
  }

  @Override
  public void addKind(ExternalKind kind)
  {
    if (kind != null) {
      kinds.add(kind);
    }
  }

  @Override
  public void removeKind(ExternalKind kind)
  {
    kinds.remove(kind);
  }

  @Override
  public void setKinds(Collection<ExternalKind> kinds)
  {
    this.kinds.clear();
    this.kinds.addAll(kinds);
  }

  @Override
  public void setImmutable(boolean immutable)
  {
    this.immutable = immutable;
  }

  @Override
  public void setReadOnly(boolean readOnly)
  {
    this.readOnly = readOnly;
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
  public URI getURI()
  {
    return uri;
  }

  @Override
  public Set<ExternalKind> getKinds()
  {
    return kinds;
  }

  @Override
  public boolean isImmutable()
  {
    return immutable;
  }

  @Override
  public boolean isReadOnly()
  {
    return readOnly;
  }

  @Override
  public MutableExternalRepository getMutator()
  {
    return new DefaultMutableExternalRepository(this);
  }

  @Override
  public ExternalRepository build()
  {
    return new DefaultExternalRepository(id, name, uri, kinds, readOnly, immutable, descriptions);
  }

  @Override
  public Lookup getLookup()
  {
    return Lookup.EMPTY;
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
    final DefaultMutableExternalRepository other = (DefaultMutableExternalRepository) obj;
    if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString()
  {
    return MessageFormat.format("{0} ~> {1}",getName(), getURI()!=null ? getURI().toString() : null);
  }
}
