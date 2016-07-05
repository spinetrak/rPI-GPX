package net.spinetrak.data;

import net.spinetrak.gpx.CommandExecutioner;

public class Location
{
  private double latitude = 53.551085;
  private double longitude = 9.993682;

  public Location()
  {
    final String location = new CommandExecutioner().executeCommand("/usr/share/i3blocks/gps");
    parse(location);
  }

  public double getLatitude()
  {
    return latitude;
  }

  public double getLongitude()
  {
    return longitude;
  }

  public void setLatitude(final double latitude_)
  {
    latitude = latitude_;
  }

  public void setLongitude(final double longitude_)
  {
    longitude = longitude_;
  }

  private void parse(final String line_)
  {
    if (null == line_ || line_.isEmpty() || line_.indexOf('/') < 0)
    {
      return;
    }
    final String[] tokens = line_.split("/");
    if (null == tokens || tokens.length != 4)
    {
      return;
    }

    final String lat = tokens[1];
    setLatitude(parseToken(lat, "N", "S"));

    final String lon = tokens[2];
    setLongitude(parseToken(lon, "E", "W"));
  }

  private double parseToken(final String token_, final String x_, final String y_)
  {
    if (null != token_ && null != x_ && null != y_ && token_.isEmpty() && (token_.contains(x_) || token_.contains(y_)))
    {
      return Double.parseDouble(token_.substring(0, token_.length() - 1));
    }
    return 0;
  }
}
