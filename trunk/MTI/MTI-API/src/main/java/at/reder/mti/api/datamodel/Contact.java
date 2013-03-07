/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel;

import java.net.URI;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import org.openide.util.Lookup;

/**
 * Dieses Interface beschreibt Lieferanten,Hersteller usw. Instanzen sollen i.A. nach folgendem Muster erstellt werden:
 * <code><pre>
 * Contact.BuilderFactory factory = Lookup.getDefault().lookup(Contact.BuilderFactory.class);
 * Contact.Builder builder = factory.createBuilder();
 * // attribute setzen
 * Contact instance = builder.build();
 * </pre></code>
 * <em>Über das Lookup darf nur zusätzliche Funktionalität aber keine neuen Eigenschaften bereit gestellt werden.</em>
 *
 * @author wolfi
 */
public interface Contact extends Lookup.Provider
{

  /**
   * Erzeugt neue Instanzen des Interfaces {@code Contact}
   *
   * @param <C>
   */
  public static interface Builder<C extends Contact> extends BaseBuilder<C>
  {

    /**
     * Kopiert die Eigenschaften von {@code contact}
     *
     * @param contact Contact von dem die Eigenschaften übernommen werden sollen.
     * @return {@code this}
     * @throws NullPointerException wenn {@code contact==null}
     */
    public Contact.Builder<? extends Contact> copy(Contact contact) throws NullPointerException;

    /**
     * Setzt die Id
     *
     * @param id neue Id
     * @return {@code this}
     * @throws NullPointerException wenn {@code id==null}
     */
    public Contact.Builder<? extends Contact> id(UUID id) throws NullPointerException;

    /**
     * Setzt den Namen
     *
     * @param name neuer Name
     * @return {@code this}
     * @throws NullPointerException wenn {@code name==null}
     * @throws IllegalArgumentException wenn {@code name.trim().isEmpty()}
     */
    public Contact.Builder<? extends Contact> name(String name) throws NullPointerException, IllegalArgumentException;

    /**
     * Setzt das Adressfeld 1
     *
     * @param address1
     * @return {@code this}
     */
    public Contact.Builder<? extends Contact> address1(String address1);

    /**
     * Setzt das Adressfeld 2
     *
     * @param address2
     * @return {@code this}
     */
    public Contact.Builder<? extends Contact> address2(String address2);

    /**
     * Setzt den Ort
     *
     * @param city
     * @return {@code this}
     */
    public Contact.Builder<? extends Contact> city(String city);

    /**
     * Setzt die Postleitzahl
     *
     * @param zip
     * @return {@code this}
     */
    public Contact.Builder<? extends Contact> zip(String zip);

    /**
     * Setzt das Land wie es in einer Postanschrift verwendung findet.
     *
     * @param country
     * @return {@code this}
     */
    public Contact.Builder<? extends Contact> country(String country);

    /**
     * Setzt die Emailadresse.
     *
     * @param email
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code email.getScheme()==null || !email.getScheme().equals("mailto")} und wenn der
     * Aufbau von {@code email.getSchemeSpecificPart()} keiner Emailadresse entspricht.
     * @see URI#getScheme()
     * @see URI#getSchemeSpecificPart()
     */
    public Contact.Builder<? extends Contact> email(URI email) throws IllegalArgumentException;

    /**
     * Setzt die Homepage. Es muss die URI zumindes die Form http://<host> oder https://<host> haben.
     *
     * @param www
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code www} nicht den oben genannten Mindestanformderungen entspricht.
     */
    public Contact.Builder<? extends Contact> www(URI www) throws IllegalArgumentException;

    /**
     * Setzt die Adresse des Web-Shops
     *
     * @param shopAddress
     * @return {@code  this}
     * @throws IllegalArgumentException wenn {@code shopAddress} nicht den bei {@link Contact.Builder#www(java.net.URI) }
     * genannten Mindestanforderungen entspricht.
     */
    public Contact.Builder<? extends Contact> shopAddress(URI shopAddress) throws IllegalArgumentException;

    /**
     * Telefonnummer Es sind nur Ziffern erlaubt. {@code phone} muss mit {code +}, einer oder zwei Nullen beginnen, und darf dann
     * nur Ziffern {@code <>0} enthalten.
     *
     * @param phone
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code phone} nicht den oben genannten Anforderungen entspricht.
     */
    public Contact.Builder<? extends Contact> phone1(String phone) throws IllegalArgumentException;

    /**
     * Telefonnummer
     *
     * @param phone
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code phone} nicht den bei {@link Contact.Builder#phone1(java.lang.String) }
     * genannten Mindestanforderungen entspricht.
     */
    public Contact.Builder<? extends Contact> phone2(String phone) throws IllegalArgumentException;

    /**
     * Faxnummer
     *
     * @param fax
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code fax} nicht den bei {@link Contact.Builder#phone1(java.lang.String) } genannten
     * Mindestanforderungen entspricht.
     */
    public Contact.Builder<? extends Contact> fax(String fax) throws IllegalArgumentException;

