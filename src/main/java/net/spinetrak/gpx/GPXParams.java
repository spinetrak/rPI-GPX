package net.spinetrak.gpx;

public class GPXParams
{
  private int dateCorrection;
  private long from;
  private long to;

  public int getDateCorrection()
  {
    return dateCorrection;
  }

  public long getFrom()
  {
    return from;
  }

  public long getTo()
  {
    return to;
  }

  public void setDateCorrection(final int dateCorrection_)
  {
    dateCorrection = dateCorrection_;
  }

  public void setFrom(final long from_)
  {
    from = from_;
  }

  public void setTo(final long to_)
  {
    to = to_;
  }


  @Override
  public String toString()
  {
    return "GPXParams{" +
      "dateCorrection=" + dateCorrection +
      ", from=" + from +
      ", to=" + to +
      '}';
  }
}
