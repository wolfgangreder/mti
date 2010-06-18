/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.externals;

import at.mountain_sd.objects.MutatorProvider;
import java.net.URI;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public interface ExternalRepository extends MutatorProvider<MutableExternalRepository>, Lookup.Provider
{

  public UUID getId();

  public String getName();

  public String getDescription(Locale loc);

  public Map<? extends String, ? extends String> getDescriptions();

  public URI getURI();

  public Set<ExternalKind> getKinds();

  public boolean isReadOnly();

  public boolean isImmutable();
}
