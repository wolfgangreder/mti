/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.rdbms.firebirdsql;

import at.motriv.datamodel.MotrivUtils;
import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.LabelKeyPair;
import at.mountainsd.dataprovider.firebirdsql.AbstractFBItemProvider;
import java.util.List;
import org.openide.util.Lookup;

/**
 *
 * @author wolfi
 */
public abstract class AbstractMotrivFBItemProvider<K, V> extends AbstractFBItemProvider<K, V>
{

  protected AbstractMotrivFBItemProvider()
  {
    super(MotrivUtils.MOTRIV_CONTEXT);
  }

  @Override
  protected Lookup createLookup()
  {
    return null;
  }

  @Override
  public LabelKeyPair<K> getLabel(K pKey) throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<LabelKeyPair<K>> getAllLabels() throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public List<LabelKeyPair<K>> getLabelByName(String pName) throws DataProviderException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
