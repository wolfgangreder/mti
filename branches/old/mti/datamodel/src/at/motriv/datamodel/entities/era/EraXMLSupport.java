/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.era;

import at.mountainsd.util.XMLSupport;

/**
 *
 * @author wolfi
 */
public interface EraXMLSupport extends XMLSupport<Era>
{

  public static enum Elements
  {

    eras,
    era,
    name,
    comment
  };
  public static final String ATTR_ID = "id";
  public static final String ATTR_LANG = "xml:lang";
  public static final String ATTR_YEARFROM = "yearfrom";
  public static final String ATTR_YEARTO = "yearto";
  public static final String ATTR_VERSION = "version";
  public static final String ATTR_COUNTRY = "country";
  public static final String DTD_PUBLIC_ID = "-//at.motriv//era//EN";
  public static final String DTD_SYSTEM_ID = "http://www.motriv.at/dtd/era.dtd";
}
