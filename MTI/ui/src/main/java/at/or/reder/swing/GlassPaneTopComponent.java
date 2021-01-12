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
package at.or.reder.swing;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import javax.swing.JPanel;
import org.openide.windows.TopComponent;

@SuppressWarnings("serial")
public class GlassPaneTopComponent extends TopComponent implements GlassPaneProvider
{

  public static final Kernel DEFAULT_KERNEL = new Kernel(3,
                                                         3,
                                                         new float[]{0.08f, 0.08f, 0.08f,
                                                                     0.08f, 0.08f, 0.08f,
                                                                     0.08f, 0.08f, 0.08f});
  private BufferedImage snapshot = null;
  ConvolveOp op = new ConvolveOp(DEFAULT_KERNEL);
  private final CardLayout cardLayout = new CardLayout();
  private final JPanel masterPanel = new JPanel();
  private final JPanel glassPanel = new JPanel()
  {

    @Override
    public void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      if (snapshot != null) {
        g.drawImage(snapshot,
                    0,
                    0,
                    null);
      }
    }

  };
  private boolean useCardContainer = false;
  private final MouseListener hideMouseListener = new MouseListener()
  {

    @Override
    public void mouseClicked(MouseEvent e)
    {
      showGlassPanel(false);
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
    }

  };
  private boolean hideGlassOnBlurClick = true;

  public GlassPaneTopComponent()
  {
    super.setLayout(cardLayout);
    super.addImpl(masterPanel,
                  "master",
                  -1);
    super.addImpl(glassPanel,
                  "glass",
                  -1);
    useCardContainer = true;
  }

  @Override
  public boolean getHideGlassOnBlurClick()
  {
    return hideGlassOnBlurClick;
  }

  @Override
  public void setHideGlassOnBlurClick(boolean hide)
  {
    hideGlassOnBlurClick = hide;
  }

  @Override
  public JPanel getContentPanel()
  {
    return masterPanel;
  }

  @Override
  public JPanel getGlassPanel()
  {
    return glassPanel;
  }

  private void takeSnapshot()
  {
    BufferedImage img = new BufferedImage(masterPanel.getWidth(),
                                          masterPanel.getHeight(),
                                          BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g2 = img.createGraphics();
    try {
      masterPanel.paint(g2);
      snapshot = op.filter(img,
                           null);
    } finally {
      g2.dispose();
    }
  }

  public void setFilterKernel(Kernel k)
  {
    op = new ConvolveOp(k != null ? k : DEFAULT_KERNEL);
  }

  public Kernel getFilterKernel()
  {
    return op.getKernel();
  }

  private void clearSnapshot()
  {
    snapshot = null;
  }

  @Override
  public void showGlassPanel(boolean show)
  {
    if (show) {
      takeSnapshot();
      if (hideGlassOnBlurClick) {
        getGlassPanel().addMouseListener(hideMouseListener);
      }
      cardLayout.show(this,
                      "glass");
      glassPanelShown();
    } else {
      clearSnapshot();
      getGlassPanel().removeMouseListener(hideMouseListener);
      cardLayout.show(this,
                      "master");
      glassPanelHidden();
    }
  }

  @Override
  public final void setLayout(LayoutManager mgr)
  {
    if (useCardContainer) {
      masterPanel.setLayout(mgr);
    } else {
      super.setLayout(mgr);
    }
  }

  @Override
  protected void addImpl(Component comp,
                         Object constraints,
                         int index)
  {
    if (useCardContainer) {
      masterPanel.add(comp,
                      constraints,
                      index);
    } else {
      super.addImpl(comp,
                    constraints,
                    index);
    }
  }

  protected void glassPanelShown()
  {
  }

  protected void glassPanelHidden()
  {
  }

}
