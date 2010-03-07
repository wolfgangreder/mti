/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.era.impl;

import at.motriv.datamodel.entities.era.Era;
import at.motriv.datamodel.entities.era.EraDataObject;
import at.motriv.datamodel.entities.era.EraXMLSupport;
import at.mountainsd.util.XMLException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author wolfi
 */
public class DefaultEraXMLSupport implements EraXMLSupport, Node.Cookie
{

  private final EraDataObject dob;

  public DefaultEraXMLSupport()
  {
    this.dob = null;
  }

  public DefaultEraXMLSupport(EraDataObject dob)
  {
    this.dob = dob;
  }

  @Override
  public void streamToXML(OutputStream out, Collection<? extends Era> clctn) throws IOException
  {
    OutputStream os;
    if (out != null) {
      os = out;
    } else {
      FileObject fo = dob.getPrimaryFile();
      os = fo.getOutputStream();
    }
    try {
      EraXMLPrintWriter printer = new EraXMLPrintWriter(os);
      Era first = clctn.isEmpty() ? clctn.iterator().next() : null;
      printer.printEraHeader(first != null ? first.getCountry() : null);
      printer.print(clctn, null);
      printer.close();
    } finally {
      if (os != null && out == null) {
        os.close();
      }
    }
  }

  @Override
  public Collection<? extends Era> loadEntitiesFromStream(InputStream in) throws IOException, XMLException
  {
    try {
      EraParser parser = new EraParser();
      SAXParserFactory parserFactory = SAXParserFactory.newInstance();
      SAXParser saxParser = parserFactory.newSAXParser();
      InputStream is;
      if (in != null) {
        is = in;
      } else {
        FileObject fo = dob.getPrimaryFile();
        is = fo.getInputStream();
      }
      try {
        saxParser.parse(is, parser);
      } finally {
        if (is != null && in == null) {
          is.close();
        }
      }
      return parser.getItems();
    } catch (ParserConfigurationException ex) {
      throw new XMLException(ex);
    } catch (SAXException ex) {
      throw new XMLException(ex);
    }
  }
}
