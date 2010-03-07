/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.era.impl;

import at.motriv.datamodel.entities.era.Era;
import at.motriv.datamodel.entities.era.EraBuilder;
import at.motriv.datamodel.entities.era.EraXMLSupport;
import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
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
public class EraParser extends DefaultHandler2
{

  private final List<Era> items = new LinkedList<Era>();
  private final StringBuilder builder = new StringBuilder();
  private UUID id;
  private int yearFrom;
  private Integer yearTo;
  private String name;
  private String comment;
  private String country;
  private final Deque<EraXMLSupport.Elements> elementStack = new LinkedList<EraXMLSupport.Elements>();

  public List<? extends Era> getItems()
  {
    return items;
  }

  @Override
  public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId) throws SAXException, IOException
  {
    if (EraXMLSupport.DTD_PUBLIC_ID.equals(publicId)) {
      FileObject fo = FileUtil.getConfigFile("xml/entities/Motriv/DTD_Era");
      if (fo != null) {
        return new InputSource(fo.getInputStream());
      }
    }
    return super.resolveEntity(name, publicId, baseURI, systemId);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException
  {
    EraXMLSupport.Elements current = popElement();
    switch (current) {
      case comment:
        comment = builder.toString();
        break;
      case name:
        name = builder.toString();
        break;
      case era:
        items.add(new EraBuilder().comment(comment).country(country).id(id).yearFrom(yearFrom).yearTo(yearTo).name(name).build());
        name = null;
        comment = null;
        id = null;
        yearFrom = 0;
        yearTo = null;
        break;
    }
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
  {
    EraXMLSupport.Elements current = pushElement(qName);
    switch (current) {
      case comment:
        builder.setLength(0);
        break;
      case era:
        processStartEra(attributes);
        break;
      case eras:
        processStartEras(attributes);
        break;
      case name:
        builder.setLength(0);
        break;
    }
  }

  private void processStartEras(Attributes attributes) throws SAXException
  {
    country = attributes.getValue(EraXMLSupport.ATTR_COUNTRY);
  }

  private void processStartEra(Attributes attributes) throws SAXException
  {
    try {
      id = UUID.fromString(attributes.getValue(EraXMLSupport.ATTR_ID));
      String tmp = attributes.getValue(EraXMLSupport.ATTR_YEARFROM);
      if (tmp == null || tmp.trim().length() == 0) {
        throw new SAXException("yearfrom not found");
      }
      try {
        yearFrom = Integer.parseInt(tmp);
      } catch (NumberFormatException e) {
        throw new SAXException("yearfrom", e);
      }
      tmp = attributes.getValue(EraXMLSupport.ATTR_YEARTO);
      if (tmp != null && tmp.trim().length() > 0) {
        try {
          yearTo = Integer.parseInt(tmp);
        } catch (NumberFormatException e) {
          yearTo = null;
        }
      } else {
        yearTo = null;
      }
    } catch (IllegalArgumentException e) {
      throw new SAXException(e);
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException
  {
    builder.append(ch, start, length);
  }

  private EraXMLSupport.Elements pushElement(String qName) throws SAXException
  {
    try {
      EraXMLSupport.Elements result = EraXMLSupport.Elements.valueOf(qName);
      elementStack.addFirst(result);
      return result;
    } catch (IllegalArgumentException e) {
      throw new SAXException(e);
    }
  }

  private EraXMLSupport.Elements popElement()
  {
    return elementStack.removeFirst();
  }
}
