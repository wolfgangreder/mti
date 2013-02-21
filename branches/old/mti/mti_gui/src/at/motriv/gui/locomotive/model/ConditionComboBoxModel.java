/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.gui.locomotive.model;

import at.motriv.datamodel.ModelCondition;
import at.motriv.datamodel.config.MotrivConfig;
import at.motriv.gui.models.DefaultValuesComboBoxModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author wolfi
 */
public class ConditionComboBoxModel extends DefaultValuesComboBoxModel<ModelCondition>
{

  public ConditionComboBoxModel()
  {
    refresh();
  }

  @Override
  protected void sortItems(List<? extends ModelCondition> items)
  {
    Collections.sort(items,new Comparator<ModelCondition>(){

      @Override
      public int compare(ModelCondition o1, ModelCondition o2)
      {
        return o1.ordinal()-o2.ordinal();
      }
    });
  }

  @Override
  protected List<? extends ModelCondition> getCurrentItems()
  {
    return Collections.emptyList();
  }

  @Override
  protected List<? extends ModelCondition> getDefaultItems()
  {
    return Arrays.asList(ModelCondition.values());
  }

  @Override
  protected ModelCondition getDefaultSelection()
  {
    return MotrivConfig.getConfigValue(MotrivConfig.KEY_DEFAULT_CONDITION);
  }
}
