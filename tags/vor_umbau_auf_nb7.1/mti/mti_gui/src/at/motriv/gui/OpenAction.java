/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;
import org.openide.cookies.OpenCookie;

public final class OpenAction implements ActionListener
{

  private final List<OpenCookie> context;

  public OpenAction(List<OpenCookie> context)
  {
    this.context = context;
  }

  @Override
  public void actionPerformed(ActionEvent ev)
  {
    for (OpenCookie openCookie : context) {
      openCookie.open();
    }
  }
}
