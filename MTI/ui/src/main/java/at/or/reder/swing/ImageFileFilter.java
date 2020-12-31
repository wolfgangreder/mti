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

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import org.openide.util.NbBundle;

public class ImageFileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter
{

  private static final class DefaultFileFilter2 extends FileFilter2
  {

    @Override
    public boolean accept(File f)
    {
      return f != null && f.isDirectory() && f.canRead();
    }

    @Override
    public String getDescription()
    {
      return NbBundle.getMessage(ImageFileFilter.class,
                                 "directoryfilter.description");
    }

  }

  private static final class FileFilterBuilder
  {

    private final Set<String> extensions = new HashSet<>();
    private String description;
    private FileFilter2 dirFilter;

    public FileFilterBuilder addExtension(String ext)
    {
      extensions.add(ext);
      return this;
    }

    public FileFilterBuilder description(String descr)
    {
      this.description = descr;
      return this;
    }

    public FileFilterBuilder directoryFilter(FileFilter2 filter)
    {
      dirFilter = filter;
      return this;
    }

    public FileFilterBuilder reset()
    {
      extensions.clear();
      dirFilter = null;
      description = null;
      return this;
    }

    public ImageFileFilter build()
    {
      if (extensions.isEmpty()) {
        throw new IllegalStateException("extensions is empty");
      }
      if (description == null) {
        throw new IllegalStateException("description is null");
      }
      StringBuilder patternBuilder = new StringBuilder();
      StringBuilder descriptionBuilder = new StringBuilder();
      for (String e : extensions) {
        patternBuilder.append(".*\\.");
        patternBuilder.append(e);
        patternBuilder.append("|");
        descriptionBuilder.append("*.");
        descriptionBuilder.append(e);
        descriptionBuilder.append(" | ");
      }
      patternBuilder.setLength(patternBuilder.length() - 1);
      descriptionBuilder.setLength(descriptionBuilder.length() - 3);
      return new ImageFileFilter(Pattern.compile(patternBuilder.toString(),
                                                 Pattern.CASE_INSENSITIVE),
                                 MessageFormat.format("{0} ({1})",
                                                      description,
                                                      descriptionBuilder.toString()),
                                 dirFilter);
    }

  }
  public static final FileFilter2 DEFAULT_DIRECTORY_FILTER = new DefaultFileFilter2();
  private final Pattern pattern;
  private final String description;
  private final FileFilter2 directoryFilter;

  public static List<ImageFileFilter> getReaderFilterList(FileFilter2 directoryFilter)
  {
    ResourceBundle bundle = NbBundle.getBundle(ImageFileFilter.class);
    Enumeration<String> keys = bundle.getKeys();
    Map<String, Set<String>> extensions = new HashMap<>();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      if (key.startsWith("extension.")) {
        String ext = key.substring(10);
        String label = bundle.getString(key);
        Set<String> l = extensions.get(label);
        if (l == null) {
          l = new HashSet<>();
          extensions.put(label,
                         l);
        }
        l.add(ext);
      }
    }
    List<ImageFileFilter> imageFilter = new ArrayList<>();
    StringBuilder allPatternBuilder = new StringBuilder();
    Map<String, FileFilterBuilder> processableExtensions = new HashMap<>();
    for (Map.Entry<String, Set<String>> e : extensions.entrySet()) {
      for (String ext : e.getValue()) {
        Iterator<ImageReader> reader = ImageIO.getImageReadersBySuffix(ext);
        if (reader.hasNext()) {
          ImageReader r = reader.next();
          FileFilterBuilder builder = processableExtensions.get(e.getKey());
          if (builder == null) {
            builder = new FileFilterBuilder().description(e.getKey()).directoryFilter(directoryFilter);
            processableExtensions.put(e.getKey(),
                                      builder);
          }
          builder.addExtension(ext);
        }
      }
    }
    for (Map.Entry<String, FileFilterBuilder> pe : processableExtensions.entrySet()) {
      ImageFileFilter tmp = pe.getValue().build();
      allPatternBuilder.append(tmp.getPattern().pattern());
      allPatternBuilder.append("|");
      imageFilter.add(tmp);
    }
    allPatternBuilder.setLength(allPatternBuilder.length() - 1);
    Pattern pattern = Pattern.compile(allPatternBuilder.toString(),
                                      Pattern.CASE_INSENSITIVE);
    imageFilter.add(new ImageFileFilter(pattern,
                                        NbBundle.getMessage(ImageFileFilter.class,
                                                            "all.images.description"),
                                        directoryFilter));
    return imageFilter;
  }

  public ImageFileFilter()
  {
    this(DEFAULT_DIRECTORY_FILTER);
  }

  public ImageFileFilter(FileFilter2 pAcceptDirectories)
  {
    this.directoryFilter = pAcceptDirectories;
    String[] names = ImageIO.getReaderFormatNames();
    Set<String> tmp = new HashSet<>();
    StringBuilder patternBuilder = new StringBuilder();
    StringBuilder formatDescription = new StringBuilder();
    for (String name : names) {
      tmp.add(name.toLowerCase());
    }
    for (String s : tmp) {
      patternBuilder.append(".*\\.");
      patternBuilder.append(s);
      patternBuilder.append("|");
      formatDescription.append("*.");
      formatDescription.append(s);
      formatDescription.append("|");
    }
    patternBuilder.setLength(patternBuilder.length() - 1);
    formatDescription.setLength(formatDescription.length() - 1);
    pattern = Pattern.compile(patternBuilder.toString(),
                              Pattern.CASE_INSENSITIVE);
    description = MessageFormat.format(ResourceBundle.getBundle("at/mountainsd/msdswing/Strings").getString("image_files"),
                                       formatDescription.toString());
  }

  private ImageFileFilter(Pattern pattern,
                          String description,
                          FileFilter2 acceptDirectories)
  {
    this.pattern = pattern;
    this.description = description;
    this.directoryFilter = acceptDirectories;
  }

  @Override
  public boolean accept(File pathname)
  {
    return pathname.canRead() && !pathname.isHidden() && ((directoryFilter != null && directoryFilter.accept(pathname))
                                                          || pattern.
                                                          matcher(pathname.getName()).
                                                          matches());
  }

  @Override
  public String getDescription()
  {
    return description;
  }

  private Pattern getPattern()
  {
    return pattern;
  }

  @Override
  public String toString()
  {
    return getDescription();
  }

}
