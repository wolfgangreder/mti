/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.SparePart;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public final class XSparePart extends AbstractXInventoryObject
{

  public static final class Adapter extends XmlAdapter<XSparePart, SparePart>
  {

    @Override
    public SparePart unmarshal(XSparePart v)
    {
      if (v != null) {
        return v.toSparePart();
      }
      return null;
    }

    @Override
    public XSparePart marshal(SparePart v)
    {
      if (v != null) {
        return new XSparePart(v);
      }
      return null;
    }

  }
  private BigDecimal amount;

  public XSparePart()
  {
  }

  public XSparePart(SparePart part)
  {
    super(part);
    this.amount = part.getAmount();
  }

  public SparePart toSparePart()
  {
    SparePart.Builder builder = Lookup.getDefault().lookup(SparePart.BuilderFactory.class).createBuilder();
    return builder.copy(this).amount(amount).build();
  }

  @XmlAttribute(name = "amount", namespace = "mti")
  public BigDecimal getAmount()
  {
    return amount;
  }

  public void setAmount(BigDecimal amount)
  {
    this.amount = amount;
  }

}
