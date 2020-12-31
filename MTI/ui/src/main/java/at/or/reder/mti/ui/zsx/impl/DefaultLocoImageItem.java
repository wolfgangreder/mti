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
package at.or.reder.mti.ui.zsx.impl;

import at.or.reder.mti.model.Epoch;
import at.or.reder.mti.model.TractionSystem;
import at.or.reder.mti.ui.zsx.ImageType;
import at.or.reder.mti.ui.zsx.LocoImageItem;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 *
 * @author Wolfgang Reder
 */
public class DefaultLocoImageItem extends DefaultImageItem implements LocoImageItem
{

  private final File smallCacheFile;
  private final File originalCacheFile;
  private final String fileNameLarge;
  private final String fileNameSmall;
  private final String originalFilename;
  private final Epoch epoch;
  private final TractionSystem tractionSystem;
  private final String productNumber;

  public DefaultLocoImageItem(File cacheFile,
                              File largeCacheFile,
                              File originalCacheFile,
                              int id,
                              int index,
                              String name,
                              String smallFile,
                              String largeFile,
                              String originalFile,
                              String author,
                              Epoch epoch,
                              TractionSystem tractionSystem,
                              String productNumber,
                              ImageType imageType)
  {
    super(largeCacheFile,
          id,
          index,
          name,
          author,
          imageType);
    fileNameLarge = largeFile;
    fileNameSmall = smallFile;
    originalFilename = originalFile;
    this.epoch = epoch;
    this.tractionSystem = tractionSystem;
    this.productNumber = productNumber;
    this.smallCacheFile = cacheFile;
    this.originalCacheFile = originalCacheFile;
  }

  @Override
  public String getFileNameLarge()
  {
    return fileNameLarge;
  }

  @Override
  public String getFileNameSmall()
  {
    return fileNameSmall;
  }

  @Override
  public String getOriginalFilename()
  {
    return originalFilename;
  }

  @Override
  public Epoch getEpoch()
  {
    return epoch;
  }

  @Override
  public TractionSystem getTractionSystem()
  {
    return tractionSystem;
  }

  @Override
  public String getProductNumber()
  {
    return productNumber;
  }

  @Override
  public BufferedImage getSmallImage() throws IOException
  {
    return ImageIO.read(smallCacheFile);
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 29 * hash + super.hashCode();
    hash = 29 * hash + Objects.hashCode(this.fileNameLarge);
    hash = 29 * hash + Objects.hashCode(this.fileNameSmall);
    hash = 29 * hash + Objects.hashCode(this.originalFilename);
    hash = 29 * hash + Objects.hashCode(this.epoch);
    hash = 29 * hash + Objects.hashCode(this.tractionSystem);
    hash = 29 * hash + Objects.hashCode(this.productNumber);
    return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof LocoImageItem)) {
      return false;
    }
    if (!super.equals(obj)) {
      return false;
    }
    final LocoImageItem other = (LocoImageItem) obj;

    if (!Objects.equals(this.fileNameLarge,
                        other.getFileNameLarge())) {
      return false;
    }
    if (!Objects.equals(this.fileNameSmall,
                        other.getFileNameSmall())) {
      return false;
    }
    if (!Objects.equals(this.originalFilename,
                        other.getOriginalFilename())) {
      return false;
    }
    if (!Objects.equals(this.productNumber,
                        other.getProductNumber())) {
      return false;
    }
    if (!Objects.equals(this.epoch,
                        other.getEpoch())) {
      return false;
    }
    return this.tractionSystem == other.getTractionSystem();
  }

}
