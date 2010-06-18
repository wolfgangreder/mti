/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.externals;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

@OptionsPanelController.SubRegistration(location = "Advanced",
                                        displayName = "#AdvancedOption_DisplayName_Externals",
                                        keywords = "#AdvancedOption_Keywords_Externals",
                                        keywordsCategory = "Advanced/Externals")
public final class ExternalsOptionsPanelController extends OptionsPanelController
{

  private ExternalsPanel panel;
  private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
  private boolean changed;

  @Override
  public void update()
  {
    getPanel().load();
    changed = false;
  }

  @Override
  public void applyChanges()
  {
    getPanel().store();
    changed = false;
  }

  @Override
  public void cancel()
  {
    // need not do anything special, if no changes have been persisted yet
  }

  @Override
  public boolean isValid()
  {
    return getPanel().valid();
  }

  @Override
  public boolean isChanged()
  {
    return changed;
  }

  @Override
  public HelpCtx getHelpCtx()
  {
    return null; // new HelpCtx("...ID") if you have a help set
  }

  @Override
  public JComponent getComponent(Lookup masterLookup)
  {
    return getPanel();
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener l)
  {
    pcs.addPropertyChangeListener(l);
  }

  @Override
  public void removePropertyChangeListener(PropertyChangeListener l)
  {
    pcs.removePropertyChangeListener(l);
  }

  private ExternalsPanel getPanel()
  {
    if (panel == null) {
      panel = new ExternalsPanel(this);
    }
    return panel;
  }

  void changed()
  {
    if (!changed) {
      changed = true;
      pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false, true);
    }
    pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
  }
}
