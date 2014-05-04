/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.controls;

import at.reder.mti.api.utils.MTIUtils;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 *
 * @author Wolfgang Reder
 */
public class URITextDocument extends MTIDocument
{

  @Override
  protected boolean testValid(String text)
  {
    if (text != null && !text.isEmpty()) {
      try {
        return MTIUtils.testWWW(new URI(text)) == null;
      } catch (URISyntaxException ex) {
        return false;
      }
    }
    return true;
  }

  public URI buildURI(String text)
  {
    if (text != null && !text.isEmpty()) {
      try {
        URI uri = new URI(text);
        if (MTIUtils.testWWW(uri) == null) {
          return uri;
        }
      } catch (URISyntaxException ex) {
        return null;
      }
    }
    return null;
  }

  @Override
  protected boolean testChanged(String text, String original)
  {
    URI originalURI = buildURI(original);
    URI newURI = buildURI(text);
    return !Objects.equals(newURI, originalURI);
  }

}
