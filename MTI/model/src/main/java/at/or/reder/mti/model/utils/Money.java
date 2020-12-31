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
package at.or.reder.mti.model.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

public final class Money extends Number implements Comparable<Money>
{

  private static final long serialVersionUID = 1L;
  public static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
  private static final MathContext MATH_CONTEXT = new MathContext(80,
                                                                  RoundingMode.HALF_UP);
  public static final Money ZERO = new Money(BigDecimal.ZERO);
  public static final Money ONE = Money.valueOf(BigDecimal.ONE);
  public static final Money TWO = Money.valueOf(2);
  public static final Money FIVE = Money.valueOf(5);
  public static final Money TEN = Money.valueOf(BigDecimal.TEN);
  public static final Money TWENTY = Money.valueOf(20);
  public static final Money FIFTY = Money.valueOf(50);
  public static final Money HUNDRED = Money.valueOf(100);
  public static final Money TWO_HUNDRED = Money.valueOf(200);
  public static final Money FIVE_HUNDRED = Money.valueOf(500);
  public static final Money THOUSAND = Money.valueOf(1000);
  public static final int SCALE = 4;
  private static final BigDecimal BD_HUNDRED = BigDecimal.valueOf(100);
  private static final BigDecimal BD_THOUSAND = BigDecimal.valueOf(1000);
  private static final BigDecimal BD_TEN_THOUSAND = BigDecimal.valueOf(10000);
  private static final BigDecimal BD_HUNDRED_THOUSAND = BigDecimal.valueOf(100000);
  private final BigDecimal value;

  public static Money valueOf(BigDecimal value)
  {
    if (value != null) {
      if (BigDecimal.ZERO.compareTo(value) == 0) {
        return Money.ZERO;
      } else {
        return new Money(value);
      }
    }
    return null;
  }

  public static Money valueOf(String value)
  {
    return value != null ? valueOf(new BigDecimal(value)) : null;
  }

  public static Money valueOf(int integer)
  {
    return valueOf(new BigDecimal(integer));
  }

  private Money(BigDecimal value)
  {
    this.value = value.setScale(SCALE,
                                ROUNDING_MODE);
  }

  public Money add(Money curr)
  {
    return valueOf(value.add(curr.value));
  }

  public Money add(BigDecimal curr)
  {
    return valueOf(value.add(curr));
  }

  public Money subtract(Money curr)
  {
    return valueOf(value.subtract(curr.value));
  }

  public Money subtract(BigDecimal curr)
  {
    return valueOf(value.subtract(curr));
  }

  public Money multiply(Money curr)
  {
    return valueOf(value.multiply(curr.value));
  }

  public Money multiply(BigDecimal curr)
  {
    return valueOf(value.multiply(curr));
  }

  public Money divide(Money curr)
  {
    return valueOf(value.divide(curr.value,
                                MATH_CONTEXT));
  }

  public Money divide(BigDecimal curr)
  {
    return valueOf(value.divide(curr,
                                MATH_CONTEXT));
  }

  public Money modulo(Money curr)
  {
    return valueOf(value.remainder(curr.value,
                                   MATH_CONTEXT));
  }

  public Money modulo(BigDecimal curr)
  {
    return valueOf(value.remainder(curr,
                                   MATH_CONTEXT));
  }

  public Money abs()
  {
    return valueOf(value.abs());
  }

  public Money negate()
  {
    return valueOf(value.negate());
  }

  public Money plus()
  {
    return valueOf(value.plus());
  }

  public BigDecimal toBigDecimal()
  {
    return value;
  }

  private BigDecimal innerRoundTo(int nDigits,
                                  boolean divide)
  {
    BigDecimal dig;
    switch (nDigits) {
      case 0:
        dig = BigDecimal.ONE;
        break;
      case 1:
        dig = BigDecimal.TEN;
        break;
      case 2:
        dig = BD_HUNDRED;
        break;
      case 3:
        dig = BD_THOUSAND;
        break;
      case 4:
        dig = BD_TEN_THOUSAND;
        break;
      case 5:
        dig = BD_HUNDRED_THOUSAND;
        break;
      default: {
        long mult = 1;
        for (int i = 0; i < nDigits; ++i) {
          mult *= 10;
        }
        dig = BigDecimal.valueOf(mult);

      }
    }
    BigDecimal tmp = value.multiply(dig);
    tmp = tmp.setScale(0,
                       ROUNDING_MODE);
    if (divide) {
      return tmp.divide(dig);
    } else {
      return tmp;
    }
  }

  public Money roundTo(int nDigits)
  {
    return Money.valueOf(innerRoundTo(nDigits,
                                      true));
  }

  public BigInteger toCent()
  {
    return new BigInteger(innerRoundTo(2,
                                       false).toPlainString());
  }

  @Override
  public boolean equals(Object obj)
  {
    return obj instanceof Money && compareTo((Money) obj) == 0;
  }

  @Override
  public int hashCode()
  {
    return value.hashCode();
  }

  @Override
  public String toString()
  {
    return value.toPlainString();
  }

  @Override
  public int compareTo(Money o)
  {
    return value.compareTo(o.value);
  }

  @Override
  public int intValue()
  {
    return value.intValue();
  }

  @Override
  public long longValue()
  {
    return value.longValue();
  }

  @Override
  public float floatValue()
  {
    return value.floatValue();
  }

  @Override
  public double doubleValue()
  {
    return value.doubleValue();
  }

  public boolean isZero()
  {
    return this == Money.ZERO || value == BigDecimal.ZERO || value.compareTo(BigDecimal.ZERO) == 0;
  }

  public boolean isGreaterThanZero()
  {
    return value.compareTo(BigDecimal.ZERO) > 0;
  }

  public boolean isLessThanZero()
  {
    return value.compareTo(BigDecimal.ZERO) < 0;
  }

}
