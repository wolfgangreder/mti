/*
 * $Id$
 * 
 * Author Wolfgang Reder
 * 
 * Copyright 2013 Wolfgang Reder
 * 
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Defect;
import at.reder.mti.api.datamodel.impl.DefaultDefectBuilderFactory;
import at.reder.mti.api.utils.Timestamp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author wolfi
 */
public class XDefectNGTest
{

  public XDefectNGTest()
  {
  }

  @BeforeClass
  public static void setUpClass() throws Exception
  {
  }

  @AfterClass
  public static void tearDownClass() throws Exception
  {
  }

  @BeforeMethod
  public void setUpMethod() throws Exception
  {
  }

  @AfterMethod
  public void tearDownMethod() throws Exception
  {
  }

  private JAXBContext context;

  @Test
  public void testJAXBContext() throws JAXBException
  {
    context = JAXBContext.newInstance(XDefect.class);
    assertNotNull(context);
  }

  @Test(dependsOnMethods = "testJAXBContext")
  public void testStreaming() throws JAXBException
  {
    Timestamp ts = new Timestamp();
    UUID id = UUID.randomUUID();
    Defect defect = new DefaultDefectBuilderFactory().createBuilder().date(ts).description("descr").id(id).build();
    XDefect xdefect = new XDefect(defect);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    context.createMarshaller().marshal(xdefect, os);
    ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
    Object tmp = context.createUnmarshaller().unmarshal(is);
    assertNotNull(tmp);
    assertSame(tmp.getClass(), XDefect.class);
    assertNotSame(xdefect, tmp);
    Defect result = ((XDefect) tmp).toDefect();
    assertNotNull(result);
    assertNotSame(defect, result);
    assertEquals(result.getDate(), defect.getDate());
    assertEquals(result.getDescription(), defect.getDescription());
    assertEquals(result.getId(), defect.getId());
  }

  @XmlRootElement
  public static final class Wrapper
  {

    private Defect defect;
    private Defect nullDefect;

    @XmlElement
    @XmlJavaTypeAdapter(XDefect.Adapter.class)
    public Defect getDefect()
    {
      return defect;
    }

    public void setDefect(Defect defect)
    {
      this.defect = defect;
    }

    @XmlElement
    @XmlJavaTypeAdapter(XDefect.Adapter.class)
    public Defect getNullDefect()
    {
      return nullDefect;
    }

    public void setNullDefect(Defect nullDefect)
    {
      this.nullDefect = nullDefect;
    }

  }

  @Test
  public void testWrappedStreaming() throws JAXBException
  {
    JAXBContext ctx = JAXBContext.newInstance(Wrapper.class);
    Timestamp ts = new Timestamp();
    UUID id = UUID.randomUUID();
    Defect defect = new DefaultDefectBuilderFactory().createBuilder().date(ts).description("descr").id(id).build();
    Wrapper wrapper = new Wrapper();
    wrapper.setDefect(defect);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ctx.createMarshaller().marshal(wrapper, os);
    ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
    Object tmp = ctx.createUnmarshaller().unmarshal(is);
    assertNotNull(tmp);
    assertNotSame(tmp, wrapper);
    assertSame(tmp.getClass(), Wrapper.class);
    Wrapper wResult = (Wrapper) tmp;
    assertNotNull(wResult.getDefect());
    assertNull(wResult.getNullDefect());
    Defect result = wResult.getDefect();
    assertNotSame(result, defect);
    assertEquals(result.getDate(), defect.getDate());
    assertEquals(result.getDescription(), defect.getDescription());
    assertEquals(result.getId(), defect.getId());
  }

  @Test
  public void testInit()
  {
    XDefect instance = new XDefect();
    assertNull(instance.getId());
    assertNull(instance.getDate());
    assertNull(instance.getDescription());
  }

}