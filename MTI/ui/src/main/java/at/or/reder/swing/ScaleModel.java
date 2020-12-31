/*
 * $Header: /CVSROOT/controls/msdswing/src/at/mountainsd/msdswing/ScaleModel.java,v 1.1 2008/01/19 15:56:52 wolfi Exp $
 *
 * ScaleModel.java
 *
 * Author Wolfgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import java.awt.Dimension;

public interface ScaleModel
{

  public Scale getCurrentScale(Dimension compDimension,
                               Dimension imageDimension);

  public Scale getCurrentScale(double compWidth,
                               double compHeight,
                               double imgWidth,
                               double imgHeight);

  public ScaleOptions getScaleOptions();

  public void setScaleOptions(ScaleOptions opt);

  public Scale getScale();

  public void setScale(Scale pScale);

  public Dimension getScaledSize(Dimension dim);

  public Dimension getScaledSize(Dimension dim,
                                 Dimension rd);

  public Dimension getScaledSize(double w,
                                 double h);

  public Dimension getScaledSize(double w,
                                 double h,
                                 Dimension rd);

  public void addScaleListener(ScaleListener l);

  public void removeScaleListener(ScaleListener l);

  public boolean getKeepAspectRatio();

  public void setKeepAspectRatio(boolean kepp);

}
