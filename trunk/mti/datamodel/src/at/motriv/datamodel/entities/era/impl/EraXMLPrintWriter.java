/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.era.impl;

import at.motriv.datamodel.entities.era.Era;
import at.motriv.datamodel.entities.era.EraXMLSupport;
import at.mountainsd.util.XMLPrintWriter;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author wolfi
 */
public class EraXMLPrintWriter extends XMLPrintWriter
{

  public EraXMLPrintWriter(OutputStream os)
  {
    super(os);
  }

  public void printEraHeader(String country)
  {
    Map<String, String> params = new HashMap<String, String>();
    params.put(EraXMLSupport.ATTR_COUNTRY, country);
    params.put(EraXMLSupport.ATTR_VERSION, "1.0");
    super.printHeader(EraXMLSupport.Elements.eras.name(),
            params,
            EraXMLSupport.DTD_SYSTEM_ID,
            EraXMLSupport.DTD_PUBLIC_ID);
  }

  public void print(Era era, Locale language)
  {
    Map<String, String> props = new HashMap<String, String>();
    if (language != null && language.getLanguage() != null && language.getLanguage().trim().length() > 0) {
      props.put(EraXMLSupport.ATTR_LANG, language.getLanguage());
    }
    write("<");
    write(EraXMLSupport.Elements.era.name());
    printProperty(EraXMLSupport.ATTR_ID, era.getId().toString());
    printProperty(EraXMLSupport.ATTR_YEARFROM, era.getYearFrom());
    if (era.getYearTo() != null) {
      printProperty(EraXMLSupport.ATTR_YEARTO, era.getYearTo());
    }
    write(">");

    openElement(EraXMLSupport.Elements.name.name(), props);
    write(encodeXML(era.getName()));
    closeElement();
    if (era.getComment() != null) {
      openElement(EraXMLSupport.Elements.comment.name(), props);
      printCDATA(era.getComment());
      closeElement();
    }
  }

  public void print(Collection<? extends Era> eras, Locale language)
  {
    for (Era era : eras) {
      print(era, language);
    }
  }
}
