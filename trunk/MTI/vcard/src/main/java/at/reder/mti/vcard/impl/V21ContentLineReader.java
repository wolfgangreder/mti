/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.vcard.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

/**
 *
 * @author Wolfgang Reder
 */
public class V21ContentLineReader implements AutoCloseable
{

  private static final Charset ASCII = Charset.forName("US-ASCII");

  private static enum State
  {

    INPROPERTY,
    INPARAMETER,
    INATTRIBUTE,
    ODREAD,
    EOL,
    SOL,
    EOF;
  }
  private static final int COLON = (int) ':';
  private static final int SEMICOLON = (int) ';';
  private static final int OD = 0x13;
  private static final int OA = 0x10;
  private final InputStream is;
  private String currentAttribute;
  private String currentProperty;
  private String currentParameter;
  private State state;
  private Charset currentCharset;
  private int lastRead;

  public V21ContentLineReader(InputStream is)
  {
    this.is = is;
    this.state = State.SOL;
    currentCharset = ASCII;
  }

  public ContentLine readLine() throws IOException
  {
    CharBuffer ch = CharBuffer.allocate(4096);
    switch (state) {
      case INPROPERTY:
        state = doInProperty();
        break;
      case INPARAMETER:
        state = doInParameter();
    }
    return null;
  }

  private String finalizeDecoder(ByteBuffer buffer, CharBuffer charBuffer, CharsetDecoder decoder) throws IOException
  {
    decoder.decode(buffer, charBuffer, true);
    CoderResult result = decoder.flush(charBuffer);
    if (result.isError()) {
      result.throwException();
    }
    return charBuffer.toString();
  }

  private String readString() throws IOException
  {
    int b;
    CharsetDecoder decoder = currentCharset.newDecoder();
    ByteBuffer buffer = ByteBuffer.allocateDirect(16);
    CharBuffer charBuffer = CharBuffer.allocate(4096);
    while ((b = is.read()) != -1) {
      lastRead = b;
      if (b == SEMICOLON || b == COLON || b == OD || b == OA) {
        return finalizeDecoder(buffer, charBuffer, decoder);
      } else {
        buffer.put((byte) b);
        if (!decoder.decode(buffer, charBuffer, false).isError()) {
          buffer.reset();
        }
      }
    }
    return finalizeDecoder(buffer, charBuffer, decoder);
  }

  private State doInProperty() throws IOException
  {
    String s = readString();
    if (s == null) {
      return State.EOF;
    }
    s = s.toUpperCase();
    switch (lastRead) {
      case SEMICOLON:
        currentProperty = s;
        return State.INPARAMETER;
      case COLON:
        currentProperty = s;
        return State.INATTRIBUTE;
    }
    return State.EOF;
  }

  private State doInParameter() throws IOException
  {
    String s = readString();
    if (s == null) {
      return State.EOF;
    }
    return State.EOF;
  }

  @Override
  public void close() throws IOException
  {
    is.close();
  }

}
