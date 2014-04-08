/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.utils;

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

//  /**
//   * Erzeugt eine kopie von {@code date}
//   *
//   * @param date Datum das kopiert werden soll.
//   * @return {@code null} wenn {@code date==null} oder eine Kopie von {@code date}.
//   */
//  public static Date copyDate(Date date)
//  {
//    return date != null ? new Date(date.getTime()) : null;
//  }
//
//  public static Timestamp getDayPart(Timestamp pDate)
//  {
//    if (pDate == null) {
//      return null;
//    }
//    Calendar cal = Calendar.getInstance();
//    cal.setTime(pDate);
//    cal.set(Calendar.HOUR_OF_DAY, 0);
//    cal.set(Calendar.MINUTE, 0);
//    cal.set(Calendar.SECOND, 0);
//    cal.set(Calendar.MILLISECOND, 0);
//    return new Timestamp(cal.getTime());
//  }
//
//  public static Timestamp getTimePart(Timestamp pDate)
//  {
//    if (pDate == null) {
//      return null;
//    }
//    Calendar cal = Calendar.getInstance();
//    cal.setTime(pDate);
//    cal.set(Calendar.YEAR, 1970);
//    cal.set(Calendar.DAY_OF_YEAR, 1);
//    return new Timestamp(cal.getTime());
//  }
//
//  public static Timestamp composeDateTime(Timestamp pDate, Timestamp pTime)
//  {
//    if (pDate == null) {
//      return pTime;
//    }
//    if (pTime == null) {
//      return pDate;
//    }
//    Calendar cal = Calendar.getInstance();
//    Calendar delta = Calendar.getInstance();
//    cal.setTime(getDayPart(pDate));
//    delta.setTime(getTimePart(pTime));
//    cal.set(Calendar.HOUR_OF_DAY, delta.get(Calendar.HOUR_OF_DAY));
//    cal.set(Calendar.MINUTE, delta.get(Calendar.MINUTE));
//    cal.set(Calendar.SECOND, delta.get(Calendar.SECOND));
//    cal.set(Calendar.MILLISECOND, delta.get(Calendar.MILLISECOND));
//    return new Timestamp(cal.getTime());
//  }
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

//  private static void listChildren(FileObject fo, int level)
//  {
//    if (fo == null) {
//      return;
//    }
//    for (int i = 0; i < level; ++i) {
//      System.out.print("_");
//    }
//    System.out.println(fo.getNameExt());
//    FileObject[] children = fo.getChildren();
//    if (children != null && children.length > 0) {
//      for (FileObject c : children) {
//        listChildren(c, level + 2);
//      }
//    }
//  }
//
//  public static Action createAction(String layerFile)
//  {
//    FileObject root = FileUtil.getConfigRoot();
//    FileObject fo = root.getFileObject("Actions/" + layerFile + ".instance");
//    if (fo != null) {
//      try {
//        DataObject dob = DataObject.find(fo);
//        if (dob != null) {
//          InstanceCookie.Of ic = dob.getLookup().lookup(InstanceCookie.Of.class);
//          if (ic != null && ic.instanceOf(Action.class)) {
//            return (Action) ic.instanceCreate();
//          }
//        }
//      } catch (IOException | ClassNotFoundException ex) {
//        Exceptions.printStackTrace(ex);
//      }
//    }
//    return null;
//  }
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
    for (SortableAction a : tmpResult) {
      result.add(a.action);
    }
    return result;
  }

  private MTIUtils()
  {
  }

}
