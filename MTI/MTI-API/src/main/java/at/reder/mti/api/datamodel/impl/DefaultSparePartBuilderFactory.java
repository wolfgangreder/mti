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
import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.datamodel.SparePart;
import at.reder.mti.api.datamodel.xml.XSparePart;
import at.reder.mti.api.utils.Money;
import at.reder.mti.api.utils.Timestamp;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author wolfi
 */
@ServiceProvider(service = SparePart.BuilderFactory.class)
public final class DefaultSparePartBuilderFactory implements SparePart.BuilderFactory
{

  @XmlSeeAlso(value = XSparePart.class)
  public static final class DefaultSparePart extends AbstractInventoryObject implements SparePart
  {

    private final BigDecimal amount;

    private DefaultSparePart(UUID id,
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
                             BigDecimal amount)
    {
      super(id,
            name,
            condition,
            dateOfPurchase,
            description,
            lastModified,
            manufacturer,
            masterImage,
            price,
            productNumber,
            retailer,
            entities);
      this.amount = amount;
    }

    @Override
    public BigDecimal getAmount()
    {
      return amount;
    }

  }

  public static final class SparePartBuilder extends AbstractInventoryObjectBuilder<DefaultSparePart> implements
          SparePart.Builder<DefaultSparePart>
  {

    private BigDecimal amount;
    private static final Collection<? extends Class<? extends DefaultSparePart>> implementingClasses = Collections.singleton(
            DefaultSparePart.class);

    @Override
    public SparePart.Builder<? extends SparePart> copy(SparePart part) throws NullPointerException
    {
      if (part == null) {
        throw new NullPointerException("part==null");
      }
      super.copy(part);
      this.amount = part.getAmount();
      return this;
    }

    @Override
    public DefaultSparePart build() throws IllegalStateException
    {
      checkState();
      if (amount == null) {
        throw new IllegalStateException("amount==null");
      }
      return new DefaultSparePart(id,
                                  name,
                                  condition,
                                  dateOfPurchase,
                                  description,
                                  lastModified,
                                  manufacturer,
                                  masterImage,
                                  price,
                                  productNumber,
                                  retailer,
                                  entities,
                                  amount);
    }

    @Override
    public Collection<? extends Class<? extends DefaultSparePart>> getImplementingClasses()
    {
      return implementingClasses;
    }

    @Override
    public Class<?> getXmlClass()
    {
      return XSparePart.class;
    }

    @Override
    public SparePart.Builder<? extends SparePart> amount(BigDecimal amount) throws NullPointerException
    {
      if (amount == null) {
        throw new NullPointerException("amount==null");
      }
      this.amount = amount;
      return this;
    }

  }

  @Override
  public SparePart.Builder<? extends SparePart> createBuilder()
  {
    return new SparePartBuilder();
  }

}
