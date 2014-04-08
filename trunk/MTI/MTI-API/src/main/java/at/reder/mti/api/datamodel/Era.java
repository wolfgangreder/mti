/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 * Diese Klasse beschreibt eine Modelepoche. Instanzen dieses Interfaces sollen nach folgendem Muster erzeugt werden:<br/>
 * <code><pre>
 * Era.BuilderFactory factory = Lookup.getDefault().lookup(Era.BuilderFactory.class);
 * Era.Builder<? extends Era> builder = factory.createBuilder(;
 * // werte setzen
 * Era era = builder.build();
 * </pre></code>
 *
 */
public interface Era
{

  /**
   * BaseBuilder-Object zum erzeugen neuer <code>Era</code>-Instanzen.
   *
   */
  public static interface Builder extends BaseBuilder<Era>
  {

    /**
     * Kopiert die Werte von {@code toCopy}.
     *
     * @param toCopy Era die kopiert werden soll.
     * @return {@code this}
     * @throws NullPointerException wenn {@code toCopy==null}.
     */
    public Builder copy(Era toCopy) throws NullPointerException;

    /**
     * Setzt die id der Era.
     *
     * @param id neue id
     * @return {@code this}
     * @throws NullPointerException wenn {@code id==null}
     */
    public Builder id(UUID id) throws NullPointerException;

    /**
     * Setzt den namen der Era.
     *
     * @param name neuer name.
     * @return {@code this}
     * @throws NullPointerException wenn {@code name==null}
     * @throws IllegalArgumentException wenn {@code name.trim().isEmpty()}
     */
    public Builder name(String name) throws IllegalArgumentException, NullPointerException;

    /**
     * Setzt das erste Jahr der Era.
     *
     * @param yearFrom
     * @return {@code this}
     */
    public Builder yearFrom(int yearFrom);

    /**
     * Setzt das letze Jahr der Era.
     *
     * @param yearTo letztes Jahr oder {@code null} falls die Era (noch) nicht abgeschlossen ist.
     * @return {@code this}
     */
    public Builder yearTo(Integer yearTo);

    /**
     * Fügt ein neues Land zur Liste der Länder hinzu für die die Era gültig ist.
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
     * @param comment Neuer Kommentar
     * @return {@code this}
     */
    public Builder comment(String comment);

  }

  /**
   * Lookupobject zum erzeugen eines {@code Era.Builder<>} Objekts.
   */
  public static interface BuilderFactory
  {

    /**
     * Erzeugt einen neuen Builder.
     *
     * @return eine neue Instanz von {@code Era.Builder<>}
     * @see Builder
     */
    public Builder createBuilder();

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
  public String getName();

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
  public String getComment();

}
