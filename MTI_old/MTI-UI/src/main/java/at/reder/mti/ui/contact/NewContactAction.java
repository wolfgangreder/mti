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
import java.beans.PropertyChangeEvent;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
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

@Messages("CTL_NewContactAction=Neue Adresse ...")
public final class NewContactAction implements ActionListener
{

  @Override
  public void actionPerformed(ActionEvent e)
  {
    final NewContactPanel pnl = new NewContactPanel();
    final DialogDescriptor descr = new DialogDescriptor(pnl,
                                                        Bundle.CTL_NewContactAction(),
                                                        true,
                                                        DialogDescriptor.OK_CANCEL_OPTION,
                                                        DialogDescriptor.OK_OPTION,
                                                        null);
    pnl.addPropertyChangeListener("dataValid", (PropertyChangeEvent evt) -> {
      descr.setValid(pnl.isDataValid());
    });
    descr.setValid(pnl.isDataValid());
    if (DialogDisplayer.getDefault().notify(descr) == DialogDescriptor.OK_OPTION) {
      EditContactTopComponent ct = EditContactTopComponent.getInstance(pnl.getContact());
      ct.open();
      ct.requestActive();
    }
  }

}
