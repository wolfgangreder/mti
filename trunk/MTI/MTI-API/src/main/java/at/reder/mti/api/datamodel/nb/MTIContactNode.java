/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.nb;

import at.reder.mti.api.utils.MTIUtils;
import java.util.List;
import javax.swing.Action;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Wolfgang Reder
 */
@Messages("MTIContactNode_name=Adressen")
public final class MTIContactNode extends AbstractNode
{

  private static final MTIContactNode INSTANCE = new MTIContactNode();
  private Action[] actions;

  public static Node getInstance()
  {
    return INSTANCE;
  }

  private MTIContactNode()
  {
    super(Children.LEAF);
  }

  @Override
  public String getName()
  {
    return Bundle.MTIContactNode_name();
  }

  @Override
  public Action[] getActions(boolean context)
  {
    if (actions == null) {
      initActions();
    }
    assert actions != null;
    return actions;
  }

  private void initActions()
  {
    List<Action> actionList = MTIUtils.actionsForPath("Nodes/mti/MTIContactNode");
    actions = new Action[actionList.size()];
    if (!actionList.isEmpty()) {
      actions = actionList.toArray(actions);
    }
  }

}
