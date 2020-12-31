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
package at.or.reder.mti.ui.zsx.impl;

import at.or.reder.mti.ui.zsx.ImageType;
import at.or.reder.mti.ui.zsx.TachoImageItem;
import java.io.File;
import java.util.Objects;

/**
 *
 * @author Wolfgang Reder
 */
public class DefaultTachoImageItem extends DefaultImageItem implements TachoImageItem
{

  private final int value;
  private final String unit;

  public DefaultTachoImageItem(File cacheFile,
                               int id,
                               int index,
                               String file,
                               String author,
                               ImageType imageType,
                               int value,
                               String unit)
  {
    super(cacheFile,
          id,
          index,
          file,
          author,
          imageType);
    this.value = value;
    this.unit = unit;
    if (imageType != ImageType.TACHO) {
      throw new IllegalArgumentException("ImageType must be TACHO");
    }
  }
//private final String unit;
//private final int value;

  @Override
  public int getValue()
  {
    return value;
  }

  @Override
  public String getUnit()
  {
    return unit;
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 97 * hash + this.value;
    hash = 97 * hash + Objects.hashCode(this.unit);
    hash = 97 * hash + super.hashCode();
    return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof TachoImageItem)) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    final TachoImageItem other = (TachoImageItem) obj;
    if (this.value != other.getValue()) {
      return false;
    }
    if (!Objects.equals(this.unit,
                        other.getUnit())) {
      return false;
    }
    return true;
  }

}
