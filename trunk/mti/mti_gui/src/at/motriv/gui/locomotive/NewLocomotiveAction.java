/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.locomotive;

import at.motriv.datamodel.entities.locomotive.Locomotive;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class NewLocomotiveAction implements ActionListener
{

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Locomotive tmp = null;
    LocomotiveTopComponent.execute(new LocomotiveNode(tmp));
  }
}
