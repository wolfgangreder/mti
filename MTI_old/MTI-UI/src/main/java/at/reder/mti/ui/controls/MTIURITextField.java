/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.controls;

import java.net.URI;
import java.net.URISyntaxException;
import org.openide.util.Exceptions;

/**
 *
 * @author Wolfgang Reder
 */
public class MTIURITextField extends MTITextField
{

  public MTIURITextField()
  {
    setDocument(new URITextDocument());
    setNullAllowed(true);
  }

  public URI getValue()
  {
    if (isValid()) {
      try {
        return new URI(getText());
      } catch (URISyntaxException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return null;
  }

  public void setValue(URI u)
  {
    String text = u != null ? u.toString() : null;
    setText(text);
  }

  public void setCommitedValue(URI u)
  {
    String text = u != null ? u.toString() : null;
    setCommitedText(text);
  }

}
