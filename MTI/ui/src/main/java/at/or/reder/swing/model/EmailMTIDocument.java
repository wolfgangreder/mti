/*
 * Copyright 2017 Wolfgang Reder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.or.reder.swing.model;

import at.or.reder.mti.ui.Utils;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import org.openide.util.Exceptions;

public class EmailMTIDocument extends MTIDocument
{

  private static final long serialVersionUID = 1L;

  public EmailMTIDocument()
  {
  }

  public EmailMTIDocument(AbstractDocument.Content c)
  {
    super(c);
  }

  public URI getURI()
  {
    if (isValid()) {
      String text = getText();
      if (text != null) {
        try {
          return new URI("mailto:" + text);
        } catch (URISyntaxException ex) {
        }
      }
    }
    return null;
  }

  public void setURI(URI uri)
  {
    try {
      if (uri != null) {
        replace(0,
                getLength(),
                uri.getSchemeSpecificPart(),
                null);
      } else {
        replace(0,
                getLength(),
                null,
                null);
      }
    } catch (BadLocationException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  public void setCommitedURI(URI uri)
  {
    try {
      if (uri != null) {
        replace(0,
                getLength(),
                uri.getSchemeSpecificPart(),
                null);
      } else {
        replace(0,
                getLength(),
                null,
                null);
      }
    } catch (BadLocationException ex) {
      Exceptions.printStackTrace(ex);
    } finally {
      commit();
    }
  }

  @Override
  protected String getText()
  {
    String text = super.getText();
    if (Utils.isEmail(text)) {
      try {
        URI uri = new URI("mailto:" + text.trim());
        return uri.getSchemeSpecificPart();
      } catch (URISyntaxException ex) {
      }
    }
    return null;
  }

  @Override
  protected boolean testValid(String text)
  {
    return Utils.isEmail(text);
  }

}
