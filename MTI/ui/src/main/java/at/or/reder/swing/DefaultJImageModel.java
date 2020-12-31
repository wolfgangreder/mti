/*
 * $Id$
 *
 * DefaultJImageModel.java
 *
 * Author Wofgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008-2018 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import at.or.reder.swing.model.JImageModel;
import at.or.reder.swing.ImageFileFilter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.event.IIOReadWarningListener;
import javax.imageio.stream.ImageInputStream;
import javax.swing.filechooser.FileFilter;
import org.openide.util.Exceptions;

public class DefaultJImageModel implements JImageModel
{

  private class MyImageLoader implements ImageLoader
  {

    @Override
    public BufferedImage loadImage()
    {
      return internalLoadImage();
    }

  }

  private class MyIIOReadListener implements IIOReadProgressListener, IIOReadWarningListener
  {

    @Override
    public void sequenceStarted(ImageReader source,
                                int minIndex)
    {
    }

    @Override
    public void sequenceComplete(ImageReader source)
    {
    }

    @Override
    public void imageStarted(ImageReader source,
                             int imageIndex)
    {
      internalImageStarted(source);
    }

    @Override
    public void imageProgress(ImageReader source,
                              float percentageDone)
    {
      internalImageProgress(source,
                            percentageDone);
    }

    @Override
    public void imageComplete(ImageReader source)
    {
      internalImageComplete();
    }

    @Override
    public void thumbnailStarted(ImageReader source,
                                 int imageIndex,
                                 int thumbnailIndex)
    {
    }

    @Override
    public void thumbnailProgress(ImageReader source,
                                  float percentageDone)
    {
    }

    @Override
    public void thumbnailComplete(ImageReader source)
    {
    }

    @Override
    public void readAborted(ImageReader source)
    {
    }

    @Override
    public void warningOccurred(ImageReader source,
                                String warning)
    {
    }

  }
  private ImageStore image;
  private final List<ImageLoadListener> imageLoadListener = new ArrayList<>();
  private URL imageURL;
  private volatile int imageHeight = -1;
  private volatile int imageWidth = -1;
  private int imageIndex = 0;
  private boolean useHardImageStore = false;
  private boolean useAsyncLoad = true;
  private boolean loadingInProcess = false;
  private boolean hadImageLoadError = false;
  private volatile boolean abortLoading = false;
  private ImageLoader imageLoader = new MyImageLoader();
  private final MyIIOReadListener iioReadListener = new MyIIOReadListener();
  private ImageReaderFactory readerFactory = new DefaultImageReaderFactory();

  public DefaultJImageModel()
  {
  }

  private ImageStore createImageStore(BufferedImage pImage)
  {
    hadImageLoadError = false;
    return getUseHardImageStore() ? new HardImageStore(pImage,
                                                       imageLoader) : new SoftImageStore(
            pImage,
            imageLoader);
  }

  public boolean getHadImageLoadError()
  {
    return hadImageLoadError;
  }

  @Override
  public synchronized BufferedImage getImage()
  {
    try {
      return image != null ? image.get() : null;
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
    return null;
  }

  @Override
  public boolean isImageSet()
  {
    return image != null;
  }

  @Override
  public void clearImage()
  {
    image = null;
    imageURL = null;
    imageHeight = -1;
    imageWidth = -1;
    fireImageChanged();
  }

  @Override
  public void setImage(ImageStore store)
  {
    image = store;
    imageURL = null;
    imageHeight = -1;
    imageWidth = -1;
    fireImageChanged();
  }

  @Override
  public void setImage(BufferedImage pImage)
  {
    image = createImageStore(pImage);
    imageURL = null;
    imageHeight = -1;
    imageWidth = -1;
    fireImageChanged();
  }

  @Override
  public URL getImageURL()
  {
    return imageURL;
  }

  @Override
  public void setImageURL(URL pURL)
  {
    imageURL = pURL;
    image = createImageStore(null);
    imageHeight = -1;
    imageWidth = -1;
    fireImageChanged();
  }

  @Override
  public boolean getAsyncLoad()
  {
    return useAsyncLoad;
  }

  @Override
  public void setAsyncLoad(boolean loadAsync)
  {
    useAsyncLoad = loadAsync;
  }

  @Override
  public boolean isLoadingInProcess()
  {
    return loadingInProcess;
  }

  public int getImageIndex()
  {
    return imageIndex;
  }

  protected void setImageIndex(int pIndex)
  {
    imageIndex = pIndex;
    fireImageChanged();
  }

  @Override
  public int getImageWidth()
  {
    if (imageWidth == -1) {
      getImage(); // setzt imageHeight und imageWidth;
    }
    return imageWidth;
  }

  @Override
  public int getImageHeight()
  {
    if (imageHeight == -1) {
      getImage(); // set imageHeight und imageWidth;
    }
    return imageHeight;
  }

  @Override
  public BufferedImage getThumbnail()
  {
    throw new UnsupportedOperationException("Not supported yet."); // No I18n
  }

  @Override
  public boolean getUseHardImageStore()
  {
    return useHardImageStore;
  }

  @Override
  public void setUseHardImageStore(boolean useHard)
  {
    if (useHardImageStore != useHard) {
      BufferedImage tmp = null;
      try {
        tmp = image != null ? image.get(false) : null;
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
      useHardImageStore = useHard;
      image = createImageStore(tmp);
    }
  }

  @Override
  public void addImageLoadListener(ImageLoadListener l)
  {
    if (l != null && !imageLoadListener.contains(l)) {
      imageLoadListener.add(l);
    }
  }

  @Override
  public void removeImageLoadListener(ImageLoadListener l)
  {
    imageLoadListener.remove(l);
  }

  protected void fireImageChanged()
  {
    for (ImageLoadListener l : imageLoadListener) {
      l.imageChanged(this);
    }
  }

  protected void fireImageLoadError(Exception e)
  {
    for (ImageLoadListener l : imageLoadListener) {
      l.imageLoadError(this,
                       e);
    }
  }

  protected void fireBeginImageLoad()
  {
    for (ImageLoadListener l : imageLoadListener) {
      l.beginImageLoad(this);
    }
  }

  protected void fireEndImageLoad()
  {
    for (ImageLoadListener l : imageLoadListener) {
      l.endImageLoad(this);
    }
  }

  protected void fireImageLoadProgress(float pProgress)
  {
    for (ImageLoadListener l : imageLoadListener) {
      l.imageLoadProgress(this,
                          pProgress);
    }
  }

  private void fireLoadAbort()
  {
    for (ImageLoadListener l : imageLoadListener) {
      l.loadAborted(this);
    }
  }

  @Override
  public void reloadImage()
  {
    if (imageURL != null) {
      setImageURL(getImageURL());
    }
  }

  @SuppressWarnings("UseSpecificCatch")
  private BufferedImage internalThreadedLoadImage(boolean assignImage)
  {
    BufferedImage result = null;
    if (!(hadImageLoadError || loadingInProcess)) {
      try {
        abortLoading = false;
        loadingInProcess = true;
        InputStream inStream = imageURL.openStream();
        ImageInputStream imageStream = ImageIO.createImageInputStream(inStream);
        if (imageStream != null) {
          ImageReaderFactory factory = getImageReaderFactory();
          ImageReader reader = factory.chooseReader(imageStream);
          if (reader != null) {
            try {
              reader.addIIOReadProgressListener(iioReadListener);
              reader.addIIOReadWarningListener(iioReadListener);
              reader.setInput(imageStream);
              imageHeight = reader.getHeight(getImageIndex());
              imageWidth = reader.getWidth(getImageIndex());
              result = reader.read(getImageIndex());
              if (assignImage) {
                image.assignImage(result);
              }
            } finally {
              factory.disposeReader(reader);
            }
          } else {
            hadImageLoadError = true;
            fireImageLoadError(new IIOException(MessageFormat.format(java.util.ResourceBundle.getBundle(
                    "at/mountainsd/msdswing/Strings").
                    getString("Cannot_find_reader_for_content"),
                                                                     imageURL.toString())));
          }
        }
      } catch (Exception e) {
        hadImageLoadError = true;
        fireImageLoadError(e);
      } finally {
        if (!loadingInProcess) {
          fireEndImageLoad();
        }
        loadingInProcess = false;
      }
    }
    return result;
  }

  private BufferedImage internalLoadImage()
  {
    if (imageURL != null) {
      if (getAsyncLoad()) {
        Thread loader = new Thread(() -> {
          internalThreadedLoadImage(true);
        });
        loader.setDaemon(true);
        loader.start();
      } else {
        return internalThreadedLoadImage(false);
      }
    }
    return null;
  }

  private void internalImageStarted(ImageReader reader)
  {
    if (!testAbort(reader)) {
      fireBeginImageLoad();
    }
  }

  private void internalImageComplete()
  {
    loadingInProcess = false;
  }

  private void internalImageProgress(ImageReader reader,
                                     float percentDone)
  {
    if (!testAbort(reader)) {
      fireImageLoadProgress(percentDone);
    }
  }

  private boolean testAbort(ImageReader reader)
  {
    if (abortLoading) {
      reader.abort();
      fireLoadAbort();
      return true;
    }
    return false;
  }

  @Override
  public ImageLoader getImageLoader()
  {
    return imageLoader;
  }

  @Override
  public void setImageLoader(ImageLoader loader)
  {
    imageLoader = loader != null ? loader : new MyImageLoader();
  }

  @Override
  public FileFilter getFileFilter()
  {
    return new ImageFileFilter();
  }

  @Override
  public void abortLoading()
  {
    abortLoading = true;
  }

  @Override
  public ImageReaderFactory getImageReaderFactory()
  {
    return readerFactory;
  }

  @Override
  public void setImageReaderFactory(ImageReaderFactory factory)
  {
    if (factory != null) {
      readerFactory = factory;
    } else {
      readerFactory = new DefaultImageReaderFactory();
    }
  }

}
