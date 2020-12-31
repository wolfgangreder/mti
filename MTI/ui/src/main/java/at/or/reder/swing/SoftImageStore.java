/*
 * $Id$
 *
 * SoftImageStore.java
 *
 * Author Wolfgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008-2016 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.ref.SoftReference;

public final class SoftImageStore implements ImageStore
{

  private SoftReference<BufferedImage> image;
  private final ImageLoader loader;

  public SoftImageStore(BufferedImage pImage,
                        ImageLoader pLoader)
  {
    image = new SoftReference<>(pImage);
    loader = pLoader;
  }

  @Override
  public BufferedImage get() throws IOException
  {
    return get(true);
  }

  @Override
  public BufferedImage get(boolean reloadImage) throws IOException
  {
    synchronized (this) {
      BufferedImage result = image.get();
      if (result == null && loader != null && reloadImage) {
        result = loader.loadImage();
        image = new SoftReference<>(result);
      }
      return result;
    }
  }

  @Override
  public void assignImage(BufferedImage pImage)
  {
    synchronized (this) {
      image = new SoftReference<>(pImage);
    }
  }

}
