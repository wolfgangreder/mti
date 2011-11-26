/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.config;

import at.mountainsd.util.csv.CSVException;
import at.mountainsd.util.csv.CSVSplitter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.openide.util.Exceptions;

/**
 *
 * @author wolfi
 */
public class UUIDSetConfigKey extends ConfigKey<Set<UUID>>
{

  public UUIDSetConfigKey(String key, Collection<UUID> coll)
  {
    super(key, coll != null ? Collections.unmodifiableSet(new HashSet<UUID>(coll)) : null);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Class<? extends Set<UUID>> getValueClass()
  {
    Class<?> tmp = Set.class;
    return (Class<? extends Set<UUID>>) (tmp);
  }

  @Override
  public Set<UUID> parseValue(String strVal)
  {
    if (strVal != null) {
      try {
        CSVSplitter splitter = new CSVSplitter(',', '\0');
        List<String> splitted = splitter.convert(strVal.trim());
        HashSet<UUID> result = new HashSet<UUID>();
        for (String s : splitted) {
          result.add(UUID.fromString(s));
        }
        return result;
      } catch (CSVException ex) {
        Exceptions.printStackTrace(ex);
      }
      return Collections.emptySet();
    }
    return null;
  }

  @Override
  public String valueToString(Object val)
  {
    if (val instanceof Collection<?>) {
      Collection<?> tmp = (Collection<?>) val;
      StringBuilder builder = new StringBuilder();
      for (Object o : tmp) {
        if (o instanceof UUID) {
          builder.append(o.toString());
          builder.append(",");
        }
      }
      if (builder.length() > 0) {
        builder.setLength(builder.length() - 1);
        return builder.toString();
      } else {
        return " ";
      }
    }
    return null;
  }
}
