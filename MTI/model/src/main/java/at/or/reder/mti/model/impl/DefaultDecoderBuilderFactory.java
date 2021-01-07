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
package at.or.reder.mti.model.impl;

import at.or.reder.mti.model.Contact;
import at.or.reder.mti.model.Decoder;
import at.or.reder.mti.model.Entity;
import at.or.reder.mti.model.ModelCondition;
import at.or.reder.mti.model.utils.Money;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Wolfgang Reder
 */
@ServiceProvider(service = Decoder.BuilderFactory.class)
public final class DefaultDecoderBuilderFactory implements Decoder.BuilderFactory
{

  @Override
  public Decoder.Builder createDecoderBuilder()
  {
    return new Builder();
  }

  private static final class Builder implements Decoder.Builder
  {

    @Override
    public Decoder.Builder copy(Decoder decoder) throws NullPointerException
    {
    }

    @Override
    public Decoder.Builder condition(ModelCondition cond) throws NullPointerException
    {
    }

    @Override
    public Decoder.Builder dateOfPurchase(LocalDate ts)
    {
    }

    @Override
    public Decoder.Builder description(String descr)
    {
    }

    @Override
    public Decoder.Builder addEntity(Entity e) throws NullPointerException
    {
    }

    @Override
    public Decoder.Builder removeEntity(Entity e)
    {
    }

    @Override
    public Decoder.Builder addEntities(Collection<? extends Entity> e) throws NullPointerException, IllegalArgumentException
    {
    }

    @Override
    public Decoder.Builder clearEntities()
    {
    }

    @Override
    public Decoder.Builder id(UUID id) throws NullPointerException
    {
    }

    @Override
    public Decoder.Builder lastModified(ZonedDateTime ts) throws NullPointerException
    {
    }

    @Override
    public Decoder.Builder manufacturer(Contact contact)
    {
    }

    @Override
    public Decoder.Builder masterImage(Entity e)
    {
    }

    @Override
    public Decoder.Builder name(String name) throws NullPointerException, IllegalArgumentException
    {
    }

    @Override
    public Decoder.Builder price(Money price)
    {
    }

    @Override
    public Decoder.Builder productNumber(String productNumber)
    {
    }

    @Override
    public Decoder.Builder retailer(Contact contact)
    {
    }

    @Override
    public Decoder build() throws NullPointerException, IllegalStateException
    {
    }

  }

  private static final class Impl extends AbstractInventoryObject implements Decoder
  {

  }
}
