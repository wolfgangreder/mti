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

import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public interface Vehicle extends ScaledObject, InventoryObject, ServiceableObject, Lookup.Provider
{

  public Epoch getEpoch();

  public double getLength();

  public double getWidth();

  public double getHeight();

  public double getWeight();

  public int getAddress();

  public int getConsistsAddress();

  public String getDecoder();

}
