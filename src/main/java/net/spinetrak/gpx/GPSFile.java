/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 spinetrak
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.spinetrak.gpx;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

public class GPSFile
{
  static final int CREATION_TIME = 0;
  static final DateTimeFormatter DTF = DateTimeFormat.forPattern("HHmmss.SSS");
  final static Path GPX_DIR = Paths.get(".").relativize(
    Paths.get("rPI-GPX/src/main/java/assets/tracks/gpx")).toAbsolutePath().normalize();
  static final int LASTMODIFIED_TIME = 1;
  final static Path NMEA_FILE = Paths.get(".").relativize(
    Paths.get("rPI-GPX/src/main/java/assets/tracks/nmea.txt")).toAbsolutePath().normalize();
  static final String SDF = "yyyy-MM-dd HH:mm:ss";
  private static final int LASTACCESS_TIME = 2;
  private final File _file;

  GPSFile(final File file_)
  {
    _file = file_;
  }

  static Date getFileTime(final File file_, int time_) throws IOException
  {
    final Path p = Paths.get(file_.getAbsolutePath());
    final BasicFileAttributes view = Files.getFileAttributeView(p, BasicFileAttributeView.class).readAttributes();

    if (time_ == CREATION_TIME)
    {
      return new Date(view.creationTime().toMillis());
    }
    else if (time_ == LASTACCESS_TIME)
    {
      return new Date(view.lastAccessTime().toMillis());
    }
    return new Date(view.lastModifiedTime().toMillis());
  }

  public void backup()
  {
    _file.renameTo(new File(_file.getAbsolutePath() + ".bak"));
  }

  public void delete()
  {
    _file.delete();
  }

  public String getDirectory()
  {
    return _file.getParentFile().getAbsolutePath();
  }

  public File getFile()
  {
    return _file;
  }

  public String getFullFileName()
  {
    return _file.getAbsolutePath();
  }

  public String getName()
  {
    return _file.getName();
  }

  public long getTimestamp()
  {
    return _file.lastModified();
  }

}
