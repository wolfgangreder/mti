/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import java.util.Date;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface Defect
{

  public UUID getId();

  public Date getDate();

  public String getDescription();
}
