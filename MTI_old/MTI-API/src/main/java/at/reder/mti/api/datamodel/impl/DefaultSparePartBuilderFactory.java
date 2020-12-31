/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.impl;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.datamodel.SparePart;
import at.reder.mti.api.datamodel.xml.XSparePart;
import at.reder.mti.api.utils.Money;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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
                             LocalDate dateOfPurchase,
                             String description,
                             Instant lastModified,
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

  public static final class SparePartBuilder implements SparePart.Builder
  {

    private final Set<Entity> entities = new HashSet<>();
    private ModelCondition condition;
    private LocalDate dateOfPurchase;
    private String description;
    private UUID id;
    private Instant lastModified;
    private Contact manufacturer;
    private Entity masterImage;
    private String name;
    private Money price;
    private String productNumber;
    private Contact retailer;
    private BigDecimal amount;
    private static final Collection<? extends Class<? extends DefaultSparePart>> implementingClasses = Collections.singleton(
            DefaultSparePart.class);

    @Override
    public SparePart.Builder copy(XSparePart part) throws NullPointerException
    {
      if (part == null) {
        throw new NullPointerException("part==null");
      }
      this.entities.clear();
      this.entities.addAll(part.getEntities());
      this.condition = part.getCondition();
      this.dateOfPurchase = part.getDateOfPurchase();
      this.description = part.getDescription();
      this.id = part.getId();
      this.lastModified = part.getLastModified();
      this.manufacturer = part.getManufacturer();
      this.masterImage = part.getMasterImage();
      this.name = part.getName();
      this.price = part.getPrice();
      this.productNumber = part.getProductNumber();
      this.retailer = part.getRetailer();
      this.amount = part.getAmount();
      return this;
    }

    @Override
    public SparePart.Builder copy(SparePart part) throws NullPointerException
    {
      if (part == null) {
        throw new NullPointerException("part==null");
      }
      this.entities.clear();
      this.entities.addAll(part.getEntities());
      this.condition = part.getCondition();
      this.dateOfPurchase = part.getDateOfPurchase();
      this.description = part.getDescription();
      this.id = part.getId();
      this.lastModified = part.getLastModified();
      this.manufacturer = part.getManufacturer();
      this.masterImage = part.getMasterImage();
      this.name = part.getName();
      this.price = part.getPrice();
      this.productNumber = part.getProductNumber();
      this.retailer = part.getRetailer();
      this.amount = part.getAmount();
      return this;
    }

    private void checkState() throws IllegalStateException
    {
      if (id == null) {
        throw new IllegalStateException("id==null");
      }
      if (name == null) {
        throw new IllegalStateException("name==null");
      }
      if (name.trim().isEmpty()) {
        throw new IllegalStateException("name is empty");
      }
      if (condition == null) {
        throw new IllegalStateException("condition==null");
      }
      if (lastModified == null) {
        throw new IllegalStateException("lastModified==null");
      }
      if (entities.contains(null)) {
        throw new IllegalStateException("entities contains null");
      }
    }

    @Override
    public SparePart.Builder condition(ModelCondition cond) throws NullPointerException
    {
      if (cond == null) {
        throw new NullPointerException("condition==null");
      }
      this.condition = cond;
      return this;
    }

    @Override
    public SparePart.Builder dateOfPurchase(LocalDate ts)
    {
      this.dateOfPurchase = ts;
      return this;
    }

    @Override
    public SparePart.Builder description(String descr)
    {
      if (descr == null) {
        description = "";
      } else {
        description = descr;
      }
      return this;
    }

    @Override
    public SparePart.Builder addEntity(Entity e) throws NullPointerException
    {
      if (e == null) {
        throw new NullPointerException("entity==null");
      }
      entities.add(e);
      return this;
    }

    @Override
    public SparePart.Builder removeEntity(Entity e)
    {
      if (e != null) {
        entities.remove(e);
      }
      return this;
    }

    @Override
    public SparePart.Builder addEntities(
            Collection<? extends Entity> e) throws
            NullPointerException,
            IllegalArgumentException
    {
      if (e == null) {
        throw new NullPointerException("entities==null");
      }
      if (e.contains(null)) {
        throw new IllegalArgumentException("entities contains null");
      }
      this.entities.addAll(e);
      return this;
    }

    @Override
    public SparePart.Builder clearEntities()
    {
      this.entities.clear();
      return this;
    }

    @Override
    public SparePart.Builder id(UUID id) throws NullPointerException
    {
      if (id == null) {
        throw new NullPointerException("id==null");
      }
      this.id = id;
      return this;
    }

    @Override
    public SparePart.Builder lastModified(Instant ts) throws NullPointerException
    {
      if (ts == null) {
        throw new NullPointerException("ts==null");
      }
      lastModified = ts;
      return this;
    }

    @Override
    public SparePart.Builder manufacturer(Contact contact)
    {
      this.manufacturer = contact;
      return this;
    }

    @Override
    public SparePart.Builder masterImage(Entity e)
    {
      this.masterImage = e;
      if (e != null) {
        addEntity(e);
      }
      return this;
    }

    @Override
    public SparePart.Builder name(String name) throws NullPointerException,
                                                      IllegalArgumentException
    {
      if (name == null) {
        throw new NullPointerException("name==null");
      }
      if (name.trim().isEmpty()) {
        throw new IllegalArgumentException("name is empty");
      }
      this.name = name;
      return this;
    }

    @Override
    public SparePart.Builder price(Money price)
    {
      this.price = price;
      return this;
    }

    @Override
    public SparePart.Builder productNumber(String productNumber)
    {
      this.productNumber = productNumber;
      return this;
    }

    @Override
    public SparePart.Builder retailer(Contact contact)
    {
      this.retailer = contact;
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
    public SparePart.Builder amount(BigDecimal amount) throws NullPointerException
    {
      if (amount == null) {
        throw new NullPointerException("amount==null");
      }
      this.amount = amount;
      return this;
    }

  }

  @Override
  public SparePart.Builder createBuilder()
  {
    return new SparePartBuilder();
  }

}
