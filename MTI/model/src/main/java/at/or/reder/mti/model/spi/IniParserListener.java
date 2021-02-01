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

/**
 *
 * @author Wolfgang Reder
 */
public interface IniParserListener
{

  public default boolean processSectionStart(IniParser parser,
                                             String section)
  {
    return true;
  }

  public boolean processValue(IniParser parser,
                              String key,
                              String value);

  public default boolean processComment(IniParser parser,
                                        String comment)
  {
    return true;
  }

  public default boolean processUnrecognized(IniParser parser,
                                             String line)
  {
    return true;
  }

}
