/*
 * Copyright 2020 Wolfgang Reder.
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
package at.or.reder.mti.model;

import java.util.MissingResourceException;
import java.util.Set;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 *
 * @author wolfi
 */
@Messages({"ContactType_RETAILER=Händler",
           "ContactType_MANUFACTURER=Hersteller",
           "ContactType_CLUB=Club",
           "ContactType_PERSONAL=Person"})
public enum ContactType
{

  RETAILER,
  MANUFACTURER,
  CLUB,
  PERSONAL;
  public static final Set<ContactType> ALL = Set.of(ContactType.values());

  @Override
  public String toString()
  {
    try {
      return NbBundle.getMessage(ContactType.class,
                                 "ContactType_" + name());
    } catch (MissingResourceException ex) {

    }
    return name();
  }

}
