/*
 * $Header: /CVSROOT/controls/msdswing/src/at/mountainsd/msdswing/ImageReaderFactory.java,v 1.1 2008/01/27 10:47:49 wolfi Exp $
 *
 * ImageReaderFactory.java
 *
 * Author Wofgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public interface ImageReaderFactory
{

  ImageReader chooseReader(ImageInputStream strm);

  void disposeReader(ImageReader reader);

  void disposeFactory();

}
