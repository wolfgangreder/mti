/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.externals;

import at.mountainsd.util.Builder;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface MutableExternalRepository extends ExternalRepository, Builder<ExternalRepository>
{

  public void setId(UUID id);

  public void setName(String name);

  public void setURI(URI uri);

  public void addKind(ExternalKind kind);

  public void removeKind(ExternalKind kind);

  public void setKinds(Collection<ExternalKind> kinds);

  public void setReadOnly(boolean readOnly);

  public void setImmutable(boolean immutable);

  public void addDescription(String language, String description);

  public void removeDescription(String language);

  public void setDescriptions(Map<? extends String, ? extends String> values);

}
