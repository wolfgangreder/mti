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

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Beschreibt ein verbautes Ersatzteil.
 *
 * @author wolfi
 * @see ServiceEntry
 * @see XUsedSparePart
 */
public final class UsedSparePart
{

  private final Set<UUID> defects;
  private final SparePart part;
  private final BigDecimal amount;
  private final String memo;

  /**
   *
   * @param defects List mit Ids von Defekten denen dieser Ersatzeil zugeordnet ist.
   * @param part Der Ersatzteil
   * @param amount Menge die Verbaut wurde
   * @param memo Notiz
   * @throws NullPointerException wenn einer der Referenzparameter {@code null} ist, oder {@code defect} {@code null} enthält.
   * @throws IllegalArgumentException Wenn {@code defects} leer ist, oder {@code amount&lt=0}
   */
  public UsedSparePart(Collection<UUID> defects,
                       SparePart part,
                       BigDecimal amount,
                       String memo) throws NullPointerException,
                                           IllegalArgumentException
  {
    if (defects == null) {
      throw new NullPointerException("defects==null");
    }
    if (defects.isEmpty()) {
      throw new IllegalArgumentException("defects is empty");
    }
    if (defects.contains(null)) {
      throw new IllegalArgumentException("defect contains null");
    }
    if (part == null) {
      throw new NullPointerException("part is null");
    }
    if (amount == null) {
      throw new NullPointerException("amount==null");
    }
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("amount<=0");
    }
    this.defects = new HashSet<>(defects);
    this.part = part;
    this.amount = amount;
    this.memo = memo != null ? memo : "";
  }

  /**
   * Id der Defekte
   *
   * @return set der ids, niemals {@code null} oder leer.
   */
  public Set<UUID> getDefects()
  {
    return defects;
  }

  /**
   * Das Ersatzteil
   *
   * @return niemals {@code null}
   */
  public SparePart getPart()
  {
    return part;
  }

  /**
   * Menge die verbaut wurde.
   *
   * @return immer &gt;0
   */
  public BigDecimal getAmount()
  {
    return amount;
  }

  /**
   * Notiz
   *
   * @return niemals {@code null}
   */
  public String getMemo()
  {
    return memo;
  }

}
