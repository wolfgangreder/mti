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
@Messages({"MTIWagonNode_name=Waggons"})
public final class MTIWagonNode extends AbstractNode
{

  private static final MTIWagonNode INSTANCE = new MTIWagonNode();

  public static Node getInstance()
  {
    return INSTANCE;
  }

  private MTIWagonNode()
  {
    super(Children.LEAF);
  }

  @Override
  public String getName()
  {
    return Bundle.MTIWagonNode_name();
  }

}
