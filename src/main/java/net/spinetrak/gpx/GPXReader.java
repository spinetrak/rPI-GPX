package net.spinetrak.gpx;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GPXReader
{
  protected static final int CREATION_TIME = 0;
  protected static final DateTimeFormatter DTF = DateTimeFormat.forPattern("HHmmss.SSS");
  protected static final int LASTACCESS_TIME = 2;
  protected static final int LASTMODIFIED_TIME = 1;
  protected static final String SDF = "yyyy-MM-dd HH:mm:ss";
  private final static Path GPX = Paths.get(".").relativize(
    Paths.get("rPI-GPX/src/main/java/assets/tracks/gpx")).toAbsolutePath().normalize();
  private final static Logger LOGGER = LoggerFactory.getLogger("net.spinetrak.gpx.GPXReader");
  private final static Path NMEA = Paths.get(".").relativize(
    Paths.get("rPI-GPX/src/main/java/assets/tracks")).toAbsolutePath().normalize();

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

  public List<GPXFile> getGPXFiles()
  {
    final List<GPXFile> gpxFiles = new ArrayList<>();
    try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(GPX, "*.{gpx}"))
    {
      for (final Path path : directoryStream)
      {
        gpxFiles.add(new GPXFile(path.toFile()));
      }
    }
    catch (final IOException ex_)
    {
      LOGGER.error("", ex_);
    }
    return gpxFiles;
  }

  public GPXFile getLatestGPXFile()
  {
    long timestamp = 0;
    GPXFile gpxFile = null;
    for (final GPXFile file : getGPXFiles())
    {
      final long newTimestamp = file.getTimestamp();
      if (newTimestamp > timestamp)
      {
        gpxFile = file;
      }
    }
    return gpxFile;
  }

  public NMEAFile getNMEAFile()
  {
    final List<NMEAFile> nmeaFiles = new ArrayList<>();
    try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(NMEA, "nmea.txt"))
    {
      for (final Path path : directoryStream)
      {
        nmeaFiles.add(new NMEAFile(path.toFile()));
      }
    }
    catch (final IOException ex_)
    {
      LOGGER.error("", ex_);
    }
    return nmeaFiles.get(0);
  }
}
