/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.motriv.filesystem.db;

import at.mountainsd.dataprovider.api.DataProviderException;
import at.mountainsd.dataprovider.api.ItemProvider;
import at.mountainsd.dataprovider.api.LabelKeyPair;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author wolfi
 */
public interface DBFileObjectItemProvider extends ItemProvider<UUID, DBFileObject>
{

  public static final UUID ID_ROOT = UUID.fromString("dee007d4-2ce3-4180-bbd1-dc14314344a4");
  public static final String NAME_ROOT = "motriv.db";
  public static final String ATTR_LAST_MODIFIED = DBFileObject.class.getName() + ".last_modified";
  public static final String ATTR_READ_ONLY = DBFileObject.class.getName() + ".read_only";

  public DBFileObject get(Iterator<String> path) throws DataProviderException;

  public DBFileObject getRoot() throws DataProviderException;

  public UUID findByName(UUID parent, String name) throws DataProviderException;

  public List<LabelKeyPair<UUID>> getChildren(UUID parent) throws DataProviderException;

  public List<LabelKeyPair<UUID>> getChildren(Iterator<String> path) throws DataProviderException;
}
