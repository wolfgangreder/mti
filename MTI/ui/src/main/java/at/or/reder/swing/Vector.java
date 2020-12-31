/*
 * Copyright 2017 Wolfgang Reder.
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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import org.netbeans.api.annotations.common.NonNull;

public final class Vector
{

  private final double x;
  private final double y;
  private final double z;

  public static Vector of(double x,
                          double y)
  {
    return new Vector(x,
                      y,
                      0);
  }

  public static Vector of(double x,
                          double y,
                          double z)
  {
    return new Vector(x,
                      y,
                      z);
  }

  public static Vector of(@NonNull Point pt)
  {
    return new Vector(pt.x,
                      pt.y,
                      0);
  }

  public static Vector of(@NonNull Dimension2D dim)
  {
    return new Vector(dim.getWidth(),
                      dim.getHeight(),
                      0);
  }

  private Vector(double x,
                 double y,
                 double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @SuppressWarnings({"unchecked"})
  public <P extends Point2D> P scale(@NonNull P p)
  {
    P result = (P) p.clone();
    result.setLocation(p.getX() * x,
                       p.getY() * y);
    return result;
  }

  @SuppressWarnings({"unchecked"})
  public <D extends Dimension2D> D scale(@NonNull D d)
  {
    D result = (D) d.clone();
    result.setSize(d.getWidth() * x,
                   d.getHeight() * y);
    return result;
  }

  public Vector scale(@NonNull Vector v)
  {
    return new Vector(x * v.x,
                      y * v.y,
                      z * v.z);
  }

  public Vector multiply(double m)
  {
    return new Vector(x * m,
                      y * m,
                      z * m);
  }

  public Vector add(@NonNull Vector v)
  {
    return new Vector(x + v.x,
                      y + v.y,
                      z + v.z);
  }

  public Vector sub(@NonNull Vector v)
  {
    return new Vector(x - v.x,
                      y - v.y,
                      z - v.z);
  }

  public double length()
  {
    return Math.sqrt(x * x + y * y + z * z);
  }

  public double getX()
  {
    return x;
  }

  public double getY()
  {
    return y;
  }

  public double getZ()
  {
    return z;
  }

  public Point2D toPoint()
  {
    return new Point((int) x,
                     (int) y);
  }

  public Dimension2D toDimension()
  {
    return new Dimension((int) x,
                         (int) y);
  }

  @Override
  public int hashCode()
  {
    int hash = 3;
    hash = 73 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
    hash = 73 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
    hash = 73 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
    return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Vector)) {
      return false;
    }
    final Vector other = (Vector) obj;
    if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
      return false;
    }
    if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
      return false;
    }
    return Double.doubleToLongBits(this.z) == Double.doubleToLongBits(other.z);
  }

  @Override
  public String toString()
  {
    return "Vector{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
  }

}
