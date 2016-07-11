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

package net.spinetrak.gpx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandExecutioner
{
  public String executeCommand(final String cmd_)
  {
    final StringBuilder output = new StringBuilder();

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
    final StringBuilder sb = new StringBuilder();
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
      is.close();
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
    final StringBuilder output = new StringBuilder();
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
