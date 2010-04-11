/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.locomotive;

import org.openide.nodes.AbstractNode;
import org.openide.util.NbBundle;

/**
 *
 * @author wolfi
 */
public class LocomotivesNode extends AbstractNode
{

  public LocomotivesNode()
  {
    super(new LocomotivesChildren());
    setDisplayName(NbBundle.getMessage(LocomotivesNode.class,"LocomotivesNode.displayName"));
  }
}
