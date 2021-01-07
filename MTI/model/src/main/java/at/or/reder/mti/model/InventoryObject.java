/*
 * Copyright 2020-2021 Wolfgang Reder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.or.reder.mti.model;

import at.or.reder.mti.model.utils.Money;
import java.time.LocalDate;
import java.time.ZonedDateTime;
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
  public ZonedDateTime getLastModified();

}
