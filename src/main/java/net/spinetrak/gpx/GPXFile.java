package net.spinetrak.gpx;

import com.hs.gpxparser.GPXParser;
import com.hs.gpxparser.modal.GPX;
import com.hs.gpxparser.modal.Track;
import com.hs.gpxparser.modal.TrackSegment;
import com.hs.gpxparser.modal.Waypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class GPXFile
{
  final static Logger logger = LoggerFactory.getLogger("net.spinetrak.gpx.GPXFile");
  private static final String SDF = "yyyy-MM-dd HH:mm:ss";
  final File _file;
  long _from;
  int _points;
  long _to;

  public GPXFile(final File file_)
  {
    _file = file_;
    init();
  }

  public GPXFile()
  {
    _file = null;
  }

  public String getFrom()
  {
    return new SimpleDateFormat(SDF).format(new Date(_from));
  }

  public String getName()
  {
    return _file.getName();
  }

  public int getPoints()
  {
    return _points;
  }

  public long getTimestamp()
  {
    return _file.lastModified();
  }

  public String getTo()
  {
    return new SimpleDateFormat(SDF).format(new Date(_to));
  }

  private void init()
  {
    int count = 0;
    long from = 0;
    long to = 0;
    GPX gpx = null;
    try
    {
      gpx = new GPXParser().parseGPX(new FileInputStream(_file));
    }
    catch (final Exception ex_)
    {
      logger.error("", ex_);
    }
    if (gpx != null)
    {
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
}
