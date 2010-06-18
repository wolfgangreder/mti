/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.models;

import at.motriv.datamodel.entities.contact.Contact;
import at.motriv.datamodel.entities.contact.ContactItemProvider;
import at.motriv.datamodel.entities.contact.ContactType;
import at.motriv.datamodel.entities.contact.MutableContact;
import at.motriv.datamodel.entities.contact.impl.DefaultMutableContact;
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
public abstract class AbstractContactComboBoxModel extends DefaultValuesComboBoxModel<Contact> implements
        DataProviderEventListener
{

  public static Contact DUMMY_CONTACT = createDummyContact();
  private DataProviderEventListener weakListener;

  private static Contact createDummyContact()
  {
    MutableContact builder = new DefaultMutableContact(ContactType.ALL);
    builder.setId(UUID.fromString("00000000-0000-0000-0004-000000000000"));
    builder.setName(NbBundle.getMessage(AbstractContactComboBoxModel.class, "AbstractContactComboBoxModel.dummy.name"));
    return builder.build();
  }

  public static boolean isDummy(Contact contact)
  {
    return contact == DUMMY_CONTACT;
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
  protected void sortItems(List<? extends Contact> items)
  {
    Collections.sort(items, new Comparator<Contact>()
    {

      private final Collator coll = Collator.getInstance();

      @Override
      public int compare(Contact o1, Contact o2)
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
  protected List<? extends Contact> getDefaultItems()
  {
    return Collections.emptyList();
  }

  @Override
  protected Contact getDefaultSelection()
  {
    return null;
  }
}
