/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.contact;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.ui.contact.model.ContactTypeCheckBoxModel;
import java.awt.event.ActionEvent;
import org.openide.util.Lookup;

public final class NewContactPanel extends javax.swing.JPanel
{

  private final ContactTypeCheckBoxModel ctModel = new ContactTypeCheckBoxModel();
  private boolean dataValid;

  public NewContactPanel()
  {
    initComponents();
    checkDataValid();
  }

  public boolean isDataValid()
  {
    return dataValid;
  }

  public Contact.Builder getContact()
  {
    return Lookup.
            getDefault().
            lookup(Contact.BuilderFactory.class).
            createBuilder().
            lastName(edLastName.getText()).
            firstName(edFirstName.getText()).
            setTypes(ctModel.getSelection());
  }

  private void checkDataValid()
  {
    boolean wasValid = dataValid;
    dataValid = enumCheckbox1.isDataValid();
    dataValid &= edLastName.isDataValid();
    if (dataValid != wasValid) {
      firePropertyChange("dataValid", wasValid, dataValid);
    }
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
   * this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {

    final javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
    final javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
    final javax.swing.JLabel jLabel3 = new javax.swing.JLabel();

    addComponentListener(new java.awt.event.ComponentAdapter()
    {
      public void componentShown(java.awt.event.ComponentEvent evt)
      {
        formComponentShown(evt);
      }
    });

    jLabel1.setLabelFor(edLastName);
    org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(NewContactPanel.class, "NewContactPanel.jLabel1.text")); // NOI18N

    edLastName.setMaxLength(100);
    edLastName.setMinLength(1);
    edLastName.setNullAllowed(false);
    edLastName.setTrimText(true);
    edLastName.addActionListener(this::checkDataValid);

    org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(NewContactPanel.class, "NewContactPanel.jLabel2.text")); // NOI18N

    enumCheckbox1.setModel(ctModel);
    enumCheckbox1.addActionListener(this::checkDataValid);
    enumCheckbox1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEADING));

    jLabel3.setLabelFor(edFirstName);
    org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(NewContactPanel.class, "NewContactPanel.jLabel3.text")); // NOI18N

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(jLabel3)
          .addComponent(jLabel2)
          .addComponent(jLabel1))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(edLastName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(enumCheckbox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(edFirstName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel3)
          .addComponent(edFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jLabel1)
          .addComponent(edLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addComponent(enumCheckbox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addGap(22, 22, 22))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void formComponentShown(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_formComponentShown
  {//GEN-HEADEREND:event_formComponentShown
    edLastName.requestFocusInWindow();
  }//GEN-LAST:event_formComponentShown

  private void checkDataValid(ActionEvent evt)
  {
    checkDataValid();
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private final at.reder.mti.ui.controls.MTITextField edFirstName = new at.reder.mti.ui.controls.MTITextField();
  private final at.reder.mti.ui.controls.MTITextField edLastName = new at.reder.mti.ui.controls.MTITextField();
  private final at.reder.mti.ui.controls.EnumCheckbox enumCheckbox1 = new at.reder.mti.ui.controls.EnumCheckbox();
  // End of variables declaration//GEN-END:variables
}
