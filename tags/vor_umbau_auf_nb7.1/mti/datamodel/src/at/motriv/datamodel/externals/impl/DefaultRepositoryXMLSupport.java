/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.externals.impl;

import at.motriv.datamodel.externals.ExternalRepository;
import at.motriv.datamodel.externals.ExternalRepositoryXMLSupport;
import at.motriv.datamodel.externals.RepositoryDataObject;
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
public class DefaultRepositoryXMLSupport implements ExternalRepositoryXMLSupport, Node.Cookie
{

  private final RepositoryDataObject dob;

  public DefaultRepositoryXMLSupport()
  {
    this.dob = null;
  }

  public DefaultRepositoryXMLSupport(RepositoryDataObject dob)
  {
    this.dob = dob;
  }

  @Override
  public void streamToXML(OutputStream out, Collection<? extends ExternalRepository> clctn) throws IOException
  {
    OutputStream os;
    if (out != null) {
      os = out;
    } else {
      FileObject fo = dob.getPrimaryFile();
      os = fo.getOutputStream();
    }
    try {
      RepositoryXMLPrintWriter printer = new RepositoryXMLPrintWriter(os);
      printer.print(clctn);
      printer.close();
    } finally {
      if (os != null && out == null) {
        os.close();
      }
    }
  }

  @Override
  public Collection<? extends ExternalRepository> loadEntitiesFromStream(InputStream in) throws IOException, XMLException
  {
    try {
      RepositoryParser parser = new RepositoryParser();
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
