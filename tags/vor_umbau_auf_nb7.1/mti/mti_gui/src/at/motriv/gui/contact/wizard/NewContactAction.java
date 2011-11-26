/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact.wizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class NewContactAction implements ActionListener
{

  @Override
  public void actionPerformed(ActionEvent e)
  {
    NewContactWizardIterator.execute(NewContactWizardData.ALL_MODES);
  }
}
