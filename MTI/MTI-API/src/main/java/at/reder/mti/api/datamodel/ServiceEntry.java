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

  public static interface Builder extends BaseBuilder<ServiceEntry>
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

  }

  public static interface BuilderFactory
  {

    public ServiceEntry.Builder createBuilder();

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

}
