package net.spinetrak.gpx;

public class GPXParams
{
  private int _dateCorrection;
  private long _from;
  private boolean _gpsFixCorrection;
  private long _to;

  public int getDateCorrection()
  {
    return _dateCorrection;
  }

  public long getFrom()
  {
    return _from;
  }

  public long getTo()
  {
    return _to;
  }

  public boolean isGpsFixCorrection()
  {
    return _gpsFixCorrection;
  }

  public void setDateCorrection(final int dateCorrection_)
  {
    _dateCorrection = dateCorrection_;
  }

  public void setFrom(final long from_)
  {
    _from = from_;
  }

  public void setGpsFixCorrection(final boolean gpsFixCorrection_)
  {
    _gpsFixCorrection = gpsFixCorrection_;
  }

  public void setTo(final long to_)
  {
    _to = to_;
  }


}
