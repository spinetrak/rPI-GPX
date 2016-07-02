package net.spinetrak.gpx;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.util.LengthUnit;
import com.javadocmd.simplelatlng.window.RectangularWindow;
import net.spinetrak.gpx.gpxparser.GPXParser;
import net.spinetrak.gpx.gpxparser.modal.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class GPXFile extends GPSFile
{
  protected static final DateTimeFormatter DTF = DateTimeFormat.forPattern("HHmmss.SSS");
  protected static final String SDF = "yyyy-MM-dd HH:mm:ss";
  private final static Logger LOGGER = LoggerFactory.getLogger("net.spinetrak.gpx.GPXFile");
  private LatLng _center;
  private long _from;
  private int _points;
  private long _to;
  private int _zoom;

  public GPXFile(final File file_)
  {
    super(file_);
    init();
  }

  public static List<GPXFile> getGPXFiles()
  {
    final List<GPXFile> gpxFiles = new ArrayList<>();
    try (final DirectoryStream<Path> directoryStream = Files.newDirectoryStream(GPSFile.GPX_DIR, "*.{gpx}"))
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

  public static GPXFile getLatestGPXFile()
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

  public LatLng getCenter()
  {
    return _center;
  }

  public String getFrom()
  {
    return new SimpleDateFormat(SDF).format(new Date(_from));
  }

  public int getPoints()
  {
    return _points;
  }

  public String getTo()
  {
    return new SimpleDateFormat(SDF).format(new Date(_to));
  }

  public int getZoom()
  {
    return _zoom;
  }

  private RectangularWindow getWindow(final Bounds bounds_)
  {
    return new RectangularWindow(new LatLng(bounds_.getMaxLat(), bounds_.getMaxLon()),
                                 new LatLng(bounds_.getMinLat(), bounds_.getMinLon()));
  }

  private void init()
  {
    int count = 0;
    long from = 0;
    long to = 0;
    RectangularWindow window = null;
    double lon = 0;
    GPX gpx = null;
    try
    {
      gpx = new GPXParser().parseGPX(new FileInputStream(getFile()));
    }
    catch (final Exception ex_)
    {
      LOGGER.error("", ex_);
    }
    if (gpx != null)
    {
      setCenter(gpx);

      final Set<Track> tracks = gpx.getTracks();
      if (tracks != null)
      {
        for (final Track track : tracks)
        {
          final List<TrackSegment> trackSegments = track.getTrackSegments();
          if (trackSegments != null)
          {
            for (final TrackSegment trackSegment : trackSegments)
            {
              final List<Waypoint> waypoints = trackSegment.getWaypoints();
              if (waypoints != null)
              {
                for (final Waypoint wp : waypoints)
                {
                  count++;
                  final Date newDate = wp.getTime();
                  final long newDateMillis = newDate != null ? newDate.getTime() : 0;
                  if (newDateMillis == 0)
                  {
                    continue;
                  }
                  if (from == 0)
                  {
                    from = newDateMillis;
                  }
                  if (to == 0)
                  {
                    to = newDateMillis;
                  }
                  if (newDateMillis > to)
                  {
                    to = newDateMillis;
                  }
                  if (newDateMillis < from)
                  {
                    from = newDateMillis;
                  }
                }
              }
            }
          }
        }
      }
    }
    _points = count;
    _from = from;
    _to = to;
  }

  private void setCenter(final GPX gpx_)
  {
    try
    {
      final Bounds bounds = gpx_.getMetadata().getBounds();
      final RectangularWindow window = getWindow(bounds);
      setZoom(window);
      _center = window.getCenter();
    }
    catch (final Exception ex_)
    {
      LOGGER.error(getName(), ex_);
      _center = new RectangularWindow(new LatLng(54, 10), new LatLng(53, 9)).getCenter();
    }
  }

  private void setZoom(final RectangularWindow window_)
  {
    final double height = window_.getHeight(LengthUnit.KILOMETER);
    if (height < 1)
    {
      _zoom = 14;
    }
    else if (height >= 1 && height < 10)
    {
      _zoom = 13;
    }
    else if (height >= 10 && height < 100)
    {
      _zoom = 12;
    }
    else if (height >= 100 && height < 150)
    {
      _zoom = 11;
    }
    else if (height >= 150 && height < 200)
    {
      _zoom = 10;
    }
    else if (height >= 200 && height < 400)
    {
      _zoom = 9;
    }
    else if (height >= 400 && height < 1000)
    {
      _zoom = 8;
    }
    else
    {
      _zoom = 7;
    }
  }
}
