/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.ui.controls;

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
   * @param nodes
   */
  public void addNodes(Collection<? extends Node> nodes);

  /**
   * entfernt alle nodes vom Typ nodeClass und fügt nodes hinzu
   *
   * @param <C>
   * @param nodeClass
   * @param nodes
   */
  public <C extends Node> void removeAddNodes(Class<C> nodeClass, Collection<C> nodes);

  /**
   * entfernt die nodes
   *
   * @param nodes
   */
  public void removeNode(Collection<? extends Node> nodes);

  /**
   * Ersetzt alle vorher zugeordneten nodes durch die in {@code nodes} übergebenen.
   *
   * @param nodes
   */
  public void setNodes(Collection<? extends Node> nodes);

}
