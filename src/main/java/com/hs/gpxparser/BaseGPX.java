package com.hs.gpxparser;

import com.hs.gpxparser.extension.IExtensionParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

class BaseGPX
{

  protected final ArrayList<IExtensionParser> extensionParsers = new ArrayList<IExtensionParser>();
  protected final SimpleDateFormat xmlDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
  protected final SimpleDateFormat xmlDateFormatMillis = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

  /**
   * Adds a new extension parser to be used when parsing a gpx steam
   *
   * @param parser an instance of a {@link IExtensionParser} implementation
   */
  public void addExtensionParser(IExtensionParser parser)
  {
    this.extensionParsers.add(parser);
  }

  /**
   * Removes an extension parser previously added
   *
   * @param parser an instance of a {@link IExtensionParser} implementation
   */
  public void removeExtensionParser(IExtensionParser parser)
  {
    this.extensionParsers.remove(parser);
  }
}
