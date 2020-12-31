/*
 * Copyright 2017 Wolfgang Reder.
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
package at.or.reder.swing.model;

import at.or.reder.swing.ModifiedCheckable;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.event.TableModelEvent;

public abstract class AbstractErrorAndCommitableEnumTableModel<E extends Enum<E>, D extends ModifiedCheckable<D>> extends AbstractEnumTableModelData<E, D>
        implements
        ErrorAndCommitableTableModel
{

  private boolean dataValid = true;
  private boolean dataChanged = false;
  private final PropertyChangeSupport2 pcs = new PropertyChangeSupport2(this);
  protected final List<D> commited = new ArrayList<>();
  private Boolean internalDataChanged;

  protected AbstractErrorAndCommitableEnumTableModel(Class<E> clazz)
  {
    super(clazz);
  }

  protected boolean testDataValid()
  {
    return true;
  }

  @Override
  protected void replaceDataItem(int index,
                                 D newItem)
  {
    internalDataChanged = null;
    super.replaceDataItem(index,
                          newItem);
  }

  protected void markChanged()
  {
    internalDataChanged = null;
  }

  protected boolean testDataChanged()
  {
    return false;
  }

  @Override
  public final boolean isDataValid()
  {
    return dataValid;
  }

  @Override
  public final void addPropertyChangeListener(String property,
                                              PropertyChangeListener l)
  {
    pcs.addListener(property,
                    l);
  }

  @Override
  public final void addPropertyChangeListener(PropertyChangeListener l)
  {
    pcs.addListener(l);
  }

  @Override
  public final void removePropertyChangeListener(String property,
                                                 PropertyChangeListener l)
  {
    pcs.removeListener(property,
                       l);
  }

  @Override
  public final void removePropertyChangeListener(PropertyChangeListener l)
  {
    pcs.removeListener(l);
  }

  protected void doCommit()
  {

  }

  @Override
  public final void commit()
  {
    commited.clear();
    commited.addAll(data);
    doCommit();
    markChanged();
  }

  protected void doRevert()
  {

  }

  @Override
  public final void revert()
  {
    boolean wasChanged = dataChanged;
    data.clear();
    data.addAll(commited);
    dataChanged = false;
    doRevert();
    markChanged();
    if (wasChanged) {
      fireContentsChanged();
    }
  }

  @Override
  public final boolean isDataChanged()
  {
    return dataChanged;
  }

  /**
   * Vergleicht die Listen data und commited. Die Standardimplementation vergleicht die Elemente paarweise.
   *
   * @param commited commited values
   * @param actual actual values
   * @return {@code true} wenn modfied
   */
  protected boolean checkItems(List<D> commited,
                               List<D> actual)
  {
    boolean result = false;
    Iterator<D> cgi = commited.iterator();
    Iterator<D> dgi = actual.iterator();
    while (!result && cgi.hasNext()) {
      D c = cgi.next();
      D d = dgi.next();
      result = c.isModified(d);
    }
    return result;
  }

  protected final void checkDataChanged()
  {
    if (internalDataChanged == null) {
      boolean wasChanged = dataChanged;
      dataChanged = testDataChanged() || commited.size() != data.size();
      if (!dataChanged) {
        dataChanged = checkItems(commited,
                                 data);
      }
      internalDataChanged = dataChanged;
      if (wasChanged != dataChanged) {
        pcs.firePropertyChange(PROP_DATACHANGED,
                               wasChanged,
                               dataChanged);
      }
    }
  }

  protected final void checkDataValid()
  {
    boolean wasValid = dataValid;
    dataValid = testDataValid();
    if (wasValid != dataValid) {
      pcs.firePropertyChange(PROP_DATAVALID,
                             wasValid,
                             dataValid);
    }
  }

  protected final void checkFlags()
  {
    checkDataChanged();
    checkDataValid();
  }

  @Override
  protected void beforeEventFired(TableModelEvent evt)
  {
    checkFlags();
  }

}
