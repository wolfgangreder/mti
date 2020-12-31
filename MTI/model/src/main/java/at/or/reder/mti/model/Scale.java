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

import at.or.reder.mti.model.utils.Fract;
import java.util.UUID;

/**
 * Dieses Interface beschreibt eine Modellspurweite.<br/>
 * Instanzen dieses Interfaces sollen nach folgendem Muster erzeugt werden:<br/>
 * <code><pre>
 * Scale.BuilderFactory factory = Lookup.getDefault().lookup(Scale.BuilderFactory.class);
 * Scale.Builder<? extends Era> builder = factory.createBuilder(;
 * // werte setzen
 * Scale scale = builder.build();
 * </pre></code>
 *
 * @author wolfi
 */
public interface Scale
{

  /**
   * Builder-Object zum erzeugen neuer
   * <code>Scale</code>-Instanzen.
   *
   * @param <S>
   */
  public static interface Builder
  {

    /**
     * Initialisiert alle Attribute mit den Werten von {@code scale}.
     *
     * @param scale Scale der kopiert werden soll.
     * @return {@code this}
     * @throws NullPointerException wenn {@code scale==null}
     */
    public Scale.Builder copy(Scale scale) throws NullPointerException;

    /**
     * Setzt die id.
     *
     * @param id neue id
     * @return {@code this}
     * @throws NullPointerException wenn {@code id==null}
     */
    public Scale.Builder id(UUID id) throws NullPointerException;

    /**
     * Setzt den Namen.
     *
     * @param name neuer Name
     * @return {@code this}
     * @throws NullPointerException wenn {@code name==null}
     * @throws IllegalArgumentException wenn {@code name.trim().isEmpty()}
     */
    public Scale.Builder name(String name) throws NullPointerException, IllegalArgumentException;

    /**
     * Setzt den Maßstab
     *
     * @param scale neuer Maßstab
     * @return {@code this}
     * @throws NullPointerException wenn {@code scale==null}
     * @throws IllegalArgumentException wenn {@code scale.doubleValue()<=0}
     */
    public Scale.Builder scale(Fract scale) throws NullPointerException, IllegalArgumentException;

    /**
     * Setzt die Spurweite
     *
     * @param trackWidth neue Spurweite
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code trackWidth()<=0}
     */
    public Scale.Builder trackWidth(double trackWidth) throws IllegalArgumentException;

    public Scale build();

  }

  /**
   * Lookupobject zum erzeugen eines {@code Scale.Builder<>} Objekts.
   */
  public static interface BuilderFactory
  {

    /**
     * Erzeugt einen neuen Builder.
     *
     * @return eine neue Instanz von {@code Scale.Builder<>}
     * @see Builder
     */
    public Scale.Builder createScaleBuilder();

  }

  /**
   * Eindeutige Id der Modellspurweite
   *
   * @return Id (niemals {@code null})
   */
  public UUID getId();

  /**
   * Name der Modellspurweite.
   *
   * @return Name {niemals {@code null})
   */
  public String getName();

  /**
   * Maßstab der Modellspurweite.
   *
   * @return Maßstab (niemals {@code null})
   */
  public Fract getScale();

  /**
   * Spurweite der der Modellspurweite in mm.
   *
   * @return Spurweiter (immer >0)
   */
  public double getTrackWidth();

}
