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
import at.reder.mti.api.utils.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Ein inventierbares Object
 *
 * @author wolfi
 */
public interface InventoryObject
{

  public static interface Builder<I extends InventoryObject> 
  {

    /**
     * Setzt den Zustand des Objekts
     *
     * @param cond
     * @return {@code this}
     * @throws NullPointerException wenn {@code cond==null}
     */
    public InventoryObject.Builder<? extends InventoryObject> condition(ModelCondition cond) throws
            NullPointerException;

    /**
     * Setzt das Kaufdatum.
     *
     * @param ts Das Kaufdatum oder {@code null} falls nicht bekannt.
     * @return {@code this}
     */
    public InventoryObject.Builder<? extends InventoryObject> dateOfPurchase(Timestamp ts);

    /**
     * Setzt die Beschreibung.
     *
     * @param descr
     * @return {@code this}
     */
    public InventoryObject.Builder<? extends InventoryObject> description(String descr);

    /**
     * Füngt eine Entität hinzu
     *
     * @param e
     * @return {@code this}
     * @throws NullPointerException wenn {@code e==null}
     */
    public InventoryObject.Builder<? extends InventoryObject> addEntity(Entity e) throws NullPointerException;

    /**
     * Entfernt falls vorhanden die Entität.
     *
     * @param e
     * @return {@code this}
     */
    public InventoryObject.Builder<? extends InventoryObject> removeEntity(Entity e);

    /**
     * Fügt die in {@code e} übergebenen Entitäten hinzu.
     *
     * @param e Sammlung von Entitäten
     * @return {@code this}
     * @throws NullPointerException wenn {@code e==null}.
     * @throws IllegalArgumentException wenn {@code e null} enthält.
     */
    public InventoryObject.Builder<? extends InventoryObject> addEntities(Collection<? extends Entity> e) throws
            NullPointerException, IllegalArgumentException;

    /**
     * Entfernt alle Entitäten
     *
     * @return {@code this}
     */
    public InventoryObject.Builder<? extends InventoryObject> clearEntities();

    /**
     * Setzt die Id
     *
     * @param id
     * @return {@code this}
     * @throws NullPointerException wenn {@code id==null}.
     */
    public InventoryObject.Builder<? extends InventoryObject> id(UUID id) throws NullPointerException;

    /**
     * Setzt den Zeitpunkt der letzten Änderung
     *
     * @param ts
     * @return {@code this}
     * @throws NullPointerException wenn {@code ts==null}
     */
    public InventoryObject.Builder<? extends InventoryObject> lastModified(Timestamp ts) throws
            NullPointerException;

    /**
     * Setzt den Hersteller.
     *
     * @param contact Der Hersteller oder {@code null} falls unbekannt.
     * @return {@code this}
     */
    public InventoryObject.Builder<? extends InventoryObject> manufacturer(Contact contact);

    /**
     * Setzt das Bild des Objekts. Wenn {@code e!=null} wird {
     *
     * @¢ode e} auch zur Liste der Entitäten hinzugefügt. Falls das Bild bereits gesetzt war, wird das alte Bild aber nicht aus
     * der Liste entfernt.
     * @param e Das Bild des Objekts oder {@code null} falls nicht vorhanden.
     * @return {@code this}
     */
    public InventoryObject.Builder<? extends InventoryObject> masterImage(Entity e);

    /**
     * Der Name des Objects
     *
     * @param name
     * @return {@code this}
     * @throws NullPointerException wenn {@code name==null}
     * @throws IllegalArgumentException wenn {@code name.trim().isEmpty()}
     */
    public InventoryObject.Builder<? extends InventoryObject> name(String name) throws NullPointerException,
                                                                                                      IllegalArgumentException;

    /**
     * Der Preis des Objekts.
     *
     * @param price Der Preis oder {@code null} wenn nicht bekannt.
     * @return {@code this}
     */
    public InventoryObject.Builder<? extends InventoryObject> price(Money price);

    /**
     * Artikelnummer des Herstellers
     *
     * @param productNumber Artikelnummer oder {@code null} falls nicht bekannt.
     * @return {@code this}
     */
    public InventoryObject.Builder<? extends InventoryObject> productNumber(String productNumber);

    /**
     * Der Händer bei dem das Objekt gekauft wurde.
     *
     * @param contact Der Händler, oder {@code null} falls nicht bekannt.
     * @return {@code this}
     */
    public InventoryObject.Builder<? extends InventoryObject> retailer(Contact contact);

  }

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
  public Timestamp getDateOfPurchase();

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
  public List<? extends Entity> getEntities();

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
  public Timestamp getLastModified();

}
