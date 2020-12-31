/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.persistence;

/**
 *
 * @author Wolfgang Reder
 */
public class ProviderException extends Exception
{

  public ProviderException()
  {

  }

  public ProviderException(String message)
  {
    super(message);
  }

  public ProviderException(Throwable cause)
  {
    super(cause);
  }

  public ProviderException(String message, Throwable cause)
  {
    super(message, cause);
  }

}
