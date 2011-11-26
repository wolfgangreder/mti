/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.config;

import at.motriv.datamodel.ModelCondition;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author wolfi
 */
public class MotrivConfig
{

  public static final UUIDConfigKey KEY_DEFAULT_SCALE = new UUIDConfigKey("at.motriv.config.defaultScale", null);
  public static final UUIDSetConfigKey KEY_SCALE_FILTER = new UUIDSetConfigKey("at.motriv.config.scaleFilter", null);
  public static final UUIDConfigKey KEY_DEFAULT_ERA = new UUIDConfigKey("at.motriv.config.defaultEra", null);
  public static final UUIDSetConfigKey KEY_ERA_FILTER = new UUIDSetConfigKey("at.motriv.config.eraFilter", null);
  public static final UUIDConfigKey KEY_DEFAULT_MANUFACTURER = new UUIDConfigKey("at.motriv.config.defaultManufacturer", null);
  public static final UUIDConfigKey KEY_DEFAULT_RETAILER = new UUIDConfigKey("at.motriv.config.defaultRetailer", null);
  public static final EnumConfigKey<ModelCondition> KEY_DEFAULT_CONDITION = new EnumConfigKey<ModelCondition>(
          "at.motriv.config.defaultCondition", ModelCondition.USED, ModelCondition.class);


  private static final ConcurrentMap<ConfigKey<?>, ? super Object> entries = new ConcurrentHashMap<ConfigKey<?>, Object>();
  @SuppressWarnings("unchecked")
  public static final Set<ConfigKey<?>> KEYS = Collections.unmodifiableSet(new HashSet<ConfigKey<?>>(Arrays.asList(
          KEY_DEFAULT_SCALE,
          KEY_DEFAULT_ERA,
          KEY_DEFAULT_MANUFACTURER,
          KEY_DEFAULT_RETAILER,
          KEY_SCALE_FILTER,
          KEY_DEFAULT_CONDITION)));

  @SuppressWarnings("unchecked")
  public static <V> V getConfigValue(ConfigKey<V> key)
  {
    if (key == null) {
      throw new NullPointerException("key==null");
    }
    if (!KEYS.contains(key)) {
      throw new IllegalArgumentException("key " + key.getKey() + " is unknown");
    }
    synchronized (entries) {
      if (entries.isEmpty()) {
        loadEntries();
      }
    }
    Object tmp = entries.get(key);
    if (tmp != null && key.getValueClass().isInstance(tmp)) {
      return (V) tmp;
    }
    return key.getDefaultValue();
  }

  private static void loadEntries()
  {
    try {
      FileObject fo = getConfigFile();
      if (fo != null) {
        InputStream is = null;
        try {
          is = fo.getInputStream();
          Properties props = new Properties();
          props.loadFromXML(is);
          for (ConfigKey<?> k : KEYS) {
            String strProp = props.getProperty(k.getKey());
            if (strProp != null) {
              entries.put(k, k.parseValue(strProp));
            }
          }
        } finally {
          if (is != null) {
            is.close();
          }
        }
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  public static void storeEntries()
  {
    try {
      FileObject fo = getConfigFile();
      if (fo != null) {
        Properties props = new Properties();
        for (Map.Entry<ConfigKey<?>, ? super Object> e : entries.entrySet()) {
          if (e.getValue() != null) {
            ConfigKey<? extends Object> key = e.getKey();
            props.put(key, key.valueToString(e.getValue()));
          }
        }
        OutputStream os = null;
        try {
          os = fo.getOutputStream();
          props.storeToXML(os, null, "UTF-8");
        } finally {
          if (os != null) {
            os.close();
          }
        }
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  private static FileObject createConfigFile() throws IOException
  {
    FileObject folder = FileUtil.createFolder(FileUtil.getConfigRoot(), "Preferences/at/mountain-sd/config/Motriv");
    return folder.createData("config", "properties");
  }

  private static FileObject getConfigFile() throws IOException
  {
    FileObject fo = FileUtil.getConfigFile("Preferences/at/mountain-sd/config/Motriv/config.xml");
    if (fo == null) {
      fo = createConfigFile();
    }
    return fo;
  }
}
