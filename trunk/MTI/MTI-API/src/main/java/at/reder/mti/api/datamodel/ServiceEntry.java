/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel;

import at.reder.mti.api.utils.Timestamp;
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

  public static interface Builder<SE extends ServiceEntry> extends BaseBuilder<SE>
  {

    public ServiceEntry.Builder<? extends ServiceEntry> copy(ServiceEntry se) throws NullPointerException;

    public ServiceEntry.Builder<? extends ServiceEntry> id(UUID id) throws NullPointerException;

    public ServiceEntry.Builder<? extends ServiceEntry> description(String description);

    public ServiceEntry.Builder<? extends ServiceEntry> date(Timestamp date) throws NullPointerException;

    public ServiceEntry.Builder<? extends ServiceEntry> addDefect(Defect def) throws NullPointerException;

    public ServiceEntry.Builder<? extends ServiceEntry> removeDefect(Defect def);

    public ServiceEntry.Builder<? extends ServiceEntry> clearDefects();

    public ServiceEntry.Builder<? extends ServiceEntry> addDefects(Collection<? extends Defect> defects) throws
            NullPointerException, IllegalArgumentException;

    public ServiceEntry.Builder<? extends ServiceEntry> addSparePart(UsedSparePart sp) throws NullPointerException;

    public ServiceEntry.Builder<? extends ServiceEntry> removeSparePart(UsedSparePart sp);

    public ServiceEntry.Builder<? extends ServiceEntry> clearSpareParts();

    public ServiceEntry.Builder<? extends ServiceEntry> addSpareParts(Collection<UsedSparePart> sp) throws NullPointerException,
                                                                                                           IllegalArgumentException;

  }

  public static interface BuilderFactory
  {

    public ServiceEntry.Builder<? extends ServiceEntry> createBuilder();

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
  public Timestamp getDate();

  /**
   * Liste der Defekte die behoben wurden.
   *
   * @return Liste der Defekte, niemals {@code null}
   */
  public List<? extends Defect> getDefectsResolved();

  /**
   * Die Verwendeten Erstatzteile.
   *
   * @return
   */
  public List<UsedSparePart> getPartsUsed();

}
