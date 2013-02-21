/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.contact.impl;

import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactXMLSupport;
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
public class DefaultContactXMLSupport implements ContactXMLSupport, Node.Cookie
{

  private final DataObject dob;

  public DefaultContactXMLSupport()
  {
    this.dob = null;
  }

  public DefaultContactXMLSupport(DataObject dob)
  {
    this.dob = dob;
  }

  @Override
  public void streamToXML(OutputStream out,
          Collection<? extends Contact> clctn) throws IOException
  {
    OutputStream os;
    if (out != null) {
      os = out;
    } else {
      FileObject fo = dob.getPrimaryFile();
      os = fo.getOutputStream();
    }
    try {
      ContactXMLPrintWriter printer = new ContactXMLPrintWriter(os);
      printer.printHeader();
      printer.print(clctn);
      printer.close();
    } finally {
      if (os != null && out == null) {
        os.close();
      }
    }
  }

  @Override
  public Collection<? extends Contact> loadEntitiesFromStream(InputStream in) throws IOException, XMLException
  {
    try {
      ContactParser parser = new ContactParser();
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
