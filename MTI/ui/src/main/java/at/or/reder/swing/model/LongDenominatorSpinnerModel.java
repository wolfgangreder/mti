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
package at.or.reder.swing.model;

import javax.swing.AbstractSpinnerModel;

public class LongDenominatorSpinnerModel extends AbstractSpinnerModel
{

  private long value = 1;
  private long maxValue = Long.MAX_VALUE;
  private long minValue = Long.MIN_VALUE;

  @Override
  public Object getValue()
  {
    return value;
  }

  public long getLongValue()
  {
    return value;
  }

  public void setValue(long value)
  {
    if (value == 0) {
      throw new IllegalArgumentException("denominator cannot be 0");
    }
    if (this.value != value) {
      this.value = value;
      this.fireStateChanged();
    }
  }

  @Override
  public void setValue(Object value)
  {
    if (value instanceof Number) {
      setValue(((Number) value).longValue());
    } else if (value != null) {
      setValue(Long.parseLong(value.toString()));
    }
  }

  @Override
  public Object getNextValue()
  {
    if (value != -1) {
      return value + 1;
    } else {
      return 1;
    }
  }

  @Override
  public Object getPreviousValue()
  {
    if (value != 1) {
      return value - 1;
    } else {
      return -1;
    }
  }

  public void setMinValue(long minValue)
  {
    if (minValue == 0) {
      throw new IllegalArgumentException("minValue cannot be 0");
    }
    if (minValue > maxValue) {
      throw new IllegalArgumentException("minValue cannot be greate than maxValue");
    }
    this.minValue = minValue;
    setValue(Math.max(value,
                      minValue));
  }

  public long getMinValue()
  {
    return minValue;
  }

  public void setMaxValue(long maxValue)
  {
    if (maxValue == 0) {
      throw new IllegalArgumentException("maxValue cannot be 0");
    }
    if (maxValue < minValue) {
      throw new IllegalArgumentException("maxValue cannot be smaller than minValue");
    }
    this.maxValue = maxValue;
    setValue(Math.min(value,
                      maxValue));
  }

  public long getMaxValue()
  {
    return maxValue;
  }

}
