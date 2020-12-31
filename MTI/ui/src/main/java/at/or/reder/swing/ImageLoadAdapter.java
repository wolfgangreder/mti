/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.or.reder.swing;

import at.or.reder.swing.model.JImageModel;

/**
 *
 * @author Wolfgang Reder
 */
public interface ImageLoadAdapter extends ImageLoadListener
{

  @Override
  public default void imageLoadError(JImageModel sender,
                                     Exception e)
  {
  }

  @Override
  public default void beginImageLoad(JImageModel sender)
  {
  }

  @Override
  public default void endImageLoad(JImageModel sender)
  {
  }

  @Override
  public default void imageLoadProgress(JImageModel sender,
                                        float percentDone)
  {
  }

  @Override
  public default void imageChanged(JImageModel sender)
  {
  }

  @Override
  public default void loadAborted(JImageModel sender)
  {
  }

}
