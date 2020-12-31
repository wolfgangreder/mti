/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Action;
import org.openide.awt.AcceleratorBinding;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.RequestProcessor;

/**
 * Definiert Konstanten und Hilfsfunktionen.
 *
 * @author wolfi
 */
public final class MTIUtils
{

  /**
   * Maximale Länge von Enum Bezeichnern.
   */
  public static final int MAX_ENUM_LENGTH = 50;
  public static final int MAX_NAME_LENGTH = 128;
  private static final RequestProcessor requestProcessor = new RequestProcessor("at.reder.mti.RequestProcessor",
                                                                                Runtime.getRuntime().availableProcessors() * 4);

  public static RequestProcessor getRequestProcessor()
  {
    return requestProcessor;
  }

  public static String trimString(String str)
  {
    if (str != null) {
      return str.trim();
    } else {
      return str;
    }
  }

  public static String limitString2Len(String str, int maxLen)
  {
    if (str == null) {
      return null;
    } else if (str.length() > maxLen) {
      return str.substring(0, maxLen);
    } else {
      return str;
    }
  }

  private static final class SortableAction implements Comparable<SortableAction>
  {

    private final int sortOrder;
    private final Action action;

    private SortableAction(Action action, FileObject fo)
    {
      this.action = action;
      int so = Integer.MAX_VALUE;
      Object attr = fo.getAttribute("position");
      if (attr instanceof Number) {
        so = ((Number) attr).intValue();
      } else if (attr != null) {
        try {
          so = Integer.parseInt(attr.toString());
        } catch (NumberFormatException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
      this.sortOrder = so;
    }

    @Override
    public int compareTo(SortableAction o)
    {
      return Integer.compare(sortOrder, o.sortOrder);
    }

  }

  public static List<Action> actionsForPath(String path)
  {
    if (path == null) {
      throw new NullPointerException("path==null");
    }
    FileObject root = FileUtil.getConfigFile(path);
    if (root == null) {
      return Collections.emptyList();
    }
    List<SortableAction> tmpResult = new LinkedList<>();
    for (FileObject fo : root.getChildren()) {
      if (!fo.isFolder() && fo.canRead()) {
        Action action = FileUtil.getConfigObject(fo.getPath(), Action.class);
        if (action != null) {
          tmpResult.add(new SortableAction(action, fo));
          AcceleratorBinding.setAccelerator(action, fo);
        }
      }
    }
    if (tmpResult.isEmpty()) {
      return Collections.emptyList();
    }
    Collections.sort(tmpResult);
    List<Action> result = new ArrayList<>(tmpResult.size());
    tmpResult.stream().
            forEach((a) -> result.add(a.action));
    return result;
  }

  public static String testWWW(URI www)
  {
    if (www != null) {
      if (!"http".equals(www.getScheme()) && !"https".equals(www.getScheme())) {
        return "illegal scheme";
      }
      if (www.getHost() == null || www.getHost().trim().isEmpty()) {
        return "no host";
      }
    }
    return null;
  }

  private MTIUtils()
  {
  }

}
