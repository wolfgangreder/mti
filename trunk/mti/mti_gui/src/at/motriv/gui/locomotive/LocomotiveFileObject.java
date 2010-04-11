/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.locomotive;

import at.mountainsd.dataprovider.api.NullFileObject;

/**
 *
 * @author wolfi
 */
public class LocomotiveFileObject extends NullFileObject
{

  private static final long serialVersionUID = 1L;

  public LocomotiveFileObject(LocomotiveNode node)
  {
    super(node);
  }

  @Override
  public boolean isRoot()
  {
    return false;
  }
}
