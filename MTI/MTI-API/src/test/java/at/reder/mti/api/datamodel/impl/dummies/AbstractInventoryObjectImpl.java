/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.impl.dummies;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.datamodel.impl.AbstractInventoryObject;
import at.reder.mti.api.utils.Money;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public class AbstractInventoryObjectImpl extends AbstractInventoryObject
{

  public AbstractInventoryObjectImpl(UUID id,
                                     String name,
                                     ModelCondition condition,
                                     LocalDate dateOfPurchase,
                                     String description,
                                     Instant lastModified,
                                     Contact manufacturer,
                                     Entity masterImage,
                                     Money price,
                                     String productNumber,
                                     Contact retailer,
                                     Collection<? extends Entity> entities)
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
  }

}
