/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.datamodel.entities.scale;

import at.motriv.datamodel.entities.scale.impl.DefaultScaleXMLSupport;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataNode;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.text.DataEditorSupport;

public class ScalesDataObject extends MultiDataObject
{
  private static final long serialVersionUID = 1L;

  public ScalesDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException
  {
    super(pf, loader);
    CookieSet cookies = getCookieSet();
    cookies.add((Node.Cookie) DataEditorSupport.create(this, getPrimaryEntry(), cookies));
    cookies.add(new DefaultScaleXMLSupport(this));
  }

  @Override
  protected Node createNodeDelegate()
  {
    return new DataNode(this, Children.LEAF, getLookup());
  }

  @Override
  public Lookup getLookup()
  {
    return getCookieSet().getLookup();
  }
}
