/*
 * $Id$
 *
 * ImageLoader.java
 *
 * Author Wolfgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008-2016 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageLoader
{

  public BufferedImage loadImage() throws IOException;

}
