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

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
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
  public static interface Builder
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

    public Defect.Builder addEntity(Entity e);

    public Defect.Builder addEntities(Collection<? extends Entity> e);

    public Defect.Builder removeEntity(Entity e);

    public Defect.Builder removeEntities(Collection<? extends Entity> e);

    public Defect.Builder clearEntities();

    public Defect build() throws NullPointerException, IllegalStateException;

  }

  /**
   * Dient zum erzeugen einer Instanz von {@link Defect.Builder}.
   *
   * @see Defect
   */
  public static interface BuilderFactory
  {

    public Defect.Builder createDefectBuilder();

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

  public List<Entity> getEntities();

}
