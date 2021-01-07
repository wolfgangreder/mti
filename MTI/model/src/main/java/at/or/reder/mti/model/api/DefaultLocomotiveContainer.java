/*
 * Copyright 2021 Wolfgang Reder.
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
package at.or.reder.mti.model.api;

import at.or.reder.dcc.util.Predicates;
import at.or.reder.dcc.util.Utils;
import at.or.reder.mti.model.Contact;
import at.or.reder.mti.model.Defect;
import at.or.reder.mti.model.Entity;
import at.or.reder.mti.model.Locomotive;
import at.or.reder.mti.model.ServiceEntry;
import at.or.reder.mti.model.SparePart;
import at.or.reder.mti.model.UsedSparePart;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author Wolfgang Reder
 */
public final class DefaultLocomotiveContainer implements LocomotiveContainer
{

  private final List<Locomotive> locomotives;
  private Map<UUID, Entity> entities;
  private Map<UUID, Contact> contacts;
  private Map<UUID, ServiceEntry> serviceEntries;
  private Map<UUID, Defect> defects;
  private Map<UUID, UsedSparePart> usedSpareparts;
  private Map<UUID, SparePart> spareParts;

  public DefaultLocomotiveContainer(Collection<? extends Locomotive> locomotives)
  {
    this.locomotives = Utils.copyToUnmodifiableList(locomotives,
                                                    Predicates::isNotNull);
  }

  @Override
  public List<Locomotive> getLocomotives()
  {
    return locomotives;
  }

  @Override
  public synchronized Map<UUID, Entity> getEntities()
  {
    if (entities == null) {
      Map<UUID, Entity> tmp = new HashMap<>();
      getLocomotives().stream().flatMap((l) -> l.getEntities().stream()).forEach((e) -> tmp.put(e.getId(),
                                                                                                e));
      getLocomotives().stream().flatMap((l) -> l.getDefect().stream()).flatMap((d) -> d.getEntities().stream()).forEach(
              (e) -> tmp.put(e.getId(),
                             e));
      getServiceEntry().values().stream().flatMap((d) -> d.getEntities().stream()).forEach((e) -> tmp.put(e.getId(),
                                                                                                          e));
      entities = Collections.unmodifiableMap(tmp);
    }
    return entities;
  }

  @Override
  public synchronized Map<UUID, Contact> getContacts()
  {
    if (contacts == null) {
      Map<UUID, Contact> tmp = new HashMap<>();
      getLocomotives().stream().map((l) -> l.getManufacturer()).forEach((e) -> tmp.put(e.getId(),
                                                                                       e));
      getLocomotives().stream().map((l) -> l.getRetailer()).forEach((e) -> tmp.put(e.getId(),
                                                                                   e));
      getServiceEntry().values().stream().map(ServiceEntry::getRepaierer).forEach((e) -> tmp.put(e.getId(),
                                                                                                 e));
      contacts = Collections.unmodifiableMap(tmp);
    }
    return contacts;
  }

  @Override
  public synchronized Map<UUID, ServiceEntry> getServiceEntry()
  {
    if (serviceEntries == null) {
      Map<UUID, ServiceEntry> tmp = new HashMap<>();
      getLocomotives().stream().flatMap((l) -> l.getServiceEntries().stream()).forEach((e) -> tmp.put(e.getId(),
                                                                                                      e));
      serviceEntries = Collections.unmodifiableMap(tmp);
    }
    return serviceEntries;
  }

  @Override
  public synchronized Map<UUID, SparePart> getSpareParts()
  {
    if (spareParts == null) {
      Map<UUID, SparePart> tmp = new HashMap<>();
      getServiceEntry().values().stream().flatMap((se) -> se.getPartsUsed().stream()).map(UsedSparePart::getPart).forEach(
              (e) -> tmp.put(e.getId(),
                             e));
      spareParts = Collections.unmodifiableMap(tmp);
    }
    return spareParts;
  }

}
