/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.vcard;

/**
 *
 * @author Wolfgang Reder
 */
public class VCardException extends Exception
{

  public VCardException(String message)
  {
    super(message);
  }

  public VCardException(String message, Throwable cause)
  {
    super(message, cause);
  }

}
