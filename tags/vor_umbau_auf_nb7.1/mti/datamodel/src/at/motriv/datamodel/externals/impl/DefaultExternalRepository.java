/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.externals.impl;

import at.motriv.datamodel.externals.ExternalKind;
import at.motriv.datamodel.externals.ExternalRepository;
import at.motriv.datamodel.externals.MutableExternalRepository;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.jcip.annotations.Immutable;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
@Immutable
public class DefaultExternalRepository implements ExternalRepository
{

  private final UUID id;
  private final String name;
  private final URI uri;
  private final Set<ExternalKind> kinds;
  private final boolean readOnly;
  private final boolean immutable;
  private final Map<String, String> descriptions;

  public DefaultExternalRepository(UUID id, String name, URI uri, Collection<ExternalKind> kinds, boolean readOnly, boolean immutable,
                                   Map<? extends String, ? extends String> descriptions)
  {
    this.id = id;
    this.name = name;
    this.uri = uri;
    this.kinds = Collections.unmodifiableSet(new HashSet<ExternalKind>(kinds));
    this.readOnly = readOnly;
    this.immutable = immutable;
    this.descriptions = Collections.unmodifiableMap(new HashMap<String, String>(descriptions));
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
  public Map<? extends String, ? extends String> getDescriptions()
  {
    return descriptions;
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
    final DefaultExternalRepository other = (DefaultExternalRepository) obj;
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
    return getName() + " -> " + uri.toString();
  }
}
