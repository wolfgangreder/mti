/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.explorer;

import at.reder.mti.api.datamodel.nb.MTIRootNode;
import java.awt.BorderLayout;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//at.reder.mti.ui.explorer//EntityExplorer//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "EntityExplorerTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "at.reder.mti.ui.explorer.EntityExplorerTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_EntityExplorerAction",
        preferredID = "EntityExplorerTopComponent"
)
@Messages({
  "CTL_EntityExplorerAction=MTI Explorer",
  "CTL_EntityExplorerTopComponent=MTI Explorer",
  "HINT_EntityExplorerTopComponent=This is a EntityExplorer window"
})
public final class EntityExplorerTopComponent extends TopComponent implements ExplorerManager.Provider
{

  private final ExplorerManager explorerManager = new ExplorerManager();
  private final BeanTreeView btv = new BeanTreeView();

  public EntityExplorerTopComponent()
  {
    explorerManager.setRootContext(MTIRootNode.getInstance());
    setLayout(new BorderLayout());
    add(btv, BorderLayout.CENTER);
    btv.setRootVisible(false);
    setName(Bundle.CTL_EntityExplorerTopComponent());
    //setToolTipText(Bundle.HINT_EntityExplorerTopComponent());
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
   * this method is always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {

    setLayout(new java.awt.BorderLayout());
  }// </editor-fold>//GEN-END:initComponents

  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
  @Override
  public void componentOpened()
  {
    // TODO add custom code on component opening
  }

  @Override
  public void componentClosed()
  {
    // TODO add custom code on component closing
  }

  void writeProperties(java.util.Properties p)
  {
    // better to version settings since initial version as advocated at
    // http://wiki.apidesign.org/wiki/PropertyFiles
    p.setProperty("version", "1.0");
    // TODO store your settings
  }

  void readProperties(java.util.Properties p)
  {
    String version = p.getProperty("version");
    // TODO read your settings according to their version
  }

  @Override
  public ExplorerManager getExplorerManager()
  {
    return explorerManager;
  }

}