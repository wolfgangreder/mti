/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.nb;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Wolfgang Reder
 */
@Messages({"MTILocomotiveNode_name=Lokomotiven"})
public final class MTILocomotiveNode extends AbstractNode
{

  private static final MTILocomotiveNode INSTANCE = new MTILocomotiveNode();

  public static Node getInstance()
  {
    return INSTANCE;
  }

  private MTILocomotiveNode()
  {
    super(Children.LEAF);
  }

  @Override
  public String getName()
  {
    return Bundle.MTILocomotiveNode_name();
  }

}
