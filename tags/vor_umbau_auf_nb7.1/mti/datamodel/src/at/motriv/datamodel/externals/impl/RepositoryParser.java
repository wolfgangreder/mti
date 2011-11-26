/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.externals.impl;

import at.motriv.datamodel.externals.ExternalKind;
import at.motriv.datamodel.externals.ExternalRepository;
import at.motriv.datamodel.externals.ExternalRepositoryXMLSupport;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

/**
 *
 * @author wolfi
 */
public class RepositoryParser extends DefaultHandler2
{

  private final StringBuilder builder = new StringBuilder();
  private String name;
  private boolean readOnly;
  private boolean immutable;
  private URI uri;
  private UUID id;
  private final Set<ExternalKind> kinds = new HashSet<ExternalKind>();
  private final Deque<ExternalRepositoryXMLSupport.Elements> elementStack = new LinkedList<ExternalRepositoryXMLSupport.Elements>();
  private final List<ExternalRepository> repositories = new LinkedList<ExternalRepository>();
  private final Map<String,String> descriptions = new HashMap<String,String>();
  private String language;

  public Collection<? extends ExternalRepository> getItems()
  {
    return new ArrayList<ExternalRepository>(repositories);
  }

  @Override
  public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws SAXException, IOException
  {
    if (ExternalRepositoryXMLSupport.DTD_PUBLIC_ID.equals(publicId)) {
      FileObject fo = FileUtil.getConfigFile("xml/entities/Motriv/DTD_Repository");
      if (fo != null) {
        return new InputSource(fo.getInputStream());
      }
    }
    return super.resolveEntity(name, publicId, baseURI, systemId);
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    builder.append(ch, start, length);
  }

  private ExternalRepositoryXMLSupport.Elements pushElement(String qName) throws SAXException
  {
    try {
      ExternalRepositoryXMLSupport.Elements result = ExternalRepositoryXMLSupport.Elements.valueOf(qName);
      elementStack.addFirst(result);
      return result;
    } catch (IllegalArgumentException e) {
      throw new SAXException(e);
    }
  }

  private ExternalRepositoryXMLSupport.Elements popElement()
  {
    return elementStack.removeFirst();
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    ExternalRepositoryXMLSupport.Elements current = popElement();
    switch (current) {
      case uri:
        try {
          this.uri = new URI(builder.toString());
        } catch (URISyntaxException ex) {
          Exceptions.printStackTrace(ex);
          uri = null;
        }
        break;
      case repository:
        if (this.uri != null) {
          repositories.add(new DefaultExternalRepository(id, name, this.uri, kinds, readOnly, immutable,descriptions));
          kinds.clear();
          uri = null;
        }
        break;
      case description:
        descriptions.put(language,builder.toString());
        builder.setLength(0);
        break;
    }
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    ExternalRepositoryXMLSupport.Elements current = pushElement(qName);
    switch (current) {
      case uri:
        builder.setLength(0);
        break;
      case kind:
        String tmp = attributes.getValue(ExternalRepositoryXMLSupport.ATTR_NAME);
        if (tmp != null) {
          try {
            ExternalKind kind = ExternalKind.valueOf(tmp.trim());
            kinds.add(kind);
          } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
          }
        }
        break;
      case repository:
        id = UUID.fromString(attributes.getValue(ExternalRepositoryXMLSupport.ATTR_ID));
        name = attributes.getValue(ExternalRepositoryXMLSupport.ATTR_NAME);
        readOnly = Boolean.parseBoolean(attributes.getValue(ExternalRepositoryXMLSupport.ATTR_READONLY));
        immutable = Boolean.parseBoolean(attributes.getValue(ExternalRepositoryXMLSupport.ATTR_IMMUTABLE));
        descriptions.clear();
        kinds.clear();
        break;
      case repositories:
        kinds.clear();
        break;
      case description:
        language = attributes.getValue(ExternalRepositoryXMLSupport.ATTR_LANG);
        builder.setLength(0);
        break;
    }
  }
}
