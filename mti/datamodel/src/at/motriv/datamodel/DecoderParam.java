/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import at.motriv.datamodel.externals.External;

/**
 *
 * @author wolfi
 */
public interface DecoderParam extends External
{

  public boolean isActive();

  public String getMemo();
}
