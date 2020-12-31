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
package at.or.reder.osplatform;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import org.openide.util.NbBundle.Messages;

@Messages({"FileSizeFormatter_byte=Byte",
           "FileSizeFormatter_dkilo=Kilobyte",
           "FileSizeFormatter_dmega=Megabyte",
           "FileSizeFormatter_dgiga=Gigabyte",
           "FileSizeFormatter_dtera=Terabyte",
           "FileSizeFormatter_dpeta=Petabyte",
           "FileSizeFormatter_dexa=Exabyte",
           "FileSizeFormatter_dzetta=Zettabyte",
           "FileSizeFormatter_dyotta=Yottabyte",
           "FileSizeFormatter_bkilo=kibibyte",
           "FileSizeFormatter_bmega=mebibyte",
           "FileSizeFormatter_bgiga=gibibyte",
           "FileSizeFormatter_btera=tebibyte",
           "FileSizeFormatter_bpeta=pebibyte",
           "FileSizeFormatter_bexa=exbibyte",
           "FileSizeFormatter_bzetta=zebibyte",
           "FileSizeFormatter_byotta=yobibyte",})
public final class FileSizeFormatter
{

  /**
   * Bestimmt die Art der Konvertierung
   */
  public static enum Flags
  {
    /**
     * Dezimale Umwandlung (Basis 1000).
     */
    DECIMAL,
    /**
     * NONISO Prefixe (immer kB anstatt kiB)
     */
    NONISO,
    /**
     * Lange Einheiten
     */
    LONGUNIT
  }

  public static String formatFileSize(long size,
                                      Flags... flags)
  {
    return formatFileSize(size,
                          3,
                          flags);
  }

  private static final class OffsetDescriptor
  {

    private final int step;
    private final BigDecimal divisor;
    private final String unit;

    public OffsetDescriptor(int step,
                            BigDecimal divisor,
                            String unit)
    {
      this.step = step;
      this.divisor = divisor;
      this.unit = unit;
    }

    public int getStep()
    {
      return step;
    }

    public BigDecimal getDivisor()
    {
      return divisor;
    }

    public String getUnit()
    {
      return unit;
    }

  }

  private static OffsetDescriptor findUnitOffset(double size,
                                                 Set<Flags> flagSet)
  {
    final double max = 1000f;
    final double baseDivisor = flagSet.contains(Flags.DECIMAL) ? 1000 : 1024;
    int step = -1;
    double divisor = 1;
    double lastDivisor;
    double running;
    do {
      lastDivisor = divisor;
      running = size / divisor;
      divisor *= baseDivisor;
      ++step;
    } while (running >= max && step < 9);
    return new OffsetDescriptor(step,
                                new BigDecimal(lastDivisor),
                                getUnit(step,
                                        flagSet));
  }

  private static String getUnit(int step,
                                Set<Flags> flagSet)
  {
    StringBuilder buffer = new StringBuilder();
    if (flagSet.contains(Flags.LONGUNIT)) {
      if (flagSet.contains(Flags.DECIMAL) || flagSet.contains(Flags.NONISO)) {
        switch (step) {
          case 0:
            buffer.append(Bundle.FileSizeFormatter_byte());
            break;
          case 1:
            buffer.append(Bundle.FileSizeFormatter_dkilo());
            break;
          case 2:
            buffer.append(Bundle.FileSizeFormatter_dmega());
            break;
          case 3:
            buffer.append(Bundle.FileSizeFormatter_dgiga());
            break;
          case 4:
            buffer.append(Bundle.FileSizeFormatter_dtera());
            break;
          case 5:
            buffer.append(Bundle.FileSizeFormatter_dpeta());
            break;
          case 6:
            buffer.append(Bundle.FileSizeFormatter_dexa());
            break;
          case 7:
            buffer.append(Bundle.FileSizeFormatter_dzetta());
            break;
          default:
            buffer.append(Bundle.FileSizeFormatter_dyotta());
            break;
        }
      } else { // !Flags.DECIMAL
        switch (step) {
          case 0:
            buffer.append(Bundle.FileSizeFormatter_byte());
            break;
          case 1:
            buffer.append(Bundle.FileSizeFormatter_bkilo());
            break;
          case 2:
            buffer.append(Bundle.FileSizeFormatter_bmega());
            break;
          case 3:
            buffer.append(Bundle.FileSizeFormatter_bgiga());
            break;
          case 4:
            buffer.append(Bundle.FileSizeFormatter_btera());
            break;
          case 5:
            buffer.append(Bundle.FileSizeFormatter_bpeta());
            break;
          case 6:
            buffer.append(Bundle.FileSizeFormatter_bexa());
            break;
          case 7:
            buffer.append(Bundle.FileSizeFormatter_bzetta());
            break;
          default:
            buffer.append(Bundle.FileSizeFormatter_byotta());
        }
      }
    } else { // !Flags.LONGUNIT
      switch (step) {
        case 0:
          break;
        case 1:
          buffer.append('k');
          break;
        case 2:
          buffer.append('M');
          break;
        case 3:
          buffer.append('G');
          break;
        case 4:
          buffer.append('T');
          break;
        case 5:
          buffer.append('P');
          break;
        case 6:
          buffer.append('E');
          break;
        case 7:
          buffer.append('Z');
          break;
        default:
          buffer.append('Y');
          break;
      }
      if (step != 0 && !flagSet.contains(Flags.DECIMAL) && !flagSet.contains(Flags.NONISO)) {
        buffer.append('i');
      }
      buffer.append('B');
    }
    return buffer.toString();
  }

  private static Set<Flags> createFlagSet(Flags... flags)
  {
    if (flags == null || flags.length == 0) {
      return Collections.emptySet();
    } else {
      return EnumSet.of(flags[0],
                        flags);
    }
  }

  public static String formatFileSize(long size,
                                      int numDigits,
                                      Flags... flags)
  {
    if (numDigits < 3 || numDigits > 9) {
      throw new IllegalArgumentException("numDigits out of range [3...9]");
    }
    final Set<Flags> flagSet = createFlagSet(flags);
    final OffsetDescriptor descr = findUnitOffset(size,
                                                  flagSet);
    BigDecimal dec = new BigDecimal(size).divide(descr.getDivisor());
    dec = dec.round(new MathContext(numDigits,
                                    RoundingMode.HALF_UP));
    MessageFormat format = new MessageFormat("{0,number,0.#########}");
    StringBuffer buffer = format.format(new Object[]{dec},
                                        new StringBuffer(),
                                        null);
    return buffer.append(" ").append(descr.getUnit()).toString();
  }

}
