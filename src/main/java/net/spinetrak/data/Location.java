package net.spinetrak.data;

public class Location
{
  private double latitude;
  private double longitude;

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
}
