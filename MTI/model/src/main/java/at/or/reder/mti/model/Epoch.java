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

import at.or.reder.dcc.util.Localizable;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.netbeans.api.annotations.common.NonNull;

/**
 * Diese Klasse beschreibt eine Modelepoche. Instanzen dieses Interfaces sollen nach folgendem Muster erzeugt werden:<br/>
 * <code><pre>
 * Epoch.BuilderFactory factory = Lookup.getDefault().lookup(Epoch.BuilderFactory.class);
 * Epoch.Builder<? extends Epoch> builder = factory.createBuilder(;
 * // werte setzen
 * Epoch era = builder.build();
 * </pre></code>
 *
 */
public interface Epoch
{

  public static final UUID EPOCH_UNKNOWN_ID = UUID.fromString("2988d848-3f7a-41fe-b125-feff67214033");
  public static final UUID EPOCH_1_ID = UUID.fromString("4b4bc6cf-3464-4034-aedf-5fd43734b4ca");
  public static final UUID EPOCH_2_ID = UUID.fromString("7ba283c9-3ec5-4a4c-8b9c-cbc19cdd7c83");
  public static final UUID EPOCH_3_ID = UUID.fromString("cb347731-14ba-42bb-bc31-f2239e5fdb68");
  public static final UUID EPOCH_4_ID = UUID.fromString("97d877d6-f2b8-4699-a623-3b95ca79c91d");
  public static final UUID EPOCH_5_ID = UUID.fromString("606ce236-ef98-4aa4-8d0b-457a4cba4719");
  public static final UUID EPOCH_6_ID = UUID.fromString("d0b7cc9d-e415-4d4e-a55d-b88e1c07b842");

  /**
   * BaseBuilder-Object zum erzeugen neuer <code>Epoch</code>-Instanzen.
   *
   */
  public static interface Builder
  {

    /**
     * Kopiert die Werte von {@code toCopy}.
     *
     * @param toCopy Epoch die kopiert werden soll.
     * @return {@code this}
     * @throws NullPointerException wenn {@code toCopy==null}.
     */
    public Builder copy(Epoch toCopy) throws NullPointerException;

    /**
     * Setzt die id der Epoch.
     *
     * @param id neue id
     * @return {@code this}
     * @throws NullPointerException wenn {@code id==null}
     */
    public Builder id(UUID id) throws NullPointerException;

    /**
     * Setzt den namen der Epoch.
     *
     * @param lang sprache
     * @param name neuer name.
     * @return {@code this}
     * @throws NullPointerException wenn {@code name==null}
     * @throws IllegalArgumentException wenn {@code name.trim().isEmpty()}
     */
    public Builder name(String lang,
                        String name) throws IllegalArgumentException, NullPointerException;

    public Builder name(@NonNull Localizable<? extends String> names) throws NullPointerException;

    /**
     * Setzt das erste Jahr der Epoch.
     *
     * @param yearFrom
     * @return {@code this}
     */
    public Builder yearFrom(int yearFrom);

    /**
     * Setzt das letze Jahr der Epoch.
     *
     * @param yearTo letztes Jahr oder {@code null} falls die Epoch (noch) nicht abgeschlossen ist.
     * @return {@code this}
     */
    public Builder yearTo(Integer yearTo);

    /**
     * Fügt ein neues Land zur Liste der Länder hinzu für die die Epoch gültig ist.
     *
     * @param country ISO3166 Ländercode (zwei Großbuchstaben) oder ein dreistelliger UN M.49 Zifferncode.
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code country} nicht der Spezifikation entspricht.
     * @throws NullPointerException wenn {@code code==null}
     */
    public Builder addCountry(String country) throws IllegalArgumentException, NullPointerException;

    /**
     * Entfernt das Land {@code country} aus der Liste.
     *
     * @param country Land das entfernt werden soll
     * @return {@code this}
     */
    public Builder removeCountry(String country);

    /**
     * Entfernt alle Länder aus der Liste.
     *
     * @return {@code this}
     */
    public Builder clearCountries();

    /**
     * Fügt mehrere Länder zur Liste hinzu.
     *
     * @param countries Liste mit Ländern.
     * @return {@code this}
     * @throws IllegalArgumentException siehe {@link Builder#addCountry(java.lang.String)}
     * @throws NullPointerException siehe {@link Builder#addCountry(java.lang.String)}
     * @see Builder#addCountry(java.lang.String)
     */
    public Builder addCountries(Collection<String> countries) throws IllegalArgumentException, NullPointerException;

    /**
     * Setzt den Kommentar.
     *
     * @param lang Sprache
     * @param comment Neuer Kommentar
     * @return {@code this}
     */
    public Builder comment(String lang,
                           String comment);

    public Builder comment(Localizable<? extends String> comments);

    public Epoch build();

  }

  /**
   * Lookupobject zum erzeugen eines {@code Epoch.Builder<>} Objekts.
   */
  public static interface BuilderFactory
  {

    /**
     * Erzeugt einen neuen Builder.
     *
     * @return eine neue Instanz von {@code Epoch.Builder<>}
     * @see Builder
     */
    public Epoch.Builder createEpochBuilder();

    public List<Epoch> getDefaultValues() throws IOException;

  }

  /**
   * Eindeutige id der Modellepoche.
   *
   * @return id der Modellepoche
   */
  public UUID getId();

  /**
   * Lokalisierter Name der Modellepoche.
   *
   * @return Name der Epoche
   */
  public Localizable<String> getName();

  /**
   * Erstes Jahr der Epoche
   *
   * @return
   */
  public int getYearFrom();

  /**
   * Letztes Jahr der Epoche.
   *
   * @return Letzes Jahr der Epoche, oder {@code null} wenn die Epoche (noch) nicht abgeschlossen ist.
   */
  public Integer getYearTo();

  /**
   * Wenn das Set nicht leer ist, ist die Epoche nur für diese Länder gültig.
   *
   * @return Set mit Ländern für die diese Epoche gilt. Niemals {@code null}.
   */
  public Set<String> getCountries();

  /**
   * Lokalisierter Kommentar zur Epoche.
   *
   * @return Kommentar zur Epoche. Niemals {@code null}.
   */
  public Localizable<String> getComment();

}
