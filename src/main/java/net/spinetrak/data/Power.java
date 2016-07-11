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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Power
{
  private final static Logger LOGGER = LoggerFactory.getLogger("net.spinetrak.data.Power");
  private String source = "P";
  private double voltage = 5.0;

  public Power()
  {
    final String power = new CommandExecutioner().executeCommand("/usr/share/i3blocks/battery_raspi");
    parse(power);
  }

  public String getSource()
  {
    return source;
  }

  public double getVoltage()
  {
    return voltage;
  }

  private void parse(final String line_)
  {
    if (null == line_ || line_.isEmpty() || line_.indexOf('V') < 0 || line_.indexOf('/') < 0)
    {
      return;
    }
    final String[] tokens = line_.split("/");
    if (tokens.length != 3)
    {
      return;
    }

    final String source = tokens[0];
    if (null != source && !source.isEmpty() && (source.contains("P") || source.contains("B")))
    {
      setSource(source.contains("P") ? "P" : "B");
    }

    final String voltage = tokens[2];
    if (null != voltage && !voltage.isEmpty() && voltage.indexOf('V') > 3)
    {
      setVoltage(Float.parseFloat(voltage.substring(0, voltage.indexOf('V'))));
    }
  }

  private void setSource(final String source_)
  {
    source = source_;
  }

  private void setVoltage(final double voltage_)
  {
    voltage = voltage_;
  }
}
