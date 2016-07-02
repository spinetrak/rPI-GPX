package net.spinetrak.gpx.gpxparser.modal;

import java.util.Date;

public class Point
{

  private double elevation;
  private double latitude;
  private double longitude;
  private Date time;

  public Point(double latitude, double longitude)
  {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getElevation()
  {
    return elevation;
  }

  public double getLatitude()
  {
    return latitude;
  }

  public double getLongitude()
  {
    return longitude;
  }

  public Date getTime()
  {
    return time;
  }

  public void setElevation(double elevation)
  {
    this.elevation = elevation;
  }

  public void setLatitude(double latitude)
  {
    this.latitude = latitude;
  }

  public void setLongitude(double longitude)
  {
    this.longitude = longitude;
  }

  public void setTime(Date time)
  {
    this.time = time;
  }
}
