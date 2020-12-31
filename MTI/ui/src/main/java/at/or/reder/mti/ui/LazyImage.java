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
package at.or.reder.mti.ui;

import at.or.reder.mti.model.utils.IconDimension;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author Wolfgang Reder
 */
public final class LazyImage
{

  public static final long RESCAN_TIMEOUT = 10_000L;
  public static final boolean RESCAN = true;

  private static final class URLItem
  {

    private long timeout = 0;
    private URL url;
    private final String key;
    private final boolean createMissing;

    public URLItem(String key,
                   boolean createMissing)
    {
      this.key = key;
      this.createMissing = createMissing;
    }

    public URL getUrl()
    {
      if (url == null || (RESCAN && isTimedout())) {
        url = LazyImage._getImageURL(key,
                                     createMissing);
        timeout = System.currentTimeMillis() + RESCAN_TIMEOUT;
      }
      return url;
    }

    public boolean isTimedout()
    {
      return timeout < System.currentTimeMillis();
    }

  }

  private static final class ImageItem
  {

    private SoftReference<Image> image;
    private final URLItem urlItem;

    public ImageItem(URLItem url)
    {
      this.urlItem = url;
    }

    private URL getURL()
    {
      return urlItem.getUrl();
    }

    private synchronized Image getImage()
    {
      Image result = image != null ? image.get() : null;
      if (result == null || urlItem.isTimedout()) {
        URL url = getURL();
        if (url != null) {
          try (InputStream is = url.openStream()) {
            if (is != null) {
              ImageInputStream iis = ImageIO.createImageInputStream(is);
              result = ImageIO.read(iis);
            }
          } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
          }
        }
        if (result == null) {
          result = EMPTY;
        }
        image = new SoftReference<>(result);
      }
      return result;
    }

  }
  private static final Image EMPTY;
  private final static String CONFIG_ROOT;
  public final static boolean loadImages;

  static {
    Dimension dim = IconDimension.HUGE.getDimension();
    EMPTY = new BufferedImage(dim.width,
                              dim.height,
                              BufferedImage.TYPE_INT_ARGB);
    String tmp = null;
    try {
      tmp = FileUtil.toFile(FileUtil.getConfigRoot()).toString();
    } catch (Throwable th) {
      Exceptions.printStackTrace(th);
    }
    if (tmp == null) {
      tmp = new File("./.mti/config").getAbsolutePath();
    }
    CONFIG_ROOT = tmp;
    loadImages = Boolean.getBoolean("lazyimage.load");
  }

  private final List<ImageItem> images = new ArrayList<>();
  private boolean imagesScanned;
  private long nextImageScan;
  private final Map<IconDimension, SoftReference<Icon>> icons = new HashMap<>();
  private long nextIconScan;
  private final String imageKey;
  private final String iconKey;
  private final Map<IconDimension, URLItem> iconURLs = new ConcurrentHashMap<>();

  public LazyImage(Class<?> baseClass,
                   String imageKey)
  {
    this(baseClass,
         imageKey,
         null);
  }

  private static String makeAbsoultePath(Class<?> baseClass,
                                         String imageKey)
  {
    String packageName = "/" + baseClass.getPackage().getName().replace('.',
                                                                        '/');

    return packageName + "/" + imageKey;
  }

  public LazyImage(Class<?> baseClass,
                   String imageKey,
                   String iconKey)
  {
    this.imageKey = makeAbsoultePath(baseClass,
                                     Objects.requireNonNull(imageKey));
    this.iconKey = makeAbsoultePath(baseClass,
                                    Objects.requireNonNull(iconKey != null ? iconKey : imageKey));
  }

  public URL getImageURL(int index)
  {
    scanImages();
    if (index < images.size()) {
      return images.get(index).getURL();
    }
    return null;
  }

  private static boolean hasRoot(String name)
  {
    int lastSlash = name.lastIndexOf(File.separatorChar);
    if (lastSlash >= 0) {
      String tmp = name.substring(0,
                                  lastSlash);
      if (tmp.startsWith(File.separator)) {
        tmp = tmp.substring(1);
      }

      Path path = Paths.get(CONFIG_ROOT,
                            tmp);
      return Files.isDirectory(path);
    }
    return false;

  }

  private static Path ensureRoot(String name)
  {
    int lastSlash = name.lastIndexOf(File.separatorChar);
    if (lastSlash >= 0) {
      try {
        String tmp = name.substring(0,
                                    lastSlash);
        if (tmp.startsWith(File.separator)) {
          tmp = tmp.substring(1);
        }

        Path path = Paths.get(CONFIG_ROOT,
                              tmp);
        return Files.createDirectories(path);
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return null;
  }

  private static URL getURLFromConfig(String name)
  {
    Path path = Paths.get(CONFIG_ROOT,
                          name);
    if (Files.isReadable(path)) {
      try {
        return path.toUri().toURL();
      } catch (MalformedURLException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return null;
  }

  private static URL _getImageURL(String name,
                                  boolean createMissing)
  {
    if (!loadImages) {
      return null;
    }
    URL result;
    if (hasRoot(name)) {
      result = getURLFromConfig(name);
      if (result != null) {
        return result;
      }
    }
    result = LazyImage.class.getResource(name);
    if (result == null && createMissing && !name.contains("UNKNOWN")) {
      ensureRoot(name);
      Path path = Paths.get(CONFIG_ROOT,
                            name);
      path = Paths.get(path.toString() + ".missing");
      try {
        if (!Files.exists(path)) {
          Files.createFile(path);
        }
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return result;
  }

  private List<ImageItem> getImageURLs()
  {
    int dotPos = imageKey.lastIndexOf('.');
    String name;
    String ext;
    if (dotPos > 0) {
      name = imageKey.substring(0,
                                dotPos);
      ext = imageKey.substring(dotPos);
    } else {
      name = imageKey;
      ext = null;
    }
    List<ImageItem> result = new ArrayList<>();
    URL u;
    int i = 0;
    do {
      String fullName = buildName(name,
                                  ext,
                                  Integer.toString(i++));
      u = _getImageURL(fullName,
                       true);
      if (u != null) {
        result.add(new ImageItem(new URLItem(fullName,
                                             true)));
      }
    } while (u != null && i < 4);
    return result;
  }

  private void scanImages()
  {
    synchronized (this) {
      if (!imagesScanned || (System.currentTimeMillis() > nextImageScan)) {
        images.clear();
        images.addAll(getImageURLs());
        imagesScanned = true;
        nextImageScan = System.currentTimeMillis() + RESCAN_TIMEOUT;
      }
    }
  }

  public Image getImage(int index)
  {
    scanImages();
    if (index <= images.size()) {
      return images.get(index).getImage();
    }
    return EMPTY;
  }

  public int getImageCount()
  {
    scanImages();
    return images.size();
  }

  private String buildName(String name,
                           String ext,
                           String toInsert)
  {
    if (ext != null) {
      return name + "_" + toInsert + ext;
    } else {
      return name + "_" + toInsert;
    }
  }

  private void checkIconScan()
  {
    synchronized (this) {
      if (RESCAN && System.currentTimeMillis() > nextIconScan) {
        iconURLs.clear();
        this.icons.clear();
        nextIconScan = System.currentTimeMillis() + RESCAN_TIMEOUT;
      }
    }
  }

  public URL getIconURL(IconDimension dim)
  {
    checkIconScan();
    URLItem item = iconURLs.computeIfAbsent(dim,
                                            this::computeIconURL);
    return item != null ? item.getUrl() : null;
  }

  private URLItem computeIconURL(IconDimension dim)
  {
    int dotPos = iconKey.lastIndexOf('.');
    String name;
    String ext;
    boolean createMissing;
    if (dotPos > 0) {
      name = iconKey.substring(0,
                               dotPos);
      ext = iconKey.substring(dotPos);
    } else {
      name = iconKey;
      ext = null;
    }
    String fname = buildName(name,
                             ext,
                             dim.name());
    URL result = _getImageURL(fname,
                              createMissing = (dim == IconDimension.HUGE));
    if (result == null) {
      for (int i = dim.ordinal(); result == null && i < IconDimension.values().length; ++i) {
        IconDimension ic = IconDimension.values()[i];
        fname = buildName(name,
                          ext,
                          ic.name());
        result = _getImageURL(fname,
                              createMissing = (ic == IconDimension.HUGE));
      }
    }
    if (result == null) {
      fname = iconKey;
      result = _getImageURL(iconKey,
                            false);
      createMissing = false;
    }
    return result != null ? new URLItem(fname,
                                        createMissing) : null;
  }

  private InputStream findIconImage(IconDimension dim)
  {
    URL url = getIconURL(dim);
    if (url != null) {
      try {
        return url.openStream();
      } catch (IOException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return null;
  }

  private Image loadIcon(IconDimension dim)
  {
    Image result = EMPTY;
    try (InputStream is = findIconImage(dim)) {
      if (is != null) {
        ImageInputStream iis = ImageIO.createImageInputStream(is);
        result = ImageIO.read(iis);
      }
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
    return result;
  }

  public synchronized Icon getIcon(IconDimension dimension)
  {
    checkIconScan();
    SoftReference<Icon> ref = icons.get(dimension);
    Icon result = ref != null ? ref.get() : null;
    if (result == null) {
      Dimension dim = dimension.getDimension();
      Image img = loadIcon(dimension);
      BufferedImage tmpImage = new BufferedImage(dim.width,
                                                 dim.height,
                                                 BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = tmpImage.createGraphics();
      try {
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                           RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                           RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                           RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(img,
                    0,
                    0,
                    dim.width,
                    dim.height,
                    0,
                    0,
                    img.getWidth(null),
                    img.getHeight(null),
                    null);
      } finally {
        g.dispose();
      }

      result = new ImageIcon(tmpImage);
      icons.put(dimension,
                new SoftReference<>(result));
    }
    return result;
  }

}
