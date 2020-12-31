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

import at.or.reder.osplatform.PlatformFolders;
import at.or.reder.osplatform.thumbnail.ThumbnailGenerator;
import at.or.reder.osplatform.thumbnail.ThumbnailOption;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import org.netbeans.api.annotations.common.NonNull;

public final class KDEThumbnailImplementation
{

  private KDEThumbnailImplementation()
  {
    throw new IllegalStateException();
  }

  public static Path findThumbnail(@NonNull PlatformFolders platformFolder,
                                   @NonNull Path fileIn,
                                   ThumbnailOption thumbSize) throws IOException
  {
    final String subFolder;
    final int size;
    final Path file = fileIn.toRealPath().toAbsolutePath();
    if (thumbSize == null) {
      thumbSize = ThumbnailOption.LARGE;
    }
    switch (thumbSize) {
      case LARGE:
        subFolder = "large";
        size = 256;
        break;
      case NORMAL:
        subFolder = "normal";
        size = 128;
        break;
      case DWARF:
        subFolder = "dwarf";
        size = 16;
        break;
      case TINY:
        subFolder = "tiny";
        size = 64;
        break;
      case HUGE:
        subFolder = "huge";
        size = 512;
        break;
      default:
        size = 256;
        subFolder = "large";
    }
    if (!Files.isRegularFile(file)) {
      throw new IOException(file.toString() + " is not a regular file");
    }
    if (!Files.isReadable(file)) {
      throw new IOException(file.toString() + " is not readable");
    }
    String thumbFileName = createThumbFileName(file);
    Path thumb = Paths.get(platformFolder.getThumbnailFolder().toString(),
                           subFolder,
                           thumbFileName);
    if (Files.isReadable(thumb)) {
      return thumb;
    }
    return null;
  }

