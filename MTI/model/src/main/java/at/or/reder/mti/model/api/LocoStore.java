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

import at.or.reder.mti.model.Entity;
import at.or.reder.mti.model.Locomotive;
import at.or.reder.mti.model.QuickInfo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import org.openide.util.Lookup;

/**
 *
 * @author Wolfgang Reder
 */
public interface LocoStore extends Lookup.Provider
{

  public List<Locomotive> getAll() throws StoreException;

  public default List<QuickInfo> getAllQuickInfos() throws StoreException
  {
    List<QuickInfo> result = new ArrayList<>();
    getAllQuickInfos(result::add);
    return result;
  }

  public int getAllQuickInfos(Consumer<? super QuickInfo> consumer) throws StoreException;

  public Locomotive get(UUID id) throws StoreException;

  public void addEntity(Locomotive loco,
                        Entity entity,
                        Consumer<Locomotive> modifiedLoco) throws StoreException;

  public void removeEntities(Locomotive loco,
                             Collection<? extends Entity> entities,
                             Consumer<Locomotive> modifiedLoco) throws StoreException;

  public void store(Locomotive loco) throws StoreException;

  public void delete(Locomotive loco) throws StoreException;

  @Override
  public default Lookup getLookup()
  {
    return Lookup.EMPTY;
  }

}
