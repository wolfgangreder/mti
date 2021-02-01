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
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.UUID;
import org.openide.util.Lookup;

/**
 * Klasse die Dokumente und Einstellungen.
 *
 * <code><pre>
 * Entity.BuilderFactory factory = Factories.getEntityBuilderFactory();
 * Entity.Builder builder = factory.createBuilder();
 * // attribute setzen
 * Entity instance = builder.build();
 * </pre></code>
 *
 */
public interface Entity extends Lookup.Provider
{

  public static interface Data
  {

    public Path getTmpFile();

  }

  public static interface Builder
  {

    /**
     * Kopiert die Eigenschaften von {@code e}. Die Daten werden dabei nicht kopiert
     *
     * @param e
     * @return {@code this}
     * @throws NullPointerException wenn {@code e==null}.
     */
    public Entity.Builder copy(Entity e) throws NullPointerException;

    public Entity.Builder id(UUID id);

    /**
     * Setzt die Beschreibung
     *
     * @param descr
     * @return {@code this}
     */
    public Entity.Builder description(Localizable<? extends String> descr);

    public Entity.Builder description(String lang,
                                      String desc);

    /**
     * Setzt den original Dateinamen
     *
     * @param fileName Der Dateiname oder {@code null} falls nicht bekannt.
     * @return {@code this}
     */
    public Entity.Builder fileName(String fileName);

    /**
     * Setzt die Art der Entity
     *
     * @param kind
     * @return {@code this}
     * @throws NullPointerException wenn {@code kind==null}
     * @see EntityKind
     */
    public Entity.Builder kind(EntityKind kind) throws NullPointerException;

    /**
     * Setzt den Mime-Typ
     *
     * @param mimeType
     * @return {@code this}
     * @throws NullPointerException wenn {@code mimeType==null}
     * @throws IllegalArgumentException wenn {@code mimeType.trim().isEmpty()}.
     */
    public Entity.Builder mimeType(String mimeType) throws NullPointerException, IllegalArgumentException;

    /**
     * Grösse der Entität oder -1 falls nicht bekannt
     *
     * @param size
     * @return {@code this}
     * @throws IllegalArgumentException wenn {@code size<-1}
     */
    public Entity.Builder size(long size);

    /**
     * Setzt die Daten der Entity
     *
     * @param tmpFile
     * @return {@code this}
     */
    public Entity.Builder data(URL tmpFile);

    public Entity.Builder origin(URL origin);

    public Entity build();

  }

  public static interface BuilderFactory
  {

    public Entity.Builder createEntityBuilder();

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
   * @return id
   */
  public UUID getId();

  /**
   * Beschreibung
   *
   * @return niemals {@code null}
   */
  public Localizable<String> getDescription();

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
  public long getSize();

  /**
   * Liest die Daten
   *
   * @return
   * @throws java.io.IOException
   */
  public InputStream getData() throws IOException;

  /**
   * URL. Kann auch null sein.
   *
   * @return url
   */
  public URL getDataURL();

  /**
   * Der Ursprung der Entity. Kann z.B. die DownloadURL sein, oder {@code null} wenn es ein eigenes Werk ist.
   *
   * @return ursprung oder {@code null}
   */
  public URL getOrigin();

}
