package net.spinetrak.gpx;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

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
  /*
cp /home/pi/scripts/template.html /home/pi/scripts/$TARGET.html
/usr/bin/perl -pi -e 's/__URL__/$TARGET.gpx/g' /home/pi/scripts/$TARGET.html

TOKEN=`cat /home/pi/scripts/urllink.token`

sudo /usr/bin/perl -pi -e 's/$TOKEN/$TARGET.html$TOKEN/g' /home/pi/tracks/gpx/index.html

sudo mv /home/pi/scripts/$TARGET.html /home/pi/tracks/gpx/
ls -al /home/pi/tracks/gpx/
   */

  private static final String GPX_DIR = "/home/pi/tracks/gpx/";
  private static final String NMEA_FILE = "/home/pi/tracks/nmea.txt";
  private final boolean _backup;
  private final Integer _date;
  private final boolean _fix;
  private final long _from;
  private final String _name;
  private final File _nmeaFile;
  private final String _outfile;
  private final long _to;

  /**
   * Constructor for objects of class GPXWriter
   *
   * @param from_
   * @param to_
   * @param name_
   * @param nmea_
   * @param fix_
   * @param date_
   * @param backup_
   */
  public GPXWriter(final long from_, final long to_, final String name_, final String nmea_, final boolean fix_,
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
    _nmeaFile = new File(nmea_);
    if (!_nmeaFile.exists() || !_nmeaFile.canRead())
    {
      throw new IllegalArgumentException("File does not exist or is not readable: " + _nmeaFile.
        getAbsolutePath());
    }
    if (date_ != 0 && (date_ < 20160101 || date_ > 20500101))
    {
      throw new IllegalArgumentException("Date is out of range: " + date_);
    }
    _from = from_;
    _to = to_;
    _name = name_;
    _fix = fix_;
    _date = date_;
    _outfile = buildOutFile();
    _backup = backup_;
    out("Using  from: " + _from + ";  to: " + _to + "; name: " + _name + "; nmea: " + _nmeaFile.
      getAbsolutePath() + "; fix: " + _fix + "; date: " + _date + "; outfile: " + _outfile);
  }

  public static void error(final String error_)
  {
    System.err.println(error_);
    System.exit(1);
  }

  /**
   * @param args_
   */
  public static void main(final String args_[])
  {
    final OptionParser parser = new OptionParser()
    {
      {
        accepts("f", "from").withRequiredArg().ofType(Long.class)
          .describedAs("from").defaultsTo(0L);
        accepts("t", "to").withRequiredArg().ofType(Long.class)
          .describedAs("to").defaultsTo(0L);
        accepts("n", "name").withRequiredArg().ofType(String.class).
          describedAs("name").
          defaultsTo("new");
        accepts("i", "input").withRequiredArg().ofType(String.class).
          describedAs("input").
          defaultsTo(NMEA_FILE);
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
    System.out.println(msg_);
  }

  private static GPXWriter getInstance(final OptionSet options) throws IOException
  {
    final long from = (Long) options.valueOf("f");
    final long to = (Long) options.valueOf("t");
    final String name = (String) options.valueOf("n");
    final String input = (String) options.valueOf("i");
    final boolean fix = options.has("g");
    final boolean backup = options.has("b");
    final int date = (Integer) options.valueOf("d");

    final GPXWriter gpx = new GPXWriter(from, to, name, input, fix, date, backup);
    return gpx;
  }

  private void backupNmea()
  {
    if (_nmeaFile.exists() && _nmeaFile.canWrite())
    {
      final String oldNmea = _nmeaFile.getAbsolutePath();
      final StringBuffer newNmea = new StringBuffer(oldNmea);

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
    final StringBuffer nmea = new StringBuffer("-i nmea");
    if (_fix)
    {
      nmea.append(",ignore_fix=1");
    }
    if (_date != 0)
    {
      nmea.append(",date=").append(_date);
    }
    return nmea.toString();
  }

  private String buildOutFile()
  {
    // -F /home/pi/tracks/gpx/2016-20500101_new.gpx
    final StringBuffer name = new StringBuffer(GPX_DIR);
    final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd_HH-mm-ss");
    final String date = sdf.format(new Date());
    name.append(date).append("_").append(_name).append(".gpx");
    return name.toString();
  }

  private String buildTrackOptions()
  {
    //[-x track,start=2016,stop=20500101,fix=3d]
    final StringBuffer track = new StringBuffer("");
    if (_from > 0 || _to > 0 || _fix)
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
      if (_fix)
      {
        track.append(",fix=3d");
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

  private void write()
  {
    /**
     * /usr/bin/sudo /usr/bin/gpsbabel -D3 -i
     * nmea[,ignore_fix=1[,date=20160612]] -f /home/pi/tracks/nmea.txt -x
     * position,distance=10m [-x track,start=2016,stop=20500101,fix=3d] -o gpx
     * -F /home/pi/tracks/gpx/2016-20500101_new.gpx
     *
     */
    final String cmd
      = "/usr/bin/sudo /usr/bin/gpsbabel -D 3 "
      + buildNmeaOptions()
      + " -f " + NMEA_FILE + " "
      + buildTrackOptions()
      + " -x position,distance=10m"
      + " -o gpx"
      + " -F " + _outfile;

    out("Running cmd \"" + cmd + "\"");
    out(new CommandExecutioner().executeCommand(cmd));
  }

  private void writeHTML()
  {
    out("Writing new GPX HTML page...");

    out("Updating GPX HTML index page...");
  }

}
