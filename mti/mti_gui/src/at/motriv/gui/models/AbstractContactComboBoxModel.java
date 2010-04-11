/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.models;

import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactItemProvider;
import at.motriv.datamodel.entities.contact.GenericContactBuilder;
import at.motriv.datamodel.entities.contact.Manufacturer;
import at.motriv.datamodel.entities.contact.ManufacturerBuilder;
import at.motriv.datamodel.entities.contact.Retailer;
import at.motriv.datamodel.entities.contact.RetailerBuilder;
import at.mountainsd.dataprovider.api.DataProviderEvent;
import at.mountainsd.dataprovider.api.DataProviderEventListener;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import javax.swing.SwingUtilities;
import org.openide.util.NbBundle;
import org.openide.util.WeakListeners;

/**
 *
 * @author wolfi
 */
public abstract class AbstractContactComboBoxModel<C extends Contact> extends DefaultValuesComboBoxModel<C> implements
        DataProviderEventListener
{

  public static Contact DUMMY_CONTACT = new GenericContactBuilder().id(UUID.fromString("00000000-0000-0000-0004-000000000000")).name(
          NbBundle.getMessage(AbstractContactComboBoxModel.class,"AbstractContactComboBoxModel.dummy.name")).build();
  public static Manufacturer DUMMY_MANUFACTURER = new ManufacturerBuilder().id(UUID.fromString("00000000-0000-0000-0004-000000000000")).
          name(NbBundle.getMessage(AbstractContactComboBoxModel.class,"AbstractContactComboBoxModel.dummy.name")).build();
  public static Retailer DUMMY_RETAILER = new RetailerBuilder().id(UUID.fromString("00000000-0000-0000-0004-000000000000")).name(
          NbBundle.getMessage(AbstractContactComboBoxModel.class,"AbstractContactComboBoxModel.dummy.name")).build();
  private DataProviderEventListener weakListener;

  public static boolean isDummy(Contact contact)
  {
    return contact == DUMMY_CONTACT || contact == DUMMY_MANUFACTURER || contact == DUMMY_RETAILER;
  }

  @Override
  public void handleDataProviderEvent(DataProviderEvent event)
  {
    SwingUtilities.invokeLater(new Runnable()
    {

      @Override
      public void run()
      {
        refresh();
      }
    });
  }

  protected synchronized void registerAtProvider(ContactItemProvider provider)
  {
    if (weakListener == null) {
      weakListener = WeakListeners.create(DataProviderEventListener.class, this, provider);
    }
    provider.addDataProviderEventListener(weakListener);
  }

  @Override
  protected void sortItems(List<? extends C> items)
  {
    Collections.sort(items, new Comparator<C>()
    {

      private final Collator coll = Collator.getInstance();

      @Override
      public int compare(C o1, C o2)
      {
        if (o1 == o2 || (isDummy(o1) && isDummy(o2))) {
          return 0;
        }
        if (isDummy(o1)) {
          return -1;
        }
        if (isDummy(o2)) {
          return 1;
        }
        return coll.compare(o1.getName(), o2.getName());
      }
    });
  }

  @Override
  protected List<? extends C> getDefaultItems()
  {
    return Collections.emptyList();
  }

  @Override
  protected C getDefaultSelection()
  {
    return null;
  }
}
