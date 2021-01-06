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
package at.or.reder.mti.model.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Iterator der nach dem letzten Element noch einmal {@code null} liefert.
 *
 * @author Wolfgang Reder
 */
public final class NullTerminateIterator<C> implements Iterator<C>
{

  private final Iterator<C> wrapped;
  private boolean nullSent;

  public NullTerminateIterator(Iterator<C> wrapped)
  {
    this.wrapped = Objects.requireNonNull(wrapped);
  }

  @Override
  public boolean hasNext()
  {
    return wrapped.hasNext() || !nullSent;
  }

  @Override
  public C next()
  {
    if (wrapped.hasNext()) {
      return wrapped.next();
    } else if (!nullSent) {
      return null;
    } else {
      throw new NoSuchElementException();
    }
  }

}
