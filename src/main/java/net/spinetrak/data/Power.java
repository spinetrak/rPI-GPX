package net.spinetrak.data;

import net.spinetrak.gpx.CommandExecutioner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Power
{
  private final static Logger LOGGER = LoggerFactory.getLogger("net.spinetrak.data.Power");
  private int source;
  private double voltage;

  public Power()
  {
    final String power = new CommandExecutioner().executeCommand("/usr/share/i3blocks/battery_raspi");
    LOGGER.info("Got power: " + power);
    parse(power);
  }

  public int getSource()
  {
    return source;
  }

  public double getVoltage()
  {
    return voltage;
  }

  public void setSource(final int source_)
  {
    source = source_;
  }

  public void setVoltage(final double voltage_)
  {
    voltage = voltage_;
  }

  private void parse(final String line_)
  {
    LOGGER.info("Parsing line [" + line_ + "]");
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
    if (null != source && !source.isEmpty() && source.length() == 1 && (source.equals("P") || source.equals("B")))
    {
      setSource(source.equals("P") ? 0 : 1);
    }

    final String voltage = tokens[2];
    if (null != voltage && !voltage.isEmpty() && voltage.indexOf('V') == (voltage.length() - 1))
    {
      setVoltage(Float.parseFloat(voltage.substring(0, voltage.indexOf('V'))));
    }
  }
}