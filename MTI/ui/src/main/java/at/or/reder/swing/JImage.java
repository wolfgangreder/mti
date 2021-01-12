/*
 * $Id$
 *
 * JImage.java
 *
 * Author Wolfgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008-2014 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import at.or.reder.swing.model.JImageModel;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.beans.BeanProperty;
import java.net.URL;
import java.util.Objects;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class JImage extends JComponent implements Scrollable,
                                                  ImageLoadListener,
                                                  ScaleListener
{

  public static final String PROPERTY_SELECTION = "selection";
  public static final String PROPERTY_MARKER = "marker";
  public static final double GOLDEN = 0.5 + Math.sqrt(5.0) / 2.0;
  public static final String PROPERTY_HORIZONTAL_ALIGMENT = "horizontalAligment";
  public static final String PROPERTY_VERTICAL_ALIGMENT = "verticalAligment";
  private static final long serialVersionUID = 1L;
  private int horizontalAligment;
  private int verticalAligment;
  private final int maxUnitIncrement = 10;
  private JImageModel model;
  private ScaleModel scaleModel;
  private GridOption gridOption = GridOption.NO_GRID;
  private Color gridColor = Color.BLACK;
  private boolean useMouseDrag = false;
  private final MouseMotionListener dragListener = new MouseAdapter()
  {
    @Override
    public void mouseDragged(MouseEvent e)
    {
      Rectangle r = new Rectangle(e.getX(),
                                  e.getY(),
                                  1,
                                  1);
      scrollRectToVisible(r);
    }

  };
  private final MouseAdapter ml = new MouseAdapter()
  {
    @Override
    public void mouseDragged(MouseEvent e)
    {
      if (allowSelection && selectionStart != null) {
        selectionEnd = e.getPoint();
        calculateSelection();
        Graphics g = getGraphics();
        try {
          paint(g);
        } finally {
          g.dispose();
        }
      }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
      if (allowSelection && selectionStart != null) {
        selectionEnd = e.getPoint();
        inSelectionDrag = false;
        repaint();
        SwingUtilities.invokeLater(() -> {
          calculateSelection();
          firePropertyChange(PROPERTY_SELECTION,
                             null,
                             getSelection());
        });
      }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
      if (allowSelection) {
        selectionStart = e.getPoint();
        selectionEnd = null;
        inSelectionDrag = true;
      }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
      selectionStart = null;
      selectionEnd = null;
      inSelectionDrag = false;
      repaint();
      firePropertyChange(PROPERTY_SELECTION,
                         null,
                         getSelection());
    }

  };
  private volatile Point selectionStart;
  private volatile Point selectionEnd;
  private boolean allowSelection;
  private final Stroke selectionStroke = new BasicStroke(1,
                                                         BasicStroke.CAP_BUTT,
                                                         BasicStroke.JOIN_BEVEL,
                                                         1,
                                                         new float[]{3f, 1f},
                                                         0f);
  private boolean inSelectionDrag;
  private Rectangle selectionImage;
  private Rectangle marker;

  public JImage()
  {
    this(new DefaultJImageModel(),
         new DefaultScaleModel());
  }

  public JImage(JImageModel pModel,
                ScaleModel pScaleModel)
  {
    horizontalAligment = SwingConstants.LEFT;
    verticalAligment = SwingConstants.TOP;
    setModel(pModel);
    setScaleModel(pScaleModel);
  }

  public Rectangle getMarker()
  {
    return marker;
  }

  public void setMarker(Rectangle newMarker)
  {
    boolean changed;
    Rectangle oldMarker = marker;
    if (newMarker != null) {
      if (changed = !Objects.equals(newMarker,
                                    marker)) {
        marker = newMarker;
      }
    } else {
      changed = marker != null;
      marker = null;
    }
    if (changed) {
      firePropertyChange(PROPERTY_MARKER,
                         oldMarker,
                         newMarker);
      repaint();
    }
  }

  @BeanProperty(bound = false, preferred = true, visualUpdate = false)
  public boolean isSelectionAllowed()
  {
    return allowSelection;
  }

  public void setSelectionAllowed(boolean sa)
  {
    if (allowSelection != sa) {
      allowSelection = sa;
      selectionStart = null;
      selectionEnd = null;
      if (allowSelection) {
        addMouseListener(ml);
        addMouseMotionListener(ml);
      } else {
        removeMouseListener(ml);
        removeMouseMotionListener(ml);
      }
    }
  }

  /**
   * Returns selection in view coordinates
   *
   * @return
   */
  public Rectangle getSelection()
  {
    return selectionImage;
  }

  public void clearSelection()
  {
    selectionImage = null;
    selectionStart = null;
    selectionEnd = null;
    inSelectionDrag = false;
  }

  private Rectangle calculateSelection()
  {
    if (selectionStart != null && selectionEnd != null) {
      BufferedImage img = getImage();
      if (img == null) {
        return null;
      }
      int width = img.getWidth();
      int height = img.getHeight();
      int minX = rangeLimit(Math.min(selectionStart.x,
                                     selectionEnd.x),
                            width,
                            0);
      int minY = rangeLimit(Math.min(selectionStart.y,
                                     selectionEnd.y),
                            height,
                            0);
      int maxX = rangeLimit(Math.max(selectionStart.x,
                                     selectionEnd.x),
                            width,
                            0);
      int maxY = rangeLimit(Math.max(selectionStart.y,
                                     selectionEnd.y),
                            height,
                            0);
      Rectangle result = new Rectangle(minX,
                                       minY,
                                       maxX - minX,
                                       maxY - minY);
      selectionImage = toImageCoord(result);
      return result;
    }
    selectionImage = null;
    return null;
  }

  @BeanProperty(bound = false, preferred = true, visualUpdate = false)
  public boolean isMouseDragEnabled()
  {
    return useMouseDrag;
  }

  public void setMouseDragEnabled(boolean mde)
  {
    if (mde != this.useMouseDrag) {
      this.useMouseDrag = mde;
      setAutoscrolls(mde);
      if (mde) {
        addMouseMotionListener(dragListener);
      } else {
        removeMouseMotionListener(dragListener);
      }
    }
  }

  public final JImageModel getModel()
  {
    return model;
  }

  public final void setModel(JImageModel pModel)
  {
    if (model != pModel) {
      if (model != null) {
        model.removeImageLoadListener(this);
      }
      model = pModel;
      if (model != null) {
        model.addImageLoadListener(this);
      }
    }
  }

  public final ScaleModel getScaleModel()
  {
    return scaleModel;
  }

  public final void setScaleModel(ScaleModel pModel)
  {
    if (scaleModel != pModel) {
      if (scaleModel != null) {
        scaleModel.removeScaleListener(this);
      }
      scaleModel = pModel != null ? pModel : new DefaultScaleModel();
      if (scaleModel != null) {
        scaleModel.addScaleListener(this);
      }
    }
  }

  public int getHorizontalAligment()
  {
    return horizontalAligment;
  }

  public void setHorizontalAligment(int align)
  {
    if (align != horizontalAligment) {
      if (align != SwingConstants.LEFT && align != SwingConstants.RIGHT && align != SwingConstants.CENTER) {
        throw new IllegalArgumentException();
      }
      int old = horizontalAligment;
      horizontalAligment = align;
      firePropertyChange(PROPERTY_HORIZONTAL_ALIGMENT,
                         old,
                         horizontalAligment);
    }
  }

  public int getVerticalAligment()
  {
    return verticalAligment;
  }

  public void setVerticalAligment(int align)
  {
    if (align != verticalAligment) {
      if (align != SwingConstants.TOP && align != SwingConstants.BOTTOM && align != SwingConstants.CENTER) {
        throw new IllegalArgumentException();
      }
      int old = verticalAligment;
      verticalAligment = align;
      firePropertyChange(PROPERTY_VERTICAL_ALIGMENT,
                         old,
                         verticalAligment);
    }
  }

  public BufferedImage getImage()
  {
    return model != null ? model.getImage() : null;
  }

  public void setImage(BufferedImage image)
  {
    if (model != null) {
      model.setImage(image);
    }
  }

  public URL getImageURL()
  {
    return model != null ? model.getImageURL() : null;
  }

  public void setImageURL(URL pURL)
  {
    if (model != null) {
      model.setImageURL(pURL);
    }
  }

  @Override
  public Dimension getPreferredScrollableViewportSize()
  {
    return getPreferredSize();
  }

  @Override
  public int getScrollableUnitIncrement(Rectangle visibleRect,
                                        int orientation,
                                        int direction)
  {
    int currentPosition;
    if (orientation == SwingConstants.HORIZONTAL) {
      currentPosition = visibleRect.x;
    } else {
      currentPosition = visibleRect.y;
    }

    //Return the number of pixels between currentPosition
    //and the nearest tick mark in the indicated direction.
    if (direction < 0) {
      int newPosition = currentPosition - (currentPosition / maxUnitIncrement) * maxUnitIncrement;
      return (newPosition == 0) ? maxUnitIncrement : newPosition;
    } else {
      return ((currentPosition / maxUnitIncrement) + 1) * maxUnitIncrement - currentPosition;
    }
  }

  @Override
  public int getScrollableBlockIncrement(Rectangle visibleRect,
                                         int orientation,
                                         int direction)
  {
    if (orientation == SwingConstants.HORIZONTAL) {
      return visibleRect.width - maxUnitIncrement;
    } else {
      return visibleRect.height - maxUnitIncrement;
    }
  }

  @Override
  public boolean getScrollableTracksViewportWidth()
  {
    return false;
  }

  @Override
  public boolean getScrollableTracksViewportHeight()
  {
    return false;
  }

  @Override
  public Dimension getPreferredSize()
  {
    if (scaleModel != null && scaleModel.getScaleOptions() == ScaleOptions.SCALE_CONTROL && model != null) {
      return scaleModel.getScaledSize(model.getImageWidth(),
                                      model.getImageHeight());
    }
    Container parent = getParent();
    if (parent instanceof JScrollPane) {
      return ((JScrollPane) parent).getViewport().getSize();
    } else if (parent instanceof JViewport) {
      return ((JViewport) parent).getSize();
    } else {
      return super.getPreferredSize();
    }
  }

  private void setHints(Graphics2D g2)
  {
    if (inSelectionDrag) {
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                          RenderingHints.VALUE_ANTIALIAS_ON);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                          RenderingHints.VALUE_RENDER_SPEED);
    } else {
      g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                          RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                          RenderingHints.VALUE_RENDER_QUALITY);
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                          RenderingHints.VALUE_ANTIALIAS_ON);

    }
  }

  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    BufferedImage theImage = model.getImage();
    if (theImage != null) {
      Scale scale = scaleModel.getCurrentScale(this.getSize(),
                                               new Dimension(theImage.getWidth(this),
                                                             theImage.getHeight(this)));
      int w = scale.scaleX(theImage.getWidth(this));
      int h = scale.scaleY(theImage.getHeight(this));
      int x = getXPosition(w);
      int y = getYPosition(h);
      Graphics2D g2 = (Graphics2D) g;
      g2.setComposite(AlphaComposite.Src);
      setHints(g2);
      g.drawImage(theImage,
                  x,
                  y,
                  w,
                  h,
                  this);

      paintGrid(g,
                x,
                y,
                w,
                h);
      if (allowSelection) {
        paintSelection(g2);
      }
      paintMarker(g2);
    }
  }

  private void paintMarker(Graphics g)
  {
    if (marker != null) {
      Graphics2D g2 = (Graphics2D) g.create();
      try {
        g2.setColor(Color.YELLOW);
        g2.setStroke(selectionStroke);
        g2.draw(toViewportCoord(marker));
      } finally {
        g2.dispose();
      }
    }

  }

  private void paintSelection(Graphics g)
  {
    Rectangle2D sel = getSelection();
    if (sel != null) {
      Graphics2D g2 = (Graphics2D) g.create();
      try {
        Scale scale = getScale();
        sel = scale.scale(sel);
        g2.setColor(gridColor);
        g2.setStroke(selectionStroke);
        g2.draw(sel);
      } finally {
        g2.dispose();
      }
    }
  }

  protected int getXPosition(BufferedImage img)
  {
    return img != null && horizontalAligment != SwingConstants.LEFT ? getXPosition(img.getWidth(this))
                   : 0;
  }

  protected int getXPosition(int x)
  {
    switch (horizontalAligment) {
      case SwingConstants.LEFT:
        return 0;
      case SwingConstants.CENTER:
        return (getWidth() - x) / 2;
      case SwingConstants.RIGHT:
        return getWidth() - x;
    }
    return 0;
  }

  protected int getYPosition(BufferedImage img)
  {
    return img != null && verticalAligment != SwingConstants.TOP ? getYPosition(img.getHeight(this))
                   : 0;
  }

  protected int getYPosition(int y)
  {
    switch (verticalAligment) {
      case SwingConstants.TOP:
        return 0;
      case SwingConstants.CENTER:
        return (getHeight() - y) / 2;
      case SwingConstants.BOTTOM:
        return getHeight() - y;
    }
    return 0;
  }

  @Override
  public void imageLoadError(JImageModel sender,
                             Exception e)
  {
  }

  @Override
  public void beginImageLoad(JImageModel sender)
  {
  }

  @Override
  public void endImageLoad(JImageModel sender)
  {
    revalidate();
    repaint();
  }

  @Override
  public void imageLoadProgress(JImageModel sender,
                                float percentDone)
  {
  }

  @Override
  public void imageChanged(JImageModel sender)
  {
    if (marker != null) {
      Rectangle oldMarker = marker;
      marker = null;
      firePropertyChange(PROPERTY_MARKER,
                         oldMarker,
                         marker);
    }
    if (selectionStart != null || selectionEnd != null) {
      selectionStart = null;
      selectionEnd = null;
      inSelectionDrag = false;
      firePropertyChange(PROPERTY_SELECTION,
                         selectionImage,
                         null);
      selectionImage = null;
    }
    revalidate();
    repaint();
  }

  @Override
  public void scaleChanged(ScaleModel model)
  {
    revalidate();
    repaint();
  }

  public GridOption getGridOption()
  {
    return gridOption;
  }

  public void setGridOption(GridOption g)
  {
    gridOption = g;
    repaint();
  }

  private void paintGrid(Graphics g,
                         int x,
                         int y,
                         int w,
                         int h)
  {
    switch (gridOption) {
      case GRID_DEC:
        printGrid(g,
                  10,
                  x,
                  y,
                  w,
                  h);
        break;
      case GRID_GOLDEN:
        printGoldenGrid(g,
                        x,
                        y,
                        w,
                        h);
        break;
      case GRID_QUAD:
        printGrid(g,
                  4,
                  x,
                  y,
                  w,
                  h);
        break;
      case GRID_THIRD:
        printGrid(g,
                  3,
                  x,
                  y,
                  w,
                  h);
        break;
    }
  }

  private void printGrid(Graphics g,
                         int num,
                         int x,
                         int y,
                         int w,
                         int h)
  {
    double diff = ((double) w) / ((double) num);
    g.setColor(gridColor);
    for (double r = diff + x; r < w + x; r += diff) {
      g.drawLine((int) r,
                 y,
                 (int) r,
                 y + h);
    }
    diff = ((double) h) / ((double) num);
    for (double r = diff + y; r < h + y; r += diff) {
      g.drawLine(x,
                 (int) r,
                 x + w,
                 (int) r);
    }
  }

  private void printGoldenGrid(Graphics g,
                               int x,
                               int y,
                               int w,
                               int h)
  {
    double b = w / (1. + GOLDEN);
    g.setColor(gridColor);
    g.drawLine(x + (int) b,
               y,
               x + (int) b,
               y + h);
    g.drawLine(x + w - (int) b,
               y,
               x + w - (int) b,
               y + h);
    b = h / (1. + GOLDEN);
    g.drawLine(x,
               y + (int) b,
               x + w,
               y + (int) b);
    g.drawLine(x,
               y + h - (int) b,
               x + w,
               y + h - (int) b);
  }

  public Color getGridColor()
  {
    return gridColor;
  }

  public void setGridColor(Color col)
  {
    gridColor = col != null ? col : Color.BLACK;
  }

  @Override
  public void loadAborted(JImageModel sender)
  {
    sender.setImageURL(null);
  }

  public ScaleOptions getScaleOptions()
  {
    return scaleModel.getScaleOptions();
  }

  public void setScaleOptions(ScaleOptions so)
  {
    scaleModel.setScaleOptions(so);
  }

  public boolean getKeepAspectRatio()
  {
    return scaleModel.getKeepAspectRatio();
  }

  public void setKeepAspectRatio(boolean kepp)
  {
    scaleModel.setKeepAspectRatio(kepp);
  }

  public Scale getScale()
  {
    return scaleModel.getScale();
  }

  public void setScale(Scale pScale)
  {
    scaleModel.setScale(pScale);
  }

  /**
   * Converts viewport coordinates to image coordinates
   *
   * @param rect viewport coordinates
   * @return image cooridnates
   */
  public Rectangle toImageCoord(Rectangle rect)
  {
    if (rect == null) {
      return null;
    }
    Scale scale = getScale().inverse();
    return scale.scale(rect);
  }

  /**
   * Converts image coordinates to image coordinates
   *
   * @param rect image coordinats
   * @return viewport coordinates
   */
  public Rectangle toViewportCoord(Rectangle rect)
  {
    if (rect == null) {
      return null;
    }
    return getScale().scale(rect);
  }

  /**
   * Converts image coordinates to relative image coordinates
   *
   * @param rect image coordinates
   * @return relative coordinates
   */
  public Rectangle2D toRelative(Rectangle rect)
  {
    if (rect == null) {
      return null;
    }
    BufferedImage image = getImage();
    double width = image.getWidth();
    double height = image.getHeight();
    return new Rectangle2D.Double(rect.getX() / width,
                                  rect.getY() / height,
                                  rect.getWidth() / width,
                                  rect.getHeight() / height);
  }

  /**
   * Converts relative image coordinates to absolute image coords.
   *
   * @param rect relative coordinates
   * @return absolute image coordinates
   */
  public Rectangle fromRelative(Rectangle2D rect)
  {
    if (rect == null) {
      return null;
    }
    BufferedImage image = getImage();
    double width = image.getWidth();
    double height = image.getHeight();
    return new Rectangle((int) (rect.getX() * width),
                         (int) (rect.getY() * height),
                         (int) (rect.getWidth() * width),
                         (int) (rect.getHeight() * height));
  }

  /**
   * Returns a subimage.
   *
   * @param rect Bounds of subimage in absolute image coordinates
   * @return subimage
   */
  public BufferedImage getSnapshot(Rectangle rect)
  {
    BufferedImage savedImage = getImage();
    BufferedImage result = new BufferedImage((int) rect.getWidth(),
                                             (int) rect.getHeight(),
                                             savedImage.getType());
    Graphics2D g = result.createGraphics();
    try {
      g.drawImage(savedImage,
                  0,
                  0,
                  result.getWidth(),
                  result.getHeight(),
                  rect.x,
                  rect.y,
                  rect.x + rect.width,
                  rect.y + rect.height,
                  null);
    } finally {
      g.dispose();
    }
    return result;
  }

  public Color getPixelColor(Point point)
  {
    return getColor(getImage().getRaster(),
                    point);
  }

  /**
   * Returns a subimage
   *
   * @param rect Bounds of subimage in relative image coordinates
   * @return
   */
  public BufferedImage getSnapshotRelative(Rectangle2D rect)
  {
    return getSnapshot(fromRelative(rect));
  }

  public static double rangeLimit(double value,
                                  double maxValue,
                                  double minValue)
  {
    double result = value;
    if (result < minValue) {
      result = minValue;
    }
    if (result > maxValue) {
      result = maxValue;
    }
    return result;
  }

  public static int rangeLimit(int value,
                               int maxValue,
                               int minValue)
  {
    int result = value;
    if (result < minValue) {
      result = minValue;
    }
    if (result > maxValue) {
      result = maxValue;
    }
    return result;
  }

  public static Color getColor(Raster raster,
                               Point center)
  {
    return getColor(raster,
                    center,
                    10);
  }

  public static Color getColor(Raster raster,
                               Point center,
                               int kernelSize)
  {
    int xs = center.x - kernelSize / 2;
    int ys = center.y - kernelSize / 2;
    int[] pixelData = null;
    pixelData = raster.getPixels(xs,
                                 ys,
                                 kernelSize,
                                 kernelSize,
                                 pixelData);
    int red = 0;
    int green = 0;
    int blue = 0;

    for (int i = 0; i < pixelData.length;) {
      red += pixelData[i++];
      green += pixelData[i++];
      blue += pixelData[i++];
    }
    int pixelCount = pixelData.length / 3;
    return new Color(red / pixelCount,
                     green / pixelCount,
                     blue / pixelCount);
  }

}
