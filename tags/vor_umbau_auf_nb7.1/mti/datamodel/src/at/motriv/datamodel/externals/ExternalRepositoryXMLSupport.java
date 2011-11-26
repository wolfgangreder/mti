/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.externals;

import at.mountainsd.util.XMLSupport;

/**
 *
 * @author wolfi
 */
public interface ExternalRepositoryXMLSupport extends XMLSupport<ExternalRepository>
{

  public static enum Elements
  {
    repositories,
    repository,
    uri,
    kind,
    description
  };
  public static final String ATTR_ID = "id";
  public static final String ATTR_LANG = "xml:lang";
  public static final String ATTR_IMMUTABLE = "immutable";
  public static final String ATTR_READONLY = "readOnly";
  public static final String ATTR_NAME = "name";
  public static final String ATTR_VERSION = "version";
  public static final String DTD_PUBLIC_ID = "-//at.motriv//repository//EN";
  public static final String DTD_SYSTEM_ID = "http://www.motriv.at/dtd/repository.dtd";
}
