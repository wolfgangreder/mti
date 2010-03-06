/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface ServiceEntry
{

  public UUID getId();

  public Date getDate();

  public List<? extends Defect> getDefectsResolved();

  public Map<? extends Defect, List<? extends SparePart>> getPartsUsed();

  public ServiceableObject getServiceObject();
}
