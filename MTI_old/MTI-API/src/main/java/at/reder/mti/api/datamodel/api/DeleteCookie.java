/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.api;

import org.openide.nodes.Node;

/**
 *
 * @author wolfi
 */
public interface DeleteCookie extends Node.Cookie
{

  public void deleteNode();

}