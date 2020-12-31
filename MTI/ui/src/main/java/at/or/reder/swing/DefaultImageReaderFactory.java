/*
 * $Header: /CVSROOT/controls/msdswing/src/at/mountainsd/msdswing/DefaultImageReaderFactory.java,v 1.1 2008/01/27 10:47:49 wolfi Exp $
 *
 * DefaultImageReaderFactory.java
 *
 * Author Wofgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class DefaultImageReaderFactory implements ImageReaderFactory
{

  @Override
  public ImageReader chooseReader(ImageInputStream strm)
  {
    if (strm != null) {
      Iterator<ImageReader> readers = ImageIO.getImageReaders(strm);
      if (readers.hasNext()) {
        return readers.next();
      }
    }
    return null;
  }

  @Override
  public void disposeReader(ImageReader reader)
  {
    if (reader != null) {
      reader.dispose();
    }
  }

  @Override
  public void disposeFactory()
  {
  }

}
