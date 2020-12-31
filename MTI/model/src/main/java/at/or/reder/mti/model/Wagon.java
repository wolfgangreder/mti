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
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface Wagon extends Vehicle
{

  public static interface Builder
  {

    public Wagon.Builder copy(Wagon wagon) throws NullPointerException;

    /**
     * Setzt den Zustand des Objekts
     *
     * @param cond
     * @return {@code this}
     * @throws NullPointerException wenn {@code cond==null}
     */
    public Wagon.Builder condition(ModelCondition cond) throws
            NullPointerException;

    /**
     * Setzt das Kaufdatum.
     *
     * @param ts Das Kaufdatum oder {@code null} falls nicht bekannt.
     * @return {@code this}
     */
    public Wagon.Builder dateOfPurchase(LocalDate ts);

    /**
     * Setzt die Beschreibung.
     *
     * @param descr
     * @return {@code this}
     */
    public Wagon.Builder description(String descr);

    /**
     * Füngt eine Entität hinzu
     *
     * @param e
     * @return {@code this}
     * @throws NullPointerException wenn {@code e==null}
     */
    public Wagon.Builder addEntity(Entity e) throws NullPointerException;

    /**
     * Entfernt falls vorhanden die Entität.
     *
     * @param e
     * @return {@code this}
     */
    public Wagon.Builder removeEntity(Entity e);

    /**
     * Fügt die in {@code e} übergebenen Entitäten hinzu.
     *
     * @param e Sammlung von Entitäten
     * @return {@code this}
     * @throws NullPointerException wenn {@code e==null}.
     * @throws IllegalArgumentException wenn {@code e null} enthält.
     */
    public Wagon.Builder addEntities(Collection<? extends Entity> e) throws
            NullPointerException, IllegalArgumentException;

    /**
     * Entfernt alle Entitäten
     *
     * @return {@code this}
     */
    public Wagon.Builder clearEntities();

    /**
     * Setzt die Id
     *
     * @param id
     * @return {@code this}
     * @throws NullPointerException wenn {@code id==null}.
     */
    public Wagon.Builder id(UUID id) throws NullPointerException;

    /**
     * Setzt den Zeitpunkt der letzten Änderung
     *
     * @param ts
     * @return {@code this}
     * @throws NullPointerException wenn {@code ts==null}
     */
    public Wagon.Builder lastModified(Instant ts) throws
            NullPointerException;

    /**
     * Setzt den Hersteller.
     *
     * @param contact Der Hersteller oder {@code null} falls unbekannt.
     * @return {@code this}
     */
    public Wagon.Builder manufacturer(Contact contact);

    /**
     * Setzt das Bild des Objekts. Wenn {@code e!=null} wird {
     *
     * @¢ode e} auch zur Liste der Entitäten hinzugefügt. Falls das Bild bereits gesetzt war, wird das alte Bild aber nicht aus
     * der Liste entfernt.
     * @param e Das Bild des Objekts oder {@code null} falls nicht vorhanden.
     * @return {@code this}
     */
    public Wagon.Builder masterImage(Entity e);

    /**
     * Der Name des Objects
     *
     * @param name
     * @return {@code this}
     * @throws NullPointerException wenn {@code name==null}
     * @throws IllegalArgumentException wenn {@code name.trim().isEmpty()}
     */
    public Wagon.Builder name(String name) throws NullPointerException,
                                                  IllegalArgumentException;

    /**
     * Der Preis des Objekts.
     *
     * @param price Der Preis oder {@code null} wenn nicht bekannt.
     * @return {@code this}
     */
    public Wagon.Builder price(Money price);

    /**
     * Artikelnummer des Herstellers
     *
     * @param productNumber Artikelnummer oder {@code null} falls nicht bekannt.
     * @return {@code this}
     */
    public Wagon.Builder productNumber(String productNumber);

    /**
     * Der Händer bei dem das Objekt gekauft wurde.
     *
     * @param contact Der Händler, oder {@code null} falls nicht bekannt.
     * @return {@code this}
     */
    public Wagon.Builder retailer(Contact contact);

    /**
     * Fügt ein Object zum Lookup hinzu
     *
     * @param item
     * @return {@code this}
     * @throws NullPointerException wenn {@code item==null}
     */
    public Wagon.Builder addLookupItem(Object item) throws NullPointerException;

    /**
     * Entfernt {@code item} vom Lookup
     *
     * @param item
     * @return {@code this}
     */
    public Wagon.Builder removeLookupItem(Object item);

    /**
     * Entfernt alle Objekte für die gilt {@code clazz.isInstance(item)==true}
     *
     * @param clazz
     * @return {@code this}
     * @throws NullPointerException wenn {@code clazz==null}
     * @see Class#isInstance(java.lang.Object)
     */
    public Wagon.Builder removeInstancesOfFromLookup(Class<?> clazz) throws NullPointerException;

    /**
     * Entfernt alle Objecte aus dem Lookup
     *
     * @return {@code this}
     */
    public Wagon.Builder clearLookup();

    public Wagon.Builder addServiceEntry(ServiceEntry e) throws NullPointerException;

    public Wagon.Builder removeServiceEntry(ServiceEntry e);

    public Wagon.Builder addServiceEntries(Collection<? extends ServiceEntry> e) throws
            NullPointerException,
            IllegalArgumentException;

    public Wagon.Builder clearServiceEntries();

    /**
     * Setzt den Masstab
     *
     * @param scale
     * @return {@code this}
     * @throws NullPointerException wenn {@code scale==null}
     */
    public Wagon.Builder scale(Scale scale) throws NullPointerException;

    public Wagon.Builder era(Epoch era);

    public Wagon.Builder length(double len);

    public Wagon.Builder width(double width);

    public Wagon.Builder height(double height);

    public Wagon.Builder weight(double weight);

    public Wagon.Builder addDecoder(Decoder d) throws NullPointerException;

    public Wagon.Builder removeDecoder(Decoder d) throws NullPointerException;

    public Wagon.Builder addDecoder(Collection<? extends Decoder> d) throws NullPointerException,
                                                                            IllegalArgumentException;

    public Wagon.Builder clearDecoder();

    public Wagon.Builder wagonNumber(String number);

    public Wagon.Builder wheelCount(int wheelCount);

    public Wagon.Builder kind(String kind);

    public Wagon.Builder wagonClass(String clazz);

    public Wagon.Builder company(String company);

    public Wagon.Builder country(String country);

    public Wagon build();

  }

  public static interface BuilderFactory
  {

    public Wagon.Builder createWagonBuilder();

  }

  public String getWagonNumber();

  public String getKind();

  public String getWagonClass();

  public String getCompany();

  public String getCountry();

  public int getWheelCount();

}
