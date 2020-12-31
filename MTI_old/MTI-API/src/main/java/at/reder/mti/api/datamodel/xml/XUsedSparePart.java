/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.SparePart;
import at.reder.mti.api.datamodel.UsedSparePart;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author wolfi
 */
@XmlRootElement(name = "usedsparepart", namespace = "mti")
public final class XUsedSparePart
{

  public static final class Adapter extends XmlAdapter<XUsedSparePart, UsedSparePart>
  {

    @Override
    public UsedSparePart unmarshal(XUsedSparePart v)
    {
      if (v != null) {
        return v.toUsedSparePart();
      }
      return null;
    }

    @Override
    public XUsedSparePart marshal(UsedSparePart v)
    {
      if (v != null) {
        return new XUsedSparePart(v);
      }
      return null;
    }

  }
  private final Set<UUID> defects = new HashSet<>();
  private SparePart part;
  private BigDecimal amount;
  private String memo;

  public XUsedSparePart()
  {
  }

  public XUsedSparePart(UsedSparePart part)
  {
    this.defects.addAll(part.getDefects());
    this.part = part.getPart();
    this.amount = part.getAmount();
    this.memo = part.getMemo();
  }

  public UsedSparePart toUsedSparePart()
  {
    return new UsedSparePart(defects, part, amount, memo);
  }

  @XmlElement(name = "defects", namespace = "mti")
  @XmlList
  public Set<UUID> getDefects()
  {
    return defects;
  }

  @XmlAttribute(name = "sparepart", namespace = "mti")
  @XmlIDREF
  public SparePart getPart()
  {
    return part;
  }

  @XmlAttribute(name = "amount", namespace = "mti")
  public BigDecimal getAmount()
  {
    return amount;
  }

  @XmlElement(name = "memo", namespace = "mti")
  public String getMemo()
  {
    return memo;
  }

  public void setPart(SparePart part)
  {
    this.part = part;
  }

  public void setAmount(BigDecimal amount)
  {
    this.amount = amount;
  }

  public void setMemo(String memo)
  {
    this.memo = memo;
  }

}
