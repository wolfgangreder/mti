/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.reder.mti.api.datamodel.nb;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;

//@Messages({
//  "LBL_VCard_LOADER=Files of VCard"
//})
//@MIMEResolver.ExtensionRegistration(
//        displayName = "#LBL_VCard_LOADER",
//        mimeType = "text/vcard",
//        extension = {"vcf", "vcard"}
//)
//@DataObject.Registration(
//        mimeType = "text/vcard",
//        iconBase = "at/reder/mti/ui/contact/vcard.png",
//        displayName = "#LBL_VCard_LOADER",
//        position = 300
//)
//@ActionReferences({
//  @ActionReference(
//          path = "Loaders/text/vcard/Actions",
//          id = @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
//          position = 100,
//          separatorAfter = 200
//  ),
//  @ActionReference(
//          path = "Loaders/text/vcard/Actions",
//          id = @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
//          position = 300
//  ),
//  @ActionReference(
//          path = "Loaders/text/vcard/Actions",
//          id = @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
//          position = 400,
//          separatorAfter = 500
//  ),
//  @ActionReference(
//          path = "Loaders/text/vcard/Actions",
//          id = @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
//          position = 600
//  ),
//  @ActionReference(
//          path = "Loaders/text/vcard/Actions",
//          id = @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
//          position = 700,
//          separatorAfter = 800
//  ),
//  @ActionReference(
//          path = "Loaders/text/vcard/Actions",
//          id = @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
//          position = 900,
//          separatorAfter = 1000
//  ),
//  @ActionReference(
//          path = "Loaders/text/vcard/Actions",
//          id = @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
//          position = 1100,
//          separatorAfter = 1200
//  ),
//  @ActionReference(
//          path = "Loaders/text/vcard/Actions",
//          id = @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
//          position = 1300
//  ),
//  @ActionReference(
//          path = "Loaders/text/vcard/Actions",
//          id = @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
//          position = 1400
//  )
//})
public class VCardDataObject extends MultiDataObject
{

  public VCardDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException
  {
    super(pf, loader);
    registerEditor("text/vcard", true);
  }

  @Override
  protected int associateLookup()
  {
    return 1;
  }

}
