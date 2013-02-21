/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.filesystem.db;

import at.mountain_sd.objects.ClientPropertySupport;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface DBFileObject extends ClientPropertySupport
{

  public UUID getId();

  public String getName();

  public UUID getParent();

  public boolean isFolder();
}
