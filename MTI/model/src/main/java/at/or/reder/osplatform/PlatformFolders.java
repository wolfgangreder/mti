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
package at.or.reder.osplatform;

import at.or.reder.osplatform.impl.KDEKnownFolders;
import at.or.reder.osplatform.impl.Win32KnownFolders;
import at.or.reder.osplatform.thumbnail.ThumbnailGenerator;
import at.or.reder.osplatform.thumbnail.ThumbnailOption;
import com.sun.jna.Platform;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class PlatformFolders
{

  private static PlatformFolders instance;

  public static boolean isPlatformFoldersSupported()
  {
    return Platform.isWindows() || Platform.isLinux();
  }

  public static synchronized PlatformFolders getInstance()
  {
    if (instance == null) {
      if (Platform.isWindows()) {
        instance = new Win32KnownFolders();
      } else if (Platform.isLinux()) {
        instance = new KDEKnownFolders();
      } else {
        throw new UnsupportedOperationException("Current platform not supported");
      }
    }
    return instance;
  }

  public abstract Path getTrashFolder();

  public abstract Path getUserHome();

  public abstract Path getDownloadsFolder();

  public abstract Path getDesktopFolder();

  public abstract Path getDokumentsFolder();

  public abstract Path getPicturesFolder();

  public abstract Path getVideosFolder();

  public abstract Path getMusicFolder();

  public abstract Path getPublicDownloadsFolder();

  public abstract Path getPublicDesktopFolder();

  public abstract Path getPublicDokumentsFolder();

  public abstract Path getPublicPicturesFolder();

  public abstract Path getPublicVideosFolder();

  public abstract Path getPublicMusicFolder();

  public abstract Path getCacheFolder();

  public Path getThumbnailFolder()
  {
    return null;
  }

  public abstract boolean isTrashAvailable();

  public abstract boolean moveToTrash(Path file) throws IOException;

  public String formatFileSize(long size)
  {
    return formatFileSize(size,
                          4);
  }

  public abstract String formatFileSize(long size,
                                        int numDigits);

  public boolean moveToTrash(File file) throws IOException
  {
    return moveToTrash(file.toPath());
  }

  public boolean moveToTrash(String file) throws IOException
  {
    return moveToTrash(Paths.get(file));
  }

  public Path findThumbnail(Path file,
                            ThumbnailOption thumbSize) throws IOException
  {
    throw new UnsupportedOperationException();
  }

  public Path getThumbnail(Path file,
                           ThumbnailOption thumbSize,
                           ThumbnailGenerator generator) throws IOException
  {
    throw new UnsupportedOperationException();
  }

}
