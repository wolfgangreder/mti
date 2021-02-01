/*
 * Copyright 2017-2021 Wolfgang Reder.
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
package at.or.reder.swing;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import org.netbeans.api.annotations.common.NonNull;

/**
 *
 * @author Wolfgang Reder
 */
public final class AspectRatio extends Number
{

  public static final AspectRatio ASPECT_4_3 = new AspectRatio(4,
                                                               3);
  public static final AspectRatio ASPECT_3_2 = new AspectRatio(3,
                                                               2);
  public static final AspectRatio ASPECT_16_9 = new AspectRatio(16,
                                                                9);
  public static final AspectRatio ASPECT_3_4 = new AspectRatio(3,
                                                               4);
  public static final AspectRatio ASPECT_2_3 = new AspectRatio(2,
                                                               3);
  public static final AspectRatio ASPECT_9_16 = new AspectRatio(9,
                                                                16);
  public static final AspectRatio ASPECT_3_1 = new AspectRatio(3,
                                                               1);
  public static final AspectRatio ASPECT_1_1 = new AspectRatio(1,
                                                               1);
  public static final AspectRatio ASPECT_KEEP = new AspectRatio(-1,
                                                                1);
  public static final AspectRatio ASPECT_FREE = new AspectRatio(0,
                                                                1);
  private final double width;
  private final double height;

  public static AspectRatio valueOf(Dimension2D dim)
  {
    return new AspectRatio(dim.getWidth(),
                           dim.getHeight());
  }

  public static AspectRatio valueOf(Point2D pt)
  {
    return new AspectRatio(pt.getX(),
                           pt.getY());
  }

  private AspectRatio(double width,
                      double height)
  {
    this.width = width;
    this.height = height;
  }

  public double getX()
  {
    return width;
  }

  public double getY()
  {
    return height;
  }

  @Override
  public int intValue()
  {
    return Math.round(floatValue());
  }

  @Override
  public long longValue()
  {
    return Math.round(doubleValue());
  }

  @Override
  public float floatValue()
  {
    return (float) width / (float) height;
  }

  @Override
  public double doubleValue()
  {
    return width / height;
  }

  public <T extends Dimension2D> T adapt(@NonNull T dim)
  {
    double aspect = doubleValue();
    if (aspect <= 0) {
      return dim;
    }
    T result = (T) dim.clone();
    double h1 = dim.getWidth() / aspect;
    if (h1 <= dim.getHeight()) {
      result.setSize(dim.getWidth(),
                     Math.round(h1));
    } else {
      result.setSize(Math.round(dim.getHeight() * aspect),
                     dim.getHeight());
    }
    return result;
  }

  public <T extends Point2D.Double> T adapt(@NonNull T dim)
  {
    double aspect = doubleValue();
    if (aspect <= 0) {
      return dim;
    }
    T result = (T) dim.clone();
    double h1 = dim.getX() / aspect;
    if (h1 <= dim.getY()) {
      result.setLocation(dim.getX(),
                         h1);
    } else {
      result.setLocation(dim.getY() * aspect,
                         dim.getY());
    }
    return result;
  }

  public <T extends Point2D.Float> T adapt(@NonNull T dim)
  {
    double aspect = doubleValue();
    if (aspect <= 0) {
      return dim;
    }
    T result = (T) dim.clone();
    double h1 = dim.getX() / aspect;
    if (h1 <= dim.getY()) {
      result.setLocation(dim.getX(),
                         h1);
    } else {
      result.setLocation(dim.getY() * aspect,
                         dim.getY());
    }
    return result;
  }

  public <T extends Point2D> T adapt(@NonNull T dim)
  {
    double aspect = doubleValue();
    if (aspect <= 0) {
      return dim;
    }
    T result = (T) dim.clone();
    double h1 = dim.getX() / aspect;
    if (h1 <= dim.getY()) {
      result.setLocation(dim.getX(),
                         Math.round(h1));
    } else {
      result.setLocation(Math.round(dim.getY() * aspect),
                         dim.getY());
    }
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof AspectRatio)) {
      return false;
    }
    final AspectRatio other = (AspectRatio) obj;
    if (this.width != other.width) {
      return false;
    }
    return this.height == other.height;
  }

  @Override
  public int hashCode()
  {
    int hash = 5;
    hash = 37 * hash + (int) (Double.doubleToLongBits(this.width) ^ (Double.doubleToLongBits(this.width) >>> 32));
    hash = 37 * hash + (int) (Double.doubleToLongBits(this.height) ^ (Double.doubleToLongBits(this.height) >>> 32));
    return hash;
  }

  @Override
  public String toString()
  {
    return "AspectRatio{" + "numeration=" + width + ", denomiatior=" + height + '}';
  }

}
