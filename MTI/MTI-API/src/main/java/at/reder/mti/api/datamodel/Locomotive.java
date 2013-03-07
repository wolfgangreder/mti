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
public interface Locomotive extends Vehicle
{

  public static interface Builder<L extends Locomotive> extends Vehicle.Builder<L>,
                                                                BaseBuilder<L>
  {

    public Locomotive.Builder<? extends Locomotive> locomotiveNumber(String number);

    public Locomotive.Builder<? extends Locomotive> wheelArrangement(String arrangement);

    public Locomotive.Builder<? extends Locomotive> kind(String kind);

    public Locomotive.Builder<? extends Locomotive> locomotiveClass(String clazz);

    public Locomotive.Builder<? extends Locomotive> company(String company);

    public Locomotive.Builder<? extends Locomotive> country(String country);

  }

  public static interface BuilderFactory
  {

    public Locomotive.Builder<? extends Locomotive> createBuilder();

  }

  public String getLocomotiveNumber();

  public String getWheelArrangement();

  public String getKind();

  public String getLocomotiveClass();

  public String getCompany();

  public String getCountry();

}
