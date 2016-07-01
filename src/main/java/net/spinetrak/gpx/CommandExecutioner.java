package net.spinetrak.gpx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandExecutioner
{
  public String executeCommand(final String cmd_)
  {
    final StringBuffer output = new StringBuffer();

    try
    {
      final Process p = Runtime.getRuntime().exec(cmd_);
      p.waitFor();

      output.append(handleProcess(p, false));
      output.append(handleProcess(p, true));

      if (p.isAlive())
      {
        p.destroy();
      }
    }
    catch (final IOException | InterruptedException ex_)
    {
      output.append(ex_.getMessage());
    }

    return output.toString();
  }

  private String handleProcess(final Process process_, final boolean isError_)
  {
    final StringBuffer sb = new StringBuffer();
    final InputStream is = isError_ ? process_.getErrorStream() : process_.
      getInputStream();
    final BufferedReader br = new BufferedReader(new InputStreamReader(is));

    final String inResult = handleReader(br);
    if (!inResult.isEmpty())
    {
      if (isError_)
      {
        sb.append("ERROR: ");
      }
      else
      {
        sb.append("RESULT: ");
      }
      sb.append(inResult);
    }
    try
    {
      if (is != null)
      {
        is.close();
      }
      br.close();
    }
    catch (final IOException ex_)
    {
      ex_.printStackTrace();
    }
    return sb.toString();
  }

  private String handleReader(final BufferedReader reader_)
  {
    final StringBuffer output = new StringBuffer();
    try
    {
      String line;
      while ((line = reader_.readLine()) != null)
      {
        output.append(line).append("\n");
      }
      reader_.close();
    }
    catch (final IOException ex_)
    {
      ex_.printStackTrace();
    }
    return output.toString();
  }
}
