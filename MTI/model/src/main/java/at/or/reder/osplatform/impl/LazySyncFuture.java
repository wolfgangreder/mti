/*
 * Copyright 2017 Wolfgang Reder.
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
package at.or.reder.osplatform.impl;

import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.netbeans.api.annotations.common.NonNull;

public class LazySyncFuture<R> implements Future<R>
{

  private final Supplier<R> initializer;
  private R value;
  private volatile boolean initialized;

  public LazySyncFuture(@NonNull Supplier<R> initializer)
  {
    this.initializer = Objects.requireNonNull(initializer);
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning)
  {
    return false;
  }

  @Override
  public boolean isCancelled()
  {
    return false;
  }

  @Override
  public boolean isDone()
  {
    return initialized;
  }

  public synchronized void reset()
  {
    initialized = false;
    value = null;
  }

  @Override
  public synchronized R get()
  {
    if (!initialized) {
      value = initializer.get();
      initialized = true;
    }
    return value;
  }

  @Override
  public R get(long timeout,
               TimeUnit unit)
  {
    return get();
  }

}
