package com.hs.gpxparser.modal;

/**
 * Created by Himanshu on 7/5/2015.
 */
public class Person
{
  private Email email;
  private Link link;
  private String name;

  public Email getEmail()
  {
    return email;
  }

  public Link getLink()
  {
    return link;
  }

  public String getName()
  {
    return name;
  }

  public void setEmail(Email email)
  {
    this.email = email;
  }

  public void setLink(Link link)
  {
    this.link = link;
  }

  public void setName(String name)
  {
    this.name = name;
  }
}
