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

import at.or.reder.mti.model.api.EntityStore;
import at.or.reder.mti.model.api.EpochStore;
import at.or.reder.mti.model.api.GaugeStore;
import at.or.reder.mti.model.api.LocoStore;
import at.or.reder.mti.model.api.StoreException;
import at.or.reder.mti.model.api.Stores;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.openide.util.Lookup;

final class FBStores implements Stores
{

  private final DataSource ds;
  private final FBEpochStore epochStore;
  private final LocalizableStore locStore;
  private final FBEntityStore entityStore;
  private final FBGaugeStore gaugeStore;
  private final FBLocoStore locoStore;
  private final List<FBStore> stores = new ArrayList<>();

  public FBStores(DataSource ds)
  {
    this.ds = ds;
    locStore = initStore(new LocalizableStore(this));
    epochStore = initStore(new FBEpochStore(this,
                                            locStore));
    entityStore = initStore(new FBEntityStore(this,
                                              locStore));
    gaugeStore = initStore(new FBGaugeStore(this));
    locoStore = initStore(new FBLocoStore(this));
  }

  private <C extends FBStore> C initStore(C store)
  {
    stores.add(store);
    return store;
  }

  void startup() throws SQLException, StoreException
  {
    try (Connection conn = getConnection()) {
      for (StartupPhase phase : StartupPhase.values()) {
        if (phase == StartupPhase.FILL) {
          conn.commit();
        }
        for (FBStore as : stores) {
          as.startup(conn,
                     phase);
        }
      }
      conn.commit();
    }
  }

  public Connection getConnection() throws SQLException
  {
    return ds.getConnection();
  }

  @Override
  public EpochStore getEpochStore()
  {
    return epochStore;
  }

  @Override
  public EntityStore getEntityStore()
  {
    return entityStore;
  }

  @Override
  public GaugeStore getGaugeStore()
  {
    return gaugeStore;
  }

  @Override
  public LocoStore getLocoStore()
  {
    return locoStore;
  }

  @Override
  public Lookup getLookup()
  {
    return Lookup.EMPTY;
  }

}
