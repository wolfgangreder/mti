/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.nodes;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Wolfgang Reder
 */
public final class MTIRootNode extends AbstractNode
{

  private static final MTIRootNode INSTANCE = new MTIRootNode();

  public static final Node getInstance()
  {
    return INSTANCE;
  }

  private final static class RootChildren extends Children.Keys<UUID>
  {

    private final List<Node> children = Collections.unmodifiableList(Arrays.asList(MTIContactNode.getInstance(),
                                                                                   MTIVehicleNode.getInstance()));

    @Override
    protected Node[] createNodes(UUID key)
    {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNodesCount(boolean optimalResult)
    {
      return children.size();
    }

    @Override
    protected Collection<Node> initCollection()
    {
      return children;
    }

  }

  private MTIRootNode()
  {
    super(new RootChildren());
  }

  @Override
  public String getName()
  {
    return "MTIRoot";
  }

}
