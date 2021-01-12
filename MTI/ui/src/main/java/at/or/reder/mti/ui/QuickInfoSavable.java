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
package at.or.reder.mti.ui;

import at.or.reder.mti.model.QuickInfo;
import java.io.IOException;
import java.util.Objects;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.cookies.SaveCookie;

/**
 *
 * @author Wolfgang Reder
 */
public class QuickInfoSavable extends AbstractSavable implements AutoCloseable
{

  private final QuickInfo qi;
  private final SaveCookie sc;

  public QuickInfoSavable(QuickInfo qi,
                          SaveCookie sc)
  {
    this.qi = qi;
    this.sc = sc;
    register();
  }

  @Override
  public void close()
  {
    unregister();
  }

  @Override
  protected String findDisplayName()
  {
    return qi.getLabel();
  }

  @Override
  protected void handleSave() throws IOException
  {
    sc.save();
  }

  @Override
  public int hashCode()
  {
    int hash = 7;
    hash = 31 * hash + Objects.hashCode(this.qi);
    return hash;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final QuickInfoSavable other = (QuickInfoSavable) obj;
    if (!Objects.equals(this.qi,
                        other.qi)) {
      return false;
    }
    return true;
  }

}
