/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import org.openide.util.Lookup;

/**
 * Klasse die Dokumente und Einstellungen.
 *
 * <code><pre>
 * Entity.BuilderFactory factory = Lookup.getDefault().lookup(DefEntityect.BuilderFactory.class);
 * Entity.Builder&lt? extends Entity&gt builder = factory.createBuilder();
 * // attribute setzen
 * Entity instance = builder.build();
 * </pre></code>
 *
 * @author wolfi
 */
public interface Entity extends Lookup.Provider
{

  public static interface Data
  {

    public Path getTmpFile();

  }

  public static interface Builder<E extends Entity> extends BaseBuilder<E>
  {

    /**
     * Kopiert die Eigenschaften von {@code e}. Die Daten werden dabei nicht kopiert
     *
     * @param e
     * @return {@code this}
     * @throws NullPointerException wenn {@code e==null}.
     */
    public Entity.Builder<? extends Entity> copy(Entity e) throws NullPointerException;

    /**
     * Setzt die Beschreibung
     *
     * @param descr
     * @return {@code this}
     */
    public Entity.Builder<? extends Entity> description(String descr);

    /**
     * Setzt den original Dateinamen
     *
     * @param fileName Der Dateiname oder {@code null} falls nicht bekannt.
     * @return {@code this}
     */
    public Entity.Builder<? extends Entity> fileName(String fileName);

    /**
     * Setzt die Art der Entity
     *
     * @param kind
     * @return {@code this}
     * @throws NullPointerException wenn {@code kind==null}
     * @see EntityKind
     */
    public Entity.Builder<? extends Entity> kind(EntityKind kind) throws NullPointerException;

    /**
     * Setzt den Mime-Typ
     *
     * @param mimeType
     * @return {@code this}
     * @throws NullPointerException wenn {@code mimeType==null}
     * @throws IllegalArgumentException wenn {@code mimeType.trim().isEmpty()}.
     */
    public Entity.Builder<? extends Entity> mimeType(String mimeType) throws NullPointerException, IllegalArgumentException;

    /**
     * Grösse der Entität oder -1 falls nicht bekannt
     *
     * @param size
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code size<-1}
     */
    public Entity.Builder<? extends Entity> size(int size);

    /**
     * Die URI mit der der Inhalt der Entität geladen werden kann.
     *
     * @param uri
     * @return {@code this}
     * @throws NullPointerException wenn {@code uri==null}
     */
    public Entity.Builder<? extends Entity> uri(URI uri) throws NullPointerException;

    /**
     * Setzt die Daten der Entity
     *
     * @param tmpFile
     * @return {@code this}
     */
    public Entity.Builder<? extends Entity> data(Path tmpFile);

  }

  public static interface BuilderFactory
  {

    public Entity.Builder<? extends Entity> createBuilder();

  }

  /**
   * Art des Entities
   *
   * @return
   */
  public EntityKind getKind();

  /**
   * Die Id der Enität.
   *
   * @return {@link Entity#getURI() }
   */
  public URI getId();

  /**
   * URI des externen Dokuments. Diese Feld wird als Id verwendet
   *
   * @return
   */
  public URI getURI();

  /**
   * Beschreibung
   *
   * @return niemals {@code null}
   */
  public String getDescription();

  /**
   * Mime-Typ
   *
   * @return
   */
  public String getMimeType();

  /**
   * Der original Dateiname
   *
   * @return
   */
  public String getFileName();

  /**
   * Grösse in bytes oder -1 falls nicht bekannt.
   *
   * @return
   */
  public int getSize();

  /**
   * Liest die Daten
   *
   * @return
   */
  public InputStream getData() throws IOException;

}
