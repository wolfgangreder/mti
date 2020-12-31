/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.BaseBuilder;
import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.InventoryObject;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.utils.Money;
import at.reder.mti.api.utils.xml.ISODateXmlAdapter;
import at.reder.mti.api.utils.xml.InstantXmlAdapter;
import at.reder.mti.api.utils.xml.MoneyXmlAdapter;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author wolfi
 * @param <B>
 */
public abstract class AbstractXInventoryObject<B extends BaseBuilder<?>>
{

  protected final List<Entity> entities = new LinkedList<>();
  protected ModelCondition condition;
  protected LocalDate dateOfPurchase;
  protected String description;
  protected UUID id;
  protected Instant lastModified;
  protected Contact manufacturer;
  protected Entity masterImage;
  protected String name;
  protected Money price;
  protected String productNumber;
  protected Contact retailer;

  protected AbstractXInventoryObject()
  {
  }

  protected AbstractXInventoryObject(InventoryObject o)
  {
    this.entities.addAll(o.getEntities());
    this.condition = o.getCondition();
    this.dateOfPurchase = o.getDateOfPurchase();
    this.description = o.getDescription();
    this.id = o.getId();
    this.lastModified = o.getLastModified();
    this.manufacturer = o.getManufacturer();
    this.masterImage = o.getMasterImage();
    this.name = o.getName();
    this.price = o.getPrice();
    this.productNumber = o.getProductNumber();
    this.retailer = o.getRetailer();
  }

  @XmlElement(name = "entities", namespace = "mti")
  @XmlJavaTypeAdapter(value = XEntity.Adapter.class)
  @XmlList
  @XmlIDREF
  public List<Entity> getEntities()
  {
    return entities;
  }

  @XmlAttribute(name = "condition", namespace = "mti")
  public ModelCondition getCondition()
  {
    return condition;
  }

  @XmlAttribute(name = "dateofpurchase", namespace = "mti")
  @XmlJavaTypeAdapter(value = ISODateXmlAdapter.class)
  public LocalDate getDateOfPurchase()
  {
    return dateOfPurchase;
  }

  @XmlElement(name = "description", namespace = "mti")
  public String getDescription()
  {
    return description;
  }

  @XmlTransient
  public UUID getId()
  {
    return id;
  }

  @XmlAttribute(name = "id")
  @XmlID
  public String getStringId()
  {
    return id != null ? id.toString() : null;
  }

  @XmlAttribute(name = "lastmodified", namespace = "mti")
  @XmlJavaTypeAdapter(InstantXmlAdapter.class)
  public Instant getLastModified()
  {
    return lastModified;
  }

  @XmlAttribute(name = "manufacturer", namespace = "mti")
  @XmlJavaTypeAdapter(value = XContact.Adapter.class)
  @XmlIDREF
  public Contact getManufacturer()
  {
    return manufacturer;
  }

  @XmlAttribute(name = "masterimage", namespace = "mti")
  @XmlJavaTypeAdapter(value = XEntity.Adapter.class)
  @XmlIDREF
  public Entity getMasterImage()
  {
    return masterImage;
  }

  @XmlElement(name = "name", namespace = "mti")
  public String getName()
  {
    return name;
  }

  @XmlAttribute(name = "price", namespace = "mti")
  @XmlJavaTypeAdapter(value = MoneyXmlAdapter.class)
  public Money getPrice()
  {
    return price;
  }

  @XmlElement(name = "productnumber", namespace = "mti")
  public String getProductNumber()
  {
    return productNumber;
  }

  @XmlAttribute(name = "retailer", namespace = "mti")
  @XmlJavaTypeAdapter(value = XContact.Adapter.class)
  @XmlIDREF
  public Contact getRetailer()
  {
    return retailer;
  }

  public void setCondition(ModelCondition condition)
  {
    this.condition = condition;
  }

  public void setDateOfPurchase(LocalDate dateOfPurchase)
  {
    this.dateOfPurchase = dateOfPurchase;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public void setId(UUID id)
  {
    this.id = id;
  }

  public void setStringId(String strId)
  {
    this.id = strId != null ? UUID.fromString(strId) : null;
  }

  public void setLastModified(Instant lastModified)
  {
    this.lastModified = lastModified;
  }

  public void setManufacturer(Contact manufacturer)
  {
    this.manufacturer = manufacturer;
  }

  public void setMasterImage(Entity masterImage)
  {
    this.masterImage = masterImage;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setPrice(Money price)
  {
    this.price = price;
  }

  public void setProductNumber(String productNumber)
  {
    this.productNumber = productNumber;
  }

  public void setRetailer(Contact retailer)
  {
    this.retailer = retailer;
  }

}
