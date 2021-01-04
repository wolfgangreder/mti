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
package at.or.reder.mti.model.impl;

import at.or.reder.mti.model.MTIConfig;
import at.or.reder.osplatform.PlatformFolders;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wolfgang Reder
 */
@ServiceProvider(service = MTIConfig.class)
public class MTIConfigImpl implements MTIConfig
{

  private final Map<String, String> values = new HashMap<>();
  private final PlatformFolders folders;
  private boolean valuesRead;

  public MTIConfigImpl()
  {
    if (PlatformFolders.isPlatformFoldersSupported()) {
      folders = PlatformFolders.getInstance();
    } else {
      folders = null;
    }
  }

  private synchronized void checkValuesRead()
  {
    if (!valuesRead) {
      FileObject file = FileUtil.getConfigFile("/at/or/reder/mti/settings.properties");
      if (file != null) {
        Properties props = new Properties();
        try (InputStream is = file.getInputStream()) {
          props.load(is);
        } catch (IOException ex) {
        }
        for (String key : props.stringPropertyNames()) {
          values.put(key,
                     props.getProperty(key));
        }
      }
      valuesRead = true;
    }
  }

  @Override
  public Map<String, String> getValues()
  {
    checkValuesRead();
    return new HashMap<>(values);
  }

  @Override
  public String getValue(String key,
                         String defaultValue)
  {
    checkValuesRead();
    String result = values.getOrDefault(key,
                                        defaultValue);
    return replaceVariables(result);
  }

  @Override
  public String replaceVariables(String value)
  {
    if (value == null) {
      return null;
    }
    if (value.isBlank()) {
      return "";
    }
    if (folders == null || !value.contains("%")) {
      return value;
    }
    String result = value;
    for (PathVariables var : PathVariables.values()) {
      result = var.replace(result,
                           folders);
    }
    return result;
  }

  @Override
  public UUID getDefaultStoreProvider()
  {
    return UUID.fromString("e7daddad-8239-47b1-873a-5b2676f40e69"); // FBServerStoreProvider    
  }

}
