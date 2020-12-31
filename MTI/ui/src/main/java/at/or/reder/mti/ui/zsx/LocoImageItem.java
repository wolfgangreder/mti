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
package at.or.reder.mti.ui.zsx;

import at.or.reder.mti.model.Epoch;
import at.or.reder.mti.model.TractionSystem;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author Wolfgang Reder
 */
public interface LocoImageItem extends ImageItem
{

  public String getFileNameLarge();

  public String getFileNameSmall();

  public String getOriginalFilename();

  public Epoch getEpoch();

  public TractionSystem getTractionSystem();

  public String getProductNumber();

  public default BufferedImage getLargeImage() throws IOException
  {
    return getImage();
  }

  public BufferedImage getSmallImage() throws IOException;

}
