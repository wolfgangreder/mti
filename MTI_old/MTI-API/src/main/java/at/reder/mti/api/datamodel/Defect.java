/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Dieses Interface beschreibt einen Defekt.  <code><pre>
 * Defect.BuilderFactory factory = Lookup.getDefault().lookup(Defect.BuilderFactory.class);
 * Defect.Builder&lt? extends Defect&gt builder = factory.createBuilder();
 * // attribute setzen
 * Defect instance = builder.build();
 * </pre></code>
 *
 */
public interface Defect
{

  /**
   * Erzeugt instanzen des Interfaces {@code Defect}. Instanzen dieses Interfaces können mittels des Interfaces
   * {@link Defect.BuilderFactory} erzeugt werden (siehe auch {@link Defect}).
   *
   */
  public static interface Builder extends BaseBuilder<Defect>
  {

    /**
     * Kopiert die Eigenschaften von {@code defect}.
     *
     * @param defect
     * @return {@code this}
     * @throws NullPointerException wenn {@code defect==null}.
     */
    public Defect.Builder copy(Defect defect) throws NullPointerException;

    /**
     * Setzt die id
     *
     * @param id
     * @return {@code this}
     * @throws NullPointerException wenn {@code id==null}.
     */
    public Defect.Builder id(UUID id) throws NullPointerException;

    /**
     * Setzt das Datum
     *
     * @param date
     * @return {@code this}
     * @throws NullPointerException wenn {@code date==null}.
     */
    public Defect.Builder date(LocalDate date) throws NullPointerException;

    /**
     * Setzt die Beschreibung des Defekts
     *
     * @param descr
     * @return {@code this}
     * @throws NullPointerException wenn {@code descr==null}
     * @throws IllegalArgumentException wenn {@code descr.trim().isEmpty()}
     */
    public Defect.Builder description(String descr) throws NullPointerException, IllegalArgumentException;

  }

  /**
   * Dient zum erzeugen einer Instanz von {@link Defect.Builder}.
   *
   * @see Defect
   */
  public static interface BuilderFactory
  {

    public Defect.Builder createBuilder();

  }

  /**
   * Die eindeutige Id des Defekts.
   *
   * @return id, niemals {@code null}
   */
  public UUID getId();

  /**
   * Das Datum wann der Defekt aufgetreten ist.
   *
   * @return datum, niemals {@code null}
   */
  public LocalDate getDate();

  /**
   * Beschreibung des Defekts.
   *
   * @return Beschreibung, niemals {@code null}
   */
  public String getDescription();

}
