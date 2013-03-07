/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel;

/**
 *
 * @author wolfi
 */
public interface Wagon extends Vehicle
{

  public static interface Builder<W extends Wagon> extends Vehicle.Builder<W>,BaseBuilder<W>
  {

    public Wagon.Builder<? extends Wagon> wagonNumber(String number);

    public Wagon.Builder<? extends Wagon> wheelCount(int wheelCount);

    public Wagon.Builder<? extends Wagon> kind(String kind);

    public Wagon.Builder<? extends Wagon> wagonClass(String clazz);

    public Wagon.Builder<? extends Wagon> company(String company);

    public Wagon.Builder<? extends Wagon> country(String country);

  }

  public static interface BuilderFactory
  {

    public Wagon.Builder<? extends Wagon> createBuilder();

  }

  public String getWagonNumber();

  public String getKind();

  public String getWagonClass();

  public String getCompany();

  public String getCountry();

  public int getWheelCount();

}
