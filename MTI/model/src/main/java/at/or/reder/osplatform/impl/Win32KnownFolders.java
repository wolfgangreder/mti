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
package at.or.reder.osplatform.impl;

import at.or.reder.osplatform.FileSizeFormatter;
import at.or.reder.osplatform.PlatformFolders;
import at.or.reder.osplatform.thumbnail.ThumbnailGenerator;
import at.or.reder.osplatform.thumbnail.ThumbnailOption;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Guid.GUID;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.KnownFolders;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.ShellAPI;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Win32KnownFolders extends PlatformFolders
{

  private final LazySyncFuture<Path> home = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_UsersFiles));
  private final LazySyncFuture<Path> recyclebin = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_RecycleBinFolder));
  private final LazySyncFuture<Path> downloads = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_Downloads));
  private final LazySyncFuture<Path> publicDesktop = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_PublicDesktop));
  private final LazySyncFuture<Path> desktop = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_Desktop));
  private final LazySyncFuture<Path> publicDocuments = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_PublicDocuments));
  private final LazySyncFuture<Path> documents = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_Documents));
  private final LazySyncFuture<Path> publicDownloads = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_Downloads));
  private final LazySyncFuture<Path> publicPictures = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_PublicPictures));
  private final LazySyncFuture<Path> pictures = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_Pictures));
  private final LazySyncFuture<Path> publicVideos = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_PublicVideos));
  private final LazySyncFuture<Path> videos = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_Videos));
  private final LazySyncFuture<Path> publicMusic = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_PublicMusic));
  private final LazySyncFuture<Path> music = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_Music));
  private final LazySyncFuture<Path> cacheDir = new LazySyncFuture(() -> loadPath(KnownFolders.FOLDERID_LocalAppData));

  public Win32KnownFolders()
  {
    if (!Platform.isWindows()) {
      throw new IllegalStateException("This is a Windows only class");
    }
  }

  private Path loadPath(GUID guid)
  {
    PointerByReference ref = new PointerByReference();
    WinNT.HRESULT result = Shell32.INSTANCE.SHGetKnownFolderPath(guid,
                                                                 0,
                                                                 null,
                                                                 ref);
    if (result.longValue() == 0) {
      try {
        String path = pointerToString(ref.getValue());
        return Paths.get(path);
      } finally {
        Ole32.INSTANCE.CoTaskMemFree(ref.getValue());
      }
    }
    return null;
  }

  private static String pointerToString(Pointer ptr)
  {
    if (ptr == null) {
      return null;
    }
    final StringBuilder result = new StringBuilder();
    char[] buffer;
    int offset = 0;
    final int bufSize = 255;
    for (;;) {
      buffer = ptr.getCharArray(offset,
                                bufSize);
      for (char ch : buffer) {
        if (ch == 0) {
          return result.toString();
        } else {
          result.append(ch);
        }
      }
      offset += bufSize * 2;
    }
  }

  @Override
  public Path getTrashFolder()
  {
    return recyclebin.get();
  }

  @Override
  public Path getUserHome()
  {
    return home.get();
  }

  @Override
  public Path getDownloadsFolder()
  {
    return downloads.get();
  }

  @Override
  public Path getPublicDownloadsFolder()
  {
    return publicDownloads.get();
  }

  @Override
  public Path getPublicDesktopFolder()
  {
    return publicDesktop.get();
  }

  @Override
  public Path getDesktopFolder()
  {
    return desktop.get();
  }

  @Override
  public Path getPublicDokumentsFolder()
  {
    return publicDocuments.get();
  }

  @Override
  public Path getDokumentsFolder()
  {
    return documents.get();
  }

  @Override
  public Path getPublicPicturesFolder()
  {
    return publicPictures.get();
  }

  @Override
  public Path getPicturesFolder()
  {
    return pictures.get();
  }

  @Override
  public Path getPublicVideosFolder()
  {
    return publicVideos.get();
  }

  @Override
  public Path getVideosFolder()
  {
    return videos.get();
  }

  @Override
  public Path getPublicMusicFolder()
  {
    return publicMusic.get();
  }

  @Override
  public Path getMusicFolder()
  {
    return music.get();
  }

  @Override
  public Path getCacheFolder()
  {
    return cacheDir.get();
  }

  @Override
  public boolean isTrashAvailable()
  {
    return true;
  }

  @Override
  public boolean moveToTrash(String file) throws IOException
  {
    Shell32 shell = Shell32.INSTANCE;
    ShellAPI.SHFILEOPSTRUCT fileop = new ShellAPI.SHFILEOPSTRUCT();
    fileop.wFunc = ShellAPI.FO_DELETE;
    String[] paths = new String[]{file};
    fileop.pFrom = new WString(fileop.encodePaths(paths)).toString();
    fileop.fFlags = ShellAPI.FOF_ALLOWUNDO | ShellAPI.FOF_NO_UI;
    int ret = shell.SHFileOperation(fileop);
    if (ret != 0) {
      throw new IOException("Move to trash failed: " + fileop.pFrom + ": "
                            + Kernel32Util.formatMessageFromLastErrorCode(ret));
    }
    if (fileop.fAnyOperationsAborted) {
      throw new IOException("Move to trash aborted");
    }
    return true;
  }

  @Override
  public boolean moveToTrash(File file) throws IOException
  {
    return moveToTrash(file.getAbsolutePath());
  }

  @Override
  public boolean moveToTrash(Path file) throws IOException
  {
    return moveToTrash(file.toAbsolutePath().toString());
  }

  @Override
  public String formatFileSize(long size,
                               int numDigits)
  {
    return FileSizeFormatter.formatFileSize(size,
                                            numDigits,
                                            FileSizeFormatter.Flags.NONISO);
  }

  @Override
  public Path getThumbnailFolder()
  {
    return Paths.get(cacheDir.get().toString(),
                     "thumbnails");
  }

  /**
   * https://specifications.freedesktop.org/thumbnail-spec/thumbnail-spec-latest.html
   *
   * @param file
   * @param thumbSize
   * @return
   * @throws IOException
   */
  @Override
  public Path getThumbnail(Path fileIn,
                           ThumbnailOption thumbSize,
                           ThumbnailGenerator generator) throws IOException
  {
    return KDEThumbnailImplementation.getThumbnail(this,
                                                   fileIn,
                                                   thumbSize,
                                                   generator);
  }

  @Override
  public Path findThumbnail(Path file,
                            ThumbnailOption thumbSize) throws IOException
  {
    return KDEThumbnailImplementation.findThumbnail(this,
                                                    file,
                                                    thumbSize);
  }

}
