/*
 * $Id$
 *
 * ImageStore.java
 *
 * Author Wolfgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008-2016 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageStore
{

  public BufferedImage get() throws IOException;

  public BufferedImage get(boolean reloadImage) throws IOException;

  public void assignImage(BufferedImage pImage);

}
