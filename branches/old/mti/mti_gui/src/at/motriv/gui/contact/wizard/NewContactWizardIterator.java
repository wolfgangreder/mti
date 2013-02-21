/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact.wizard;

import at.motriv.datamodel.entities.contact.Contact;
import java.awt.Component;
import java.awt.Dialog;
import java.text.MessageFormat;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.WizardDescriptor.Panel;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;

public final class NewContactWizardIterator implements WizardDescriptor.Iterator<WizardDescriptor>
{

  // To invoke this wizard, copy-paste and run the following code, e.g. from
  // SomeAction.performAction():
    /*
  WizardDescriptor.Iterator iterator = new NewContactWizardIterator();
  WizardDescriptor wizardDescriptor = new WizardDescriptor(iterator);
  // {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
  // {1} will be replaced by WizardDescriptor.Iterator.name()
  wizardDescriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
  wizardDescriptor.setTitle("Your wizard dialog title here");
  Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
  dialog.setVisible(true);
  dialog.toFront();
  boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
  if (!cancelled) {
  // do something
  }
   */
  private int index;
  private WizardDescriptor.Panel<WizardDescriptor> panels[];
  private NewContactWizardData data;
  private String[] steps = new String[2];

  public void setData(NewContactWizardData data)
  {
    this.data = data;
  }

  /**
   * Initialize panels representing individual wizard's steps and sets
   * various properties for them influencing wizard appearance.
   */
  @SuppressWarnings("unchecked")
  private WizardDescriptor.Panel<WizardDescriptor>[] getPanels()
  {
    if (panels == null) {
      panels = new WizardDescriptor.Panel[]{new NewContactWizardPanel1(),
                                            new NewContactWizardPanel2()};
      for (int i = 0; i < panels.length; i++) {
        Component c = panels[i].getComponent();
        // Default step name to component name of panel.
        steps[i] = c.getName();
        if (c instanceof JComponent) { // assume Swing components
          JComponent jc = (JComponent) c;
          initComponent(jc, i, steps);
        }
      }
    }
    return panels;
  }

  private void initComponent(JComponent jc, int index, String[] steps)
  {
    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_SELECTED_INDEX, index);
    // Sets steps names for a panel
    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DATA, steps);
    // Turn on subtitle creation on each step
    jc.putClientProperty(WizardDescriptor.PROP_AUTO_WIZARD_STYLE, true);
    // Show steps on the left side with the image on the background
    jc.putClientProperty(WizardDescriptor.PROP_CONTENT_DISPLAYED, true);
    // Turn on numbering of all steps
    //jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
  }

  @Override
  public WizardDescriptor.Panel<WizardDescriptor> current()
  {
    return getPanels()[index];
  }

  @Override
  public String name()
  {
    return NbBundle.getMessage(getClass(), "NewContactWizard.steps", index + 1, getPanels().length);
  }

  @Override
  public boolean hasNext()
  {
    return index < getPanels().length - 1;
  }

  @Override
  public boolean hasPrevious()
  {
    return index > 0;
  }

  @Override
  public void nextPanel()
  {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    index++;
  }

  @Override
  public void previousPanel()
  {
    if (!hasPrevious()) {
      throw new NoSuchElementException();
    }
    index--;
  }
  // If something changes dynamically (besides moving between panels), e.g.
  // the number of panels changes in response to user input, then uncomment
  // the following and call when needed: fireChangeEvent();
  private Set<ChangeListener> listeners = new CopyOnWriteArraySet<ChangeListener>(); // or can use ChangeSupport in NB 6.0

  @Override
  public final void addChangeListener(ChangeListener l)
  {
    if (l != null) {
      listeners.add(l);
    }
  }

  @Override
  public final void removeChangeListener(ChangeListener l)
  {
    listeners.remove(l);
  }

  protected final void fireChangeEvent()
  {
    ChangeEvent ev = new ChangeEvent(this);
    for (ChangeListener l : listeners) {
      l.stateChanged(ev);
    }
  }

  public static Contact execute(Set<CreationMode> allowedModes)
  {
    NewContactWizardIterator iterator = new NewContactWizardIterator();
    WizardDescriptor wizardDescriptor = new WizardDescriptor(iterator);
    // {0} will be replaced by WizardDescriptor.Panel.getComponent().getName()
    // {1} will be replaced by WizardDescriptor.Iterator.name()
    wizardDescriptor.setTitleFormat(new MessageFormat("{0} ({1})"));
    wizardDescriptor.setTitle(NbBundle.getMessage(NewContactWizardIterator.class, "NewContactWizard.title"));
    NewContactWizardData data = new NewContactWizardData(allowedModes);
    wizardDescriptor.putProperty(WizardDescriptor.PROP_CONTENT_DATA, data);
    iterator.setData(data);
    Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
    dialog.setVisible(true);
    dialog.toFront();
    boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
    if (!cancelled && data.getFinisher() != null) {
      try {
        return data.getFinisher().call();
      } catch (Exception ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return null;
  }
}
