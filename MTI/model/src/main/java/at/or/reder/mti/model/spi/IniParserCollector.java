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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Wolfgang Reder
 */
public class IniParserCollector
{

  private final Map<String, Map<String, String>> items = new HashMap<>();
  private Map<String, String> currentSection;
  private String endSectionName;
  private final List<String> comments = new ArrayList<>();
  private boolean collectComments = false;

  public IniParserCollector(IniParser parser)
  {
    parser.setListener(myListener);
  }

  public boolean isCollectComments()
  {
    return collectComments;
  }

  public IniParserCollector setCollectComments(boolean collectComments)
  {
    this.collectComments = collectComments;
    return this;
  }

  public Collection<String> getSections()
  {
    return Collections.unmodifiableCollection(items.keySet());
  }

  public Map<String, String> getSectionValues(String sectionName)
  {
    return Collections.unmodifiableMap(items.getOrDefault(sectionName,
                                                          Collections.emptyMap()));
  }

  public List<String> getComments()
  {
    return Collections.unmodifiableList(comments);
  }

  private final IniParserListener myListener = new IniParserListener()
  {
    @Override
    public boolean processSectionStart(IniParser parser,
                                       String section)
    {
      if (Objects.equals(section,
                         endSectionName)) {
        currentSection = null;
        endSectionName = null;
      } else {
        currentSection = items.computeIfAbsent(section,
                                               (s) -> new HashMap<>());
        endSectionName = "/" + section;
      }
      return true;
    }

    @Override
    public boolean processValue(IniParser parser,
                                String key,
                                String value)
    {
      if (currentSection == null) {
        processSectionStart(parser,
                            "");
      }
      currentSection.put(key,
                         value != null ? value : "");
      return true;
    }

    @Override
    public boolean processComment(IniParser parser,
                                  String comment)
    {
      if (collectComments) {
        comments.add(comment);
      }
      return true;
    }

  };
}
