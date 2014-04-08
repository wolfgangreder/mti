/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.nb;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author Wolfgang Reder
 */
@Messages({"MTIVehicleNode_name=Fahrezeuge"})
public final class MTIVehicleNode extends AbstractNode
{

  private static final MTIVehicleNode INSTANCE = new MTIVehicleNode();

  public static Node getInstance()
  {
    return INSTANCE;
  }

  private static final class MTIVehicleChildren extends Children.Keys<UUID>
  {

    private static final List<Node> children = Collections.unmodifiableList(Arrays.asList(MTILocomotiveNode.getInstance(),
                                                                                          MTIWagonNode.getInstance()));

    @Override
    protected Node[] createNodes(UUID key)
    {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Collection<Node> initCollection()
    {
      return children;
    }

    @Override
    public int getNodesCount(boolean optimalResult)
    {
      return children.size();
    }

  }

  private MTIVehicleNode()
  {
    super(new MTIVehicleChildren());
  }

  @Override
  public String getName()
  {
    return Bundle.MTIVehicleNode_name();
  }

}
