/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.Decoder;
import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.Era;
import at.reder.mti.api.datamodel.Wagon;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.datamodel.Scale;
import at.reder.mti.api.datamodel.ServiceEntry;
import at.reder.mti.api.datamodel.xml.XWagon;
import at.reder.mti.api.utils.Money;
import at.reder.mti.api.utils.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author wolfi
 */
@ServiceProvider(service = Wagon.BuilderFactory.class)
public final class DefaultWagonBuilderFactory implements Wagon.BuilderFactory
{

  public static final class DefaultWagon extends AbstractVehicle
          implements Wagon
  {

    private final String number;
    private final int wheelcount;
    private final String kind;
    private final String clazz;
    private final String company;
    private final String country;

    private DefaultWagon(UUID id,
                         String name,
                         ModelCondition condition,
                         Timestamp dateOfPurchase,
                         String description,
                         Timestamp lastModified,
                         Contact manufacturer,
                         Entity masterImage,
                         Money price,
                         String productNumber,
                         Contact retailer,
                         Collection<? extends Entity> entities,
                         Era era,
                         double length,
                         double width,
                         double height,
                         double weight,
                         Collection<? extends ServiceEntry> serviceEntries,
                         Collection<? extends Decoder> decoder,
                         Scale scale,
                         Collection<? extends Object> lookupContent,
                         String number,
                         int wheelcount,
                         String kind,
                         String clazz,
                         String company,
                         String country)
    {
      super(id, name, condition, dateOfPurchase, description, lastModified, manufacturer, masterImage, price, productNumber,
            retailer, entities, era, length, width, height, weight, serviceEntries, decoder, scale, lookupContent);
      this.number = number;
      this.wheelcount = wheelcount;
      this.kind = kind;
      this.clazz = clazz;
      this.company = company;
      this.country = country;
    }

    @Override
    public String getWagonNumber()
    {
      return number;
    }

    @Override
    public int getWheelCount()
    {
      return wheelcount;
    }

    @Override
    public String getKind()
    {
      return kind;
    }

    @Override
    public String getWagonClass()
    {
      return clazz;
    }

    @Override
    public String getCompany()
    {
      return company;
    }

    @Override
    public String getCountry()
    {
      return country;
    }

  }

  public static final class DefaultBuilder extends AbstractVehicleBuilder<Wagon>
          implements Wagon.Builder<Wagon>
  {

    private static final Collection<? extends Class<? extends Wagon>> implementingClasses = Collections.singleton(
            DefaultWagon.class);
    private String number;
    private int  wheelcount;
    private String kind;
    private String clazz;
    private String company;
    private String country;

    @Override
    public Wagon.Builder<? extends Wagon> wagonNumber(String number)
    {
      this.number = number;
      return this;
    }

    @Override
    public Wagon.Builder<? extends Wagon> wheelCount(int wheelCount)
    {
      this.wheelcount = wheelCount;
      return this;
    }

    @Override
    public Wagon.Builder<? extends Wagon> kind(String kind)
    {
      this.kind = kind;
      return this;
    }

    @Override
    public Wagon.Builder<? extends Wagon> wagonClass(String clazz)
    {
      this.clazz = clazz;
      return this;
    }

    @Override
    public Wagon.Builder<? extends Wagon> company(String company)
    {
      this.company = company;
      return this;
    }

    @Override
    public Wagon.Builder<? extends Wagon> country(String country)
    {
      this.country = country;
      return this;
    }

    @Override
    public Wagon build() throws IllegalStateException
    {
      checkState();
      return new DefaultWagon(id, name, condition, dateOfPurchase, description, lastModified, manufacturer, masterImage,
                              price, productNumber, retailer, entities, era, length, width, height, weight, serviceEntries,
                              decoder, scale, lookupContent, number, wheelcount, kind, clazz, company, country);
    }

    @Override
    public Collection<? extends Class<? extends Wagon>> getImplementingClasses()
    {
      return implementingClasses;
    }

    @Override
    public Class<?> getXmlClass()
    {
      return XWagon.class;
    }

  }

  @Override
  public Wagon.Builder<? extends Wagon> createBuilder()
  {
    return new DefaultBuilder();
  }

}
