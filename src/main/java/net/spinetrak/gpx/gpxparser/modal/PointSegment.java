package net.spinetrak.gpx.gpxparser.modal;

import java.util.ArrayList;

/**
 * Created by Himanshu on 7/5/2015.
 */
public class PointSegment
{
  private ArrayList<Point> points;

  public PointSegment()
  {
  }

  public void addPoint(Point point)
  {
    if (points == null)
    {
      points = new ArrayList<>();
    }
    points.add(point);
  }

  public ArrayList<Point> getPoints()
  {
    return points;
  }

  public void setPoints(ArrayList<Point> points)
  {
    this.points = points;
  }
}
