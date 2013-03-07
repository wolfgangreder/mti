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
import at.reder.mti.api.datamodel.Locomotive;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.datamodel.Scale;
import at.reder.mti.api.datamodel.ServiceEntry;
import at.reder.mti.api.datamodel.xml.XLocomotive;
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
@ServiceProvider(service = Locomotive.BuilderFactory.class)
public final class DefaultLocomotiveBuilderFactory implements Locomotive.BuilderFactory
{

  public static final class DefaultLocomotive extends AbstractVehicle
          implements Locomotive
  {

    private final String number;
    private final String arrangement;
    private final String kind;
    private final String clazz;
    private final String company;
    private final String country;

    private DefaultLocomotive(UUID id,
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
                              String arrangement,
                              String kind,
                              String clazz,
                              String company,
                              String country)
    {
      super(id, name, condition, dateOfPurchase, description, lastModified, manufacturer, masterImage, price, productNumber,
            retailer, entities, era, length, width, height, weight, serviceEntries, decoder, scale, lookupContent);
      this.number = number;
      this.arrangement = arrangement;
      this.kind = kind;
      this.clazz = clazz;
      this.company = company;
      this.country = country;
    }

    @Override
    public String getLocomotiveNumber()
    {
      return number;
    }

    @Override
    public String getWheelArrangement()
    {
      return arrangement;
    }

    @Override
    public String getKind()
    {
      return kind;
    }

    @Override
    public String getLocomotiveClass()
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

  public static final class DefaultBuilder extends AbstractVehicleBuilder<Locomotive>
          implements Locomotive.Builder<Locomotive>
  {

    private static final Collection<? extends Class<? extends Locomotive>> implementingClasses = Collections.singleton(
            DefaultLocomotive.class);
    private String number;
    private String arrangement;
    private String kind;
    private String clazz;
    private String company;
    private String country;

    @Override
    public Locomotive.Builder<? extends Locomotive> locomotiveNumber(String number)
    {
      this.number = number;
      return this;
    }

    @Override
    public Locomotive.Builder<? extends Locomotive> wheelArrangement(String arrangement)
    {
      this.arrangement = arrangement;
      return this;
    }

    @Override
    public Locomotive.Builder<? extends Locomotive> kind(String kind)
    {
      this.kind = kind;
      return this;
    }

    @Override
    public Locomotive.Builder<? extends Locomotive> locomotiveClass(String clazz)
    {
      this.clazz = clazz;
      return this;
    }

    @Override
    public Locomotive.Builder<? extends Locomotive> company(String company)
    {
      this.company = company;
      return this;
    }

    @Override
    public Locomotive.Builder<? extends Locomotive> country(String country)
    {
      this.country = country;
      return this;
    }

    @Override
    public Locomotive build() throws IllegalStateException
    {
      checkState();
      return new DefaultLocomotive(id, name, condition, dateOfPurchase, description, lastModified, manufacturer, masterImage,
                                   price, productNumber, retailer, entities, era, length, width, height, weight, serviceEntries,
                                   decoder, scale, lookupContent, number, arrangement, kind, clazz, company, country);
    }

    @Override
    public Collection<? extends Class<? extends Locomotive>> getImplementingClasses()
    {
      return implementingClasses;
    }

    @Override
    public Class<?> getXmlClass()
    {
      return XLocomotive.class;
    }

  }

  @Override
  public Locomotive.Builder<? extends Locomotive> createBuilder()
  {
    return new DefaultBuilder();
  }

}
