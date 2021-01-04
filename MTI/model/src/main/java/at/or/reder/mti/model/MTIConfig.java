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
package at.or.reder.mti.model;

import at.or.reder.osplatform.PlatformFolders;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.openide.util.Exceptions;

/**
 *
 * @author Wolfgang Reder
 */
public interface MTIConfig
{

  public static enum PathVariables
  {
    USER_DOC("%USER-DOCUMENTS%",
             "getDocumentsFolder"),
    USER_IMAGES("%USER-IMAGES%",
                "getPicturesFolder"),
    PUBLIC_DOC("%PUBLIC-DOCUMENTS%",
               "getPublicDocumentsFolder"),
    PUBLIC_IMAGES("%PUBLIC-IMAGES%",
                  "getPublicPicturesFolder");
    private final String pattern;
    private final Method replacementSupplier;

    private PathVariables(String p,
                          String getterName)
    {
      pattern = p;
      replacementSupplier = resolveSupplier(getterName);
    }

    private Method resolveSupplier(String getterName)
    {
      try {
        Class<?> clazz = PlatformFolders.class;
        Method method = clazz.getMethod(getterName);
        int mod = method.getModifiers();
        if (Modifier.isPublic(mod)) {
          return method;
        }
      } catch (NoSuchMethodException | SecurityException ex) {
        Exceptions.printStackTrace(ex);
      }
      return null;
    }

    private String invokeReplacementSupplier(PlatformFolders folders)
    {
      if (replacementSupplier == null) {
        return pattern;
      }
      try {
        Object tmp = replacementSupplier.invoke(folders);
        if (tmp instanceof Path) {
          return ((Path) tmp).toString();
        } else if (tmp != null) {
          return tmp.toString();
        }
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        Exceptions.printStackTrace(ex);
      }
      return pattern;
    }

    public String getPattern()
    {
      return pattern;
    }

    public String replace(String strIn,
                          PlatformFolders folders)
    {
      if (strIn == null) {
        return null;
      }
      if (strIn.isBlank()) {
        return "";
      }
      if (folders == null || !strIn.contains("%")) {
        return strIn;
      }
      String replacement = invokeReplacementSupplier(folders);
      if (!Objects.equals(replacement,
                          pattern)) {
        return strIn.replaceAll(pattern,
                                replacement);
      }
      return strIn;
    }

  }

  public static final String KEY_DATADIR = "datadir";
  public static final String KEY_STOREPROVIDER = "storeprovider";

  public Map<String, String> getValues();

  public String getValue(String key,
                         String defaultValue);

  public String replaceVariables(String value);

  public default String getDatadirectory()
  {
    return getValue(KEY_DATADIR,
                    PathVariables.USER_DOC.getPattern() + "/mtidata");
  }

  public UUID getDefaultStoreProvider();

  public default UUID getStoreProvider()
  {
    UUID result = getDefaultStoreProvider();
    String strId = getValue(KEY_STOREPROVIDER,
                            result.toString());
    try {
      result = UUID.fromString(strId);
    } catch (Throwable th) {
    }
    return result;
  }

}
