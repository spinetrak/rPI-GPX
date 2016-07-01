package net.spinetrak.gpx.gpxparser.modal;

/**
 * Created by Himanshu on 7/5/2015.
 */
public class Copyright
{
  private String author;
  private String license;
  private String year;

  public Copyright(String author)
  {
    this.author = author;
  }

  public String getAuthor()
  {
    return author;
  }

  public String getLicense()
  {
    return license;
  }

  public String getYear()
  {
    return year;
  }

  public void setAuthor(String author)
  {
    this.author = author;
  }

  public void setLicense(String license)
  {
    this.license = license;
  }

  public void setYear(String year)
  {
    this.year = year;
  }
}
