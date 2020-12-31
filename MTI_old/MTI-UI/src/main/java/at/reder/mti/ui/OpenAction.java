/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.OpenCookie;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "at.reder.mti.ui.OpenAction"
)
@ActionRegistration(
        displayName = "#CTL_OpenAction"
)
@ActionReferences({
  @ActionReference(path = "Menu/File", position = 3333),
  @ActionReference(path = "Nodes/mti/ContactNode", position = 200)

})
@Messages("CTL_OpenAction=Öffnen")
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
    context.stream().
            forEach((openCookie) -> {
              openCookie.open();
            });
  }

}
