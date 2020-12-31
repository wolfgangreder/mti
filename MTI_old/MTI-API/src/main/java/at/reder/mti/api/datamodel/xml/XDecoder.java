/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Decoder;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
@XmlRootElement(name = "decoder", namespace = "mti")
public final class XDecoder extends AbstractXInventoryObject
{

  public static final class Adapter extends XmlAdapter<XDecoder, Decoder>
  {

    @Override
    public Decoder unmarshal(XDecoder v)
    {
      if (v != null) {
        return v.toDecoder();
      }
      return null;
    }

    @Override
    public XDecoder marshal(Decoder v)
    {
      if (v != null) {
        return new XDecoder(v);
      }
      return null;
    }

  }

  public XDecoder()
  {
  }

  public XDecoder(Decoder d)
  {
    super(d);
  }

  public Decoder toDecoder()
  {
    Decoder.Builder builder = Lookup.getDefault().lookup(Decoder.BuilderFactory.class).createBuilder();
    return builder.copy(this).build();
  }

}
