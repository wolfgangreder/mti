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
package at.or.reder.osplatform.thumbnail;

public enum ThumbnailOption
{
  /**
   * Thumbnail size 16x16px
   */
  DWARF(16),
  /**
   * Thumbnail size 64x64px
   */
  TINY(64),
  /**
   * Thumbnail size 128x128px
   */
  NORMAL(128),
  /**
   * Thumbnail size 256x256px
   */
  LARGE(256),
  /**
   * Thumbnail size 512x512px (not in specification)
   */
  HUGE(512);

  private final int size;

  private ThumbnailOption(int size)
  {
    this.size = size;
  }

  public int getSize()
  {
    return size;
  }

}
