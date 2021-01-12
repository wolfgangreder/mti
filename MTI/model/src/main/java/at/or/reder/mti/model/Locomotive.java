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
import java.util.Collection;
import java.util.UUID;

public interface Locomotive extends Vehicle
{

  public static interface Builder
  {

    public Locomotive.Builder copy(Locomotive locomotive) throws NullPointerException;

    /**
     * Setzt den Zustand des Objekts
     *
     * @param cond
     * @return {@code this}
     * @throws NullPointerException wenn {@code cond==null}
     */
    public Locomotive.Builder condition(ModelCondition cond) throws NullPointerException;

    /**
     * Setzt das Kaufdatum.
     *
     * @param ts Das Kaufdatum oder {@code null} falls nicht bekannt.
     * @return {@code this}
     */
    public Locomotive.Builder dateOfPurchase(LocalDate ts);

    /**
     * Setzt die Beschreibung.
     *
     * @param descr
     * @return {@code this}
     */
    public Locomotive.Builder description(String descr);

    /**
     * Füngt eine Entität hinzu
     *
     * @param e
     * @return {@code this}
     * @throws NullPointerException wenn {@code e==null}
     */
    public Locomotive.Builder addEntity(Entity e) throws NullPointerException;

    /**
     * Entfernt falls vorhanden die Entität.
     *
     * @param e
     * @return {@code this}
     */
    public Locomotive.Builder removeEntity(Entity e);

    /**
     * Fügt die in {@code e} übergebenen Entitäten hinzu.
     *
     * @param e Sammlung von Entitäten
     * @return {@code this}
     * @throws NullPointerException wenn {@code e==null}.
     * @throws IllegalArgumentException wenn {@code e null} enthält.
     */
    public Locomotive.Builder addEntities(Collection<? extends Entity> e) throws NullPointerException, IllegalArgumentException;

    /**
     * Entfernt alle Entitäten
     *
     * @return {@code this}
     */
    public Locomotive.Builder clearEntities();

    /**
     * Setzt die Id
     *
     * @param id
     * @return {@code this}
     * @throws NullPointerException wenn {@code id==null}.
     */
    public Locomotive.Builder id(UUID id) throws NullPointerException;

    /**
     * Setzt den Zeitpunkt der letzten Änderung
     *
     * @param ts
     * @return {@code this}
     * @throws NullPointerException wenn {@code ts==null}
     */
    public Locomotive.Builder lastModified(ZonedDateTime ts) throws NullPointerException;

    /**
     * Setzt den Hersteller.
     *
     * @param contact Der Hersteller oder {@code null} falls unbekannt.
     * @return {@code this}
     */
    public Locomotive.Builder manufacturer(Contact contact);

    /**
     * Setzt das Bild des Objekts. Wenn {@code e!=null} wird {
     *
     * @¢ode e} auch zur Liste der Entitäten hinzugefügt. Falls das Bild bereits gesetzt war, wird das alte Bild aber nicht aus
     * der Liste entfernt.
     * @param e Das Bild des Objekts oder {@code null} falls nicht vorhanden.
     * @return {@code this}
     */
    public Locomotive.Builder masterImage(UUID e);

    /**
     * Der Name des Objects
     *
     * @param name
     * @return {@code this}
     * @throws NullPointerException wenn {@code name==null}
     * @throws IllegalArgumentException wenn {@code name.trim().isEmpty()}
     */
    public Locomotive.Builder name(String name) throws NullPointerException,
                                                       IllegalArgumentException;

    /**
     * Der Preis des Objekts.
     *
     * @param price Der Preis oder {@code null} wenn nicht bekannt.
     * @return {@code this}
     */
    public Locomotive.Builder price(Money price);

    /**
     * Artikelnummer des Herstellers
     *
     * @param productNumber Artikelnummer oder {@code null} falls nicht bekannt.
     * @return {@code this}
     */
    public Locomotive.Builder productNumber(String productNumber);

    /**
     * Der Händer bei dem das Objekt gekauft wurde.
     *
     * @param contact Der Händler, oder {@code null} falls nicht bekannt.
     * @return {@code this}
     */
    public Locomotive.Builder retailer(Contact contact);

    public Locomotive.Builder addServiceEntry(ServiceEntry e) throws NullPointerException;

    public Locomotive.Builder removeServiceEntry(ServiceEntry e);

    public Locomotive.Builder addServiceEntries(Collection<? extends ServiceEntry> e) throws NullPointerException,
                                                                                             IllegalArgumentException;

    public Locomotive.Builder clearServiceEntries();

    public Locomotive.Builder addDefect(Defect d);

    public Locomotive.Builder addDefects(Collection<? extends Defect> d);

    public Locomotive.Builder removeDefect(Defect d);

    public Locomotive.Builder removeDefects(Collection<? extends Defect> d);

    public Locomotive.Builder clearDefects();

    /**
     * Setzt den Masstab
     *
     * @param scale
     * @return {@code this}
     * @throws NullPointerException wenn {@code scale==null}
     */
    public Locomotive.Builder gauge(Gauge scale) throws NullPointerException;

    public Locomotive.Builder epoch(Epoch era);

    public Locomotive.Builder length(double len);

    public Locomotive.Builder width(double width);

    public Locomotive.Builder height(double height);

    public Locomotive.Builder weight(double weight);

    public Locomotive.Builder address(int address);

    public Locomotive.Builder consistsAddress(int address);

    public Locomotive.Builder decoder(String decoder);

    public Locomotive.Builder locomotiveNumber(String number);

    public Locomotive.Builder wheelArrangement(String arrangement);

    public Locomotive.Builder locomotiveClass(String clazz);

    public Locomotive.Builder company(String company);

    public Locomotive.Builder country(String country);

    public Locomotive.Builder tractionSystem(TractionSystem ts);

    public Locomotive build();

  }

  public static interface BuilderFactory
  {

    public Locomotive.Builder createBuilder();

  }

  public String getLocomotiveNumber();

  public String getWheelArrangement();

  public String getLocomotiveClass();

  public String getCompany();

  public String getCountry();

  public TractionSystem getTractionSystem();

}
