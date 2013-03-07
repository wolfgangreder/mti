/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel;

/**
 * Beschreibt die Art der externen Entität
 *
 * @author wolfi
 * @see Entity
 */
public enum EntityKind
{

  /**
   * Es handelt sich um eine Bild.
   */
  IMAGE,
  /**
   * Es handel sich um ein Dekoder Soundfile.
   */
  SOUND,
  /**
   * Es handelt sich um Dekoderparameter.
   */
  PARAMETER,
  /**
   * Es handelt sich um ein Ersatzteilblatt.
   */
  SPARE_PART_INFO,
  /**
   * Es handelt sich um eine Dekoderbeschreibung.
   */
  DECODER_DESCRIPTION,
  /**
   * Es handelt sich um eine Betriebsanleitung.
   */
  MANUAL,
  /**
   * Es handelt sich um eine allgemeines Dokument.
   */
  DOCUMENT;
}
