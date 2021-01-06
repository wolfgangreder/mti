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
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Wolfgang Reder
 */
public interface EntityStore
{

  public Entity getEntity(UUID entity) throws StoreException;

  public List<Entity> getEntities() throws StoreException;

  public void storeEntity(Entity e) throws StoreException;

  public void deleteEntity(Entity e) throws StoreException;

}
