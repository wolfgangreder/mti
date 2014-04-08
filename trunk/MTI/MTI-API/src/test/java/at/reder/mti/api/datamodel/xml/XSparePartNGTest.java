/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013-2014 Wolfgang Reder
 *
 */
package at.reder.mti.api.datamodel.xml;

import at.reder.mti.api.datamodel.Contact;
import at.reder.mti.api.datamodel.Entity;
import at.reder.mti.api.datamodel.EntityKind;
import at.reder.mti.api.datamodel.ModelCondition;
import at.reder.mti.api.datamodel.SparePart;
import at.reder.mti.api.datamodel.impl.DefaultSparePartBuilderFactoryNGTest;
import at.reder.mti.api.datamodel.impl.dummies.DummyContact;
import at.reder.mti.api.datamodel.impl.dummies.DummySparePart;
import at.reder.mti.api.utils.Money;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openide.util.Lookup;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 *
 * @author wolfi
 */
public class XSparePartNGTest
{

  private final Random random = new Random();
  private final UUID id = UUID.randomUUID();
  private final Instant lastModified = Instant.now();
  private final String name = "spareDummy";
  private final ModelCondition condition = ModelCondition.values()[random.nextInt(ModelCondition.values().length)];
  private final LocalDate dateOfPurchase = LocalDate.now();
  private final String description = "spareDescription";
  private final Contact manufacturer = new DummyContact();
  private final Contact retailer = new DummyContact();
  private final String productNumber = "spareNumber";
  private final Money price = Money.valueOf(random.nextInt());
  private final BigDecimal amount = new BigDecimal(random.nextInt());
  private final Entity masterImage;
  private final SparePart dummy;

  public XSparePartNGTest() throws URISyntaxException
  {
    URI uri = DefaultSparePartBuilderFactoryNGTest.class.getResource("cubacar.jpg").toURI();
    Entity.Builder builder = Lookup.getDefault().lookup(Entity.BuilderFactory.class).createBuilder();
    masterImage = builder.kind(EntityKind.IMAGE).mimeType("image/jpeg").uri(uri).build();
    Collection<? extends Entity> et = Collections.singleton(masterImage);
    dummy = new DummySparePart(id,
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
                               et,
                               amount);
  }

  @Test
  public void testConversion()
  {
    XSparePart instance = new XSparePart(dummy);
    SparePart result = instance.toSparePart();
    assertNotNull(result);
    assertNotSame(result, dummy);
    assertEquals(result.getAmount(), dummy.getAmount());
    assertEquals(result.getCondition(), dummy.getCondition());
    assertEquals(result.getDateOfPurchase(), dummy.getDateOfPurchase());
    assertEquals(result.getDescription(), dummy.getDescription());
    assertEquals(result.getId(), dummy.getId());
    assertEquals(result.getLastModified(), dummy.getLastModified());
    assertEquals(result.getManufacturer(), dummy.getManufacturer());
    assertEquals(result.getMasterImage().getId(), dummy.getMasterImage().getId());
    assertEquals(result.getName(), dummy.getName());
    assertEquals(result.getPrice(), dummy.getPrice());
    assertEquals(result.getProductNumber(), dummy.getProductNumber());
    assertEquals(result.getRetailer(), dummy.getRetailer());
  }

  @Test
  public void testAdapter()
  {
    XSparePart.Adapter ad = new XSparePart.Adapter();
    assertNull(ad.marshal(null));
    assertNull(ad.unmarshal(null));
    XSparePart xpart = ad.marshal(dummy);
    assertNotNull(xpart);
    SparePart result = ad.unmarshal(xpart);
    assertNotNull(result);
    assertNotSame(result, dummy);
    assertEquals(result.getAmount(), dummy.getAmount());
    assertEquals(result.getCondition(), dummy.getCondition());
    assertEquals(result.getDateOfPurchase(), dummy.getDateOfPurchase());
    assertEquals(result.getDescription(), dummy.getDescription());
    assertEquals(result.getId(), dummy.getId());
    assertEquals(result.getLastModified(), dummy.getLastModified());
    assertEquals(result.getManufacturer(), dummy.getManufacturer());
    assertEquals(result.getMasterImage().getId(), dummy.getMasterImage().getId());
    assertEquals(result.getName(), dummy.getName());
    assertEquals(result.getPrice(), dummy.getPrice());
    assertEquals(result.getProductNumber(), dummy.getProductNumber());
    assertEquals(result.getRetailer(), dummy.getRetailer());
  }

  @XmlRootElement(name = "wrapper")
  public static final class Wrapper
  {

    private SparePart part;
    private final List<Entity> entities = new LinkedList<>();
    private final List<Contact> contact = new LinkedList<>();

    public Wrapper()
    {
    }

    public Wrapper(SparePart part)
    {
      this.part = part;
//      entities.addAll(part.getEntities());
      contact.add(part.getManufacturer());
      contact.add(part.getRetailer());
    }

    @XmlElement(name = "part")
    @XmlJavaTypeAdapter(value = XSparePart.Adapter.class)
    public SparePart getPart()
    {
      return part;
    }

    @XmlElement(name = "entitiy")
    @XmlElementWrapper(name = "entities")
    @XmlJavaTypeAdapter(value = XEntity.Adapter.class)
    public List<Entity> getEntities()
    {
      return entities;
    }

    public void setPart(SparePart part)
    {
      this.part = part;
    }

    @XmlElement(name = "contact")
    @XmlElementWrapper(name = "contacts")
    @XmlJavaTypeAdapter(value = XContact.Adapter.class)
    public List<Contact> getContact()
    {
      return contact;
    }

  }

  @Test(enabled = false)
  public void testStreaming() throws JAXBException
  {
    JAXBContext context = JAXBContext.newInstance(Wrapper.class, XSparePart.class, XEntity.class, XContact.class);
    Wrapper xpart = new Wrapper(dummy);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    context.createMarshaller().marshal(xpart, os);
    ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
    Object tmp = context.createUnmarshaller().unmarshal(is);
    assertTrue(tmp instanceof Wrapper);
    SparePart result = ((Wrapper) tmp).getPart();
    assertNotSame(result, dummy);
    assertEquals(result.getAmount(), dummy.getAmount());
    assertEquals(result.getCondition(), dummy.getCondition());
    assertEquals(result.getDateOfPurchase(), dummy.getDateOfPurchase());
    assertEquals(result.getDescription(), dummy.getDescription());
    assertEquals(result.getId(), dummy.getId());
    assertEquals(result.getLastModified(), dummy.getLastModified());
    assertEquals(result.getManufacturer(), dummy.getManufacturer());
    assertEquals(result.getMasterImage().getId(), dummy.getMasterImage().getId());
    assertEquals(result.getName(), dummy.getName());
    assertEquals(result.getPrice(), dummy.getPrice());
    assertEquals(result.getProductNumber(), dummy.getProductNumber());
    assertEquals(result.getRetailer(), dummy.getRetailer());
  }

}
