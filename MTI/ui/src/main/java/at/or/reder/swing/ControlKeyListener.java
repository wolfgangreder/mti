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
package at.or.reder.swing;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;

public final class ControlKeyListener
{

  private static ControlKeyListener instance;

  public static synchronized ControlKeyListener getInstance()
  {
    if (instance == null) {
      instance = new ControlKeyListener();
    }
    return instance;
  }

  private boolean altPressed;
  private boolean altGraphPressed;
  private boolean shiftPressed;
  private boolean ctrlPressed;
  private boolean metaPressed;
  private boolean installed;
  private final ChangeSupport changeSupport = new ChangeSupport(this);

  private ControlKeyListener()
  {

  }

  public synchronized void install()
  {
    if (!installed) {
      KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this::dispatchKeyEvent);
      installed = true;
    }
  }

  private synchronized boolean dispatchKeyEvent(KeyEvent e)
  {
    boolean oldAlt = altPressed;
    boolean oldGraph = altGraphPressed;
    boolean oldShift = shiftPressed;
    boolean oldCtrl = ctrlPressed;
    boolean oldMeta = metaPressed;
    altPressed = e.isAltDown();
    altGraphPressed = e.isAltGraphDown();
    shiftPressed = e.isShiftDown();
    ctrlPressed = e.isControlDown();
    metaPressed = e.isMetaDown();
    if (oldAlt != altPressed || oldGraph != altGraphPressed || oldShift != shiftPressed || oldCtrl != ctrlPressed || oldMeta
                                                                                                                             != metaPressed) {
      SwingUtilities.invokeLater(changeSupport::fireChange);
    }
    return false;
  }

  public synchronized boolean isAltPressed()
  {
    return altPressed;
  }

  public synchronized boolean isAltGraphPressed()
  {
    return altGraphPressed;
  }

  public synchronized boolean isShiftPressed()
  {
    return shiftPressed;
  }

  public synchronized boolean isCtrlPressed()
  {
    return ctrlPressed;
  }

  public synchronized boolean isMetaPressed()
  {
    return metaPressed;
  }

  public void addChangeListener(ChangeListener changeListener)
  {
    changeSupport.addChangeListener(changeListener);
  }

  public void removeChangeListener(ChangeListener changeListener)
  {
    changeSupport.removeChangeListener(changeListener);
  }

}
