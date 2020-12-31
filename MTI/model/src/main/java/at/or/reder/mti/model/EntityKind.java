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
