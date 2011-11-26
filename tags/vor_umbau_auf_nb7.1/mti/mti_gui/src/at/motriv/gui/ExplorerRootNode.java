/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui;

import org.openide.nodes.AbstractNode;
import org.openide.util.NbBundle;

/**
 *
 * @author wolfi
 */
public class ExplorerRootNode extends AbstractNode
{

  public ExplorerRootNode()
  {
    super(new ExplorerRootChildren());
    setDisplayName(NbBundle.getMessage(ExplorerRootNode.class, "ExplorerRootNode.name"));
  }
}
