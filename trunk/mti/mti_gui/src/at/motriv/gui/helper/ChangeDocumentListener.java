/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.helper;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author wolfi
 */
public abstract class ChangeDocumentListener implements DocumentListener
{

  protected abstract void changed(DocumentEvent e);

  @Override
  public void insertUpdate(DocumentEvent e)
  {
    changed(e);
  }

  @Override
  public void removeUpdate(DocumentEvent e)
  {
    changed(e);
  }

  @Override
  public void changedUpdate(DocumentEvent e)
  {
    changed(e);
  }
}
