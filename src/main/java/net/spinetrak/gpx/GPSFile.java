package net.spinetrak.gpx;

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
  protected static final int CREATION_TIME = 0;
  protected final static Path GPX_DIR = Paths.get(".").relativize(
    Paths.get("rPI-GPX/src/main/java/assets/tracks/gpx")).toAbsolutePath().normalize();
  protected static final int LASTACCESS_TIME = 2;
  protected static final int LASTMODIFIED_TIME = 1;
  protected final static Path NMEA_FILE = Paths.get(".").relativize(
    Paths.get("rPI-GPX/src/main/java/assets/tracks/nmea.txt")).toAbsolutePath().normalize();
  private final File _file;

  public GPSFile(final File file_)
  {
    _file = file_;
  }

  public static Date getFileTime(final File file_, int time_) throws IOException
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
