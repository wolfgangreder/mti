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
package at.or.reder.mti.ui.loco;

import at.or.reder.mti.model.Locomotive;
import at.or.reder.swing.Commitable;
import at.or.reder.swing.CommitableAndErrorFlagableContainer;
import at.or.reder.swing.ErrorFlagable;
import java.util.function.Supplier;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 *
 * @author Wolfgang Reder
 */
public abstract class LocoPanel extends JPanel implements Commitable, ErrorFlagable
{

  protected final CommitableAndErrorFlagableContainer cef;

  public LocoPanel()
  {
    this(null,
         null,
         null);
  }

  public LocoPanel(Supplier<Boolean> testValid,
                   Supplier<Boolean> testModified,
                   String context)
  {
    cef = new CommitableAndErrorFlagableContainer(this,
                                                  testValid,
                                                  testModified,
                                                  context);
  }

  public abstract void showValues(Locomotive loco);

  public abstract void assignValues(Locomotive.Builder builder);

  @Override
  public void commit()
  {
    cef.commit();
  }

  @Override
  public void revert()
  {
    cef.revert();
  }

  @Override
  public boolean isDataChanged()
  {
    return cef.isDataChanged();
  }

  @Override
  public Border getErrorBorder()
  {
    return cef.getErrorBorder();
  }

  @Override
  public void setErrorBorder(Border newErrorBorder)
  {
    cef.setErrorBorder(newErrorBorder);
  }

  @Override
  public boolean isDataValid()
  {
    return cef.isDataValid();
  }

}
