/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.locomotive;

import org.openide.cookies.OpenCookie;

/**
 *
 * @author wolfi
 */
public class LocomotiveOpenCookie implements OpenCookie
{

  LocomotiveDataObject dob;

  public LocomotiveOpenCookie(LocomotiveDataObject dob)
  {
    this.dob = dob;
  }

  @Override
  public void open()
  {
    LocomotiveTopComponent.execute(dob.getNode());
  }
}
