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
package at.or.reder.swing.model;

import at.or.reder.swing.ImageLoadListener;
import at.or.reder.swing.ImageLoader;
import at.or.reder.swing.ImageReaderFactory;
import at.or.reder.swing.ImageStore;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.filechooser.FileFilter;

public interface JImageModel
{

  public void clearImage();

  public boolean isImageSet();

  public void setImage(ImageStore store);

  BufferedImage getImage();

  public void setImage(BufferedImage pImage);

  public URL getImageURL();

  public void setImageURL(URL pURL);

  public boolean getAsyncLoad();

  public void setAsyncLoad(boolean loadAsync);

  public boolean isLoadingInProcess();

  public void abortLoading();

  public int getImageWidth();

  public int getImageHeight();

  public BufferedImage getThumbnail();

  public boolean getUseHardImageStore();

  public void setUseHardImageStore(boolean useHard);

  public void addImageLoadListener(ImageLoadListener l);

  public void removeImageLoadListener(ImageLoadListener l);

  public void reloadImage();

  public ImageLoader getImageLoader();

  public void setImageLoader(ImageLoader loader);

  public FileFilter getFileFilter();

  public ImageReaderFactory getImageReaderFactory();

  public void setImageReaderFactory(ImageReaderFactory factory);

}
