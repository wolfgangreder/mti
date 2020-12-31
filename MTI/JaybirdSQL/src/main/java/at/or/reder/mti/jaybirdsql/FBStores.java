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

import at.or.reder.mti.model.api.EpochStore;
import at.or.reder.mti.model.api.Stores;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.openide.util.Lookup;

final class FBStores implements Stores
{

  private final DataSource ds;

  public FBStores(DataSource ds)
  {
    this.ds = ds;
  }

  void startup()
  {

  }

  public Connection getConnection() throws SQLException
  {
    return ds.getConnection();
  }

  @Override
  public EpochStore getEpochStore()
  {
    try (Connection conn = getConnection()) {

    } catch (SQLException ex) {

    }
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public Lookup getLookup()
  {
    return Lookup.EMPTY;
  }

}
