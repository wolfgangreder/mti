/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Scale;
import at.reder.mti.api.datamodel.impl.DefaultScaleBuilderFactory;
import at.reder.mti.api.utils.Fract;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author wolfi
 */
public class XScaleNGTest
{

  private static final UUID defaultId = UUID.randomUUID();
  private static final String defaultName = "scale";
  private static final Fract defaultScale = Fract.valueOf(1, 160);
  private static final double defaultWidth = 9;

  private static Scale.Builder<?> createBuilder()
  {
    return new DefaultScaleBuilderFactory().createBuilder().id(defaultId).name(defaultName).scale(defaultScale).trackWidth(
            defaultWidth);
  }

  @Test
  public void testToScale()
  {
    XScale instance = new XScale(createBuilder().build());
    Scale expResult = createBuilder().build();
    Scale result = instance.toScale();
    assertNotNull(result);
    assertNotNull(expResult);
    assertEquals(result, expResult);
    assertNotSame(result, expResult);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testToScaleInvalid()
  {
    XScale instance = new XScale();
    instance.toScale();
  }

  @Test
  public void testGetId()
  {
    XScale instance = new XScale();
    assertNull(instance.getId());
    instance = new XScale(createBuilder().build());
    assertEquals(instance.getId(), defaultId.toString());
  }

  @Test
  public void testSetId()
  {
    XScale instance = new XScale();
    instance.setId(defaultId.toString());
    assertEquals(instance.getId(), defaultId.toString());
  }

  @Test
  public void testGetName()
  {
    XScale instance = new XScale();
    assertNull(instance.getName());
    instance = new XScale(createBuilder().build());
    assertEquals(instance.getName(), defaultName);
  }

  @Test
  public void testSetName()
  {
    XScale instance = new XScale();
    instance.setName(defaultName);
    assertEquals(instance.getName(), defaultName);
  }

  @Test
  public void testGetScale()
  {
    XScale instance = new XScale();
    assertNull(instance.getScale());
    instance = new XScale(createBuilder().build());
    assertEquals(instance.getScale(), defaultScale);
  }

  @Test
  public void testSetScale()
  {
    XScale instance = new XScale();
    instance.setScale(defaultScale);
    assertEquals(instance.getScale(), defaultScale);
  }

  @Test
  public void testGetTrackWidth()
  {
    XScale instance = new XScale();
    assertEquals(instance.getTrackWidth(), 0.d);
    instance = new XScale(createBuilder().build());
    assertEquals(instance.getTrackWidth(), defaultWidth);
  }

  @Test
  public void testSetTrackWidth()
  {
    XScale instance = new XScale();
    instance.setTrackWidth(defaultWidth);
    assertEquals(instance.getTrackWidth(), defaultWidth);
  }

  @Test
  public void testJAXBContext() throws JAXBException
  {
    JAXBContext.newInstance(XScale.class);
  }

  @Test
  public void testJAXB() throws JAXBException
  {
    JAXBContext ctx = JAXBContext.newInstance(XScale.class);
    Scale scale = createBuilder().build();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    XScale xscale = new XScale(scale);
    ctx.createMarshaller().marshal(xscale, bos);
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    Object tmp = ctx.createUnmarshaller().unmarshal(bis);
    assertNotNull(tmp);
    assertTrue(tmp instanceof XScale);
    assertNotSame(tmp, xscale);
    Scale result = ((XScale) tmp).toScale();
    assertNotNull(result);
    assertNotSame(result, scale);
    assertEquals(result.getId(), scale.getId());
    assertEquals(result.getName(), scale.getName());
    assertEquals(result.getScale(), scale.getScale());
    assertEquals(result.getTrackWidth(), scale.getTrackWidth());
  }

  @XmlRootElement
  public static final class Wrapper
  {

    private Scale scale;

    @XmlElement
    @XmlJavaTypeAdapter(value = XScale.Adapter.class)
    public Scale getScale()
    {
      return scale;
    }

    public void setScale(Scale scale)
    {
      this.scale = scale;
    }

  }

  @Test
  public void testJAXBWrapped() throws JAXBException
  {
    JAXBContext ctx = JAXBContext.newInstance(Wrapper.class);
    Scale scale = createBuilder().build();
    Wrapper wrapper = new Wrapper();
    wrapper.setScale(scale);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ctx.createMarshaller().marshal(wrapper, bos);
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    Object tmp = ctx.createUnmarshaller().unmarshal(bis);
    assertTrue(tmp instanceof Wrapper);
    assertNotSame(tmp, wrapper);
    Scale result = ((Wrapper) tmp).getScale();
    assertNotNull(result);
    assertNotSame(result, scale);
    assertEquals(result.getId(), scale.getId());
    assertEquals(result.getName(), scale.getName());
    assertEquals(result.getScale(), scale.getScale());
    assertEquals(result.getTrackWidth(), scale.getTrackWidth());
  }

}