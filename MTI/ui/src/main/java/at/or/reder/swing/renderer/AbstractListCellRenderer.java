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
package at.or.reder.swing.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public abstract class AbstractListCellRenderer<E> extends JLabel implements ListCellRenderer<E>
{

  /**
   * An empty <code>Border</code>. This field might not be used. To change the <code>Border</code> used by this renderer override
   * the <code>getListCellRendererComponent</code> method and set the border of the returned component directly.
   */
  private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1,
                                                                     1,
                                                                     1,
                                                                     1);
  private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1,
                                                                        1,
                                                                        1,
                                                                        1);
  protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a default renderer object for an item in a list.
   */
  @SuppressWarnings("OverridableMethodCallInConstructor")
  public AbstractListCellRenderer()
  {
    super();
    setOpaque(true);
    setBorder(getNoFocusBorder());
    setName("List.cellRenderer");
  }

  private Border getNoFocusBorder()
  {
    Border border = UIManager.getBorder("List.cellNoFocusBorder");
    if (System.getSecurityManager() != null) {
      if (border != null) {
        return border;
      }
      return SAFE_NO_FOCUS_BORDER;
    } else {
      if (border != null && (noFocusBorder == null || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
        return border;
      }
      return noFocusBorder;
    }
  }

  protected String getLabel(JList<? extends E> list,
                            E value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus)
  {
    if (value != null) {
      return value.toString();
    } else {
      return null;
    }
  }

  protected Icon getIcon(JList<? extends E> list,
                         E value,
                         int index,
                         boolean isSelected,
                         boolean cellHasFocus)
  {
    if (value instanceof Icon) {
      return (Icon) value;
    } else {
      return null;
    }
  }

  @Override
  public Component getListCellRendererComponent(
          JList<? extends E> list,
          E value,
          int index,
          boolean isSelected,
          boolean cellHasFocus)
  {
    setComponentOrientation(list.getComponentOrientation());

    Color bg = null;
    Color fg = null;

    JList.DropLocation dropLocation = list.getDropLocation();
    if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

      bg = UIManager.getColor("List.dropCellBackground");
      fg = UIManager.getColor("List.dropCellForeground");

      isSelected = true;
    }

    if (isSelected) {
      setBackground(bg == null ? list.getSelectionBackground() : bg);
      setForeground(fg == null ? list.getSelectionForeground() : fg);
    } else {
      setBackground(list.getBackground());
      setForeground(list.getForeground());
    }
    String text = getLabel(list,
                           value,
                           index,
                           isSelected,
                           cellHasFocus);
    setText(text != null ? text : "");
    Icon icon = getIcon(list,
                        value,
                        index,
                        isSelected,
                        cellHasFocus);
    setIcon(icon);

    setEnabled(list.isEnabled());
    setFont(list.getFont());

    Border border = null;
    if (cellHasFocus) {
      if (isSelected) {
        border = UIManager.getBorder("List.focusSelectedCellHighlightBorder");
      }
      if (border == null) {
        border = UIManager.getBorder("List.focusCellHighlightBorder");
      }
    } else {
      border = getNoFocusBorder();
    }
    setBorder(border);

    return this;
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   *
   * @since 1.5
   * @return <code>true</code> if the background is completely opaque and differs from the JList's background; <code>false</code>
   * otherwise
   */
  @Override
  public boolean isOpaque()
  {
    Color back = getBackground();
    Component p = getParent();
    if (p != null) {
      p = p.getParent();
    }
    // p should now be the JList.
    boolean colorMatch = (back != null) && (p != null) && back.equals(p.getBackground()) && p.isOpaque();
    return !colorMatch && super.isOpaque();
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   */
  @Override
  public void validate()
  {
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   *
   * @since 1.5
   */
  @Override
  public void invalidate()
  {
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   *
   * @since 1.5
   */
  @Override
  public void repaint()
  {
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   */
  @Override
  public void revalidate()
  {
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   */
  @Override
  public void repaint(long tm,
                      int x,
                      int y,
                      int width,
                      int height)
  {
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   */
  @Override
  public void repaint(Rectangle r)
  {
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   */
  @Override
  protected void firePropertyChange(String propertyName,
                                    Object oldValue,
                                    Object newValue)
  {
    // Strings get interned...
    if ("text".equals(propertyName)
                || (("font".equals(propertyName) || "foreground".equals(propertyName))
                    && oldValue != newValue
                    && getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey) != null)) {

      super.firePropertyChange(propertyName,
                               oldValue,
                               newValue);
    }
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   */
  @Override
  public void firePropertyChange(String propertyName,
                                 byte oldValue,
                                 byte newValue)
  {
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   */
  @Override
  public void firePropertyChange(String propertyName,
                                 char oldValue,
                                 char newValue)
  {
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   */
  @Override
  public void firePropertyChange(String propertyName,
                                 short oldValue,
                                 short newValue)
  {
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   */
  @Override
  public void firePropertyChange(String propertyName,
                                 int oldValue,
                                 int newValue)
  {
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   */
  @Override
  public void firePropertyChange(String propertyName,
                                 long oldValue,
                                 long newValue)
  {
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   */
  @Override
  public void firePropertyChange(String propertyName,
                                 float oldValue,
                                 float newValue)
  {
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   */
  @Override
  public void firePropertyChange(String propertyName,
                                 double oldValue,
                                 double newValue)
  {
  }

  /**
   * Overridden for performance reasons. See the <a href="#override">Implementation Note</a>
   * for more information.
   */
  @Override
  public void firePropertyChange(String propertyName,
                                 boolean oldValue,
                                 boolean newValue)
  {
  }

  /**
   * A subclass of DefaultListCellRenderer that implements UIResource. DefaultListCellRenderer doesn't implement UIResource
   * directly so that applications can safely override the cellRenderer property with DefaultListCellRenderer subclasses.
   * <p>
   * <strong>Warning:</strong>
   * Serialized objects of this class will not be compatible with future Swing releases. The current serialization support is
   * appropriate for short term storage or RMI between applications running the same version of Swing. As of 1.4, support for long
   * term storage of all JavaBeans&trade; has been added to the <code>java.beans</code> package. Please see
   * {@link java.beans.XMLEncoder}.
   */
  public static class UIResource extends DefaultListCellRenderer
          implements javax.swing.plaf.UIResource
  {

    private static final long serialVersionUID = 1L;
  }

}
