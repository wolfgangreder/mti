/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel;

import at.reder.mti.api.utils.Fract;
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
  public static interface Builder<S extends Scale> extends BaseBuilder<Scale>
  {

    /**
     * Initialisiert alle Attribute mit den Werten von {@code scale}.
     *
     * @param scale Scale der kopiert werden soll.
     * @return {@code this}
     * @throws NullPointerException wenn {@code scale==null}
     */
    public Scale.Builder<? extends Scale> copy(Scale scale) throws NullPointerException;

    /**
     * Setzt die id.
     *
     * @param id neue id
     * @return {@code this}
     * @throws NullPointerException wenn {@code id==null}
     */
    public Scale.Builder<? extends Scale> id(UUID id) throws NullPointerException;

    /**
     * Setzt den Namen.
     *
     * @param name neuer Name
     * @return {@code this}
     * @throws NullPointerException wenn {@code name==null}
     * @throws IllegalArgumentException wenn {@code name.trim().isEmpty()}
     */
    public Scale.Builder<? extends Scale> name(String name) throws NullPointerException, IllegalArgumentException;

    /**
     * Setzt den Maßstab
     *
     * @param scale neuer Maßstab
     * @return {@code this}
     * @throws NullPointerException wenn {@code scale==null}
     * @throws IllegalArgumentException wenn {@code scale.doubleValue()<=0}
     */
    public Scale.Builder<? extends Scale> scale(Fract scale) throws NullPointerException, IllegalArgumentException;

    /**
     * Setzt die Spurweite
     *
     * @param trackWidth neue Spurweite
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code trackWidth()<=0}
     */
    public Scale.Builder<? extends Scale> trackWidth(double trackWidth) throws IllegalArgumentException;

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
    public Builder<? extends Scale> createBuilder();

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
