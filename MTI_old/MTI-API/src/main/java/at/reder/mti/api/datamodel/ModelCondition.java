/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel;

import org.openide.util.NbBundle.Messages;

/**
 * Diese Klasse beschreibt den Zustand eines Modells
 *
 * @author wolfi
 */
@Messages({
  "ModelCondition.ORIGINAL=Original verpackt",
  "ModelCondition.NEW=Neu",
  "ModelCondition.USED=Gebraucht",
  "ModelCondition.HEAVY_USED=Stark benutzt",
  "ModelCondition.DEFECT=Defekt",
  "ModelCondition.NOT_FUNCTIONAL=Funktionsuntüchtig",
  "ModelCondition.DESTROYED=Zerstört",
  "ModelCondition.SOLD=Verkauft"
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
    switch (this) {
      case DEFECT:
        return Bundle.ModelCondition_DEFECT();
      case DESTROYED:
        return Bundle.ModelCondition_DESTROYED();
      case HEAVY_USED:
        return Bundle.ModelCondition_HEAVY_USED();
      case NEW:
        return Bundle.ModelCondition_NEW();
      case NOT_FUNCTIONAL:
        return Bundle.ModelCondition_NOT_FUNCTIONAL();
      case ORIGINAL:
        return Bundle.ModelCondition_ORIGINAL();
      case SOLD:
        return Bundle.ModelCondition_SOLD();
      case USED:
        return Bundle.ModelCondition_USED();
      default:
        return name();
    }
  }

}
