/*
 * Copyright 2017-2021 Wolfgang Reder.
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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.LongAdder;

/*
 * https://www.freedesktop.org/wiki/Specifications/
 */
public final class KDEKnownFolders extends PlatformFolders
{

  private final LazySyncFuture<Path> publicShare = new LazySyncFuture(() -> loadPathFromUserDir("XDG_PUBLICSHARE_DIR"));
  private final LazySyncFuture<Path> home = new LazySyncFuture(() -> Paths.get(System.getProperty("user.home")));
  private final LazySyncFuture<Path> recyclebin = new LazySyncFuture(this::createRecycleBinPath);
  private final LazySyncFuture<Path> downloads = new LazySyncFuture(() -> loadPathFromUserDir("XDG_DOWNLOAD_DIR"));
  private final LazySyncFuture<Path> desktop = new LazySyncFuture(() -> loadPathFromUserDir("XDG_DESKTOP_DIR"));
  private final LazySyncFuture<Path> documents = new LazySyncFuture(() -> loadPathFromUserDir("XDG_DOCUMENTS_DIR"));
  private final LazySyncFuture<Path> pictures = new LazySyncFuture(() -> loadPathFromUserDir("XDG_PICTURES_DIR"));
  private final LazySyncFuture<Path> videos = new LazySyncFuture(() -> loadPathFromUserDir("XDG_VIDEOS_DIR"));
  private final LazySyncFuture<Path> music = new LazySyncFuture(() -> loadPathFromUserDir("XDG_MUSIC_DIR"));
  private final LazySyncFuture<Path> cacheDir = new LazySyncFuture(this::getCacheRoot);
  private final LazySyncFuture<Path> thumbnailDir = new LazySyncFuture(this::getThumbnailDir);

  public KDEKnownFolders()
  {
    if (!Platform.isLinux()) {
      throw new IllegalStateException("This is a Linux only class");
    }
  }

  private Path getEnvOrDefault(String env,
                               String defaultValue)
  {
    String tmp = System.getenv(env);
    if (tmp == null || tmp.isEmpty()) {
      tmp = defaultValue;
    }
    if (tmp != null) {
      tmp = tmp.replaceAll("\\$HOME",
                           System.getProperty("user.home"));
      return Paths.get(tmp);
    }
    return null;
  }

  private Path getDataHome()
  {
    return getEnvOrDefault("XDG_DATA_HOME",
                           "$HOME" + File.separator + ".local" + File.separator + "share");
  }

  private Path getConfigHome()
  {
    return getEnvOrDefault("XDG_CONFIG_HOME",
                           "$HOME" + File.separator + ".config");
  }

  private Path getCacheRoot()
  {
    return getEnvOrDefault("XDG_CACHE_HOME",
                           "$HOME" + File.separator + ".cache");

  }

  private Path getThumbnailDir()
  {
    Path cacheRoot = getEnvOrDefault("XDG_CACHE_HOME",
                                     null);
    if (cacheRoot == null) {
      Path suseCandicate = Paths.get(getUserHome().toString(),
                                     ".thumbnails");
      if (Files.isDirectory(suseCandicate)) {
        Path sub = Paths.get(suseCandicate.toString(),
                             "normal");
        int subCounter = 0;
        if (Files.isDirectory(sub)) {
          ++subCounter;
        }
        sub = Paths.get(suseCandicate.toString(),
                        "large");
        if (Files.isDirectory(sub)) {
          ++subCounter;
        }
        if (subCounter > 0) {
          return suseCandicate;
        }
      }
      return Paths.get(getUserHome().toString(),
                       ".cache",
                       "thumbnails");
    } else {
      return Paths.get(cacheRoot.toString(),
                       "thumbnails");
    }
  }

  private Path createRecycleBinPath()
  {
    return Paths.get(getDataHome().toString(),
                     "Trash");
  }

