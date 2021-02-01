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
import at.or.reder.mti.model.api.Factories;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Wolfgang Reder
 */
@XmlRootElement(name = "epoch")
public class XmlEpoch implements XmlObject<Epoch>
{

  @XmlID
  @XmlAttribute(name = "id")
  private String id;
  @XmlElement(name = "name")
  private XmlStringLocalizable name;
  @XmlAttribute(name = "year-from")
  private int yearFrom;
  @XmlAttribute(name = "year-to")
  private Integer yearTo;
  @XmlElement(name = "countries") // Jackson mag @XmlList nicht, darum wird das hier simuliert
  private String countries;
  @XmlElement(name = "comment")
  private XmlStringLocalizable comment;

  public XmlEpoch()
  {
  }

  public XmlEpoch(Epoch epoch)
  {
    id = Objects.requireNonNull(epoch,
                                "epoch is null").getId().toString();
    name = new XmlStringLocalizable(epoch.getName());
    yearFrom = epoch.getYearFrom();
    yearTo = epoch.getYearTo();
    Set<String> c = epoch.getCountries();
    if (!c.isEmpty()) {
      countries = c.stream().
              filter((country) -> country != null && !country.isBlank()).
              collect(Collectors.joining(" "));
    }
    comment = new XmlStringLocalizable(epoch.getComment());
  }

  @Override
  public Epoch toModel()
  {
    Epoch.Builder builder = Factories.getEpochBuilderFactory().createEpochBuilder();
    if (countries != null) {
      builder.addCountries(Arrays.asList(countries.split(" ")));
    }
    builder.id(UUID.fromString(id));
    builder.yearFrom(yearFrom);
    builder.yearTo(yearTo);
    if (name != null) {
      builder.name(name.toLocalizable(true));
    }
    if (comment != null) {
      builder.comment(comment.toLocalizable(true));
    }
    return builder.build();
  }

}
