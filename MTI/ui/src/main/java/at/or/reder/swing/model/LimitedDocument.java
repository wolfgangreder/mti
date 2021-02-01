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

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.openide.util.Exceptions;

public class LimitedDocument extends PlainDocument
{

  private static final long serialVersionUID = 1L;
  private int limit;

  public LimitedDocument()
  {
    this(-1);
  }

  public LimitedDocument(int limit)
  {
    this.limit = limit;
  }

  @Override
  public void insertString(int offs,
                           String str,
                           AttributeSet a) throws BadLocationException
  {
    if (str == null) {
      return;
    }
    if (limit <= 0 || ((getLength() + str.length()) <= limit)) {
      super.insertString(offs,
                         str,
                         a);
    }
  }

  public int getLimit()
  {
    return limit;
  }

  public void setLimit(int limit)
  {
    if (this.limit != limit) {
      this.limit = limit;
      if (limit > 0 && getLength() > limit) {
        try {
          String tmp = getText(0,
                               Math.min(getLength(),
                                        limit));
          this.remove(0,
                      getLength());
          this.insertString(0,
                            tmp,
                            null);
        } catch (BadLocationException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
    }
  }

}
