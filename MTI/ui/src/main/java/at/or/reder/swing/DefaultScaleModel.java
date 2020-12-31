/*
 * $Id$
 *
 * Author Wolfgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008-2010 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DefaultScaleModel implements ScaleModel, Serializable
{

  private static final long serialVersionUID = 1L;
  private Scale scale;
  private ScaleOptions options;
  private final List<ScaleListener> scaleListeners = new ArrayList<>();
  private boolean keepAspectRatio;

  public DefaultScaleModel()
  {
    scale = Scale.IDENTITY;
    options = ScaleOptions.NO_SCALE;
    keepAspectRatio = true;
  }

  @Override
  public Scale getCurrentScale(Dimension componentDimension,
                               Dimension imageDimension)
  {
    return getCurrentScale(componentDimension.getWidth(),
                           componentDimension.getHeight(),
                           imageDimension.getWidth(),
                           imageDimension.getHeight());
  }

  @Override
  public Scale getCurrentScale(double compWidth,
                               double compHeight,
                               double imgWidth,
                               double imgHeight)
  {
    Scale s;
    switch (options) {
      case SCALE_IMAGE:
        s = calcScale(compWidth,
                      compHeight,
                      imgWidth,
                      imgHeight);
        break;
      case SCALE_CONTROL:
        s = getScale();
        break;
      default:
        s = Scale.IDENTITY;
    }
    setScale(s);
    return getScale();
  }

  @Override
  public ScaleOptions getScaleOptions()
  {
    return options;
  }

  @Override
  public void setScaleOptions(ScaleOptions opt)
  {
    if (opt != options) {
      options = opt;
      if (options == ScaleOptions.NO_SCALE) {
        scale = Scale.IDENTITY; // event wird ja sowieso gefeuert
      }
      fireScaleChanged();
    }
  }

  @Override
  public Scale getScale()
  {
    return scale != null ? scale : Scale.IDENTITY;
  }

  @Override
  public void setScale(Scale pScale)
  {
    if (pScale != scale) {
      scale = pScale;
      options = ScaleOptions.SCALE_CONTROL;
      fireScaleChanged();
    }
  }

  @Override
  public Dimension getScaledSize(Dimension dim)
  {
    return getScaledSize(dim.getWidth(),
                         dim.getHeight(),
                         null);
  }

  @Override
  public Dimension getScaledSize(Dimension dim,
                                 Dimension rv)
  {
    return getScaledSize(dim.getWidth(),
                         dim.getHeight(),
                         rv);
  }

  @Override
  public Dimension getScaledSize(double w,
                                 double h)
  {
    return getScaledSize(w,
                         h,
                         null);
  }

  @Override
  public Dimension getScaledSize(double w,
                                 double h,
                                 Dimension rv)
  {
    Scale myscale = getScale();
    Dimension result = rv != null ? rv : new Dimension();
    result.setSize(myscale.scaleX(w),
                   myscale.scaleY(h));
    return result;
  }

  protected void fireScaleChanged()
  {
    for (ScaleListener l : scaleListeners) {
      l.scaleChanged(this);
    }
  }

  @Override
  public void addScaleListener(ScaleListener l)
  {
    if (l != null && !scaleListeners.contains(l)) {
      scaleListeners.add(l);
    }
  }

  @Override
  public void removeScaleListener(ScaleListener l)
  {
    scaleListeners.remove(l);
  }

  @Override
  public boolean getKeepAspectRatio()
  {
    return keepAspectRatio;
  }

  @Override
  public void setKeepAspectRatio(boolean keep)
  {
    if (keepAspectRatio != keep) {
      keepAspectRatio = keep;
      fireScaleChanged();
    }
  }

  private Scale calcScale(double compW,
                          double compH,
                          double imgW,
                          double imgH)
  {
    if (imgW != 0 && imgH != 0) {
      double scaleX = compW / imgW;
      double scaleY = compH / imgH;
      if (getKeepAspectRatio()) {
        return new Scale(Math.min(scaleX,
                                  scaleY));
      } else {
        return new Scale(scaleX,
                         scaleY);
      }
    }
    return Scale.IDENTITY;
  }

}
