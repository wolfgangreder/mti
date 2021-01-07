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
 * Ein eintrag ins &qout;Servicebuch&quout; eines {@link ServiceableObject}.
 *
 * @author wolfi
 */
public interface ServiceEntry
{

  public static interface Builder
  {

    public ServiceEntry.Builder copy(ServiceEntry se) throws NullPointerException;

    public ServiceEntry.Builder id(UUID id) throws NullPointerException;

    public ServiceEntry.Builder description(String description);

    public ServiceEntry.Builder date(LocalDate date) throws NullPointerException;

    public ServiceEntry.Builder addDefect(Defect def) throws NullPointerException;

    public ServiceEntry.Builder removeDefect(Defect def);

    public ServiceEntry.Builder clearDefects();

    public ServiceEntry.Builder addDefects(Collection<? extends Defect> defects) throws
            NullPointerException, IllegalArgumentException;

    public ServiceEntry.Builder addSparePart(UsedSparePart sp) throws NullPointerException;

    public ServiceEntry.Builder removeSparePart(UsedSparePart sp);

    public ServiceEntry.Builder clearSpareParts();

    public ServiceEntry.Builder addSpareParts(Collection<? extends UsedSparePart> sp) throws
            NullPointerException,
            IllegalArgumentException;

    public ServiceEntry.Builder repairer(Contact contact);

    public ServiceEntry.Builder addEntity(Entity e);

    public ServiceEntry.Builder addEntities(Collection<? extends Entity> e);

    public ServiceEntry.Builder removeEntity(Entity e);

    public ServiceEntry.Builder removeEntities(Collection<? extends Entity> e);

    public ServiceEntry.Builder clearEntities();

    public ServiceEntry build();

  }

  public static interface BuilderFactory
  {

    public ServiceEntry.Builder createServiceEntryBuilder();

  }

  /**
   * Id des Eintrags.
   *
   * @return id, niemals {@code null}.
   */
  public UUID getId();

  /**
   * Beschreibung der durchgeführten Arbeiten
   *
   * @return niemals {@code null}
   */
  public String getDescription();

  /**
   * Datum der Durchführung.
   *
   * @return ein datum, niemals {@code null}
   */
  public LocalDate getDate();

  /**
   * Liste der Defekte die behoben wurden.
   *
   * @return Liste der Defekte, niemals {@code null}
   */
  public List<Defect> getDefectsResolved();

  /**
   * Die Verwendeten Erstatzteile.
   *
   * @return
   */
  public List<UsedSparePart> getPartsUsed();

  public List<Entity> getEntities();

  public Contact getRepaierer();

}
