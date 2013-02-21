/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.scale;

import at.mountainsd.util.XMLSupport;

/**
 *
 * @author wolfi
 */
public interface ScaleXMLSupport extends XMLSupport<Scale>
{

  public static enum Elements
  {

    scales,
    scale,
    name
  };
  public static final String ATTR_ID = "id";
  public static final String ATTR_LANG = "xml:lang";
  public static final String ATTR_SCALE = "scale";
  public static final String ATTR_TRACKWIDTH = "trackwidth";
  public static final String ATTR_FAMILY = "family";
  public static final String ATTR_VERSION = "version";
  public static final String DTD_PUBLIC_ID = "-//at.motriv//scales//EN";
  public static final String DTD_SYSTEM_ID = "http://www.motriv.at/dtd/scales.dtd";
}
