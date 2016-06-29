package com.hs.gpxparser.modal;

/**
 * Created by Himanshu on 7/5/2015.
 */
public class Bounds
{
  private double maxLat;
  private double maxLon;
  private double minLat;
  private double minLon;

  public Bounds(double minlat, double maxlat, double minlon, double maxlon)
  {
    this.minLat = minlat;
    this.maxLat = maxlat;
    this.minLon = minlon;
    this.maxLon = maxlon;
  }

  public double getMaxLat()
  {
    return maxLat;
  }

  public double getMaxLon()
  {
    return maxLon;
  }

  public double getMinLat()
  {
    return minLat;
  }

  public double getMinLon()
  {
    return minLon;
  }

  public void setMaxLat(double maxLat)
  {
    this.maxLat = maxLat;
  }

  public void setMaxLon(double maxLon)
  {
    this.maxLon = maxLon;
  }

  public void setMinLat(double minLat)
  {
    this.minLat = minLat;
  }

  public void setMinLon(double minLon)
  {
    this.minLon = minLon;
  }
}
