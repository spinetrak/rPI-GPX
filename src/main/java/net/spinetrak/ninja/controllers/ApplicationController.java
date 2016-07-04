/**
 * Copyright (C) 2013 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.spinetrak.ninja.controllers;

import com.google.inject.Singleton;
import net.spinetrak.gpx.GPXFile;
import net.spinetrak.gpx.GPXParams;
import net.spinetrak.gpx.GPXWriter;
import net.spinetrak.gpx.NMEAFile;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;
import ninja.validation.JSR303Validation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Singleton
public class ApplicationController
{
  private final static Logger LOGGER = LoggerFactory.getLogger("net.spinetrak.ninja.controllers.ApplicationController");

  public Result backupNmea(final @PathParam("id") String id_)
  {
    final NMEAFile nmeaFile = new NMEAFile();
    if (nmeaFile != null && nmeaFile.getName().equals(id_))
    {
      nmeaFile.backup();
    }
    return Results.redirect("/");
  }

  public Result create()
  {
    final NMEAFile nmeaFile = new NMEAFile();
    final Result result = Results.html();
    result.render("nmeaFile", nmeaFile);
    result.render("editActive", "");
    result.render("currentActive", "");
    result.render("newActive", "active");
    return result;
  }

  public Result delete(final @PathParam("id") String id_)
  {
    final GPXFile gpxFile = getGPXFile(id_);
    if (gpxFile != null)
    {
      gpxFile.delete();
    }
    return Results.redirect("/");
  }

  public Result deleteNmea(final @PathParam("id") String id_)
  {
    final NMEAFile nmeaFile = new NMEAFile();
    if (nmeaFile != null && nmeaFile.getName().equals(id_))
    {
      nmeaFile.delete();
    }
    return Results.redirect("/");
  }

  public Result edit()
  {
    final List<GPXFile> gpxFiles = GPXFile.getGPXFiles();
    final Result result = Results.html();
    result.render("gpxFiles", gpxFiles);
    result.render("editActive", "active");
    result.render("currentActive", "");
    result.render("newActive", "");
    return result;
  }

  public Result index()
  {
    final Result result = Results.html();
    result.render("editActive", "");
    result.render("currentActive", "");
    result.render("newActive", "");
    return result;
  }

  public Result postGPXParams(final Context context_, @JSR303Validation final GPXParams params_)
  {
    final GPXWriter gpxWriter = new GPXWriter(params_);
    gpxWriter.write();
    return Results.redirect("/");
  }

  public Result view(final @PathParam("id") String id_)
  {
    final GPXFile gpxFile = getGPXFile(id_);
    final Result result = Results.html();
    result.render("gpxFile", gpxFile);
    result.render("editActive", "");
    result.render("currentActive", "active");
    result.render("newActive", "");
    return result;
  }

  private GPXFile getGPXFile(final String id_)
  {
    if (id_ == null || id_.isEmpty() || "current".equals(id_))
    {
      return GPXFile.getLatestGPXFile();
    }

    for (final GPXFile gpxFile : GPXFile.getGPXFiles())
    {
      if (id_.equals(gpxFile.getName()))
      {
        return gpxFile;
      }
    }

    return GPXFile.getLatestGPXFile();
  }

}
