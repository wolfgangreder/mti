/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.locomotive;

import at.motriv.datamodel.MotrivUtils;
import at.motriv.gui.MotrivGUIConstants;
import at.motriv.gui.RefreshCookie;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.util.NbBundle;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author wolfi
 */
public class LocomotivesNode extends AbstractNode
{

  private final InstanceContent content;
  private final Action defaultAction;
  private final Action[] actions;

  public LocomotivesNode()
  {
    this(new InstanceContent());
  }

  private LocomotivesNode(InstanceContent content)
  {
    super(new LocomotivesChildren(), new AbstractLookup(content));
    setDisplayName(NbBundle.getMessage(LocomotivesNode.class, "LocomotivesNode.displayName"));
    this.content = content;
    actions = initActions();
    defaultAction = actions[1];
    content.add(new RefreshCookie()
    {

      @Override
      public void refresh()
      {
        ((LocomotivesChildren) getChildren()).refreshItems();
      }
    });
  }

  private Action[] initActions()
  {
    return new Action[]{getNewLocomotiveAction(),
                        getRefreshAction()};
  }

  @Override
  public Action getPreferredAction()
  {
    return defaultAction;
  }

  @Override
  public Action[] getActions(boolean context)
  {
    return actions;
  }

  private Action getNewLocomotiveAction()
  {
    return MotrivUtils.getActionFromFileObject("Actions/Inventory/at-motriv-gui-locomotive-NewLocomotiveAction.instance");
  }

  private Action getRefreshAction()
  {
    return MotrivUtils.getActionFromFileObject(MotrivGUIConstants.ACTION_REFRESH);
  }
}
