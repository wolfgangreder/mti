/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel;

import at.reder.mti.api.utils.Money;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Ein inventierbares Object
 *
 * @author wolfi
 */
public interface InventoryObject
{

  /**
   * Id des Objekts
   *
   * @return niemals {@code null}
   */
  public UUID getId();

  /**
   * Name
   *
   * @return niemals {@code null} oder leer
   */
  public String getName();

  /**
   * Beschreibung
   *
   * @return niemals{@code null}
   */
  public String getDescription();

  /**
   * Preis
   *
   * @return der Preis oder {@code null} falls unbekannt.
   */
  public Money getPrice();

  /**
   * Datum des Kaufs
   *
   * @return das Datum oder {@code null} falls unbekannt.
   */
  public LocalDate getDateOfPurchase();

  /**
   * Artikelnummer der Herstellers
   *
   * @return niemals {@code null}
   */
  public String getProductNumber();

  /**
   * Referenz auf den Hersteller.
   *
   * @return der Hersteller oder {@code null} falls unbekannt.
   */
  public Contact getManufacturer();

  /**
   * Referenz auf den Händler
   *
   * @return der Händler oder {@code null} falls unbekannt.
   */
  public Contact getRetailer();

  /**
   * Zustand der Objekts
   *
   * @return niemals {@code null}
   */
  public ModelCondition getCondition();

  /**
   * Mit dem Objekt verbundene Enitäten
   *
   * @return niemals {@code null}
   */
  public List<Entity> getEntities();

  /**
   * Bild des Objekts
   *
   * @return werweis auf das Bild oder {@code null} falls nicht vorhanden.
   */
  public Entity getMasterImage();

  /**
   * Zeitstempel wann das Objekt das letzte mal geändert wurde.
   *
   * @return zeitstempel der letzten Änderung, niemals {@code null}
   */
  public Instant getLastModified();

}
