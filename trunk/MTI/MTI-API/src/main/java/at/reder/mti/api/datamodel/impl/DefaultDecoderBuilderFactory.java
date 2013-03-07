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
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.datamodel.xml.XDecoder;
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
@ServiceProvider(service = Decoder.BuilderFactory.class)
public final class DefaultDecoderBuilderFactory implements Decoder.BuilderFactory
{

  public static final class DefaultDecoder extends AbstractInventoryObject implements Decoder
  {

    private DefaultDecoder(UUID id,
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
                           Collection<? extends Entity> entities)
    {
      super(id, name, condition, dateOfPurchase, description, lastModified, manufacturer, masterImage, price, productNumber,
            retailer, entities);
    }

  }

  public static final class DefaultDecoderBuilder extends AbstractInventoryObjectBuilder<Decoder>
          implements Decoder.Builder<Decoder>
  {

    private static final Collection<? extends Class<? extends Decoder>> implementingClasses = Collections.singleton(
            DefaultDecoder.class);

    @Override
    public Decoder build() throws IllegalStateException
    {
      checkState();
      return new DefaultDecoder(id, name, condition, dateOfPurchase, description, lastModified, manufacturer, masterImage, price,
                                productNumber, retailer, entities);
    }

    @Override
    public Collection<? extends Class<? extends Decoder>> getImplementingClasses()
    {
      return implementingClasses;
    }

    @Override
    public Class<?> getXmlClass()
    {
      return XDecoder.class;
    }

  }

  @Override
  public Decoder.Builder<? extends Decoder> createBuilder()
  {
    return new DefaultDecoderBuilder();
  }

}
