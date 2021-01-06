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

import at.or.reder.mti.model.MTIConfig;
import at.or.reder.mti.model.api.StoreException;
import at.or.reder.mti.model.api.StoreProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import org.firebirdsql.gds.impl.GDSType;
import org.firebirdsql.gds.impl.jni.EmbeddedGDSFactoryPlugin;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = StoreProvider.class)
@Messages({"FBEmbStoreProvider_name=FirebirdSQL Embedded Store",
           "FBEmbStoreProvider_err_nodefault_dir=Es kann das Datenverzeichnis nicht automatisch ermittelt werden.",
           "# {0} - dirName",
           "# {1} - errMsg",
           "FBEmbStoreProvider_err_cannot_create_datadir=Es kann das Datenverzeichnis \"{0}\" nicht erzeugt werden:{2}",
           "# {0} - dirName",
           "FBEmbStoreProver_err_cannot_write_datadir=Das datenverzeichis \"{0}\" ist nicht schreibbar."})
public class FBEmbStoreProvider extends AbstractFBStoreProvider implements StoreProvider
{

  public static final UUID ID = UUID.fromString("1906c1d3-560f-4bb3-b386-b65a230fd7ad");

  public FBEmbStoreProvider()
  {
    super(GDSType.getType(EmbeddedGDSFactoryPlugin.EMBEDDED_TYPE_NAME));
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
    return null;
  }

  @Override
  protected int getPort(Map<String, String> config)
  {
    return -1;
  }

  @Override
  protected String prepareCreateDatabase(Map<String, String> config,
                                         Properties props) throws SQLException, StoreException, IOException
  {
    Path dataPath = null;
    String dataDir = config.get("datadir");
    try {
      if (dataDir == null) {
        MTIConfig cfg = Lookup.getDefault().lookup(MTIConfig.class);
        dataPath = cfg.getDatadirectory();
      } else {
        dataPath = Paths.get(dataDir).toRealPath();
      }
      Files.createDirectories(dataPath);
      if (!Files.isWritable(dataPath)) {
        throw new StoreException(Bundle.FBEmbStoreProver_err_cannot_write_datadir(dataPath));
      }
      dataPath = Paths.get(dataPath.toString(),
                           "database");
      Files.createDirectories(dataPath);
    } catch (IOException ex) {
      throw new StoreException(Bundle.FBEmbStoreProvider_err_cannot_create_datadir(dataPath.toString(),
                                                                                   ex.getLocalizedMessage()),
                               ex);
    }
    return Path.of(dataPath.toString(),
                   "mti.firebird").toString();
  }

}
