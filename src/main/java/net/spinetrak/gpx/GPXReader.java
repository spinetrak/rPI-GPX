package net.spinetrak.gpx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GPXReader
{
  final static Path GPX = Paths.get(".").relativize(
    Paths.get("rPI-GPX/src/main/java/assets/tracks/gpx")).toAbsolutePath().normalize();
  final static Path NMEA = Paths.get(".").relativize(
    Paths.get("rPI-GPX/src/main/java/assets/tracks")).toAbsolutePath().normalize();
  final static Logger logger = LoggerFactory.getLogger("net.spinetrak.gpx.GPXReader");

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
      logger.error("", ex_);
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
}
