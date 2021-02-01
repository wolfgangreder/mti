/*
 * Copyright 2021 Wolfgang Reder.
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
package at.or.reder.mti.model.spi;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Wolfgang Reder
 */
public final class IniParser
{

  private final Pattern _section = Pattern.compile("\\s*\\[([^]]*)\\]\\s*");
  private final Pattern _keyValue = Pattern.compile("\\s*([^=]*)=(.*)");
  private Pattern _comment = Pattern.compile("\\s*#(.*)");
  private LineNumberReader reader;
  private IniParserListener listener;
  private boolean processing;
  private String line;

  public IniParser()
  {
  }

  public IniParser(File file) throws IOException
  {
    setInput(new FileReader(file));
  }

  public IniParser(File file,
                   Charset ch) throws IOException
  {
    setInput(new FileReader(file,
                            ch));
  }

  public IniParser(Reader reader)
  {
    setInput(reader);
  }

  public IniParser(InputStream is)
  {
    this(is,
         Charset.defaultCharset());
  }

  public IniParser(InputStream is,
                   Charset ch)
  {
    setInput(new InputStreamReader(is,
                                   ch));
  }

  public IniParser setCommentStart(String commentStart)
  {
    _comment = Pattern.compile("\\s*" + commentStart + "(.*)");
    return this;
  }

  public IniParser setInput(Reader reader)
  {
    checkNotProcessing();
    if (reader instanceof LineNumberReader) {
      this.reader = (LineNumberReader) reader;
    } else {
      this.reader = new LineNumberReader(reader);
    }
    return this;
  }

  public IniParser setListener(IniParserListener listener)
  {
    checkNotProcessing();
    this.listener = listener;
    return this;
  }

  private void checkNotProcessing()
  {
    if (processing) {
      throw new IllegalStateException("Parser is processing");
    }
  }

  public void parse() throws IOException
  {
    checkNotProcessing();
    if (listener == null) {
      throw new IllegalStateException("Listener is null");
    }
    if (reader == null) {
      throw new IllegalStateException("No input");
    }
    processing = true;
    try {
      while (processing && (line = reader.readLine()) != null) {
        Matcher m = _keyValue.matcher(line);
        if (m.matches()) {
          String key = m.group(1);
          String value = m.group(2);
          processing = listener.processValue(this,
                                             key,
                                             value);
          continue;
        }
        m = _section.matcher(line);
        if (m.matches()) {
          processing = listener.processSectionStart(this,
                                                    m.group(1));
          continue;
        }
        m = _comment.matcher(line);
        if (m.matches()) {
          processing = listener.processComment(this,
                                               m.group(1));
          continue;
        }
        processing = listener.processUnrecognized(this,
                                                  line);
      }
    } finally {
      processing = false;
      line = null;
    }
  }

  public int getCurrentLineNumber()
  {
    return processing ? reader.getLineNumber() : -1;
  }

  public String getCurrentLine()
  {
    return line;
  }

}
