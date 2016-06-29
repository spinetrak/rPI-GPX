package com.hs.gpxparser.modal;

import java.util.Date;
import java.util.HashSet;

/**
 * Created by Himanshu on 7/5/2015.
 */
public class Metadata extends Extension
{
  private Person author;
  private Bounds bounds;
  private Copyright copyright;
  private String desc;
  private String keywords;
  private HashSet<Link> links;
  private String name;
  private Date time;

  public Metadata()
  {
  }

  public void addLink(Link link)
  {
    if (links == null)
    {
      links = new HashSet<>();
    }
    links.add(link);
  }

  public Person getAuthor()
  {
    return author;
  }

  public Bounds getBounds()
  {
    return bounds;
  }

  public Copyright getCopyright()
  {
    return copyright;
  }

  public String getDesc()
  {
    return desc;
  }

  public String getKeywords()
  {
    return keywords;
  }

  public HashSet<Link> getLinks()
  {
    return links;
  }

  public String getName()
  {
    return name;
  }

  public Date getTime()
  {
    return time;
  }

  public void setAuthor(Person author)
  {
    this.author = author;
  }

  public void setBounds(Bounds bounds)
  {
    this.bounds = bounds;
  }

  public void setCopyright(Copyright copyright)
  {
    this.copyright = copyright;
  }

  public void setDesc(String desc)
  {
    this.desc = desc;
  }

  public void setKeywords(String keywords)
  {
    this.keywords = keywords;
  }

  public void setLinks(HashSet<Link> links)
  {
    this.links = links;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public void setTime(Date time)
  {
    this.time = time;
  }
}
