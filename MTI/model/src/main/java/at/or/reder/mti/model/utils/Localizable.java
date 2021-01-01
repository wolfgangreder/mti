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
package at.or.reder.mti.model.utils;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Wolfgang Reder
 */
public final class Localizable implements Serializable
{

  private Map<String, String> values = new HashMap<>();
  private final boolean mutable;

  public Localizable(boolean mutable)
  {
    this.mutable = mutable;
  }

  public Localizable(String defaultValue)
  {
    this(defaultValue,
         false);
  }

  public Localizable(String defaultValue,
                     boolean mutable)
  {
    this.mutable = mutable;
    values.put("",
               defaultValue);
  }

  private Localizable(Localizable copy,
                      boolean mutable)
  {
    this.values.putAll(copy.values);
    this.mutable = mutable;
  }

  public String getValue()
  {
    return getValue("");
  }

  public String getValue(String language)
  {
    if (language == null) {
      language = "";
    }
    return values.getOrDefault(language.trim(),
                               values.get(""));
  }

  public Map<String, String> getValues()
  {
    if (mutable) {
      return new HashMap<>(values);
    } else {
      return Collections.unmodifiableMap(values);
    }
  }

  private void checkMutable()
  {
    if (!mutable) {
      throw new UnsupportedOperationException("Localizable is imutable");
    }
  }

  public void addValues(Localizable loc)
  {
    addValues(loc.values);
  }

  public void addValues(Map<String, String> values)
  {
    checkMutable();
    for (Map.Entry<String, String> e : values.entrySet()) {
      addValue(e.getKey(),
               e.getValue());
    }
  }

  public void addValue(String language,
                       String value)
  {
    checkMutable();
    if (language == null) {
      language = "";
    }
    values.put(language.trim(),
               value);
  }

  public boolean normalize()
  {
    return normalize(Locale.getDefault().getLanguage());
  }

  public boolean normalize(String defaultLang)
  {
    checkMutable();
    String val = values.get("");
    if (val == null || val.isBlank()) {
      val = values.get(defaultLang);
      if (val != null && !val.isBlank()) {
        values.put("",
                   val);
        values.remove(defaultLang);
        return true;
      }
      return false;
    } else {
      values.remove(defaultLang);
      return true;
    }
  }

  public void removeLanguage(String language)
  {
    checkMutable();
    if (language != null && !language.isBlank()) {
      values.remove(language.trim());
    }
  }

  public boolean isMutable()
  {
    return mutable;
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 61 * hash + Objects.hashCode(this.values);
    hash = 61 * hash + (this.mutable ? 1 : 0);
    return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Localizable other = (Localizable) obj;
    if (this.mutable != other.mutable) {
      return false;
    }
    return Objects.equals(this.values,
                          other.values);
  }

  public Localizable toMutable()
  {
    return new Localizable(this,
                           true);
  }

  public Localizable toImutable()
  {
    if (!mutable) {
      return this;
    } else {
      return new Localizable(this,
                             false);
    }
  }

  @Override
  public String toString()
  {
    return getValue("");
  }

}
