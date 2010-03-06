/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel;

import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface External
{

  /**
   * Eindeutige ID des Externals
   * @return
   */
  public UUID getId();

  /**
   * ID des Repositories
   * @return
   */
  public ExternalRepository getRepository();

  /**
   * Art des Externals
   * @return
   */
  public ExternalKind getKind();

  /**
   * Pfad relativ zum repository
   * @return
   */
  public String getFile();

  /**
   * Beschreibung
   * @return
   */
  public String getDescription();
  
  /**
   * Mime-Typ
   * @return
   */
  public String getMimeType();
}