    /**
     * Setzt die Rolle des Kontakts
     *
     * @param type
     * @return {@code this}
     * @throws NullPointerException wenn {@code type==null}
     */
    public Contact.Builder<? extends Contact> addType(ContactType type) throws NullPointerException;

    /**
     * Entfernt die Rolle des Kontakts
     *
     * @param type
     * @return {@code this}
     */
    public Contact.Builder<? extends Contact> removeType(ContactType type);

    /**
     * Entfernt alle Rollen des Kontakts
     *
     * @return {@code this}
     */
    public Contact.Builder<? extends Contact> clearTypes();

    /**
     * Fügt die in {@code types} enthaltenen Rollen hinzu
     *
     * @param types
     * @return {@code this}
     * @throws NullPointerException wenn {@code types==null} oder {@code types.contains(null)}
     */
    public Contact.Builder<? extends Contact> setTypes(Collection<ContactType> types) throws NullPointerException;

    public Contact.Builder<? extends Contact> memo(String memo);

    /**
     * Fügt ein Object zum Lookup hinzu
     *
     * @param item
     * @return {@code this}
     * @throws NullPointerException wenn {@code item==null}
     */
    public Contact.Builder<? extends Contact> addLookupItem(Object item) throws NullPointerException;

    /**
     * Entfernt {@code item} vom Lookup
     *
     * @param item
     * @return {@code this}
     */
    public Contact.Builder<? extends Contact> removeLookupItem(Object item);

    /**
     * Entfernt alle Objekte für die gilt {@code clazz.isInstance(item)==true}
     *
     * @param clazz
     * @return {@code this}
     * @throws NullPointerException wenn {@code clazz==null}
     * @see Class#isInstance(java.lang.Object)
     */
    public Contact.Builder<? extends Contact> removeInstancesOfFromLookup(Class<?> clazz) throws NullPointerException;

    /**
     * Entfernt alle Objecte aus dem Lookup
     *
     * @return {@code this}
     */
    public Contact.Builder<? extends Contact> clearLookup();

  }

  /**
   * Dient zum erzeugen eines Builders für Contact
   *
   * @see Contact
   */
  public static interface BuilderFactory
  {

    public Builder<? extends Contact> createBuilder();

  }

  /**
   * Eindeutige Id.
   *
   * @return niemals {@code null}.
   */
  public UUID getId();

  /**
   * Name des Kontakts.
   *
   * @return niemals {@code null} oder leer, und immer ohne führende oder abschließende Leerzeichen.
   */
  public String getName();

  /**
   * Addressfeld 1
   *
   * @return niemals {@code null}, und immer ohne führende oder abschließende Leerzeichen.
   */
  public String getAddress1();

  /**
   * Adressfeld 2
   *
   * @return niemals {@code null}, und immer ohne führende oder abschließende Leerzeichen.
   */
  public String getAddress2();

  /**
   * Postleitzahl
   *
   * @return niemals {@code null}, und immer ohne führende oder abschließende Leerzeichen.
   */
  public String getZip();

  /**
   * Ort
   *
   * @return niemals {@code null}, und immer ohne führende oder abschließende Leerzeichen.
   */
  public String getCity();

  /**
   * Land
   *
   * @return niemals {@code null}, und immer ohne führende oder abschließende Leerzeichen.
   */
  public String getCountry();

  /**
   * Emailadresse
   *
   * @return entweder {@code null}, oder {@link URI#getScheme()}{@code .equals("mailto")==true} und
   * {@link URI#getSchemeSpecificPart()} entspricht im Aufbau einer Emailadresse.
   */
  public URI getEmail();

  /**
   * Homepage des Kontakts
   *
   * @return entweder {@code null}, oder eine Korrekte Adresse mit dem Scheme http oder https
   */
  public URI getWWW();

  /**
   * Adresse des Webshops
   *
   * @return entweder {@code null}, oder eine Korrekte Adresse mit dem Scheme http oder https
   */
  public URI getShopAddress();

  /**
   * Das Memofeld des Kontaktes.
   *
   * @return niemals {@code null}
   */
  public String getMemo();

  /**
   * Telefonnummer des Kontakts
   *
   * @return entwender {@code null} oder die der String entspricht dem bei {@link Contact.Builder#phone1(java.lang.String) }
   * spezifierten Format.
   */
  public String getPhone1();

  /**
   * Telefonnummer des Kontakts
   *
   * @return entwender {@code null} oder die der String entspricht dem bei {@link Contact.Builder#phone1(java.lang.String) }
   * spezifierten Format.
   */
  public String getPhone2();

  /**
   * Faxnummer des Kontakts
   *
   * @return entwender {@code null} oder die der String entspricht dem bei {@link Contact.Builder#phone1(java.lang.String) }
   * spezifierten Format.
   */
  public String getFax();

  /**
   * Rollen des Kontakts
   *
   * @return niemals {@code null} oder leer.
   */
  public Set<ContactType> getTypes();

}
