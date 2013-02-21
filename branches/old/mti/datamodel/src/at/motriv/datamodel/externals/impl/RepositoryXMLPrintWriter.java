/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.externals.impl;

import at.motriv.datamodel.externals.ExternalKind;
import at.motriv.datamodel.externals.ExternalRepository;
import at.motriv.datamodel.externals.ExternalRepositoryXMLSupport;
import at.mountainsd.util.XMLPrintWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author wolfi
 */
public class RepositoryXMLPrintWriter extends XMLPrintWriter
{

  public RepositoryXMLPrintWriter(OutputStream os)
  {
    super(os);
  }

  public void print(Collection<? extends ExternalRepository> repositories)
  {
    Map<String, String> params = new HashMap<String, String>();
    params.put(ExternalRepositoryXMLSupport.ATTR_VERSION, "1.0");
    printHeader(ExternalRepositoryXMLSupport.Elements.repositories.name(),
                params,
                ExternalRepositoryXMLSupport.DTD_SYSTEM_ID,
                ExternalRepositoryXMLSupport.DTD_PUBLIC_ID);
    for (ExternalRepository repository : repositories) {
      params.clear();
      params.put(ExternalRepositoryXMLSupport.ATTR_ID, repository.getId().toString());
      params.put(ExternalRepositoryXMLSupport.ATTR_IMMUTABLE, Boolean.toString(repository.isImmutable()));
      params.put(ExternalRepositoryXMLSupport.ATTR_READONLY, Boolean.toString(repository.isReadOnly()));
      params.put(ExternalRepositoryXMLSupport.ATTR_NAME, encodeXML(repository.getName()));
      openElement(ExternalRepositoryXMLSupport.Elements.repository.name(), params);
      openElement(ExternalRepositoryXMLSupport.Elements.uri.name());
      print(encodeXML(repository.getURI().toString()));
      closeElement();
      params.clear();
      for (ExternalKind k : repository.getKinds()) {
        params.put(ExternalRepositoryXMLSupport.ATTR_NAME, k.name());
        openElement(ExternalRepositoryXMLSupport.Elements.kind.name(), params);
        closeElement();
      }
      for (Map.Entry<? extends String, ? extends String> e : repository.getDescriptions().entrySet()) {
        params.clear();
        if (e.getKey() != null) {
          params.put(ExternalRepositoryXMLSupport.ATTR_LANG, e.getKey());
        }
        openElement(ExternalRepositoryXMLSupport.Elements.description.name(), params);
        printCDATA(e.getValue());
        closeElement();
      }
      closeElement();
    }
    closeElement();
  }
}
