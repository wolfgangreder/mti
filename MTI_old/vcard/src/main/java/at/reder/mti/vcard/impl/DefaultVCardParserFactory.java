/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.vcard.impl;

import at.reder.mti.vcard.VCardException;
import at.reder.mti.vcard.VCardFormat;
import at.reder.mti.vcard.VCardParser;
import at.reder.mti.vcard.VCardParserFactory;
import at.reder.mti.vcard.VCardVersion;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wolfgang Reder
 */
@ServiceProvider(service = VCardParserFactory.class)
public final class DefaultVCardParserFactory implements VCardParserFactory
{

  private static final int READ_LIMIT = 4096;

  private String readLine(InputStream is) throws IOException
  {
    LineNumberReader reader = new LineNumberReader(new InputStreamReader(is, "UTF-8"));
    return reader.readLine();
  }

  private VCardFormat detectFormat(InputStream is) throws IOException, VCardException
  {
    try {
      is.mark(READ_LIMIT);
      String line = readLine(is);
      if (line == null) {
        throw new IOException("eof");
      }
      line = line.toLowerCase();
      if (line.startsWith("begin:vcard")) {
        return VCardFormat.FORMAT_VCARD;
      } else if (line.startsWith("<?xml")) {
        return VCardFormat.FORMAT_XCARD;
      } else {
        throw new VCardException("unkown vcard-format");
      }
    } finally {
      is.reset();
    }
  }

  @Override
  public VCardParser createParser(InputStream is, VCardVersion version, VCardFormat format) throws IOException, VCardException
  {
    try (BufferedInputStream bis = new BufferedInputStream(is, READ_LIMIT)) {
      if (format == null || format == VCardFormat.FORMAT_DETECT) {
        format = detectFormat(bis);
      }
      switch (format) {
        case FORMAT_VCARD:
          return new VVCardParser(bis);
        case FORMAT_XCARD:
          return new XVCardParser(bis);
        default:
          throw new VCardException("unknown vcard-format");
      }
    }
  }

}
