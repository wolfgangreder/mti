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
package at.or.reder.mti.model.spi;

import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.Test;

/**
 *
 * @author Wolfgang Reder
 */
public class IniParserNGTest
{

  public IniParserNGTest()
  {
  }

  @Test
  public void testParse() throws Exception
  {
    InputStream is = getClass().getResourceAsStream("iniTest1.ini");

    IniParser parser = new IniParser(is).setCommentStart("#");
    IniParserCollector collector = new IniParserCollector(parser).setCollectComments(true);
    parser.parse();
    assertEquals(2,
                 collector.getComments().size());
    assertEquals(" ini-comment",
                 collector.getComments().get(0));
    assertEquals(" ini-comment 2",
                 collector.getComments().get(1));
    Collection<String> sections = collector.getSections();
    assertEquals(2,
                 sections.size());
    Map<String, String> section = collector.getSectionValues("Section 1");
    assertEquals(2,
                 section.size());
    assertEquals("value1",
                 section.get("key1"));
    assertEquals("value2",
                 section.get("key2"));
    section = collector.getSectionValues("Section 2");
    assertEquals(1,
                 section.size());
    assertEquals("value3",
                 section.get("key3"));
  }

  @Test
  public void testParse2() throws Exception
  {
    InputStream is = getClass().getResourceAsStream("iniTest2.ini");
    IniParser parser = new IniParser(is).setCommentStart("//");
    IniParserCollector collector = new IniParserCollector(parser).setCollectComments(true);
    parser.parse();
    assertEquals(2,
                 collector.getComments().size());
    assertEquals(" ini-comment",
                 collector.getComments().get(0));
    assertEquals(" ini-comment 2",
                 collector.getComments().get(1));
    Collection<String> sections = collector.getSections();
    assertEquals(2,
                 sections.size());
    Map<String, String> section = collector.getSectionValues("Section 1");
    assertEquals(2,
                 section.size());
    assertEquals("value1",
                 section.get("key1"));
    assertEquals("value2",
                 section.get("key2"));
    section = collector.getSectionValues("Section 2");
    assertEquals(1,
                 section.size());
    assertEquals("value3",
                 section.get("key3"));
  }

  @Test
  public void testParse3() throws Exception
  {
    InputStream is = getClass().getResourceAsStream("iniTest3.ini");
    IniParser parser = new IniParser(is).setCommentStart("//");
    IniParserCollector collector = new IniParserCollector(parser).setCollectComments(true);
    parser.parse();
    assertEquals(2,
                 collector.getComments().size());
    assertEquals(" ini-comment",
                 collector.getComments().get(0));
    assertEquals(" ini-comment 2",
                 collector.getComments().get(1));
    Collection<String> sections = collector.getSections();
    assertEquals(3,
                 sections.size());
    Map<String, String> section = collector.getSectionValues("Section 1");
    assertEquals(2,
                 section.size());
    assertEquals("value1",
                 section.get("key1"));
    assertEquals("value2",
                 section.get("key2"));
    section = collector.getSectionValues("Section 2");
    assertEquals(1,
                 section.size());
    assertEquals("value3",
                 section.get("key3"));
    section = collector.getSectionValues("");
    assertEquals(1,
                 section.size());
    assertEquals("value4",
                 section.get("key4"));
  }

}
