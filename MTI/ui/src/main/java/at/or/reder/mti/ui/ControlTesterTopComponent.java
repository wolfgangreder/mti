/*
 * Copyright 2020 Wolfgang Reder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.or.reder.mti.ui;

import at.or.reder.mti.model.api.Factories;
import at.or.reder.mti.model.api.StoreException;
import at.or.reder.mti.ui.zsx.ImageItem;
import at.or.reder.mti.ui.zsx.ImageType;
import at.or.reder.mti.ui.zsx.ZSXFile;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.DefaultListModel;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//at.or.reder.mti.ui//ControlTester//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ControlTesterTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "at.or.reder.mti.ui.ControlTesterTopComponent")
@ActionReference(path = "Menu/Window" /* , position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ControlTesterAction",
        preferredID = "ControlTesterTopComponent"
)
@Messages({
  "CTL_ControlTesterAction=ControlTester",
  "CTL_ControlTesterTopComponent=ControlTester Window",
  "HINT_ControlTesterTopComponent=This is a ControlTester window"
})
public final class ControlTesterTopComponent extends TopComponent
{

  private static final class ListImageItem implements ImageItem
  {

    private final ImageItem wrapped;

    public ListImageItem(ImageItem wrapped)
    {
      this.wrapped = wrapped;
    }

    @Override
    public int getId()
    {
      return wrapped.getId();
    }

    @Override
    public int getIndex()
    {
      return wrapped.getIndex();
    }

    @Override
    public String getName()
    {
      return wrapped.getName();
    }

    @Override
    public String getAuthor()
    {
      return wrapped.getAuthor();
    }

    @Override
    public ImageType getImageType()
    {
      return wrapped.getImageType();
    }

    @Override
    public BufferedImage getImage() throws IOException
    {
      return wrapped.getImage();
    }

    @Override
    public int hashCode()
    {
      return wrapped.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
      return wrapped.equals(obj);
    }

    @Override
    public String toString()
    {
      return wrapped.getName();
    }

  };

  public ControlTesterTopComponent()
  {
    initComponents();
    setName(Bundle.CTL_ControlTesterTopComponent());
    setToolTipText(Bundle.HINT_ControlTesterTopComponent());
    try {
      Factories.getStores();
    } catch (StoreException ex) {
    }
  }

  private void readFiles()
  {
    ZSXFile zsx = ZSXFile.getLocoInstance();
    ZSXFile loco = ZSXFile.getIconInstance();
    DefaultListModel<ImageItem> itemModel = new DefaultListModel<>();
    for (ImageItem i : zsx.getImageItems()) {
      itemModel.addElement(new ListImageItem(i));
    }
    for (ImageItem i : loco.getImageItems()) {
      itemModel.addElement(new ListImageItem(i));
    }
    jList1.setModel(itemModel);
  }

  private void showSelected()
  {
    ImageItem selected = jList1.getSelectedValue();
    BufferedImage img = null;
    if (selected != null) {
      try {
        img = selected.getImage();
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    jImage1.setImage(img);
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {

    jButton1 = new javax.swing.JButton();
    jScrollPane1 = new javax.swing.JScrollPane();
    jImage1 = new at.or.reder.swing.JImage();

    org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(ControlTesterTopComponent.class, "ControlTesterTopComponent.jButton1.text")); // NOI18N
    jButton1.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(java.awt.event.ActionEvent evt)
      {
        jButton1ActionPerformed(evt);
      }
    });

    jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    jList1.addListSelectionListener(new javax.swing.event.ListSelectionListener()
    {
      public void valueChanged(javax.swing.event.ListSelectionEvent evt)
      {
        jList1ValueChanged(evt);
      }
    });
    jScrollPane1.setViewportView(jList1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jButton1)
            .addGap(0, 0, Short.MAX_VALUE))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jImage1, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jButton1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jImage1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE))
        .addContainerGap())
    );
  }// </editor-fold>//GEN-END:initComponents

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
  {//GEN-HEADEREND:event_jButton1ActionPerformed
    readFiles();
  }//GEN-LAST:event_jButton1ActionPerformed

  private void jList1ValueChanged(javax.swing.event.ListSelectionEvent evt)//GEN-FIRST:event_jList1ValueChanged
  {//GEN-HEADEREND:event_jList1ValueChanged
    showSelected();
  }//GEN-LAST:event_jList1ValueChanged

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton jButton1;
  private at.or.reder.swing.JImage jImage1;
  private final javax.swing.JList<ImageItem> jList1 = new javax.swing.JList<>();
  private javax.swing.JScrollPane jScrollPane1;
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
    p.setProperty("version",
                  "1.0");
    // TODO store your settings
  }

  void readProperties(java.util.Properties p)
  {
    String version = p.getProperty("version");
    // TODO read your settings according to their version
  }

}
