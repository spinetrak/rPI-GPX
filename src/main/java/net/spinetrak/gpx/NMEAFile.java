package net.spinetrak.gpx;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import static net.spinetrak.gpx.GPXFile.DTF;
import static net.spinetrak.gpx.GPXFile.SDF;

public class NMEAFile extends GPSFile
{
  private final static Logger LOGGER = LoggerFactory.getLogger("net.spinetrak.gpx.NMEAFile");
  long _from;
  int _points;
  long _to;

  public NMEAFile()
  {
    super(GPSFile.NMEA_FILE.toFile());
    init();
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

  private void init()
  {
    int count = 0;

    LineIterator it = null;
    Date from;
    Date to = null;
    String toStr = null;
    try
    {
      it = FileUtils.lineIterator(getFile(), "UTF-8");
      from = GPSFile.getFileTime(getFile(), GPSFile.CREATION_TIME);
      to = GPSFile.getFileTime(getFile(), GPSFile.LASTMODIFIED_TIME);

      while (it.hasNext())
      {
        final String line = it.nextLine();
        if (line.contains("GGA"))
        {
          count++;
          if (count == 1)
          {
            _from = parseDate(from, parseTime(line));
          }
          toStr = parseTime(line);
        }
      }
    }
    catch (final Exception ex_)
    {
      ex_.printStackTrace();
      LOGGER.error("", ex_);
    }
    finally
    {
      LineIterator.closeQuietly(it);
      _points = count;
      _to = parseDate(to, toStr);
    }
  }

  private long parseDate(final Date date_, final String time_)
  {
    final DateTime dateTime = new DateTime(date_).withTimeAtStartOfDay();
    final DateTime time = DTF.parseDateTime(time_);
    return dateTime.withTime(time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute(),
                             time.getMillisOfSecond()).toDate().getTime();
  }

  private String parseTime(final String line_)
  {
    final String[] fields = line_.split(",");
    return fields[1];
  }
}
