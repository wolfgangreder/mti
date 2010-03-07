/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.scale.impl;

import at.motriv.datamodel.entities.scale.Scale;
import at.motriv.datamodel.entities.scale.ScaleBuilder;
import at.motriv.datamodel.entities.scale.ScaleXMLSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

/**
 *
 * @author wolfi
 */
public class ScaleParser extends DefaultHandler2
{

  private static class NameKey
  {

    private final String lang;
    private final String name;

    public NameKey(String lang, String name)
    {
      this.lang = lang != null ? lang : "";
      this.name = name;
    }

    public String getLang()
    {
      return lang;
    }

    public String getName()
    {
      return name;
    }

    @Override
    public String toString()
    {
      return lang + " => " + name;
    }
  }
  private final List<Scale> items = new LinkedList<Scale>();
  private final List<NameKey> name = new ArrayList<NameKey>();
  private String language;
  private UUID id;
  private double scale;
  private double trackwidth;
  private UUID family;
  private final StringBuilder builder = new StringBuilder();
  private Deque<ScaleXMLSupport.Elements> elementStack = new LinkedList<ScaleXMLSupport.Elements>();

  public Collection<? extends Scale> getItems()
  {
    return items;
  }

  @Override
  public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws SAXException, IOException
  {
    if (ScaleXMLSupport.DTD_PUBLIC_ID.equals(publicId)) {
      FileObject fo = FileUtil.getConfigFile("xml/entities/Motriv/DTD_Scales");
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

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    ScaleXMLSupport.Elements current = popElement();
    switch (current) {
      case name:
        name.add(new NameKey(language, builder.toString()));
        break;
      case scale:
        processEndScale();
        break;
    }
  }

  private String getLocName()
  {
    String locLang = Locale.getDefault().getLanguage();
    String result = null;
    String def = null;
    for (NameKey k:name) {
      if (k.getLang().equals(locLang)) {
        result = k.getName();
        break;
      }
      if ("".equals(k.getLang())) {
        def = k.getName();
      }
    }
    if (result==null) {
      if (def!=null) {
      result = def;
      } else {
        if (!name.isEmpty()) {
          result = name.get(0).getName();
        } else {
          result = id.toString();
        }
      }
    }
    return result;
  }
  private void processEndScale()
  {
    String locName = getLocName();
    ScaleBuilder scalebuilder = new ScaleBuilder();
    scalebuilder.family(family);
    scalebuilder.id(id);
    scalebuilder.name(locName);
    scalebuilder.scale(scale);
    scalebuilder.trackWidth(trackwidth);
    items.add(scalebuilder.build());
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    ScaleXMLSupport.Elements current = pushElement(qName);
    switch (current) {
      case name:
        language = attributes.getValue(ScaleXMLSupport.ATTR_LANG);
        builder.setLength(0);
        break;
      case scale:
        processStartScale(attributes);
        break;
    }
  }

  private void processStartScale(Attributes attributes) throws SAXException
  {
    name.clear();
    try {
      id = UUID.fromString(attributes.getValue(ScaleXMLSupport.ATTR_ID));
      family = UUID.fromString(attributes.getValue(ScaleXMLSupport.ATTR_FAMILY));
      String tmp = attributes.getValue(ScaleXMLSupport.ATTR_SCALE);
      try {
        scale = Double.parseDouble(tmp);
      } catch (NullPointerException e) {
        throw new SAXException("scale:", e);
      } catch (NumberFormatException e) {
        throw new SAXException("scale", e);
      }
      tmp = attributes.getValue(ScaleXMLSupport.ATTR_TRACKWIDTH);
      try {
        trackwidth = Double.parseDouble(tmp);
      } catch (NullPointerException e) {
        throw new SAXException("trackwidth:", e);
      } catch (NumberFormatException e) {
        throw new SAXException("trackwidth", e);
      }
    } catch (IllegalArgumentException e) {
      throw new SAXException(e);
    }
  }

  private ScaleXMLSupport.Elements pushElement(String qName) throws SAXException
  {
    try {
      ScaleXMLSupport.Elements result = ScaleXMLSupport.Elements.valueOf(qName);
      elementStack.addFirst(result);
      return result;
    } catch (IllegalArgumentException e) {
      throw new SAXException(e);
    }
  }

  private ScaleXMLSupport.Elements popElement()
  {
    return elementStack.removeFirst();
  }
}
