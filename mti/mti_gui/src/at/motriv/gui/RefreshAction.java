/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

public final class RefreshAction implements ActionListener
{

  private final List<RefreshCookie> context;

  public RefreshAction(List<RefreshCookie> context)
  {
    this.context = context;
  }

  @Override
  public void actionPerformed(ActionEvent ev)
  {
    for (RefreshCookie refreshCookie : context) {
      refreshCookie.refresh();
    }
  }
}
