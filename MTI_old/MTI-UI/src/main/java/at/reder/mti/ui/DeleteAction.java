/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui;

import at.reder.mti.api.datamodel.api.DeleteCookie;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Edit",
        id = "at.reder.mti.ui.DeleteAction"
)
@ActionRegistration(
        iconBase = "at/reder/mti/ui/delete.png",
        displayName = "#CTL_DeleteAction"
)
@ActionReferences({
  @ActionReference(path = "Nodes/mti/ContactNode", position = 500)})
@Messages({"CTL_DeleteAction=Löschen ...",
           "# {0} - numberOfNodes",
           "DeleteAction_askDeleteMany=Solle die {0,number,0} gewählten Elemente gelöscht werden ?",
           "DeleteAction_askDeleteOne=Soll das gewählte Element gelöscht werden ? "})
public final class DeleteAction implements ActionListener
{

  private final List<DeleteCookie> context;

  public DeleteAction(List<DeleteCookie> context)
  {
    this.context = context;
  }

  @Override
  public void actionPerformed(ActionEvent ev)
  {
    if (askDelete()) {
      context.stream().forEach((c) -> c.deleteNode());
    }
  }

  private boolean askDelete()
  {
    NotifyDescriptor nd;
    if (context.size() == 1) {
      nd = new NotifyDescriptor.Confirmation(Bundle.DeleteAction_askDeleteOne(),
                                             Bundle.CTL_DeleteAction(),
                                             NotifyDescriptor.OK_CANCEL_OPTION);
    } else {
      nd = new NotifyDescriptor.Confirmation(Bundle.DeleteAction_askDeleteMany(context.size()),
                                             Bundle.CTL_DeleteAction(),
                                             NotifyDescriptor.OK_CANCEL_OPTION);
    }
    return DialogDisplayer.getDefault().notify(nd) == NotifyDescriptor.OK_OPTION;
  }

}
