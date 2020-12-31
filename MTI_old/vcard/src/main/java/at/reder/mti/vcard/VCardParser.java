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

/**
 *
 * @author wolfi
 */
public interface VCardParser
{

  public VCardFormat getFormat();

  public VCardVersion getVersion();

  public VCard parse() throws VCardException, IOException;

}
