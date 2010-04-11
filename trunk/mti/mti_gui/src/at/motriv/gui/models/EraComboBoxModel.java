/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.models;

import at.motriv.datamodel.MotrivItemProviderLookup;
import at.motriv.datamodel.config.MotrivConfig;
import at.motriv.datamodel.entities.era.Era;
import at.motriv.datamodel.entities.era.EraItemProvider;
import at.mountainsd.dataprovider.api.DataProviderException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.openide.util.Exceptions;

/**
 *
 * @author wolfi
 */
public class EraComboBoxModel extends DefaultValuesComboBoxModel<Era>
{

  private static class EraWrapper implements Era
  {

    private Era wrapped;

    public EraWrapper(Era wrapped)
    {
      this.wrapped = wrapped;
    }

    @Override
    public Integer getYearTo()
    {
      return wrapped.getYearTo();
    }

    @Override
    public int getYearFrom()
    {
      return wrapped.getYearFrom();
    }

    @Override
    public String getName()
    {
      return wrapped.getName();
    }

    @Override
    public UUID getId()
    {
      return wrapped.getId();
    }

    @Override
    public String getCountry()
    {
      return wrapped.getCountry();
    }

    @Override
    public String getComment()
    {
      return wrapped.getComment();
    }

    @Override
    public String toString()
    {
      if (getYearTo()!=null) {
        return MessageFormat.format("{0} ({3}) {1,number,#}-{2,number,#}", getName(), getYearFrom(), getYearTo(), getCountry());
      } else {
        return MessageFormat.format("{0} ({3}) {1,number,#}-", getName(), getYearFrom(), getYearTo(), getCountry());
      }
    }
  }

  @Override
  protected void sortItems(List<? extends Era> items)
  {
    Collections.sort(items, new Comparator<Era>()
    {

      @Override
      public int compare(Era o1, Era o2)
      {
        int result = o1.getYearFrom() - o2.getYearFrom();
        if (result == 0) {
          int y1 = o1.getYearTo() != null ? o1.getYearTo() : -1;
          int y2 = o2.getYearTo() != null ? o2.getYearTo() : -1;
          return y2 - y1;
        }
        return result;
      }
    });
  }

  @Override
  protected List<? extends Era> getCurrentItems()
  {
    EraItemProvider provider = MotrivItemProviderLookup.lookup(EraItemProvider.class);
    if (provider != null) {
      try {
        List<? extends Era> tmp = provider.getAll();
        List<Era> result = new ArrayList<Era>(tmp.size());
        for (Era e : tmp) {
          result.add(new EraWrapper(e));
        }
        return result;
      } catch (DataProviderException ex) {
        Exceptions.printStackTrace(ex);
      }
    }
    return Collections.emptyList();
  }

  @Override
  protected List<? extends Era> getDefaultItems()
  {
    return Collections.emptyList();
  }

  @Override
  protected Era getDefaultSelection()
  {
    UUID defaultId = MotrivConfig.getConfigValue(MotrivConfig.KEY_DEFAULT_ERA);
    if (defaultId != null) {
      EraItemProvider provider = MotrivItemProviderLookup.lookup(EraItemProvider.class);
      if (provider != null) {
        try {
          return provider.get(defaultId);
        } catch (DataProviderException ex) {
          Exceptions.printStackTrace(ex);
        }
      }
    }
    return null;
  }
}
