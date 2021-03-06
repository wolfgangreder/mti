/*
 * Copyright 2020 Wolfgang Reder.
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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

/**
 * Ein gelagertes Ersatzteil
 *
 * @author wolfi
 */
public interface SparePart extends InventoryObject
{

  public static interface Builder
  {

    /**
     * Setzt den Zustand des Objekts
     *
     * @param cond
     * @return {@code this}
     * @throws NullPointerException wenn {@code cond==null}
     */
    public SparePart.Builder condition(ModelCondition cond) throws
            NullPointerException;

    /**
     * Setzt das Kaufdatum.
     *
     * @param ts Das Kaufdatum oder {@code null} falls nicht bekannt.
     * @return {@code this}
     */
    public SparePart.Builder dateOfPurchase(LocalDate ts);

    /**
     * Setzt die Beschreibung.
     *
     * @param descr
     * @return {@code this}
     */
    public SparePart.Builder description(String descr);

    /**
     * Füngt eine Entität hinzu
     *
     * @param e
     * @return {@code this}
     * @throws NullPointerException wenn {@code e==null}
     */
    public SparePart.Builder addEntity(Entity e) throws NullPointerException;

    /**
     * Entfernt falls vorhanden die Entität.
     *
     * @param e
     * @return {@code this}
     */
    public SparePart.Builder removeEntity(Entity e);

    /**
     * Fügt die in {@code e} übergebenen Entitäten hinzu.
     *
     * @param e Sammlung von Entitäten
     * @return {@code this}
     * @throws NullPointerException wenn {@code e==null}.
     * @throws IllegalArgumentException wenn {@code e null} enthält.
     */
    public SparePart.Builder addEntities(Collection<? extends Entity> e) throws
            NullPointerException, IllegalArgumentException;

    /**
     * Entfernt alle Entitäten
     *
     * @return {@code this}
     */
    public SparePart.Builder clearEntities();

    /**
     * Setzt die Id
     *
     * @param id
     * @return {@code this}
     * @throws NullPointerException wenn {@code id==null}.
     */
    public SparePart.Builder id(UUID id) throws NullPointerException;

    /**
     * Setzt den Zeitpunkt der letzten Änderung
     *
     * @param ts
     * @return {@code this}
     * @throws NullPointerException wenn {@code ts==null}
     */
    public SparePart.Builder lastModified(Instant ts) throws
            NullPointerException;

    /**
     * Setzt den Hersteller.
     *
     * @param contact Der Hersteller oder {@code null} falls unbekannt.
     * @return {@code this}
     */
    public SparePart.Builder manufacturer(Contact contact);

    /**
     * Setzt das Bild des Objekts. Wenn {@code e!=null} wird {
     *
     * @¢ode e} auch zur Liste der Entitäten hinzugefügt. Falls das Bild bereits gesetzt war, wird das alte Bild aber nicht aus
     * der Liste entfernt.
     * @param e Das Bild des Objekts oder {@code null} falls nicht vorhanden.
     * @return {@code this}
     */
    public SparePart.Builder masterImage(Entity e);

    /**
     * Der Name des Objects
     *
     * @param name
     * @return {@code this}
     * @throws NullPointerException wenn {@code name==null}
     * @throws IllegalArgumentException wenn {@code name.trim().isEmpty()}
     */
    public SparePart.Builder name(String name) throws NullPointerException,
                                                      IllegalArgumentException;

    /**
     * Der Preis des Objekts.
     *
     * @param price Der Preis oder {@code null} wenn nicht bekannt.
     * @return {@code this}
     */
    public SparePart.Builder price(Money price);

    /**
     * Artikelnummer des Herstellers
     *
     * @param productNumber Artikelnummer oder {@code null} falls nicht bekannt.
     * @return {@code this}
     */
    public SparePart.Builder productNumber(String productNumber);

    /**
     * Der Händer bei dem das Objekt gekauft wurde.
     *
     * @param contact Der Händler, oder {@code null} falls nicht bekannt.
     * @return {@code this}
     */
    public SparePart.Builder retailer(Contact contact);

    /**
     * Kopiert die Werte von {@code part}
     *
     * @param part
     * @return {
     * @this this}
     * @throws NullPointerException wenn {@code part==null}
     */
    public SparePart.Builder copy(SparePart part) throws NullPointerException;

    /**
     * Die vorrätige Menge des Ersatzteils.
     *
     * @param amount
     * @return {@code this}
     * @throws NullPointerException wenn {@code amount==null}
     */
    public SparePart.Builder amount(BigDecimal amount) throws NullPointerException;

    public SparePart build();

  }

  public static interface BuilderFactory
  {

    public SparePart.Builder createSparePartBuilder();

  }

  /**
   * Die vorrätige Menge
   *
   * @return niemals {@code null}
   */
  public BigDecimal getAmount();

}
