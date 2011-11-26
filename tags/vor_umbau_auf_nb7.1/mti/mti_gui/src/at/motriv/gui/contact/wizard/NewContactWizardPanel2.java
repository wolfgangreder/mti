/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact.wizard;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

public class NewContactWizardPanel2 implements WizardDescriptor.Panel<WizardDescriptor>, PropertyChangeListener
{

  /**
   * The visual component that displays this panel. If you need to access the
   * component from this class, just use getComponent().
   */
  private NewContactVisualPanel2 component;
  private WeakReference<NewContactWizardData> data;
  // Get the visual component for the panel. In this template, the component
  // is kept separate. This can be more efficient: if the wizard is created
  // but never displayed, or not all panels are displayed, it is better to
  // create only those which really need to be visible.

  @Override
  public Component getComponent()
  {
    if (component == null) {
      component = new NewContactVisualPanel2();
      component.addPropertyChangeListener("selection", this);
    }
    return component;
  }

  @Override
  public HelpCtx getHelp()
  {
    // Show no Help button for this panel:
    return HelpCtx.DEFAULT_HELP;
    // If you have context help:
    // return new HelpCtx(SampleWizardPanel1.class);
  }

  @Override
  public boolean isValid()
  {
    NewContactWizardData tmp = data.get();
    return tmp!=null && tmp.getFinisher()!=null;
  }
  
  private final Set<ChangeListener> listeners = new CopyOnWriteArraySet<ChangeListener>();

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

  // You can use a settings object to keep track of state. Normally the
  // settings object will be the WizardDescriptor, so you can use
  // WizardDescriptor.getProperty & putProperty to store information entered
  // by the user.
  @Override
  public void readSettings(WizardDescriptor settings)
  {
    NewContactWizardData tmpdata = (NewContactWizardData) settings.getProperty(WizardDescriptor.PROP_CONTENT_DATA);
    data = new WeakReference<NewContactWizardData>(tmpdata);
    component.setMode(tmpdata);
  }

  @Override
  public void storeSettings(WizardDescriptor settings)
  {
    NewContactWizardData tmpdata = (NewContactWizardData) settings.getProperty(WizardDescriptor.PROP_CONTENT_DATA);
    data = new WeakReference<NewContactWizardData>(tmpdata);
    tmpdata.setFinisher(component.getFinisher());
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt)
  {
    fireChangeEvent();
  }
}

