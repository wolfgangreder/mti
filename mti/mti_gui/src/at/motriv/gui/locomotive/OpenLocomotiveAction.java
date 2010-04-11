/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.locomotive;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.openide.cookies.OpenCookie;

public final class OpenLocomotiveAction implements ActionListener
{

  private final OpenCookie context;

  public OpenLocomotiveAction(OpenCookie context)
  {
    this.context = context;
  }

  @Override
  public void actionPerformed(ActionEvent ev)
  {
    context.open();
  }
}
