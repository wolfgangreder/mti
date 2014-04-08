/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.vcard.impl;

import at.reder.mti.vcard.VCard;
import at.reder.mti.vcard.VCardException;
import at.reder.mti.vcard.VCardFormat;
import at.reder.mti.vcard.VCardParser;
import at.reder.mti.vcard.VCardVersion;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Wolfgang Reder
 */
final class VVCardParser implements VCardParser
{

  private final InputStream stream;
  private VCardVersion version = null;

  public VVCardParser(InputStream is)
  {
    this.stream = is;

  }

  @Override
  public VCardFormat getFormat()
  {
    return VCardFormat.FORMAT_VCARD;
  }

  @Override
  public VCardVersion getVersion()
  {
    return version;
  }

  @Override
  public VCard parse() throws VCardException, IOException
  {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
