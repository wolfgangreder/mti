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

import java.io.IOException;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;

final class TrashContext implements AutoCloseable
{

  private final Path newDataFile;
  private final FileLock infoLock;
  private final Path infoFile;
  private boolean success;

  public TrashContext(Path newDataFile,
                      FileLock infoLock,
                      Path infoFile)
  {
    this.newDataFile = newDataFile;
    this.infoLock = infoLock;
    this.infoFile = infoFile;
  }

  public Path getNewDataFile()
  {
    return newDataFile;
  }

  public FileLock getInfoLock()
  {
    return infoLock;
  }

  public Path getInfoFile()
  {
    return infoFile;
  }

  public boolean isSuccess()
  {
    return success;
  }

  public void setSuccess(boolean s)
  {
    success = s;
  }

  @Override
  public void close() throws IOException
  {
    infoLock.close();
    infoLock.channel().close();
    if (!success) {
      Files.deleteIfExists(infoFile);
    }
  }

}
