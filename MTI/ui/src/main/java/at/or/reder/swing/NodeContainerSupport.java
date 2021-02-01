/*
 * Copyright 2017 Wolfgang Reder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.or.reder.swing;

import at.or.reder.swing.model.NodeContainer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;

public final class NodeContainerSupport implements NodeContainer
{

  private final TopComponent tc;

  public NodeContainerSupport(TopComponent tc)
  {
    this.tc = tc;
    assert tc != null;
  }

  @Override
  public Lookup getLookup()
  {
    return tc.getLookup();
  }

  @Override
  public void setNodes(Collection<? extends Node> nodes)
  {
    if (nodes == null || nodes.isEmpty()) {
      tc.setActivatedNodes(null);
    } else {
      tc.setActivatedNodes(nodes.toArray(new Node[nodes.size()]));
    }
  }

  @Override
  public void addNodes(Collection<? extends Node> nodes)
  {
    Node[] nodesArray = tc.getActivatedNodes();
    HashSet<Node> tmp;
    if (nodesArray != null) {
      tmp = new HashSet<>(Arrays.asList(nodesArray));
    } else {
      tmp = new HashSet<>();
    }
    tmp.addAll(nodes);
    tc.setActivatedNodes(tmp.toArray(new Node[tmp.size()]));
  }

  @Override
  public <C extends Node> void removeAddNodes(Class<C> nodeClass,
                                              Collection<C> nodes)
  {
    Node[] oldNodes = tc.getActivatedNodes();
    Set<Node> tmp = oldNodes != null ? new CopyOnWriteArraySet<>(Arrays.asList(tc.getActivatedNodes()))
                            : new CopyOnWriteArraySet<>();
    tmp.stream().
            filter((n) -> (n.getClass() == nodeClass)).
            forEach((n) -> {
              tmp.remove(n);
            });
    tmp.addAll(nodes);
    tc.setActivatedNodes(tmp.toArray(new Node[tmp.size()]));
  }

  @Override
  public void removeNode(Collection<? extends Node> nodes)
  {
    HashSet<Node> tmp = new HashSet<>(Arrays.asList(tc.getActivatedNodes()));
    tmp.removeAll(nodes);
    tc.setActivatedNodes(tmp.toArray(new Node[tmp.size()]));
  }

}