  private Path loadPathFromUserDir(String folderId)
  {
    String strHome = home.get().toString().trim();
    Path path = Paths.get(getConfigHome().toString(),
                          "user-dirs.dirs");
    try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(Files.newInputStream(path)))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.startsWith(folderId)) {
          int equalPos = line.indexOf('=');
          if (equalPos > 0) {
            String tmp = line.substring(equalPos + 1);
            tmp = tmp.replaceAll("\"",
                                 "");
            tmp = tmp.replaceAll("\\$HOME",
                                 strHome);
            return Paths.get(tmp);
          }
        }
      }
    } catch (IOException ex) {

    }
    return loadPathFromUserDirDefaults(folderId,
                                       strHome);
  }

  private Path loadPathFromUserDirDefaults(String folderId,
                                           String strHome)
  {
    Path path = Paths.get(File.separator + "etc",
                          "xdg",
                          "user-dirs.defaults");
    String transformed = folderId.replaceAll("(XDG_)|(_DIR)",
                                             "");
    try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(Files.newInputStream(path)))) {
      String line;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.startsWith(transformed)) {
          int equalPos = line.indexOf('=');
          if (equalPos > 0) {
            String tmp = line.substring(equalPos + 1);
            tmp = tmp.replaceAll("\"",
                                 "");
            return Paths.get(strHome,
                             tmp);
          }
        }
      }
    } catch (IOException ex) {

    }
    return null;
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
  public Path getDesktopFolder()
  {
    return desktop.get();
  }

  @Override
  public Path getDocumentsFolder()
  {
    return documents.get();
  }

  @Override
  public Path getPicturesFolder()
  {
    return pictures.get();
  }

  @Override
  public Path getVideosFolder()
  {
    return videos.get();
  }

  @Override
  public Path getMusicFolder()
  {
    return music.get();
  }

  @Override
  public Path getPublicDownloadsFolder()
  {
    return publicShare.get();
  }

  @Override
  public Path getPublicDesktopFolder()
  {
    return publicShare.get();
  }

  @Override
  public Path getPublicDocumentsFolder()
  {
    return publicShare.get();
  }

  @Override
  public Path getPublicPicturesFolder()
  {
    return publicShare.get();
  }

  @Override
  public Path getPublicVideosFolder()
  {
    return publicShare.get();
  }

  @Override
  public Path getPublicMusicFolder()
  {
    return publicShare.get();
  }

  @Override
  public Path getCacheFolder()
  {
    return cacheDir.get();
  }

  private FileLock testCreateFile(Path path)
  {
    try {
      FileChannel channel = FileChannel.open(path,
                                             StandardOpenOption.WRITE,
                                             StandardOpenOption.CREATE_NEW);
      return channel.lock();
    } catch (IOException ex) {
    }
    return null;
  }

  private TrashContext findTrashFileName(String name,
                                         Path infoDir,
                                         Path filesDir) throws IOException
  {
    String strInfoDir = infoDir.toString();
    String strNewName = name;
    Path newName = Paths.get(strInfoDir,
                             strNewName + ".trashinfo");
    long ctr = 1;
    FileLock lock = null;
    while ((lock = testCreateFile(newName)) == null) {
      strNewName = name + "_" + ctr;
      newName = Paths.get(strInfoDir,
                          strNewName + ".trashinfo");
      ++ctr;
    }
    return new TrashContext(Paths.get(filesDir.toString(),
                                      strNewName),
                            lock,
                            newName);
  }

  private static final DateTimeFormatter TRASH_TS_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  private void createTrashInfoFile(Path fileToDelete,
                                   TrashContext trashContext) throws IOException
  {
    Path info = trashContext.getInfoFile();
    try (PrintWriter writer = new PrintWriter(Files.newOutputStream(info,
                                                                    StandardOpenOption.WRITE))) {
      writer.println("[Trash Info]");
      writer.print("Path=");
      String tmp = URLEncoder.encode(fileToDelete.toString(),
                                     StandardCharsets.UTF_8.name());
      writer.println(tmp);
      writer.print("DeletionDate=");
      synchronized (TRASH_TS_FORMAT) {
        tmp = TRASH_TS_FORMAT.format(LocalDateTime.now());
      }
      writer.println(tmp);
    }
  }

  private long calculateDirectorySize(Path dir) throws IOException
  {
    final LongAdder adder = new LongAdder();
    Files.walkFileTree(dir,
                       new SimpleFileVisitor<Path>()
               {
                 @Override
                 public FileVisitResult visitFile(Path file,
                                                  BasicFileAttributes attrs) throws IOException
                 {
                   if (attrs.isRegularFile()) {
                     adder.add(attrs.size());
                   }
                   return FileVisitResult.CONTINUE;
                 }

               });
    return adder.longValue();
  }

  private void updateDirectoriesSize(Path oldFile,
                                     TrashContext op) throws IOException
  {
    final long dirSize = calculateDirectorySize(oldFile);
    StringBuilder builder = new StringBuilder();
    builder.append(Long.toString(dirSize));
    builder.append(" ");
    FileTime fTime = Files.getLastModifiedTime(op.getInfoFile());
    builder.append(Long.toString(fTime.toMillis()));
    builder.append(" ");
    String tmp = URLEncoder.encode(oldFile.getFileName().toString(),
                                   StandardCharsets.UTF_8.name());
    builder.append(tmp);
    builder.append(System.getProperty("line.separator"));
    ByteBuffer buffer = ByteBuffer.wrap(builder.toString().getBytes(StandardCharsets.UTF_8));
    try (FileChannel channel = FileChannel.open(Paths.get(getTrashFolder().toString(),
                                                          "directorysizes"),
                                                StandardOpenOption.CREATE,
                                                StandardOpenOption.APPEND,
                                                StandardOpenOption.WRITE);
            FileLock lock = channel.lock()) {
      channel.write(buffer);
    }
  }

  /*
   * https://specifications.freedesktop.org/trash-spec/trashspec-latest.html
   */
  @Override
  public boolean moveToTrash(Path file) throws IOException
  {
    if (!isTrashAvailable()) {
      return false;
    }
    if (!Files.exists(file)) {
      return false;
    }
    if (!Files.isWritable(file)) {
      return false;
    }
    if (!Files.isExecutable(file.getParent())) {
      return false;
    }
    final BasicFileAttributes attr = Files.readAttributes(file,
                                                          BasicFileAttributes.class,
                                                          LinkOption.NOFOLLOW_LINKS);
    if (attr.isSymbolicLink()) {
      return false;
    }
    final Path normalized = file.toRealPath();
    final String strRecycleBin = getTrashFolder().toString();
    final Path files = Paths.get(strRecycleBin,
                                 "files");
    final Path info = Paths.get(strRecycleBin,
                                "info");
    try (TrashContext op = findTrashFileName(normalized.getFileName().toString(),
                                             info,
                                             files)) {
      createTrashInfoFile(normalized,
                          op);
      if (attr.isDirectory()) {
        updateDirectoriesSize(file,
                              op);
      }
      Path moved = Files.move(normalized,
                              op.getNewDataFile());
      op.setSuccess(Files.exists(moved));
      return op.isSuccess();
    }
  }

  @Override
  public boolean isTrashAvailable()
  {
    Path trash = getTrashFolder();
    return trash != null && Files.isWritable(trash);
  }

  @Override
  public String formatFileSize(long size,
                               int numDigits)
  {
    return FileSizeFormatter.formatFileSize(size,
                                            numDigits);
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

  @Override
  public Path getThumbnailFolder()
  {
    return thumbnailDir.get();
  }

  @Override
  public Path findCommand(String command)
  {
    String path = System.getenv("PATH");
    if (path != null) {
      String paths[] = path.split(File.pathSeparator);
      for (String p : paths) {
        Path result = Paths.get(p,
                                command);
        if (Files.isExecutable(result)) {
          return result;
        }
      }
    }
    return null;
  }

  @Override
  public String getHostName()
  {
    String result;
    result = System.getenv("HOSTNAME");
    if (result == null || result.isEmpty() || result.trim().isEmpty()) {
      try {
        result = execReadToString("hostname");
      } catch (IOException ex) {
      }

    }
    if (result == null || result.isEmpty() || result.trim().isEmpty()) {
      try {
        result = execReadToString("cat /etc/hostname");
      } catch (IOException ex) {
      }
    }
    if (result == null || result.isEmpty() || result.trim().isEmpty()) {
      return null;
    } else {
      return result;
    }
  }

}
