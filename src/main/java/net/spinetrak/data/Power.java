package net.spinetrak.data;

import net.spinetrak.gpx.CommandExecutioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Power
{
  private final static Logger LOGGER = LoggerFactory.getLogger("net.spinetrak.data.Power");
  private double source = 4.75;
  private double voltage = 5.0;

  public Power()
  {
    final String power = new CommandExecutioner().executeCommand("/usr/share/i3blocks/battery_raspi");
    parse(power);
  }

  public double getSource()
  {
    return source;
  }

  public double getVoltage()
  {
    return voltage;
  }

  public void setSource(final double source_)
  {
    source = source_;
  }

  public void setVoltage(final double voltage_)
  {
    voltage = voltage_;
  }

  private void parse(final String line_)
  {
    if (null == line_ || line_.isEmpty() || line_.indexOf('V') < 0 || line_.indexOf('/') < 0)
    {
      return;
    }
    final String[] tokens = line_.split("/");
    if (null == tokens || tokens.length != 3)
    {
      return;
    }

    final String source = tokens[0];
    if (null != source && !source.isEmpty() && (source.contains("P") || source.contains("B")))
    {
      setSource(source.contains("P") ? 4.75 : 5.25);
    }

    final String voltage = tokens[2];
    if (null != voltage && !voltage.isEmpty() && voltage.indexOf('V') > 3)
    {
      setVoltage(Float.parseFloat(voltage.substring(0, voltage.indexOf('V'))));
    }
  }
}
