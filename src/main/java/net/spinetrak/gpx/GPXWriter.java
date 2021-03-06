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

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Write a description of class GPXWriter here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class GPXWriter
{
  //private static final String GPX_DIR = "/home/pi/tracks/gpx/";
  /*
cp /home/pi/scripts/template.html /home/pi/scripts/$TARGET.html
/usr/bin/perl -pi -e 's/__URL__/$TARGET.gpx/g' /home/pi/scripts/$TARGET.html

TOKEN=`cat /home/pi/scripts/urllink.token`

sudo /usr/bin/perl -pi -e 's/$TOKEN/$TARGET.html$TOKEN/g' /home/pi/tracks/gpx/index.html

sudo mv /home/pi/scripts/$TARGET.html /home/pi/tracks/gpx/
ls -al /home/pi/tracks/gpx/
   */
  private final static Logger LOGGER = LoggerFactory.getLogger("net.spinetrak.gpx.GPXWriter");
  private static String OS = System.getProperty("os.name").toLowerCase();
  private static final String CMD = (OS.contains(
    "mac")) ? "/Applications/GPSBabelFE.app/Contents/MacOS/gpsbabel" : "/usr/bin/sudo /usr/bin/gpsbabel";
  private final boolean _backup;
  private final Integer _date;
  private final long _from;
  private final String _gpxDir;
  private final NMEAFile _nmeaFile;
  private final String _outfile;
  private final long _to;


  public GPXWriter(final GPXParams gpxParams_)
  {
    this(gpxParams_.getFrom(), gpxParams_.getTo(),
         gpxParams_.getDateCorrection(), false);
    LOGGER.info(gpxParams_.toString());
  }

  private GPXWriter(final long from_, final long to_,
                    final int date_, final boolean backup_)
  {
    if (from_ != 0 && (from_ < 2016 || from_ > 205001010000L))
    {
      throw new IllegalArgumentException("From is out of range: " + from_);
    }
    if (to_ != 0 && (to_ < 2016 || to_ > 205001010000L))
    {
      throw new IllegalArgumentException("To is out of range: " + to_);
    }
    _nmeaFile = new NMEAFile();
    if (!_nmeaFile.getFile().exists() || !_nmeaFile.getFile().canRead())
    {
      throw new IllegalArgumentException("NMEA file does not exist or is not readable: " + _nmeaFile.getFile().
        getAbsolutePath());
    }

    final File gpxDir = GPSFile.GPX_DIR.toFile();
    if (!gpxDir.exists() || !gpxDir.canRead() || !gpxDir.isDirectory())
    {
      throw new IllegalArgumentException(
        "GPX directory does not exist or is not readable: " + gpxDir.getAbsolutePath());
    }
    _gpxDir = gpxDir.getAbsolutePath();

    if (date_ != 0 && (date_ < 20160101 || date_ > 20500101))
    {
      throw new IllegalArgumentException("Date is out of range: " + date_);
    }
    _from = from_;
    _to = to_;
    _date = date_;
    _outfile = buildOutFile();
    _backup = backup_;
    out(this.toString());
  }

  public static void main(final String args_[])
  {
    final OptionParser parser = new OptionParser()
    {
      {
        accepts("f", "from").withRequiredArg().ofType(Long.class)
          .describedAs("from").defaultsTo(0L);
        accepts("t", "to").withRequiredArg().ofType(Long.class)
          .describedAs("to").defaultsTo(0L);
        accepts("g", "gpsfix_correction");
        accepts("b", "backup nmea");
        accepts("h", "help");
        accepts("d", "date_correction").withRequiredArg().ofType(Integer.class).
          describedAs("date_correction (\"YYYYMMDD\")").
          defaultsTo(0);
      }
    };

    try
    {
      final OptionSet options = parser.parse(args_);
      final boolean help = options.has("h");
      if (help)
      {
        parser.printHelpOn(System.out);
        System.exit(1);
      }
      final GPXWriter gpx = GPXWriter.getInstance(options);
      gpx.write();
      if (gpx.validate())
      {
        gpx.writeHTML();
      }
      parser.printHelpOn(System.out);
    }
    catch (final Exception ex_)
    {
      try
      {
        parser.printHelpOn(System.out);
      }
      catch (final IOException ex2_)
      {
        error(ex2_.getMessage());
      }

      error(ex_.getMessage());
      System.exit(1);
    }
  }

  public static void out(String msg_)
  {
    LOGGER.info(msg_);
  }

  private static void error(final String error_)
  {
    LOGGER.error(error_);
    System.exit(1);
  }

  private static GPXWriter getInstance(final OptionSet options) throws IOException
  {
    final long from = (Long) options.valueOf("f");
    final long to = (Long) options.valueOf("t");
    final boolean backup = options.has("b");
    final int date = (Integer) options.valueOf("d");

    return new GPXWriter(from, to, date, backup);
  }

  @Override
  public String toString()
  {
    return "GPXWriter{" +
      "_backup=" + _backup +
      ", _date=" + _date +
      ", _from=" + _from +
      ", _gpxDir='" + _gpxDir + '\'' +
      ", _nmeaFile=" + _nmeaFile +
      ", _outfile='" + _outfile + '\'' +
      ", _to=" + _to +
      '}';
  }

  public void write()
  {
    /**
     * /usr/bin/sudo /usr/bin/gpsbabel -D3 -i
     * nmea[,ignore_fix=1[,date=20160612]] -f /home/pi/tracks/nmea.txt -x
     * position,distance=10m [-x track,start=2016,stop=20500101,fix=3d] -o gpx
     * -F /home/pi/tracks/gpx/2016-20500101_new.gpx
     *
     */
    final String cmd
      = CMD + " -D 3 "
      + buildNmeaOptions()
      + " -f " + _nmeaFile.getFile().getAbsolutePath() + " "
      + buildTrackOptions()
      + " -x simplify,count=1500"
      + " -o gpx,gpxver=1.1"
      + " -F " + _outfile;

    out("Running under OS [" + OS + "]");
    out("Running cmd \"" + cmd + "\"");
    out(new CommandExecutioner().executeCommand(cmd));
  }

  private void backupNmea()
  {
    if (_nmeaFile.getFile().exists() && _nmeaFile.getFile().canWrite())
    {
      final String oldNmea = _nmeaFile.getFile().getAbsolutePath();
      final StringBuilder newNmea = new StringBuilder(oldNmea);

      final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd_HH-mm-ss");
      final String date = sdf.format(new Date());

      newNmea.insert(newNmea.indexOf(".txt"), "_" + date);

      try
      {
        final Path fromPath = Paths.get(oldNmea);
        final Path targetPath = Paths.get(newNmea.toString());
        out("Moving " + fromPath.toString() + " to " + targetPath.toString());
        Files.move(fromPath, targetPath, StandardCopyOption.REPLACE_EXISTING);
      }
      catch (final Exception ex_)
      {
        ex_.printStackTrace();
      }
    }
  }

  private String buildNmeaOptions()
  {
    //-i nmea[,ignore_fix=1[,date=20160612]]
    final StringBuilder nmea = new StringBuilder("-i nmea");
    if (_date != 0)
    {
      nmea.append(",date=").append(_date);
    }
    return nmea.toString();
  }

  private String buildOutFile()
  {
    // -F /home/pi/tracks/gpx/2016-20500101.gpx
    final StringBuilder name = new StringBuilder(_gpxDir).append("/");
    final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd_HH-mm-ss");
    final String date = sdf.format(new Date());
    name.append(date).append(".gpx");
    return name.toString();
  }

  private String buildTrackOptions()
  {
    //[-x track,start=2016,stop=20500101,fix=3d]
    final StringBuilder track = new StringBuilder("");
    if (_from > 0 || _to > 0)
    {
      track.append("-x track");
      if (_from != 0)
      {
        track.append(",start=").append(_from);
      }
      if (_to != 0)
      {
        track.append(",stop=").append(_to);
      }
    }
    return track.toString();
  }

  private boolean validate()
  {
    final File gpx = new File(_outfile);
    if (gpx.canRead() && gpx.isFile() && gpx.length() > 1000L)
    {
      if (_backup)
      {
        backupNmea();
      }
      return true;
    }
    out("Deleting " + _outfile + " (" + gpx.length() + " bytes)");
    gpx.delete();
    return false;
  }

  private void writeHTML()
  {
    out("Writing new GPX HTML page...");

    out("Updating GPX HTML index page...");
  }

}
