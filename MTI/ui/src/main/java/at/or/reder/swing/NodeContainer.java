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

import java.util.Collection;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public interface NodeContainer extends Lookup.Provider
{

  /**
   * fügt die nodes hinzu
   *
   * @param nodes nodes
   */
  public void addNodes(Collection<? extends Node> nodes);

  /**
   * entfernt alle nodes vom Typ nodeClass und fügt nodes hinzu
   *
   * @param <C> c
   * @param nodeClass nodeclass
   * @param nodes nodes
   */
  public <C extends Node> void removeAddNodes(Class<C> nodeClass,
                                              Collection<C> nodes);

  /**
   * entfernt die nodes
   *
   * @param nodes nodes
   */
  public void removeNode(Collection<? extends Node> nodes);

  /**
   * Ersetzt alle vorher zugeordneten nodes durch die in {@code nodes} übergebenen.
   *
   * @param nodes nodes
   */
  public void setNodes(Collection<? extends Node> nodes);

}
