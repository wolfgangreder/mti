/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.vcard;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author wolfi
 */
public interface VCardParserFactory
{

  public VCardParser createParser(InputStream is, VCardVersion version, VCardFormat format) throws IOException, VCardException;

}
