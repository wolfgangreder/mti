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
package at.or.reder.mti.jaybirdsql;

import at.or.reder.mti.model.api.StoreException;
import at.or.reder.mti.model.api.StoreProvider;
import at.or.reder.osplatform.PlatformFolders;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import org.firebirdsql.gds.impl.GDSType;
import org.firebirdsql.gds.impl.wire.WireGDSFactoryPlugin;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = StoreProvider.class)
@Messages({"FBServerStoreProvider_name=FirebirdSQL Embedded Store",
           "FBServerStoreProvider_err_nodefault_dir=Es kann das Datenverzeichnis nicht automatisch ermittelt werden.",
           "# {0} - dirName",
           "# {1} - errMsg",
           "FBServerStoreProvider_err_cannot_create_datadir=Es kann das Datenverzeichnis \"{0}\" nicht erzeugt werden:{2}",
           "# {0} - dirName",
           "FBServerStoreProver_err_cannot_write_datadir=Das datenverzeichis \"{0}\" ist nicht schreibbar."})
public class FBServerStoreProvider extends AbstractFBStoreProvider implements StoreProvider
{

  public static final UUID ID = UUID.fromString("e7daddad-8239-47b1-873a-5b2676f40e69");

  public FBServerStoreProvider()
  {
    super(GDSType.getType(WireGDSFactoryPlugin.PURE_JAVA_TYPE_NAME));
  }

  @Override
  public UUID getId()
  {
    return ID;
  }

  @Override
  public String getName()
  {
    return Bundle.FBEmbStoreProvider_name();
  }

  @Override
  protected String getHost(Map<String, String> config)
  {
    return config.get("host");
  }

  @Override
  protected int getPort(Map<String, String> config)
  {
    int result = -1;
    try {
      result = Integer.parseInt(config.get("port"));
    } catch (Throwable th) {
    }
    return result;
  }

  @Override
  protected String prepareCreateDatabase(Map<String, String> config,
                                         Properties props) throws SQLException, StoreException, IOException
  {
    String processName = "MTI";
    if (PlatformFolders.isPlatformFoldersSupported()) {
      processName = processName + "@" + PlatformFolders.getInstance().getHostName();
    }
    props.put("authPlugins",
              "Srp256,Srp");//,Legacy_Auth");
    //props.put("wireCrypt","");
    props.put("processId",
              Long.toString(ProcessHandle.current().pid()));
    props.put("processName",
              processName);
    return "mti";
  }

}
