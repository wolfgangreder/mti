/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Defect;
import at.reder.mti.api.datamodel.ServiceEntry;
import at.reder.mti.api.datamodel.UsedSparePart;
import at.reder.mti.api.datamodel.xml.XServiceEntry;
import at.reder.mti.api.utils.MTIUtils;
import at.reder.mti.api.utils.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author wolfi
 */
@ServiceProvider(service = ServiceEntry.BuilderFactory.class)
public final class DefaultServiceEntryBuilderFactory implements ServiceEntry.BuilderFactory
{

  @XmlSeeAlso(value = XServiceEntry.class)
  @XmlJavaTypeAdapter(value = XServiceEntry.Adapter.class)
  public static final class DefaultServiceEntry implements ServiceEntry
  {

    private final UUID id;
    private final Timestamp date;
    private final List<? extends Defect> defects;
    private final List<UsedSparePart> parts;
    private final String description;

    private DefaultServiceEntry(UUID id,
                                Timestamp date,
                                String description,
                                Collection<? extends Defect> defects,
                                Collection<UsedSparePart> parts)
    {
      this.id = id;
      this.date = date;
      if (defects.isEmpty()) {
        this.defects = Collections.emptyList();
      } else {
        this.defects = Collections.unmodifiableList(new ArrayList<>(defects));
      }
      if (parts.isEmpty()) {
        this.parts = Collections.emptyList();
      } else {
        this.parts = Collections.unmodifiableList(new ArrayList<>(parts));
      }
      this.description = description;
    }

    @Override
    public UUID getId()
    {
      return id;
    }

    @Override
    public Timestamp getDate()
    {
      return date;
    }

    @Override
    public String getDescription()
    {
      return description;
    }

    @Override
    public List<? extends Defect> getDefectsResolved()
    {
      return defects;
    }

    @Override
    public List<UsedSparePart> getPartsUsed()
    {
      return parts;
    }

    @Override
    public int hashCode()
    {
      int hash = 5;
      hash = 61 * hash + Objects.hashCode(this.id);
      return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      final DefaultServiceEntry other = (DefaultServiceEntry) obj;
      if (!Objects.equals(this.id, other.id)) {
        return false;
      }
      return true;
    }

  }

  public static final class ServiceEntryBuilder implements ServiceEntry.Builder<ServiceEntry>
  {

    private UUID id;
    private Timestamp date;
    private final List<Defect> defects = new LinkedList<>();
    private final List<UsedSparePart> parts = new LinkedList<>();
    private String description;

    @Override
    public ServiceEntry.Builder<? extends ServiceEntry> copy(ServiceEntry se) throws NullPointerException
    {
      if (se == null) {
        throw new NullPointerException("serviceEntry==null");
      }
      this.id = se.getId();
      this.date = se.getDate();
      this.description = se.getDescription();
      defects.clear();
      defects.addAll(se.getDefectsResolved());
      parts.clear();
      parts.addAll(se.getPartsUsed());
      return this;
    }

    @Override
    public ServiceEntry.Builder<? extends ServiceEntry> id(UUID id) throws NullPointerException
    {
      if (id == null) {
        throw new NullPointerException("id==null");
      }
      this.id = id;
      return this;
    }

    @Override
    public ServiceEntry.Builder<? extends ServiceEntry> date(Timestamp date) throws NullPointerException
    {
      if (date == null) {
        throw new NullPointerException("date==null");
      }
      this.date = MTIUtils.getDayPart(date);
      return this;
    }

    @Override
    public ServiceEntry.Builder<? extends ServiceEntry> description(String description)
    {
      if (description == null) {
        this.description = "";
      } else {
        this.description = description;
      }
      return this;
    }

    @Override
    public ServiceEntry.Builder<? extends ServiceEntry> addDefect(Defect def) throws NullPointerException
    {
      if (def == null) {
        throw new NullPointerException("defect==null");
      }
      defects.add(def);
      return this;
    }

    @Override
    public ServiceEntry.Builder<? extends ServiceEntry> removeDefect(Defect def)
    {
      if (def != null) {
        defects.remove(def);
      }
      return this;
    }

    @Override
    public ServiceEntry.Builder<? extends ServiceEntry> clearDefects()
    {
      defects.clear();
      return this;
    }

    @Override
    public ServiceEntry.Builder<? extends ServiceEntry> addDefects(
            Collection<? extends Defect> defects) throws NullPointerException,
                                                         IllegalArgumentException
    {
      if (defects == null) {
        throw new NullPointerException("defects==null");
      }
      if (defects.contains(null)) {
        throw new IllegalArgumentException("defects contains null");
      }
      this.defects.addAll(defects);
      return this;
    }

    @Override
    public ServiceEntry.Builder<? extends ServiceEntry> addSparePart(UsedSparePart sp) throws NullPointerException
    {
      if (sp == null) {
        throw new NullPointerException("sparePart==null");
      }
      parts.add(sp);
      return this;
    }

    @Override
    public ServiceEntry.Builder<? extends ServiceEntry> removeSparePart(UsedSparePart sp)
    {
      if (sp != null) {
        parts.remove(sp);
      }
      return this;
    }

    @Override
    public ServiceEntry.Builder<? extends ServiceEntry> clearSpareParts()
    {
      parts.clear();
      return this;
    }

    @Override
    public ServiceEntry.Builder<? extends ServiceEntry> addSpareParts(
            Collection<UsedSparePart> sp) throws NullPointerException,
                                                 IllegalArgumentException
    {
      if (sp == null) {
        throw new NullPointerException("parts==null");
      }
      if (sp.contains(null)) {
        throw new IllegalArgumentException("parts contains null");
      }
      parts.addAll(sp);
      return this;
    }

    @Override
    public ServiceEntry build() throws IllegalStateException
    {
      if (id == null) {
        throw new IllegalStateException("id==null");
      }
      if (date == null) {
        throw new IllegalStateException("date==null");
      }
      return new DefaultServiceEntry(id, date, description, defects, parts);
    }

    private static final Collection<? extends Class<? extends ServiceEntry>> implementingClasses = Collections.singleton(
            DefaultServiceEntry.class);

    @Override
    public Collection<? extends Class<? extends ServiceEntry>> getImplementingClasses()
    {
      return implementingClasses;
    }

    @Override
    public Class<?> getXmlClass()
    {
      return XServiceEntry.class;
    }

  }

  @Override
  public ServiceEntry.Builder<? extends ServiceEntry> createBuilder()
  {
    return new ServiceEntryBuilder();
  }

}
