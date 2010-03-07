/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.scale.impl;

import at.motriv.datamodel.entities.scale.Scale;
import at.motriv.datamodel.entities.scale.ScaleXMLSupport;
import at.mountainsd.util.XMLException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author wolfi
 */
public class DefaultScaleXMLSupport implements ScaleXMLSupport, Node.Cookie
{

  private final DataObject dob;

  public DefaultScaleXMLSupport()
  {
    dob = null;
  }

  public DefaultScaleXMLSupport(DataObject dob)
  {
    this.dob = dob;
  }

  @Override
  public void streamToXML(OutputStream out,
          Collection<? extends Scale> clctn) throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Collection<? extends Scale> loadEntitiesFromStream(InputStream in) throws IOException, XMLException
  {
    try {
      ScaleParser parser = new ScaleParser();
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
