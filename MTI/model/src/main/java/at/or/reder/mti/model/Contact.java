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

import java.net.URI;
import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import org.openide.util.Lookup;

/**
 * Dieses Interface beschreibt Lieferanten,Hersteller usw. Instanzen sollen i.A. nach folgendem Muster erstellt werden:  <code><pre>
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
   */
  public static interface Builder
  {

    /**
     * Kopiert die Eigenschaften von {@code contact}
     *
     * @param contact Contact von dem die Eigenschaften übernommen werden sollen.
     * @return {@code this}
     * @throws NullPointerException wenn {@code contact==null}
     */
    public Contact.Builder copy(Contact contact) throws NullPointerException;

    /**
     * Setzt die Id
     *
     * @param id neue Id
     * @return {@code this}
     * @throws NullPointerException wenn {@code id==null}
     */
    public Contact.Builder id(UUID id) throws NullPointerException;

    /**
     * Setzt den Namen
     *
     * @param name neuer Name
     * @return {@code this}
     * @throws NullPointerException wenn {@code name==null}
     * @throws IllegalArgumentException wenn {@code name.trim().isEmpty()}
     */
    public Contact.Builder lastName(String name) throws NullPointerException, IllegalArgumentException;

    /**
     * Setzt den Namen
     *
     * @param name neuer Name
     * @return {@code this}
     */
    public Contact.Builder firstName(String name);

    /**
     * Setzt das Adressfeld 1
     *
     * @param address1
     * @return {@code this}
     */
    public Contact.Builder address1(String address1);

    /**
     * Setzt das Adressfeld 2
     *
     * @param address2
     * @return {@code this}
     */
    public Contact.Builder address2(String address2);

    /**
     * Setzt den Ort
     *
     * @param city
     * @return {@code this}
     */
    public Contact.Builder city(String city);

    /**
     * Setzt die Postleitzahl
     *
     * @param zip
     * @return {@code this}
     */
    public Contact.Builder zip(String zip);

    /**
     * Setzt das Land wie es in einer Postanschrift verwendung findet.
     *
     * @param country
     * @return {@code this}
     */
    public Contact.Builder country(String country);

    /**
     * Setzt die Emailadresse des Verkaufs.
     *
     * @param email
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code email.getScheme()==null || !email.getScheme().equals("mailto")} und wenn der
     * Aufbau von {@code email.getSchemeSpecificPart()} keiner Emailadresse entspricht.
     * @see URI#getScheme()
     * @see URI#getSchemeSpecificPart()
     */
    public Contact.Builder emailShop(URI email) throws IllegalArgumentException;

    /**
     * Setzt die Emailadresse des Supports.
     *
     * @param email
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code email.getScheme()==null || !email.getScheme().equals("mailto")} und wenn der
     * Aufbau von {@code email.getSchemeSpecificPart()} keiner Emailadresse entspricht.
     * @see URI#getScheme()
     * @see URI#getSchemeSpecificPart()
     */
    public Contact.Builder emailService(URI email) throws IllegalArgumentException;

    /**
     * Setzt die Homepage. Es muss die URI zumindest die Form http://&lt;host&gt; oder https://&lt;host&gt; haben.
     *
     * @param www
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code www} nicht den oben genannten Mindestanformderungen entspricht.
     */
    public Contact.Builder www(URI www) throws IllegalArgumentException;

    /**
     * Setzt die Adresse des Web-Shops
     *
     * @param shopAddress
     * @return {@code  this}
     * @throws IllegalArgumentException wenn {@code shopAddress} nicht den bei {@link Contact.Builder#www(java.net.URI) }
     * genannten Mindestanforderungen entspricht.
     */
    public Contact.Builder shopAddress(URI shopAddress) throws IllegalArgumentException;

    /**
     * Telefonnummer Es sind nur Ziffern erlaubt. {@code phone} muss mit {code +}, einer oder zwei Nullen beginnen, und darf dann
     * nur Ziffern {@code <>0} enthalten.
     *
     * @param phone
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code phone} nicht den oben genannten Anforderungen entspricht.
     */
    public Contact.Builder phone1(String phone) throws IllegalArgumentException;

    /**
     * Telefonnummer
     *
     * @param phone
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code phone} nicht den bei {@link Contact.Builder#phone1(java.lang.String) }
     * genannten Mindestanforderungen entspricht.
     */
    public Contact.Builder phone2(String phone) throws IllegalArgumentException;

    /**
     * Faxnummer
     *
     * @param fax
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code fax} nicht den bei {@link Contact.Builder#phone1(java.lang.String) } genannten
     * Mindestanforderungen entspricht.
     */
    public Contact.Builder fax(String fax) throws IllegalArgumentException;

    /**
     * Setzt die Rolle des Kontakts
     *
     * @param type
     * @return {@code this}
     * @throws NullPointerException wenn {@code type==null}
     */
    public Contact.Builder addType(ContactType type) throws NullPointerException;

    /**
     * Entfernt die Rolle des Kontakts
     *
     * @param type
     * @return {@code this}
     */
    public Contact.Builder removeType(ContactType type);

    /**
     * Entfernt alle Rollen des Kontakts
     *
     * @return {@code this}
     */
    public Contact.Builder clearTypes();

    /**
     * Fügt die in {@code types} enthaltenen Rollen hinzu
     *
     * @param types
     * @return {@code this}
     * @throws NullPointerException wenn {@code types==null} oder {@code types.contains(null)}
     */
    public Contact.Builder setTypes(Collection<ContactType> types) throws NullPointerException;

    public Contact.Builder memo(String memo);

    /**
     * Fügt ein Object zum Lookup hinzu
     *
     * @param item
     * @return {@code this}
     * @throws NullPointerException wenn {@code item==null}
     */
    public Contact.Builder addLookupItem(Object item) throws NullPointerException;

    /**
     * Entfernt {@code item} vom Lookup
     *
     * @param item
     * @return {@code this}
     */
    public Contact.Builder removeLookupItem(Object item);

    /**
     * Entfernt alle Objekte für die gilt {@code clazz.isInstance(item)==true}
     *
     * @param clazz
     * @return {@code this}
     * @throws NullPointerException wenn {@code clazz==null}
     * @see Class#isInstance(java.lang.Object)
     */
    public Contact.Builder removeInstancesOfFromLookup(Class<?> clazz) throws NullPointerException;

    /**
     * Entfernt alle Objecte aus dem Lookup
     *
     * @return {@code this}
     */
    public Contact.Builder clearLookup();

    /**
     * Setzt den letzten Änderungszeitpunkg
     *
     * @param lm
     * @return
     * @throws NullPointerException wenn {@code lm==null}.
     */
    public Contact.Builder lastModified(Instant lm) throws NullPointerException;

    public Contact build() throws IllegalStateException, NullPointerException;

  }

  /**
   * Dient zum erzeugen eines Builders für Contact
   *
   * @see Contact
   */
  public static interface BuilderFactory
  {

    public Contact.Builder createContactBuilder();

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
   * @return niemals {@code null}, und immer ohne führende oder abschließende Leerzeichen.
   */
  public String getFirstName();

  /**
   * Name des Kontakts.
   *
   * @return niemals {@code null} oder leer, und immer ohne führende oder abschließende Leerzeichen.
   */
  public String getLastName();

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
   * Emailadresse des Verkaufs
   *
   * @return entweder {@code null}, oder {@link URI#getScheme()}{@code .equals("mailto")==true} und
   * {@link URI#getSchemeSpecificPart()} entspricht im Aufbau einer Emailadresse.
   */
  public URI getEmailShop();

  /**
   * Emailadresse des Supports
   *
   * @return entweder {@code null}, oder {@link URI#getScheme()}{@code .equals("mailto")==true} und
   * {@link URI#getSchemeSpecificPart()} entspricht im Aufbau einer Emailadresse.
   */
  public URI getEmailService();

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

  /**
   * Das letze Änderungsdatum des Kontakts
   *
   * @return
   */
  public Instant getLastModified();

}
