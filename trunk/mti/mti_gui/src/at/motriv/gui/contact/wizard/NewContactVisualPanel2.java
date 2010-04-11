/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact.wizard;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactItemProvider;
import at.motriv.datamodel.entities.contact.ContactType;
import at.motriv.datamodel.entities.contact.ManufacturerBuilder;
import at.motriv.datamodel.entities.contact.RetailerBuilder;
import at.motriv.datamodel.entities.contact.impl.ContactBuilder;
import at.motriv.gui.contact.ChooseContactPanel;
import at.motriv.gui.contact.ContactPanel;
import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Callable;
import javax.swing.JPanel;

public final class NewContactVisualPanel2 extends JPanel implements PropertyChangeListener
{

  private static final long serialVersionUID = 1L;
  private NewContactWizardData data;
  private JPanel content;

  /** Creates new form NewContactVisualPanel2 */
  public NewContactVisualPanel2()
  {
    initComponents();
  }

  public void setMode(NewContactWizardData data)
  {
    if (content != null) {
      remove(content);
    }
    this.data = data;
    switch (data.getCreationMode()) {
      case NEW_MANUFACTURER:
      case NEW_RETAILER: {
        ContactPanel ctPanel = new ContactPanel();
        ctPanel.setBuilder(data.getCreationMode() == CreationMode.NEW_MANUFACTURER ? new ManufacturerBuilder() : new RetailerBuilder());
        content = ctPanel;
      }
      break;
      case NEW_MANUFACTURER_FROM_RETAILER:
      case NEW_RETAILER_FROM_MANUFACTURER: {
        ChooseContactPanel ctPanel = new ChooseContactPanel();
        ctPanel.setFilter(data.getCreationMode() == CreationMode.NEW_MANUFACTURER_FROM_RETAILER ? ContactType.MANUFACTURER : ContactType.RETAILER);
        content = ctPanel;
      }
      break;
    }
    content.addPropertyChangeListener("valid", this);
    add(content, BorderLayout.CENTER);
  }

  @Override
  public String getName()
  {
    return "Step #2";
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    setLayout(new java.awt.BorderLayout());
  }// </editor-fold>//GEN-END:initComponents

  public Callable<? extends Contact> getFinisher()
  {
    switch (data.getCreationMode()) {
      case NEW_MANUFACTURER:
      case NEW_RETAILER:
        final ContactBuilder<? extends Contact> builder = ((ContactPanel) content).getBuilder();
        final CreationMode cm = data.getCreationMode();
        return new Callable<Contact>()
        {

          @Override
          public Contact call() throws Exception
          {
            ContactItemProvider provider = MotrivItemProviderLookup.lookup(ContactItemProvider.class);
            if (provider != null) {
              Contact tmp= provider.store(builder.build());
              switch (cm) {
                case NEW_MANUFACTURER:
                  return provider.makeManufacturer(tmp);
                case NEW_RETAILER:
                  return provider.makeRetailer(tmp);
              }
            }
            return null;
          }
        };
      case NEW_MANUFACTURER_FROM_RETAILER:
      case NEW_RETAILER_FROM_MANUFACTURER:
    }
    return null;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt)
  {
    if ("valid".equals(evt.getPropertyName())) {
      if ((Boolean) evt.getNewValue()) {
        data.setFinisher(getFinisher());
      } else {
        data.setFinisher(null);
      }
    }
  }
  // Variables declaration - do not modify//GEN-BEGIN:variables
  // End of variables declaration//GEN-END:variables
}

