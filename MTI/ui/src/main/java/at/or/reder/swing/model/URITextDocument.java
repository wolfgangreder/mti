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
import java.util.Objects;
import java.util.function.Function;

public final class URITextDocument extends MTIDocument
{

  private static final long serialVersionUID = 1L;
  public Function<URI, String> validTester;

  public Function<URI, String> getValidTester()
  {
    return validTester;
  }

  public void setValidTester(Function<URI, String> validTester)
  {
    this.validTester = validTester;
  }

  @Override
  protected boolean testValid(String text)
  {
    if (text != null && !text.isEmpty()) {
      try {
        if (validTester != null) {
          return validTester.apply(new URI(text)) == null;
        } else {
          return Utils.testIsHTTP(new URI(text)) == null;
        }
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
        if (validTester != null) {
          if (validTester.apply(uri) == null) {
            return uri;
          }
        } else {
          if (Utils.testIsHTTP(uri) == null) {
            return uri;
          }
        }
      } catch (URISyntaxException ex) {
        return null;
      }
    }
    return null;
  }

  @Override
  protected boolean testChanged(String text,
                                String original)
  {
    URI originalURI = buildURI(original);
    URI newURI = buildURI(text);
    return !Objects.equals(newURI,
                           originalURI);
  }

}
