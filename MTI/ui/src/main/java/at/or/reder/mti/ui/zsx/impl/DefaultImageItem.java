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

import at.or.reder.mti.ui.zsx.ImageItem;
import at.or.reder.mti.ui.zsx.ImageType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 *
 * @author Wolfgang Reder
 */
public class DefaultImageItem implements ImageItem
{

  private final int id;
  private final int index;
  private final String file;
  private final String author;
  private final ImageType imageType;
  private final File cacheFile;

  public DefaultImageItem(File cacheFile,
                          int id,
                          int index,
                          String file,
                          String author,
                          ImageType imageType)
  {
    this.cacheFile = cacheFile;
    this.id = id;
    this.index = index;
    this.file = file;
    this.author = author;
    this.imageType = imageType;
  }

  @Override
  public int getId()
  {
    return id;
  }

  @Override
  public int getIndex()
  {
    return index;
  }

  @Override
  public String getName()
  {
    return file;
  }

  @Override
  public String getAuthor()
  {
    return author;
  }

  @Override
  public ImageType getImageType()
  {
    return imageType;
  }

  @Override
  public BufferedImage getImage() throws IOException
  {
    return ImageIO.read(cacheFile);
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 59 * hash + this.id;
    hash = 59 * hash + this.index;
    hash = 59 * hash + Objects.hashCode(this.file);
    hash = 59 * hash + Objects.hashCode(this.author);
    hash = 59 * hash + Objects.hashCode(this.imageType);
    return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof ImageItem)) {
      return false;
    }
    final ImageItem other = (ImageItem) obj;
    if (this.id != other.getId()) {
      return false;
    }
    if (this.index != other.getIndex()) {
      return false;
    }
    if (!Objects.equals(this.file,
                        other.getName())) {
      return false;
    }
    if (!Objects.equals(this.author,
                        other.getAuthor())) {
      return false;
    }
    return this.imageType == other.getImageType();
  }

}
