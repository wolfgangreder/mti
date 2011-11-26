/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui;

import at.motriv.gui.contact.ContactsNode;
import at.motriv.gui.locomotive.LocomotivesNode;
import java.util.Collections;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author wolfi
 */
public class ExplorerRootChildren extends Children.Keys<String>
{

  @Override
  protected Node[] createNodes(String key)
  {
    return new Node[]{new LocomotivesNode(),
              new ContactsNode()};
  }

  @Override
  protected void addNotify()
  {
    setKeys(Collections.singletonList(""));
  }
}
