/*
 * Copyright 2021 Wolfgang Reder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.or.reder.mti.model.impl;

import at.or.reder.dcc.DecoderClass;
import at.or.reder.dcc.DecoderInfo;
import at.or.reder.dcc.IdentifyProvider;
import at.or.reder.dcc.util.DCCUtils;
import at.or.reder.dcc.util.SimpleCVAddress;
import at.or.reder.mti.model.api.CVResult;
import at.or.reder.mti.model.api.CVSource;
import at.or.reder.mti.model.api.CVSourceFactory;
import at.or.reder.mti.model.api.StoreException;
import at.or.reder.zcan20.CVReadState;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.openide.util.Lookup;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Wolfgang Reder
 */
public class ZCSCVSourceFactoryNGTest
{

  public ZCSCVSourceFactoryNGTest()
  {
  }

  @BeforeClass
  public static void setUpClass() throws Exception
  {
  }

  @Test
  public void testRegistration()
  {
    Collection<? extends CVSourceFactory> factories = Lookup.getDefault().lookupAll(CVSourceFactory.class);
    List<CVSourceFactory> found = factories.stream().
            filter((f) -> f.getId().equals(ZCSCVSourceFactory.ID)).
            collect(Collectors.toList());
    assertEquals(1,
                 found.size());
    assertTrue(found.get(0) instanceof ZCSCVSourceFactory);

  }

  @Test
  public void testGetName()
  {
  }

  @Test
  public void testOpenSourceGUI() throws Exception
  {
  }

  @Test
  public void testOpenSource() throws Exception
  {
    URL url = getClass().getResource("2062.zcs");
    ZCSCVSourceFactory factory = new ZCSCVSourceFactory();
    CVSource source = factory.openSource(url);
    assertNotNull(source);
    CVResult result17 = source.readCV(0,
                                      SimpleCVAddress.valueOf(17),
                                      0,
                                      TimeUnit.DAYS);
    assertNotNull(result17);
    assertEquals(CVReadState.READ,
                 result17.getState());
    assertEquals(200,
                 result17.getValue());
    assertEquals(17,
                 result17.getAddress().getFlatAddress());
    CVResult result18 = source.readCV(0,
                                      SimpleCVAddress.valueOf(18),
                                      0,
                                      TimeUnit.DAYS);
    assertEquals(CVReadState.READ,
                 result18.getState());
    assertEquals(14,
                 result18.getValue());
    assertEquals(18,
                 result18.getAddress().getFlatAddress());
    int decoderAddress = DCCUtils.decodeLongAddress(result17.getValue(),
                                                    result18.getValue());
    assertEquals(2062,
                 decoderAddress);
  }

  @Test
  public void testIdentifyProvider() throws IOException, StoreException
  {
    URL url = getClass().getResource("2062.zcs");
    ZCSCVSourceFactory factory = new ZCSCVSourceFactory();
    CVSource source = factory.openSource(url);
    assertNotNull(source);
    IdentifyProvider provider = source.getLookup().lookup(IdentifyProvider.class);
    assertNotNull(provider);
    DecoderInfo info = DCCUtils.identifyDecoder(provider);
    assertEquals(2062,
                 info.getAddress());
    assertEquals(DecoderClass.LOCO,
                 info.getDecoderType());
  }

  @Test
  public void testGetFileFilter()
  {
  }

  @Test
  public void testOpen() throws Exception
  {
  }

}
