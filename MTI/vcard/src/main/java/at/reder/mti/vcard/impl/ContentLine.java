/*
 * $Id$
 *
 * Author Wolfgang Reder
 *
 * Copyright 2013 Wolfgang Reder
 *
 */
package at.reder.mti.vcard.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Wolfgang Reder
 */
public final class ContentLine
{

  private final String property;
  private final String parameter;
  private final List<String> attributes;
  private String string;

  public ContentLine(String property, String paramter, List<String> attributes)
  {
    this.property = property;
    this.parameter = paramter;
    if (attributes == null || attributes.isEmpty()) {
      this.attributes = Collections.emptyList();
    } else {
      this.attributes = Collections.unmodifiableList(new ArrayList<>(attributes));
    }
  }

  public String getProperty()
  {
    return property;
  }

  public String getParameter()
  {
    return parameter;
  }

  public List<String> getAttributes()
  {
    return attributes;
  }

  @Override
  public String toString()
  {
    synchronized (this) {
      if (string == null) {
        StringBuilder builder = new StringBuilder();
        builder.append(property);
        if (parameter != null) {
          builder.append(';');
          builder.append(parameter);
        }
        builder.append(':');
        for (String a : attributes) {
          builder.append(a);
          builder.append(';');
        }
        string = builder.toString();
      }
      return string;
    }
  }

}
