/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import java.util.List;

/**
 *
 * @author wolfi
 */
public interface ServiceableObject
{

  public List<? extends ServiceEntry> getServiceEntries();
}
