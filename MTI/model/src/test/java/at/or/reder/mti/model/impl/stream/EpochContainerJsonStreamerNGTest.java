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
package at.or.reder.mti.model.impl.stream;

import at.or.reder.mti.model.Epoch;
import at.or.reder.mti.model.api.EpochContainer;
import at.or.reder.mti.model.api.Factories;
import at.or.reder.mti.model.api.StreamFormat;
import at.or.reder.mti.model.api.StreamSupport;
import at.or.reder.mti.model.api.Streamer;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.UUID;
import org.openide.util.Lookup;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertSame;
import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

/**
 *
 * @author Wolfgang Reder
 */
public class EpochContainerJsonStreamerNGTest
{

  public EpochContainerJsonStreamerNGTest()
  {
  }

  @org.testng.annotations.Test
  public void testRegistration()
  {
    Collection<? extends StreamSupport> streamSupporter = Lookup.getDefault().lookupAll(StreamSupport.class);
    for (StreamSupport support : streamSupporter) {
      Streamer<EpochContainer> streamer = support.getStreamer(StreamFormat.JSON,
                                                              EpochContainer.class);
      if (streamer != null && streamer.getClass() == EpochContainerJsonStreamer.class) {
        return;
      }
    }
    fail("EpochContainsJsonStream not registered");
  }

  @org.testng.annotations.Test
  public void testGetFormat()
  {
    EpochContainerJsonStreamer streamer = new EpochContainerJsonStreamer();
    assertSame(StreamFormat.JSON,
               streamer.getFormat());
  }

  @org.testng.annotations.Test
  public void testGetStreamableClass()
  {
    EpochContainerJsonStreamer streamer = new EpochContainerJsonStreamer();
    assertSame(EpochContainer.class,
               streamer.getStreamableClass());
  }

  @org.testng.annotations.Test
  public void testIsMarshalSupported()
  {
    EpochContainerJsonStreamer streamer = new EpochContainerJsonStreamer();
    assertTrue(streamer.isMarshalSupported());
  }

  @org.testng.annotations.Test
  public void testIsUnmarshalSupported()
  {
    EpochContainerJsonStreamer streamer = new EpochContainerJsonStreamer();
    assertTrue(streamer.isUnmarshalSupported());
  }

  @org.testng.annotations.Test
  public void testUnmarshal() throws Exception
  {
    EpochContainerJsonStreamer streamer = new EpochContainerJsonStreamer();
    Epoch.Builder b = Factories.getEpochBuilderFactory().createEpochBuilder();
    Epoch e = b.id(UUID.randomUUID()).addCountry("at").comment("",
                                                               "Ein Kommentar").name("",
                                                                                     "Name der Epoche").yearFrom(1837).yearTo(
            2021).build();
    EpochContainer container = new EpochContainer();
    container.add(e);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    streamer.marshal(container,
                     bos);
    ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
    EpochContainer resultContainer = streamer.unmarshal(bis);
    assertNotNull(resultContainer);
    assertEquals(1,
                 resultContainer.size());
    Epoch r = resultContainer.get(0);
    assertNotNull(r);
    assertEquals(e,
                 r);
  }

}
