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

import at.or.reder.mti.model.Entity;
import at.or.reder.mti.model.EntityKind;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author Wolfgang Reder
 */
public class XmlEntity implements XmlObject<Entity>
{

  @XmlAttribute(name = "id")
  @XmlID
  private UUID id;
  @XmlElement(name = "description")
  private XmlStringLocalizable description;
  @XmlAttribute(name = "file-name")
  private String fileName;
  @XmlAttribute(name = "kind")
  private EntityKind kind;
  @XmlAttribute(name = "mime-type")
  private String mimeType;
  @XmlAttribute(name = "size")
  private long size;
  @XmlAttribute(name = "origin")
  private URL origin;
  @XmlTransient
  private Entity entity;

  public XmlEntity()
  {
  }

  public XmlEntity(Entity e)
  {
    id = e.getId();
    description = new XmlStringLocalizable(e.getDescription());
    fileName = e.getFileName();
    kind = e.getKind();
    mimeType = e.getMimeType();
    size = e.getSize();
    origin = e.getOrigin();
    entity = e;
  }

  @Override
  public Entity toModel()
  {
    return null;
//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @XmlValue
  public InputStream getData() throws IOException
  {
    return entity.getData();
  }

}
