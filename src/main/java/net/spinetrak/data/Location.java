/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 spinetrak
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.spinetrak.data;

import net.spinetrak.gpx.CommandExecutioner;

public class Location
{
  private double latitude = 53.551085;
  private double longitude = 9.993682;

  public Location()
  {
    final String location = new CommandExecutioner().executeCommand("/usr/share/i3blocks/gps");
    parse(location);
  }

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

  private void parse(final String line_)
  {
    if (null == line_ || line_.isEmpty() || line_.indexOf('/') < 0)
    {
      return;
    }
    final String[] tokens = line_.split("/");
    if (tokens.length != 4)
    {
      return;
    }

    final String lat = tokens[1];
    setLatitude(parseToken(lat, "N", "S"));

    final String lon = tokens[2];
    setLongitude(parseToken(lon, "E", "W"));
  }

  private double parseToken(final String token_, final String x_, final String y_)
  {
    if (null != token_ && null != x_ && null != y_ && token_.isEmpty() && (token_.contains(x_) || token_.contains(y_)))
    {
      return Double.parseDouble(token_.substring(0, token_.length() - 1));
    }
    return 0;
  }
}
