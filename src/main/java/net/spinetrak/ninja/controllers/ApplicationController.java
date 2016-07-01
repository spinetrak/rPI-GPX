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
import net.spinetrak.gpx.*;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import ninja.params.PathParam;

import java.util.List;

@Singleton
public class ApplicationController
{

  public Result create()
  {
    final NMEAFile nmeaFile = getNMEAFile();
    final Result result = Results.html();
    result.render("nmeaFile", nmeaFile);
    result.render("editActive", "");
    result.render("currentActive", "");
    result.render("newActive", "active");
    return result;
  }

  public Result index()
  {
    final List<GPXFile> gpxFiles = getGPXFiles();
    final Result result = Results.html();
    result.render("gpxFiles", gpxFiles);
    result.render("editActive", "active");
    result.render("currentActive", "");
    result.render("newActive", "");
    return result;
  }

  public Result postGPXParams(final Context context_, final GPXParams params_)
  {

    params_.setNmeaFile(getNMEAFile().getFullFileName());
    params_.setGpxDir(getLatestGPXFile().getDirectory());
    final GPXWriter gpxWriter = new GPXWriter(params_);
    gpxWriter.write();
    return Results.redirect("/");
  }

  public Result view(final @PathParam("id") String id_)
  {
    final GPXFile gpxFile = getGPXFile(id_);
    return Results.html().render("gpxFile", gpxFile);
  }

  private GPXFile getGPXFile(final String id_)
  {
    if (id_ == null || id_.isEmpty() || "current".equals(id_))
    {
      return getLatestGPXFile();
    }

    for (final GPXFile gpxFile : getGPXFiles())
    {
      if (id_.equals(gpxFile.getName()))
      {
        return gpxFile;
      }
    }

    return getLatestGPXFile();
  }

  private List<GPXFile> getGPXFiles()
  {
    return new GPXReader().getGPXFiles();
  }

  private GPXFile getLatestGPXFile()
  {
    return new GPXReader().getLatestGPXFile();
  }

  private NMEAFile getNMEAFile()
  {
    return new GPXReader().getNMEAFile();
  }
}
