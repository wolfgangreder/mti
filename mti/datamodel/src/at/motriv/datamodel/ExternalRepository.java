/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import java.net.URI;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface ExternalRepository
{

  public UUID getId();

  public String getName();

  public URI getURI();
}
