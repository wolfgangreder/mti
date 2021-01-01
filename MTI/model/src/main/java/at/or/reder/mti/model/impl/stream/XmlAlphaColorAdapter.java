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

import at.or.reder.mti.model.utils.MTIUtils;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Wolfgang Reder
 */
public final class XmlAlphaColorAdapter extends XmlAdapter<String, Color>
{

  @Override
  public Color unmarshal(String vt)
  {
    if (vt != null) {
      try {
        return MTIUtils.stringToColor(vt);
      } catch (Throwable th) {
        Logger.getLogger("at.or.reder.ptc").log(Level.WARNING,
                                                "Cannot parse color string " + vt);
      }
    }
    return null;
  }

  @Override
  public String marshal(Color bt)
  {
    if (bt != null) {
      return MTIUtils.colorToHTMLString(bt,
                                        true);
    }
    return null;
  }

}
