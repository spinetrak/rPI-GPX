
package net.spinetrak.gpx.gpxparser.modal;

import java.util.ArrayList;
import java.util.HashSet;

public class Route extends Extension
{
  private String comment;
  private String description;
  private HashSet<Link> links;
  private String name;
  private int number;
  private ArrayList<Waypoint> routePoints;
  private String src;
  private String type;

  public void addLink(Link link)
  {
    if (links == null)
    {
      links = new HashSet<>();
    }
    links.add(link);
  }

  /**
   * Adds this new waypoint to this route.
   *
   * @param waypoint a {@link Waypoint}.
   */
  public void addRoutePoint(Waypoint waypoint)
  {
    if (routePoints == null)
    {
      routePoints = new ArrayList<Waypoint>();
    }
    routePoints.add(waypoint);
  }

  /**
   * Returns the comment of this route.
   *
   * @return A String representing the comment of this route.
   */
  public String getComment()
  {
    return comment;
  }

  /**
   * Returns the description of this route.
   *
   * @return A String representing the description of this route.
   */
  public String getDescription()
  {
    return description;
  }

  public HashSet<Link> getLinks()
  {
    return links;
  }

  /**
   * Returns the name of this route.
   *
   * @return A String representing the name of this route.
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the number of this route.
   *
   * @return A String representing the number of this route.
   */
  public Integer getNumber()
  {
    return number;
  }

  /**
   * Getter for the list of waypoints of this route.
   *
   * @return an ArrayList of {@link Waypoint} representing the points of the route.
   */
  public ArrayList<Waypoint> getRoutePoints()
  {
    return routePoints;
  }

  /**
   * Returns the src of this route.
   *
   * @return A String representing the src of this route.
   */
  public String getSrc()
  {
    return src;
  }

  /**
   * Returns the type of this route.
   *
   * @return A String representing the type of this route.
   */
  public String getType()
  {
    return type;
  }

  /**
   * Setter for route comment property. This maps to &lt;comment&gt; tag value.
   *
   * @param comment A String representing the comment of this route.
   */
  public void setComment(String comment)
  {
    this.comment = comment;
  }

  /**
   * Setter for route description property. This maps to &lt;description&gt; tag value.
   *
   * @param description A String representing the description of this route.
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  public void setLinks(HashSet<Link> links)
  {
    this.links = links;
  }

  /**
   * Setter for route name property. This maps to &lt;name&gt; tag value.
   *
   * @param name A String representing the name of this route.
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Setter for route number property. This maps to &lt;number&gt; tag value.
   *
   * @param number An Integer representing the number of this route.
   */
  public void setNumber(Integer number)
  {
    this.number = number;
  }

  /**
   * Setter for the list of waypoints of this route.
   *
   * @param routePoints an ArrayList of {@link Waypoint} representing the points of the route.
   */
  public void setRoutePoints(ArrayList<Waypoint> routePoints)
  {
    this.routePoints = routePoints;
  }

  /**
   * Setter for src type property. This maps to &lt;src&gt; tag value.
   *
   * @param src A String representing the src of this route.
   */
  public void setSrc(String src)
  {
    this.src = src;
  }

  /**
   * Setter for route type property. This maps to &lt;type&gt; tag value.
   *
   * @param type A String representing the type of this route.
   */
  public void setType(String type)
  {
    this.type = type;
  }

  /**
   * Returns a String representation of this track.
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("rte[");
    sb.append("name:" + name + " ");
    int points = 0;
    if (routePoints != null)
    {
      points = routePoints.size();
    }
    sb.append("rtepts:" + points + " ");
    sb.append("]");
    return sb.toString();
  }
}
