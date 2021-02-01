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

import at.or.reder.dcc.cv.CVAddress;
import at.or.reder.dcc.cv.CVEntry;
import at.or.reder.dcc.cv.CVSet;
import at.or.reder.dcc.util.DCCUtils;
import at.or.reder.swing.model.AbstractErrorAndCommitableEnumTableModel;
import at.or.reder.swing.model.ColumnDescriptor;
import java.util.Objects;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import org.openide.util.NbBundle.Messages;

@Messages({"CVTableModel_col_STATE=Status",
           "CVTableModel_col_ADDRESS=Nummer",
           "CVTableModel_col_BANK=Bank",
           "CVTableModel_col_NAME=Name",
           "CVTableModel_col_VALUE=Wert",
           "CVTableModel_col_READVALUE=Gelesen",
           "CVTableModel_col_DESCRIPTION=Beschreibung"})
public final class CVTableModel extends AbstractErrorAndCommitableEnumTableModel<CVTableModel.Columns, CVRecord>
{

  public static enum Columns implements ColumnDescriptor
  {

    STATE(Bundle.CVTableModel_col_STATE(),
          CVState.class),
    ADDRESS(Bundle.CVTableModel_col_ADDRESS(),
            Integer.class),
    BANK(Bundle.CVTableModel_col_BANK(),
         Integer.class),
    NAME(Bundle.CVTableModel_col_NAME(),
         String.class),
    VALUE(Bundle.CVTableModel_col_VALUE(),
          Integer.class),
    READVALUE(Bundle.CVTableModel_col_READVALUE(),
              Integer.class),
    DESCRIPTION(Bundle.CVTableModel_col_DESCRIPTION(),
                String.class);

    private final Class<?> clazz;
    private final String headerValue;

    private Columns(String headerValue,
                    Class<?> clazz)
    {
      this.clazz = clazz;
      this.headerValue = headerValue;
    }

    @Override
    public String getHeaderValue()
    {
      return headerValue;
    }

    @Override
    public Class<?> getValueClass()
    {
      return clazz;
    }

  }

  private static final class RecordImpl implements CVRecord
  {

    private final CVAddress address;
    private final Integer value;
    private final Integer readValue;
    private final CVState state;
    private final CVEntry entry;

    public RecordImpl(CVAddress address,
                      Integer value,
                      Integer readValue,
                      CVState state,
                      CVEntry entry)
    {
      this.address = address;
      this.value = value;
      this.readValue = readValue;
      this.state = state;
      this.entry = entry;
    }

    public CVRecord withValue(Integer value)
    {
      return new RecordImpl(address,
                            value,
                            readValue,
                            state,
                            entry);
    }

    public CVRecord withState(CVState state)
    {
      return new RecordImpl(address,
                            value,
                            readValue,
                            state,
                            entry);
    }

    public CVRecord withValueAndState(Integer value,
                                      CVState state)
    {
      return new RecordImpl(address,
                            value,
                            readValue,
                            state,
                            entry);
    }

    public CVRecord withReadValueAndState(Integer readValue,
                                          CVState state)
    {
      return new RecordImpl(address,
                            value,
                            readValue,
                            state,
                            entry);
    }

    @Override
    public CVAddress getCVAddress()
    {
      return address;
    }

    @Override
    public String getName()
    {
      return entry != null ? entry.getName() : "CV " + DCCUtils.formatCVAddress(address);
    }

    @Override
    public String getDescription()
    {
      return entry != null ? entry.getDescription() : "";
    }

    @Override
    public Integer getValue()
    {
      return value;
    }

    @Override
    public Integer getReadValue()
    {
      return readValue;
    }

    @Override
    public CVState getState()
    {
      return state;
    }

    @Override
    public CVEntry getEntry()
    {
      return entry;
    }

    @Override
    public boolean isModified(CVRecord mc)
    {
      return mc.getState() != getState() || !Objects.equals(mc.getValue(),
                                                            getValue());
    }

  }
  private CVSet cvSet;

  public CVTableModel()
  {
    super(Columns.class);
  }

  public CVSet getCvSet()
  {
    return cvSet;
  }

  public void setCvSet(CVSet cvSet)
  {
    this.cvSet = cvSet;
    fireHeaderChanged();
    fireContentsChanged();
  }

  @Override
  protected Object getValue(CVRecord data,
                            Columns col)
  {
    switch (col) {
      case ADDRESS:
        return data.getCVAddress();
      case DESCRIPTION:
        return data.getDescription();
      case NAME:
        return data.getName();
      case READVALUE:
        return data.getReadValue();
      case STATE:
        return data.getState();
      case VALUE:
        return data.getValue();
      default:
        return null;
    }
  }

  @Override
  public TableCellRenderer getRenderer(Columns col)
  {
    return super.getRenderer(col); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public TableCellEditor getEditor(Columns col)
  {
    return super.getEditor(col); //To change body of generated methods, choose Tools | Templates.
  }

}
