/*
 * $Id$
 *
 * HardImageStore.java
 *
 * Author Wolfgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008-2016 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import java.awt.image.BufferedImage;
import java.io.IOException;

public final class HardImageStore implements ImageStore
{

  private BufferedImage image;
  private final ImageLoader imageLoader;

  public HardImageStore(BufferedImage pImage,
                        ImageLoader pImageLoader)
  {
    image = pImage;
    imageLoader = pImageLoader;
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
      if (image == null && reloadImage && imageLoader != null) {
        image = imageLoader.loadImage();
      }
    }
    return image;
  }

  @Override
  public void assignImage(BufferedImage pImage)
  {
    synchronized (this) {
      image = pImage;
    }
  }

}
