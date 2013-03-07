/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Era;
import at.reder.mti.api.datamodel.Era.BuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openide.util.Lookup;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author wolfi
 */
public class XEraNGTest
{

  public XEraNGTest()
  {
  }

  private static final UUID defaultId = UUID.randomUUID();
  private static final String defaultName = "name";
  private static final String defaultComment = "comment";
  private static final int defaultYearFrom = 1800;
  private static final Integer defaultYearTo = 1900;
  private static final Set<String> countries = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("DE", "AT")));

  private Era.Builder<? extends Era> createEraBuilder()
  {
    return Lookup.getDefault().lookup(BuilderFactory.class).createBuilder().
            addCountries(countries).
            comment(defaultComment).
            id(defaultId).
            name(defaultName).
            yearFrom(defaultYearFrom).
            yearTo(defaultYearTo);
  }

  @Test
  public void testToEra()
  {
    System.out.println("toEra");
    Era.Builder<? extends Era> builder = createEraBuilder();
    Era expResult = builder.build();
    XEra instance = new XEra(expResult);
    Era result = instance.toEra();
    assertNotSame(expResult, result);
    assertEquals(result.getComment(), defaultComment);
    assertEquals(result.getCountries(), countries);
    assertEquals(result.getId(), defaultId);
    assertEquals(result.getName(), defaultName);
    assertEquals(result.getYearFrom(), defaultYearFrom);
    assertEquals(result.getYearTo(), defaultYearTo);
    builder.yearTo(null);
    expResult = builder.build();
    instance = new XEra(expResult);
    result = instance.toEra();
    assertNotSame(expResult, result);
    assertEquals(result.getComment(), defaultComment);
    assertEquals(result.getCountries(), countries);
    assertEquals(result.getId(), defaultId);
    assertEquals(result.getName(), defaultName);
    assertEquals(result.getYearFrom(), defaultYearFrom);
    assertNull(result.getYearTo());
    builder.comment(null);
    expResult = builder.build();
    instance = new XEra(expResult);
    result = instance.toEra();
    assertNotSame(expResult, result);
    assertEquals(result.getComment(), "");
    assertEquals(result.getCountries(), countries);
    assertEquals(result.getId(), defaultId);
    assertEquals(result.getName(), defaultName);
    assertEquals(result.getYearFrom(), defaultYearFrom);
    assertNull(result.getYearTo());
    builder.clearCountries();
    expResult = builder.build();
    instance = new XEra(expResult);
    result = instance.toEra();
    assertNotSame(expResult, result);
    assertEquals(result.getComment(), "");
    assertTrue(result.getCountries().isEmpty());
    assertEquals(result.getId(), defaultId);
    assertEquals(result.getName(), defaultName);
    assertEquals(result.getYearFrom(), defaultYearFrom);
    assertNull(result.getYearTo());
  }

  @Test
  public void testGetId()
  {
    System.out.println("getId");
    XEra instance = new XEra(createEraBuilder().build());
    UUID expResult = defaultId;
    UUID result = instance.getId();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetId()
  {
    System.out.println("setId");
    XEra instance = new XEra();
    assertNull(instance.getId());
    instance.setId(defaultId);
    assertEquals(defaultId, instance.getId());
  }

  @Test
  public void testGetStringId()
  {
    System.out.println("getStringId");
    XEra instance = new XEra(createEraBuilder().build());
    String expResult = defaultId.toString();
    String result = instance.getStringId();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetStringId()
  {
    System.out.println("setStringId");
    String strId = defaultId.toString();
    XEra instance = new XEra();
    assertNull(instance.getStringId());
    instance.setStringId(strId);
    assertEquals(strId, instance.getStringId());
    assertEquals(defaultId, instance.getId());
  }

  @Test
  public void testGetName()
  {
    System.out.println("getName");
    XEra instance = new XEra(createEraBuilder().build());
    String expResult = defaultName;
    String result = instance.getName();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetName()
  {
    System.out.println("setName");
    String name = defaultName;
    XEra instance = new XEra();
    assertNull(instance.getName());
    instance.setName(name);
    assertEquals(name, instance.getName());
  }

  @Test
  public void testGetYearFrom()
  {
    System.out.println("getYearFrom");
    XEra instance = new XEra(createEraBuilder().build());
    int expResult = defaultYearFrom;
    int result = instance.getYearFrom();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetYearFrom()
  {
    System.out.println("setYearFrom");
    int yearFrom = defaultYearFrom;
    XEra instance = new XEra();
    assertEquals(0, instance.getYearFrom());
    instance.setYearFrom(yearFrom);
    assertEquals(yearFrom, instance.getYearFrom());
  }

  @Test
  public void testGetYearTo()
  {
    System.out.println("getYearTo");
    XEra instance = new XEra(createEraBuilder().build());
    Integer expResult = defaultYearTo;
    Integer result = instance.getYearTo();
    assertEquals(result, expResult);
  }

  @Test
  public void testSetYearTo()
  {
    System.out.println("setYearTo");
    XEra instance = new XEra();
    instance.setYearTo(null);
    assertNull(instance.getYearTo());
    instance.setYearTo(defaultYearTo);
    assertEquals(defaultYearTo, instance.getYearTo());
  }

  @Test
  public void testGetComment()
  {
    System.out.println("getComment");
    XEra instance = new XEra(createEraBuilder().build());
    String result = instance.getComment();
    assertEquals(result, defaultComment);
  }

  @Test
  public void testSetComment()
  {
    System.out.println("setComment");
    XEra instance = new XEra();
    assertNull(instance.getComment());
    instance.setComment(defaultComment);
    assertEquals(instance.getComment(), defaultComment);
  }

  @Test
  public void testGetCountries()
  {
    System.out.println("getCountries");
    XEra instance = new XEra(createEraBuilder().build());
    List result = instance.getCountries();
    assertEquals(result, countries);
  }

  @Test
  public void testJAXBContext() throws JAXBException
  {
    System.out.println("jaxbContext");
    JAXBContext ctx = JAXBContext.newInstance(XEra.class);
  }

  @Test
  public void testXMLRoot() throws JAXBException
  {
    System.out.println("xmlRoot");
    JAXBContext ctx = JAXBContext.newInstance(XEra.class);
    Era era = createEraBuilder().build();
    XEra xera = new XEra(era);
    Marshaller m = ctx.createMarshaller();
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    m.marshal(xera, os);
    ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
    Unmarshaller u = ctx.createUnmarshaller();
    Object o = u.unmarshal(is);
    assertTrue(o instanceof XEra);
    Era result = ((XEra) o).toEra();
    assertNotSame(result, era);
    assertEquals(result.getComment(), era.getComment());
    assertEquals(result.getCountries(), era.getCountries());
    assertEquals(result.getId(), era.getId());
    assertEquals(result.getName(), era.getName());
    assertEquals(result.getYearFrom(), era.getYearFrom());
    assertEquals(result.getYearTo(), era.getYearTo());
  }

  @XmlRootElement
  public static final class Wrapper
  {

    private Era era;

    public Wrapper()
    {
    }

    public Wrapper(Era era)
    {
      this.era = era;
    }

    @XmlElement
    @XmlJavaTypeAdapter(value = XEra.Adapter.class)
    public Era getEra()
    {
      return era;
    }

    public void setEra(Era era)
    {
      this.era = era;
    }

  }

  @Test
  public void testJAXBAdapter() throws JAXBException
  {
    System.out.println("jaxbAdater");
    JAXBContext ctx = JAXBContext.newInstance(Wrapper.class);
    Era era = createEraBuilder().build();
    Wrapper w = new Wrapper(era);
    Marshaller m = ctx.createMarshaller();
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    m.marshal(w, os);
    ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
    Unmarshaller u = ctx.createUnmarshaller();
    Object o = u.unmarshal(is);
    assertTrue(o instanceof Wrapper);
    Wrapper wrapperResult = (Wrapper) o;
    assertNotSame(wrapperResult, w);
    Era result = wrapperResult.getEra();
    assertNotSame(result, era);
    assertEquals(result.getComment(), era.getComment());
    assertEquals(result.getCountries(), era.getCountries());
    assertEquals(result.getId(), era.getId());
    assertEquals(result.getName(), era.getName());
    assertEquals(result.getYearFrom(), era.getYearFrom());
    assertEquals(result.getYearTo(), era.getYearTo());
  }

}