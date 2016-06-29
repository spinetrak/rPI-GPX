package com.hs.gpxparser.modal;

/**
 * Created by Himanshu on 7/5/2015.
 */
public class Link
{

  private String href;

  private String text;
  private String type;

  public Link(String href)
  {
    this.href = href;
  }

  public String getHref()
  {
    return href;
  }

  public String getText()
  {
    return text;
  }

  public String getType()
  {
    return type;
  }

  public void setHref(String href)
  {
    this.href = href;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public void setType(String type)
  {
    this.type = type;
  }
}
