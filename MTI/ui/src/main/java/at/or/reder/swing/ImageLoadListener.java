/*
 * $Header: /CVSROOT/controls/msdswing/src/at/mountainsd/msdswing/helper/ImageLoadListener.java,v 1.2 2008/01/27 10:47:49 wolfi Exp $
 *
 * LoadErrorListener.java
 *
 * Author Wolfgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import at.or.reder.swing.model.JImageModel;

public interface ImageLoadListener
{

  public void imageLoadError(JImageModel sender,
                             Exception e);

  public void beginImageLoad(JImageModel sender);

  public void endImageLoad(JImageModel sender);

  public void imageLoadProgress(JImageModel sender,
                                float percentDone);

  public void imageChanged(JImageModel sender);

  public void loadAborted(JImageModel sender);

}