  public static Path getThumbnail(@NonNull PlatformFolders platformFolder,
                                  @NonNull Path fileIn,
                                  ThumbnailOption thumbSize,
                                  @NonNull ThumbnailGenerator generator) throws IOException
  {
    final String subFolder;
    final int size;
    final Path file = fileIn.toRealPath().toAbsolutePath();
    if (thumbSize == null) {
      thumbSize = ThumbnailOption.LARGE;
    }
    switch (thumbSize) {
      case LARGE:
        subFolder = "large";
        size = 256;
        break;
      case NORMAL:
        subFolder = "normal";
        size = 128;
        break;
      case DWARF:
        subFolder = "dwarf";
        size = 16;
        break;
      case TINY:
        subFolder = "tiny";
        size = 64;
        break;
      case HUGE:
        subFolder = "huge";
        size = 512;
        break;
      default:
        size = 256;
        subFolder = "large";
    }
    if (!Files.isRegularFile(file)) {
      throw new IOException(file.toString() + " is not a regular file");
    }
    if (!Files.isReadable(file)) {
      throw new IOException(file.toString() + " is not readable");
    }
    String thumbFileName = createThumbFileName(file);
    Path thumbDir = Paths.get(platformFolder.getThumbnailFolder().toString(),
                              subFolder);
    Files.createDirectories(thumbDir);
    Path thumbFilePath = Paths.get(thumbDir.toString(),
                                   thumbFileName);
    KDEThumbnailMetaData meta = null;
    final FileTime fTime = Files.getLastModifiedTime(file);
    if (Files.isReadable(thumbFilePath)) {
      try (ImageInputStream iis = new FileImageInputStream(thumbFilePath.toFile())) {
        Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
        if (iter.hasNext()) {
          ImageReader reader = iter.next();
          try {
            reader.setInput(iis);
            meta = KDEThumbnailMetaData.fromMeta(reader.getImageMetadata(0));
          } finally {
            reader.dispose();
          }
        } else { // kein lesbares format damit auch kein PNG
          Files.delete(thumbFilePath);
        }
      }
    }
    if (meta != null) {
      if (meta.getMTime() != fTime.toMillis() / 1000) {
        meta = null;
      }
    }
    if (meta != null) {
      return thumbFilePath;
    }
    Path tmpFile = Files.createTempFile(thumbDir,
                                        "tmpThumb",
                                        ".png");
    Dimension dim = generator.getDimension(file);
    if (dim != null) {
      double scaleX = size / dim.getWidth();
      double scaleY = size / dim.getHeight();
      double scale = Math.min(scaleY,
                              scaleX);
      dim = new Dimension((int) (dim.getWidth() * scale),
                          (int) (dim.getHeight() * scale));
    } else {
      dim = new Dimension(size,
                          size);
    }
    BufferedImage img = new BufferedImage(dim.width,
                                          dim.height,
                                          BufferedImage.TYPE_4BYTE_ABGR);
    final String contentType = generator.getContentType(file);
    final ImageWriter writer = getPNGImageWriter();
    try (ImageOutputStream ios = new FileImageOutputStream(tmpFile.toFile())) {
      writer.setOutput(ios);
      ImageWriteParam writeParam = writer.getDefaultWriteParam();
      if (writeParam.canWriteProgressive()) {
        writeParam.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
      }
      ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_4BYTE_ABGR);
      IIOMetadata imeta = writer.getDefaultImageMetadata(typeSpecifier,
                                                         writeParam);
      meta = new KDEThumbnailMetaData();
      meta.setMTime(fTime.toMillis() / 1000);
      meta.setMime(contentType);
      meta.setSize(Files.size(file));
      meta.setUri(file.toUri());
      img = generator.paintThumbnail(file,
                                     img,
                                     meta);
      if (img != null) {
        meta.toMeta(imeta);
        writer.write(imeta,
                     new IIOImage(img,
                                  null,
                                  imeta),
                     writeParam);
      } else {
        return null;
      }
    } finally {
      writer.dispose();
    }
    Files.move(tmpFile,
               thumbFilePath,
               StandardCopyOption.ATOMIC_MOVE,
               StandardCopyOption.REPLACE_EXISTING);
    Files.setPosixFilePermissions(thumbFilePath,
                                  EnumSet.of(PosixFilePermission.OWNER_READ,
                                             PosixFilePermission.OWNER_WRITE));
    return thumbFilePath;
  }

  private static String createThumbFileName(Path file) throws IOException
  {
    try {
      MessageDigest digest = MessageDigest.getInstance("MD5");
      ByteBuffer bb = ByteBuffer.wrap(digest.digest(file.toUri().toASCIIString().getBytes(StandardCharsets.UTF_8)));
      StringBuilder builder = new StringBuilder();
      String tmp = Long.toHexString(bb.getLong());
      for (int i = tmp.length(); i < 16; ++i) {
        builder.append('0');
      }
      builder.append(tmp);
      tmp = Long.toHexString(bb.getLong());
      for (int i = tmp.length(); i < 16; ++i) {
        builder.append('0');
      }
      builder.append(tmp);
      return builder.append(".png").toString();
    } catch (NoSuchAlgorithmException ex) {
      Logger.getLogger(KDEKnownFolders.class.getName()).log(Level.SEVERE,
                                                            null,
                                                            ex);
    }
    return null;
  }

  private static boolean canWriteCompressed(ImageWriter w)
  {
    ImageWriteParam p = w.getDefaultWriteParam();
    return p.canWriteCompressed();
  }

  private static ImageWriter getPNGImageWriter()
  {
    Iterator<ImageWriter> iter = ImageIO.getImageWritersByMIMEType("image/png");
    ImageWriter candidate = null;
    while (iter.hasNext()) {
      ImageWriter w = iter.next();
      if (candidate == null) {
        candidate = w;
      } else if (canWriteCompressed(w)) {
        candidate = w;
      }
      if (canWriteCompressed(candidate)) {
        return candidate;
      }
    }
    return candidate;
  }

}
