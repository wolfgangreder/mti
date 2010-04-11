/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.contact;

import at.mountainsd.dataprovider.api.NullFileObject;

/**
 *
 * @author wolfi
 */
public class ContactFileObject extends NullFileObject
{

  private static final long serialVersionUID = 1L;

  public ContactFileObject(ContactNode node)
  {
    super(node);
  }

  @Override
  public boolean isRoot()
  {
    return false;
  }
}
