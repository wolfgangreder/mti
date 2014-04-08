/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.contact;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "at.reder.mti.api.datamodel.impl.NewContactAction"
)
@ActionRegistration(
        displayName = "#CTL_NewContactAction"
)
@ActionReferences({
  @ActionReference(path = "Menu/File/New", position = 100),
  @ActionReference(path = "Nodes/mti/MTIContactNode", position = 100)})

@Messages("CTL_NewContactAction=Adresse")
public final class NewContactAction implements ActionListener
{

  @Override
  public void actionPerformed(ActionEvent e)
  {
    EditContactTopComponent ct = new EditContactTopComponent(null);
    ct.open();
    ct.requestActive();
  }

}
