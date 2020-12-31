/*
 * $Header: /CVSROOT/controls/msdswing/src/at/mountainsd/msdswing/helper/Scale.java,v 1.2 2008/01/19 15:57:17 wolfi Exp $
 *
 * Scale.java
 *
 * Author Wolfgang Reder (w.reder@mountain-sd.at)
 *
 * Copyright (c) 2008 Mountain Software Design KG
 *
 */
package at.or.reder.swing;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.text.MessageFormat;

public final class Scale implements Serializable
{

  public static final Scale IDENTITY = new Scale(1);
  public static final Scale SCALE_32_1 = new Scale(32);
  public static final Scale SCALE_16_1 = new Scale(16);
  public static final Scale SCALE_8_1 = new Scale(8);
  public static final Scale SCALE_4_1 = new Scale(4);
  public static final Scale SCALE_2_1 = new Scale(2);
  public static final Scale SCALE_1_2 = new Scale(1 / 2.);
  public static final Scale SCALE_1_4 = new Scale(1 / 4.);
  public static final Scale SCALE_1_8 = new Scale(1 / 8.);
  public static final Scale SCALE_1_16 = new Scale(1 / 16.);
  public static final Scale SCALE_1_32 = new Scale(1 / 32.);
  private static final long serialVersionUID = 1L;
  private double scaleX;
  private double scaleY;

  public Scale(double pScaleX,
               double pScaleY)
  {
    scaleX = pScaleX;
    scaleY = pScaleY;
  }

  public Scale(double pScale)
  {
    this(pScale,
         pScale);
  }

  public double getScaleX()
  {
    return scaleX;
  }

  public double getScaleY()
  {
    return scaleY;
  }

  public Rectangle scale(Rectangle rect)
  {
    double x = rect.x * scaleX;
    double y = rect.y * scaleY;
    double w = rect.width * scaleX;
    double h = rect.height * scaleY;
    return new Rectangle((int) Math.round(x),
                         (int) Math.round(y),
                         (int) Math.round(w),
                         (int) Math.round(h));
  }

  public Rectangle2D scale(Rectangle2D rect)
  {
    double x = rect.getX() * scaleX;
    double y = rect.getY() * scaleY;
    double w = rect.getWidth() * scaleX;
    double h = rect.getHeight() * scaleY;
    return new Rectangle2D.Double(x,
                                  y,
                                  w,
                                  h);
  }

  public Dimension2D scale(Dimension2D dim)
  {
    Dimension2D result = new Dimension(0,
                                       0);
    result.setSize(dim.getWidth() * scaleX,
                   dim.getHeight() * scaleY);
    return result;
  }

  public void scaleDimension(Dimension2D dim)
  {
    dim.setSize(dim.getWidth() * scaleX,
                dim.getHeight() * scaleY);
  }

  public double scaleX(double x)
  {
    return x * scaleX;
  }

  public int scaleX(int x)
  {
    return (int) (x * scaleX);
  }

  public double scaleY(double y)
  {
    return y * scaleY;
  }

  public int scaleY(int y)
  {
    return (int) (y * scaleY);
  }

  public Scale inverse()
  {
    return new Scale(1 / scaleX,
                     1 / scaleY);
  }

  @Override
  public String toString()
  {
    return MessageFormat.format("({0,number},{1,number})",
                                scaleX,
                                scaleY);
  }

  @Override
  public boolean equals(Object obj)
  {
    return (obj instanceof Scale) && ((Scale) obj).getScaleX() == scaleX && ((Scale) obj).getScaleY() == scaleY;
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 89 * hash + (int) (Double.doubleToLongBits(this.scaleX) ^ (Double.doubleToLongBits(this.scaleX) >>> 32));
    hash = 89 * hash + (int) (Double.doubleToLongBits(this.scaleY) ^ (Double.doubleToLongBits(this.scaleY) >>> 32));
    return hash;
  }

}
