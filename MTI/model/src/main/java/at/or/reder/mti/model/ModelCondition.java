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

import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 * Diese Klasse beschreibt den Zustand eines Modells
 *
 * @author wolfi
 */
@Messages({
  "ModelCondition_ORIGINAL=Original verpackt",
  "ModelCondition_NEW=Neu",
  "ModelCondition_USED=Gebraucht",
  "ModelCondition_HEAVY_USED=Stark benutzt",
  "ModelCondition_DEFECT=Defekt",
  "ModelCondition_NOT_FUNCTIONAL=Funktionsuntüchtig",
  "ModelCondition_DESTROYED=Zerstört",
  "ModelCondition_SOLD=Verkauft"
})
public enum ModelCondition
{

  /**
   * Das Model ist original Verpackt.
   */
  ORIGINAL,
  /**
   * Das Modell ist neuwertig.
   */
  NEW,
  /**
   * Das Modell ist völlig in Ordnung und weist nur leichte Gebrauchsspuren auf.
   */
  USED,
  /**
   * Das Modell weist Gebrauchsspuren auf.
   */
  HEAVY_USED,
  /**
   * Das Modell ist weist defekte auf, ist aber funktionstüchtig.
   */
  DEFECT,
  /**
   * Das Modell ist defekt und nicht funktionstüchtig.
   */
  NOT_FUNCTIONAL,
  /**
   * Das Modell ist zerstört.
   */
  DESTROYED,
  /**
   * Das Model wurde verkauft.
   */
  SOLD;

  @Override
  public String toString()
  {
    return NbBundle.getMessage(ModelCondition.class,
                               "ModelCondition_" + name());
  }

}
