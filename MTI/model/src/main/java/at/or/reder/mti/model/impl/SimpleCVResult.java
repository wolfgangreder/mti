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

import at.or.reder.dcc.cv.CVAddress;
import at.or.reder.mti.model.api.CVResult;
import at.or.reder.mti.model.api.CVSource;
import at.or.reder.zcan20.CVReadState;
import java.util.Objects;

/**
 *
 * @author Wolfgang Reder
 */
public final class SimpleCVResult implements CVResult
{

  private final CVSource source;
  private final int decoderAddress;
  private final CVAddress address;
  private final CVReadState state;
  private final int value;
  private final Throwable error;

  public SimpleCVResult(CVSource source,
                        CVAddress address,
                        int value)
  {
    this(source,
         0,
         address,
         CVReadState.READ,
         value,
         null);
  }

  public SimpleCVResult(CVSource source,
                        int decoderAddress,
                        CVAddress address,
                        CVReadState state,
                        int value,
                        Throwable error)
  {
    this.source = source;
    this.decoderAddress = decoderAddress;
    this.address = address;
    this.state = state;
    this.value = value;
    this.error = error;
  }

  @Override
  public CVSource getSource()
  {
    return source;
  }

  @Override
  public int getDecoderAddress()
  {
    return decoderAddress;
  }

  @Override
  public CVAddress getAddress()
  {
    return address;
  }

  @Override
  public CVReadState getState()
  {
    return state;
  }

  @Override
  public int getValue()
  {
    return value;
  }

  @Override
  public Throwable getError()
  {
    return error;
  }

  @Override
  public int hashCode()
  {
    int hash = 3;
    hash = 23 * hash + Objects.hashCode(this.source);
    hash = 23 * hash + this.decoderAddress;
    hash = 23 * hash + Objects.hashCode(this.address);
    hash = 23 * hash + Objects.hashCode(this.state);
    hash = 23 * hash + this.value;
    hash = 23 * hash + Objects.hashCode(this.error);
    return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final SimpleCVResult other = (SimpleCVResult) obj;
    if (this.decoderAddress != other.decoderAddress) {
      return false;
    }
    if (this.value != other.value) {
      return false;
    }
    if (!Objects.equals(this.source,
                        other.source)) {
      return false;
    }
    if (!Objects.equals(this.address,
                        other.address)) {
      return false;
    }
    if (this.state != other.state) {
      return false;
    }
    return Objects.equals(this.error,
                          other.error);
  }

  @Override
  public String toString()
  {
    return address.getFlatAddress() + "@" + decoderAddress + "=>" + value + "; " + state.name();
  }

}
